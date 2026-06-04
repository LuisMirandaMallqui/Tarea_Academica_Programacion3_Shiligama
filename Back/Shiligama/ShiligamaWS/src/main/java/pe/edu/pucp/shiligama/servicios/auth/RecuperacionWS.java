package pe.edu.pucp.shiligama.servicios.auth;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.correo.EnvioCorreo;
import pe.edu.pucp.model.seguridad.RestablecerPasswordDto;
import pe.edu.pucp.model.seguridad.SolicitarRecuperacionDto;
import pe.edu.pucp.usuario.bo.RecuperacionBo;
import pe.edu.pucp.usuario.impl.RecuperacionBoImpl;
import java.util.List;

/**
 * Web Service REST para la recuperación de contraseña por correo.
 *
 * URL base: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/recuperacion
 *
 *  - POST /api/recuperacion/solicitar   body: { "correo": "..." }
 *      Envía (si la cuenta existe) un correo con el enlace de restablecimiento.
 *  - POST /api/recuperacion/restablecer body: { "token": "...", "nuevaContrasena": "..." }
 *      Valida el token y actualiza la contraseña.
 */
@Path("/recuperacion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecuperacionWS {

    private final RecuperacionBo recuperacionBo = new RecuperacionBoImpl();

    /**
     * GET /api/recuperacion/test-correo?email=tu@correo.com
     * Envía un correo de prueba y devuelve OK o el error SMTP exacto.
     * SOLO para desarrollo local — eliminar antes de producción.
     */
    @GET
    @Path("/test-correo")
    @Produces(MediaType.TEXT_PLAIN)
    public Response testCorreo(@QueryParam("email") String email) {
        if (email == null || email.isBlank()) {
            return Response.status(400).entity("Falta ?email=tu@correo.com").build();
        }
        try {
            EnvioCorreo.getInstance().enviarEmail(
                List.of(email),
                "Shiligama — Test SMTP",
                "<h2>✅ El correo SMTP funciona correctamente.</h2>"
                + "<p>Si ves este mensaje, la configuración de Gmail está bien.</p>"
            );
            return Response.ok("Correo enviado exitosamente a " + email).build();
        } catch (Exception ex) {
            return Response.status(500)
                    .entity("ERROR SMTP: " + ex.getMessage()).build();
        }
    }

    @POST
    @Path("/solicitar")
    public Response solicitar(SolicitarRecuperacionDto dto) {
        try {
            if (dto == null || dto.getCorreo() == null || dto.getCorreo().isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("El correo es obligatorio.").build();
            }
            recuperacionBo.solicitarRecuperacion(dto.getCorreo());
            // Respuesta genérica: no revela si el correo está registrado.
            return Response.ok("Si el correo está registrado, se enviaron las instrucciones de recuperación.")
                    .build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al solicitar la recuperación: " + ex.getMessage()).build();
        }
    }

    @POST
    @Path("/restablecer")
    public Response restablecer(RestablecerPasswordDto dto) {
        try {
            if (dto == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datos obligatorios.").build();
            }
            recuperacionBo.restablecerContrasena(dto.getToken(), dto.getNuevaContrasena());
            return Response.ok("La contraseña se restableció correctamente.").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No se pudo restablecer la contraseña: " + ex.getMessage()).build();
        }
    }
}
