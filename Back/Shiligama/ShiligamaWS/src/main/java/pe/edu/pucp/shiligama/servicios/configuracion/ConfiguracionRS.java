package pe.edu.pucp.shiligama.servicios.configuracion;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.configuracion.bo.ConfiguracionBo;
import pe.edu.pucp.configuracion.impl.ConfiguracionBoImpl;
import pe.edu.pucp.model.configuracion.Configuracion;

@Path("/configuracion")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfiguracionRS {

    private final ConfiguracionBo configuracionBo = new ConfiguracionBoImpl();

    // GET /api/configuracion
    @GET
    public Response obtener() {
        try {
            Configuracion cfg = configuracionBo.obtener();
            return Response.ok(cfg).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener configuración: " + ex.getMessage()).build();
        }
    }

    // PUT /api/configuracion
    @PUT
    public Response actualizar(Configuracion cfg) {
        try {
            int filasAfectadas = configuracionBo.actualizar(cfg);
            return Response.ok(filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al actualizar configuración: " + ex.getMessage()).build();
        }
    }
}
