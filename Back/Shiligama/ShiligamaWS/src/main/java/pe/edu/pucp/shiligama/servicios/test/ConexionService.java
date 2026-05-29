package pe.edu.pucp.shiligama.servicios.test;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.db.DBManager;

@Path("/conexion") // Ruta específica para este recurso
public class ConexionService {

    @GET // Indicamos que responderá a peticiones HTTP GET
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8") // Tipo de respuesta (Texto plano)
    public Response probarConexionBD() {
        try {
            // Intentamos obtener una conexión directa usando tu DBManager
            java.sql.Connection con = DBManager.getInstance().getConnection();

            if (con != null && !con.isClosed()) {
                con.close(); // La cerramos de inmediato, solo queríamos probar

                // Retornamos un HTTP 200 OK con el mensaje de éxito
                return Response.ok("CONEXIÓN EXITOSA").build();
            } else {
                // Retornamos un HTTP 500 indicando el error interno
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error: La conexión retornó nula o cerrada.")
                        .build();
            }
        } catch (Exception e) {
            // Capturamos TODO el error y lo volvemos un texto largo para depurar
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);

            // Retornamos un HTTP 500 con el StackTrace completo
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("FALLÓ LA CONEXIÓN. DETALLE DEL ERROR:\n" + sw.toString())
                    .build();
        }
    }
}