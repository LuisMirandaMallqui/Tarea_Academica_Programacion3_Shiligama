package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.venta.bo.PagoBo;
import pe.edu.pucp.venta.impl.PagoBoImpl;
import pe.edu.pucp.venta.pasarela.PasarelaPagoService;
import pe.edu.pucp.venta.pasarela.PasarelaPagoService.ResultadoPasarela;

/**
 * Notificación de pago de Izipay (IPN, API REST V4).
 *
 * Izipay envía un POST {@code application/x-www-form-urlencoded} con los campos
 * {@code kr-answer} (JSON del resultado), {@code kr-hash} (firma) y
 * {@code kr-hash-key} (indica con qué clave se firmó). Se valida la firma
 * HMAC-SHA256 antes de confirmar el pago.
 *
 * URL: POST http://<host>/shiligamaws-1.0-SNAPSHOT/api/pagos/izipay/callback
 * (debe ser pública para que Izipay la alcance — configúrala en el Back Office;
 *  en local usa ngrok, en producción tu instancia EC2).
 */
@Path("/pagos/izipay")
public class IzipayCallbackWS {

    private final PagoBo pagoBo = new PagoBoImpl();
    private final PasarelaPagoService pasarela = new PasarelaPagoService();

    @POST
    @Path("/callback")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response recibirNotificacion(MultivaluedMap<String, String> form) {
        String krAnswer  = form.getFirst("kr-answer");
        String krHash    = form.getFirst("kr-hash");
        String krHashKey = form.getFirst("kr-hash-key"); // "password" (IPN) | "sha256_hmac"

        if (krAnswer == null || krHash == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Faltan kr-answer / kr-hash").build();
        }

        ResultadoPasarela resultado = pasarela.procesarRespuesta(krAnswer, krHash, krHashKey);
        if (!resultado.firmaValida) {
            System.err.println("[Izipay] Firma IPN no válida.");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Firma no válida").build();
        }

        try {
            pagoBo.procesarResultadoPasarela(resultado.orderId, resultado.autorizado, resultado.transId);
            System.out.printf("[Izipay] Order %s procesado (autorizado=%s)%n",
                    resultado.orderId, resultado.autorizado);
        } catch (Exception ex) {
            System.err.println("[Izipay] Error procesando callback: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error procesando el pago").build();
        }

        return Response.ok("OK").build();
    }
}
