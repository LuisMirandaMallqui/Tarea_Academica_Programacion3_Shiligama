package pe.edu.pucp.venta.pasarela;

import org.json.JSONObject;
import pe.edu.pucp.config.Config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servicio de integración con la pasarela de pago externa <b>Izipay</b>
 * (PayZen / Lyra), siguiendo el mismo enfoque que el proyecto de referencia
 * Compu-Rangers.
 *
 * <p>Flujo:
 * <ol>
 *   <li>{@link #generarPago} arma la petición de creación de pago y la envía
 *       al endpoint de formulario/token configurado; Izipay devuelve una
 *       {@code redirectionUrl} a la que el cliente es redirigido para pagar.</li>
 *   <li>Cuando el cliente termina, Izipay notifica (IPN) a nuestro callback
 *       REST con los campos {@code vads_*} firmados con HMAC-SHA256.</li>
 *   <li>El callback valida la firma con {@link #calcularFirma} y confirma el
 *       pago en la base de datos.</li>
 * </ol>
 *
 * <p>Las credenciales y URLs se leen de {@code shiligama-config.properties}
 * (o variables de entorno) vía {@link Config}; nunca van hardcodeadas.
 */
public class PasarelaPagoService {

    /**
     * Solicita a la pasarela la creación de un pago y devuelve la URL de
     * redirección a la que se debe enviar al cliente. Devuelve {@code null}
     * si la pasarela no respondió correctamente.
     *
     * @param email    correo del comprador
     * @param amount   monto en céntimos (p.ej. "S/ 25.50" -> "2550")
     * @param currency moneda ISO ("PEN")
     * @param orderId  identificador de orden (incluye el id de pedido)
     */
    public String generarPago(String email, String amount, String currency,
                              String orderId) {
        String endpoint = Config.get("izipay.create.payment.url");
        if (endpoint == null) {
            System.err.println("[Pasarela] Falta 'izipay.create.payment.url' en la configuración.");
            return null;
        }
        String mode = Config.get("izipay.mode", "TEST");
        String language = Config.get("izipay.language", "es-ES");
        String shopId = Config.get("izipay.shop.id", "");
        String returnUrl = Config.get("izipay.return.url", "");

        try {
            JSONObject body = new JSONObject();
            body.put("shopId", shopId);
            body.put("email", email);
            body.put("amount", amount);
            body.put("currency", currency);
            body.put("mode", mode);
            body.put("language", language);
            body.put("orderId", orderId);
            body.put("returnUrl", returnUrl);

            HttpURLConnection connection = (HttpURLConnection) URI.create(endpoint)
                    .toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            String auth = Config.get("izipay.auth.header");
            if (auth != null && !auth.isBlank()) {
                connection.setRequestProperty("Authorization", auth);
            }
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = connection.getResponseCode();
            if (status >= 200 && status < 300) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }
                JSONObject json = new JSONObject(response.toString());
                if (json.has("redirectionUrl")) {
                    return json.getString("redirectionUrl");
                }
                System.err.println("[Pasarela] Respuesta sin redirectionUrl: " + response);
            } else {
                System.err.println("[Pasarela] Error al generar el pago. HTTP " + status);
            }
        } catch (Exception e) {
            System.err.println("[Pasarela] Excepción en generarPago: " + e.getMessage());
        }
        return null;
    }

    /**
     * Calcula la firma HMAC-SHA256 (codificada en Base64) de los campos del
     * formulario IPN de Izipay, según su algoritmo: ordenar las claves
     * alfabéticamente (excluyendo {@code signature}), concatenar los valores
     * separados por "+", anexar la clave secreta y aplicar HMAC-SHA256.
     */
    public static String calcularFirma(Map<String, String> campos, String clave) {
        List<String> claves = new ArrayList<>(campos.keySet());
        claves.remove("signature");
        Collections.sort(claves);

        StringBuilder data = new StringBuilder();
        for (String c : claves) {
            String valor = campos.get(c);
            if (valor != null) {
                data.append(valor).append("+");
            }
        }
        data.append(clave);

        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    clave.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256.init(secretKey);
            byte[] hash = sha256.doFinal(data.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error al calcular la firma HMAC", e);
        }
    }
}
