package pe.edu.pucp.venta.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import pe.edu.pucp.config.Config;
import pe.edu.pucp.model.nubefact.NubefactRequestDTO;
import pe.edu.pucp.model.nubefact.NubefactResponseDTO;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NubefactService {

    private final String url;
    private final String token;
    private final int timeout;
    private final HttpClient httpClient;
    private static final Jsonb jsonb = JsonbBuilder.create();

    public NubefactService() {
        this.url = limpiar(Config.get("nubefact.api.url",
                "https://api.nubefact.com/api/v1/7d4e33d2-b63e-45ef-8ee1-55d1ea26f537"));
        this.token = limpiar(Config.get("nubefact.token", ""));
        this.timeout = Integer.parseInt(Config.get("nubefact.timeout", "60"));
        boolean trustAllSsl = Boolean.parseBoolean(Config.get("nubefact.ssl.trust.all", "true"));
        this.httpClient = crearHttpClient(timeout, trustAllSsl);
    }

    /**
     * Consulta el último comprobante emitido en Nubefact para una serie dada
     * y retorna el siguiente número disponible (último + 1).
     * Si no hay comprobantes previos, retorna 1.
     */
    public int consultarUltimoNumero(String serie) throws Exception {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("El token de Nubefact no está configurado (nubefact.token).");
        }
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("La RUTA de Nubefact es inválida.");
        }

        String consultUrl = url + "?serie=" + java.net.URLEncoder.encode(serie, "UTF-8");
        System.out.println("[NubefactService] Consultando último número: " + consultUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(consultUrl))
                .timeout(Duration.ofSeconds(timeout))
                .header("Authorization", "Token token=\"" + token + "\"")
                .GET()
                .build();

        HttpResponse<String> httpResponse;
        try {
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo conectar con Nubefact para consulta: " + mensaje(ex), ex);
        }

        System.out.println("[NubefactService] Consulta status: " + httpResponse.statusCode());
        System.out.println("[NubefactService] Consulta body: " + httpResponse.body());

        if (httpResponse.statusCode() == 200) {
            try {
                JSONObject json = new JSONObject(httpResponse.body());
                int ultimoNumero = json.optInt("numero", 0);
                System.out.println("[NubefactService] Último número para " + serie + ": " + ultimoNumero);
                return Math.max(ultimoNumero + 1, 1);
            } catch (Exception ex) {
                throw new RuntimeException("Respuesta de Nubefact no reconocida: " + httpResponse.body(), ex);
            }
        }

        // Si la serie no tiene comprobantes, Nubefact retorna 422 o similar
        if (httpResponse.statusCode() == 422 || httpResponse.statusCode() == 404) {
            System.out.println("[NubefactService] Serie " + serie + " sin comprobantes previos");
            return 1;
        }

        throw new RuntimeException("Error al consultar Nubefact. HTTP status: "
                + httpResponse.statusCode() + ", body: " + httpResponse.body());
    }

    public NubefactResponseDTO enviarComprobante(NubefactRequestDTO request) throws Exception {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("El token de Nubefact no está configurado (nubefact.token).");
        }
        if (url == null || url.isBlank() || url.endsWith("/comprobante")) {
            throw new IllegalStateException(
                    "La RUTA de Nubefact es inválida. Copia la URL exacta desde Nubefact → API (Integración).");
        }

        request.setSunatTransaction(1);
        request.setEnviarAutomaticamenteALaSunat(true);

        String jsonPayload = jsonb.toJson(request);
        System.out.println("[NubefactService] URL: " + url);
        System.out.println("[NubefactService] Sending payload: " + jsonPayload);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeout))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Authorization", "Token token=\"" + token + "\"")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> httpResponse;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (ConnectException ex) {
            throw new RuntimeException(
                    "No se pudo conectar con Nubefact en " + url
                            + ". Verifique internet/firewall del servidor y que la RUTA sea la de su cuenta.",
                    ex);
        } catch (HttpTimeoutException ex) {
            throw new RuntimeException(
                    "Tiempo de espera agotado al conectar con Nubefact (" + timeout + "s).", ex);
        } catch (SSLException ex) {
            throw new RuntimeException(
                    "Error SSL al conectar con Nubefact. Active nubefact.ssl.trust.all=true o importe el certificado al JDK.",
                    ex);
        } catch (IOException ex) {
            if (esErrorSsl(ex)) {
                throw new RuntimeException(
                        "Error SSL al conectar con Nubefact. Active nubefact.ssl.trust.all=true o importe el certificado al JDK.",
                        ex);
            }
            throw new RuntimeException("Error de red al conectar con Nubefact: " + mensaje(ex), ex);
        }

        String body = httpResponse.body();

        System.out.println("[NubefactService] Response status code: " + httpResponse.statusCode());
        System.out.println("[NubefactService] Response body: " + body);

        if (httpResponse.statusCode() == 401) {
            throw new RuntimeException("Error de autenticación Nubefact: token o ruta inválidos. " + body);
        }

        if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 422) {
            return parseResponse(body);
        }

        throw new RuntimeException("Error al comunicarse con Nubefact API. HTTP status: "
                + httpResponse.statusCode() + ", body: " + body);
    }

    private static HttpClient crearHttpClient(int timeout, boolean trustAllSsl) {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeout))
                .followRedirects(HttpClient.Redirect.NORMAL);

        if (trustAllSsl) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{
                        new X509TrustManager() {
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            }

                            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            }

                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                            }
                        }
                }, new SecureRandom());
                builder.sslContext(sslContext);
            } catch (Exception ex) {
                throw new IllegalStateException("No se pudo configurar SSL para Nubefact", ex);
            }
        }

        return builder.build();
    }

    private static boolean esErrorSsl(Throwable ex) {
        Throwable actual = ex;
        while (actual != null) {
            if (actual instanceof SSLException) {
                return true;
            }
            actual = actual.getCause();
        }
        return false;
    }

    private static String mensaje(Throwable ex) {
        return ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
    }

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private NubefactResponseDTO parseResponse(String body) {
        if (body == null || body.isBlank()) {
            throw new RuntimeException("Nubefact respondió sin contenido JSON.");
        }
        try {
            return jsonb.fromJson(body, NubefactResponseDTO.class);
        } catch (Exception ex) {
            System.out.println("[NubefactService] JSON-B parse fallback: " + ex.getMessage());
            try {
                return mapResponse(new JSONObject(body));
            } catch (Exception jsonEx) {
                throw new RuntimeException("No se pudo interpretar la respuesta de Nubefact: " + body, jsonEx);
            }
        }
    }

    private NubefactResponseDTO mapResponse(JSONObject json) {
        NubefactResponseDTO response = new NubefactResponseDTO();
        response.setEnlace(json.optString("enlace", null));
        response.setEnlaceDelPdf(json.optString("enlace_del_pdf", null));
        response.setEnlaceDelXml(json.optString("enlace_del_xml", null));
        response.setEnlaceDelCdr(json.optString("enlace_del_cdr", null));
        response.setCadenaParaCodigoQr(json.optString("cadena_para_codigo_qr", null));
        response.setCodigoHash(json.optString("codigo_hash", null));
        response.setAceptadaPorSunat(json.optBoolean("aceptada_por_sunat", false));
        response.setSunatDescription(json.optString("sunat_description", null));
        response.setSunatResponsecode(json.optString("sunat_responsecode", null));
        response.setErrors(extraerErrores(json));
        return response;
    }

    private String extraerErrores(JSONObject json) {
        if (!json.has("errors") || json.isNull("errors")) {
            return null;
        }
        Object errors = json.get("errors");
        if (errors instanceof JSONArray arr) {
            return arr.toString();
        }
        String texto = String.valueOf(errors).trim();
        return texto.isEmpty() || "null".equalsIgnoreCase(texto) ? null : texto;
    }
}
