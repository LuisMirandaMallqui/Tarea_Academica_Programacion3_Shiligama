package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.config.Config;
import pe.edu.pucp.venta.bo.PagoBo;
import pe.edu.pucp.venta.impl.PagoBoImpl;
import pe.edu.pucp.venta.pasarela.PasarelaPagoService;

import java.util.HashMap;
import java.util.Map;

/**
 * Callback (IPN) de la pasarela Izipay, equivalente al
 * {@code IzipayCallbackResource} del proyecto de referencia Compu-Rangers.
 *
 * Izipay envía un POST con campos {@code vads_*} firmados. Se valida la firma
 * HMAC-SHA256 con la clave secreta del comercio antes de confirmar el pago.
 *
 * URL: POST http://<host>/shiligamaws-1.0-SNAPSHOT/api/pagos/izipay/callback
 * (esta URL debe ser pública para que Izipay la alcance — p.ej. la instancia EC2).
 */
@Path("/pagos/izipay")
public class IzipayCallbackWS {

    private final PagoBo pagoBo = new PagoBoImpl();

    @POST
    @Path("/callback")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response recibirNotificacion(MultivaluedMap<String, String> form) {
        String claveComercio = Config.get("izipay.merchant.key");
        if (claveComercio == null || claveComercio.isBlank()) {
            System.err.println("[Izipay] Falta 'izipay.merchant.key' en la configuración.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Pasarela no configurada").build();
        }

        String firmaRecibida = form.getFirst("signature");
        if (firmaRecibida == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Falta signature").build();
        }

        // Aplanar la MultivaluedMap a Map<String,String> (primer valor) para la firma.
        Map<String, String> campos = new HashMap<>();
        for (String clave : form.keySet()) {
            campos.put(clave, form.getFirst(clave));
        }

        String firmaCalculada = PasarelaPagoService.calcularFirma(campos, claveComercio);
        if (!firmaRecibida.equals(firmaCalculada)) {
            System.err.println("[Izipay] Firma no válida para order " + form.getFirst("vads_order_id"));
            return Response.status(Response.Status.UNAUTHORIZED).entity("Firma no válida").build();
        }

        String estado = form.getFirst("vads_trans_status");
        String orderId = form.getFirst("vads_order_id");
        String transId = form.getFirst("vads_trans_id");

        try {
            // Izipay marca AUTHORISED / CAPTURED como pago exitoso.
            boolean autorizado = "AUTHORISED".equalsIgnoreCase(estado)
                    || "CAPTURED".equalsIgnoreCase(estado);
            pagoBo.procesarResultadoPasarela(orderId, autorizado, transId);
            System.out.printf("[Izipay] Order %s procesado con estado %s%n", orderId, estado);
        } catch (Exception ex) {
            System.err.println("[Izipay] Error procesando callback: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error procesando el pago").build();
        }

        return Response.ok("OK").build();
    }
}
