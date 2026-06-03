package pe.edu.pucp.venta.pasarela;

import org.json.JSONArray;
import org.json.JSONObject;
import pe.edu.pucp.config.Config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Integración con la pasarela <b>Izipay</b> mediante su API REST V4
 * (formulario embebido / Krypton), siguiendo el ejemplo oficial
 * {@code izipay-pe/Embedded-PaymentForm-Servlet-Java}.
 *
 * <p>Flujo real:
 * <ol>
 *   <li>El servidor llama a {@code CreatePayment} (Basic auth con
 *       usuario:password) y obtiene un <b>formToken</b>.</li>
 *   <li>El frontend carga el SDK Krypton con la <b>clave pública</b> y el
 *       formToken; el cliente paga en el formulario embebido (la tarjeta nunca
 *       pasa por nuestro servidor → PCI-DSS).</li>
 *   <li>Izipay devuelve el resultado firmado (kr-answer + kr-hash). La firma
 *       se valida con HMAC-SHA256: con la <b>clave HMAC</b> para el retorno del
 *       navegador y con la <b>password</b> para la notificación servidor (IPN).</li>
 * </ol>
 *
 * <p>Credenciales en {@code shiligama-config.properties} (Back Office Izipay).
 *
 * <p>NOTA: implementado según la documentación V4; verificar los nombres exactos
 * de los campos al probar con credenciales TEST reales.
 */
public class PasarelaPagoService {

    /**
     * Crea un pago en Izipay y devuelve el {@code formToken} necesario para
     * renderizar el formulario embebido. Devuelve {@code null} si falla.
     *
     * @param amountCents monto en céntimos (entero), p.ej. S/ 25.50 -> 2550
     */
    public String crearFormToken(long amountCents, String currency, String orderId, String email) {
        String endpoint = Config.get("izipay.endpoint",
                "https://api.micuentaweb.pe/api-payment/V4/Charge/CreatePayment");
        String username = Config.get("izipay.username");
        String password = Config.get("izipay.password");
        if (username == null || password == null) {
            System.err.println("[Izipay] Faltan credenciales (izipay.username / izipay.password).");
            return null;
        }

        try {
            JSONObject body = new JSONObject();
            body.put("amount", amountCents);
            body.put("currency", currency != null ? currency : Config.get("izipay.currency", "PEN"));
            body.put("orderId", orderId);
            if (email != null && !email.isBlank()) {
                JSONObject customer = new JSONObject();
                customer.put("email", email);
                body.put("customer", customer);
            }

            String auth = Base64.getEncoder().encodeToString(
                    (username + ":" + password).getBytes(StandardCharsets.UTF_8));

            HttpURLConnection conn = (HttpURLConnection) URI.create(endpoint).toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + auth);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) response.append(line);
            }

            JSONObject json = new JSONObject(response.toString());
            if ("SUCCESS".equalsIgnoreCase(json.optString("status"))
                    && json.has("answer")) {
                return json.getJSONObject("answer").optString("formToken", null);
            }
            System.err.println("[Izipay] CreatePayment no exitoso: " + response);
        } catch (Exception e) {
            System.err.println("[Izipay] Excepción en crearFormToken: " + e.getMessage());
        }
        return null;
    }

    /** Clave pública para el SDK del navegador, en formato {@code username:publicKey}. */
    public String getPublicKey() {
        return Config.get("izipay.username", "") + ":" + Config.get("izipay.public.key", "");
    }

    /** Base del SDK JavaScript de Izipay (Krypton). */
    public String getJsBase() {
        return Config.get("izipay.js.base", "https://api.micuentaweb.pe");
    }

    /**
     * Procesa la notificación de Izipay (IPN o retorno del navegador): valida la
     * firma del {@code kr-answer} y extrae el resultado del pago.
     *
     * @param krAnswer   JSON con la respuesta del pago
     * @param krHash     firma recibida (hex)
     * @param krHashKey  "password" (IPN) o "sha256_hmac" (navegador)
     */
    public ResultadoPasarela procesarRespuesta(String krAnswer, String krHash, String krHashKey) {
        ResultadoPasarela r = new ResultadoPasarela();
        if (krAnswer == null || krHash == null) {
            return r; // valida=false por defecto
        }

        // La IPN firma con la "password"; el retorno del navegador con la clave HMAC.
        String clave = "password".equalsIgnoreCase(krHashKey)
                ? Config.get("izipay.password")
                : Config.get("izipay.hmac.key");

        String calculada = hmacSha256Hex(krAnswer, clave);
        r.firmaValida = calculada != null && calculada.equalsIgnoreCase(krHash);
        if (!r.firmaValida) {
            return r;
        }

        try {
            JSONObject answer = new JSONObject(krAnswer);
            String orderStatus = answer.optString("orderStatus", "");
            r.autorizado = "PAID".equalsIgnoreCase(orderStatus);

            JSONObject orderDetails = answer.optJSONObject("orderDetails");
            if (orderDetails != null) {
                r.orderId = orderDetails.optString("orderId", null);
            }
            JSONArray transacciones = answer.optJSONArray("transactions");
            if (transacciones != null && transacciones.length() > 0) {
                r.transId = transacciones.getJSONObject(0).optString("uuid", null);
            }
        } catch (Exception e) {
            System.err.println("[Izipay] Error parseando kr-answer: " + e.getMessage());
        }
        return r;
    }

    /** HMAC-SHA256 en hexadecimal (minúsculas) del texto con la clave dada. */
    public static String hmacSha256Hex(String texto, String clave) {
        if (clave == null) return null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(clave.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculando HMAC-SHA256", e);
        }
    }

    /** Resultado del procesamiento de una respuesta de la pasarela. */
    public static class ResultadoPasarela {
        public boolean firmaValida = false;
        public boolean autorizado = false;
        public String orderId;
        public String transId;
    }
}
