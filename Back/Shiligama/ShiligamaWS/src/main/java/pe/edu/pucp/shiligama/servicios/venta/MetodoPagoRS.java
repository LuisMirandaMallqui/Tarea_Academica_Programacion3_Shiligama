package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.venta.MetodoPago;
import pe.edu.pucp.venta.bo.MetodoPagoBo;
import pe.edu.pucp.venta.impl.MetodoPagoBoImpl;

import java.util.List;

@Path("/metodos-pago")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MetodoPagoRS {

    private final MetodoPagoBo metodoPagoBo = new MetodoPagoBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<MetodoPago> metodos = metodoPagoBo.listarTodos();
            return Response.ok(metodos).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar métodos de pago: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            MetodoPago metodo = metodoPagoBo.buscarPorId(id);
            if (metodo != null) {
                return Response.ok(metodo).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Método de pago con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar método de pago: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(MetodoPago metodo) {
        try {
            int idGenerado = metodoPagoBo.insertar(metodo);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el método de pago.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar método de pago: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(MetodoPago metodo) {
        try {
            int filasAfectadas = metodoPagoBo.modificar(metodo);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar método de pago: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = metodoPagoBo.eliminar(id);
            return Response.ok("Método de pago eliminado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar método de pago: " + ex.getMessage()).build();
        }
    }
}
