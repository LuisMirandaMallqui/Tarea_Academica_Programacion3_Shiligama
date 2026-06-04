package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.venta.DetallePedido;
import pe.edu.pucp.venta.bo.DetallePedidoBo;
import pe.edu.pucp.venta.impl.DetallePedidoBoImpl;

import java.util.List;

/**
 * Web Service REST para Detalles de Pedido (líneas de cada pedido).
 * URL base: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/detalles-pedido
 */
@Path("/detalles-pedido")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DetallePedidoWS {

    private final DetallePedidoBo detallePedidoBo = new DetallePedidoBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<DetallePedido> detalles = detallePedidoBo.listarTodos();
            return Response.ok(detalles).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar detalles de pedido: " + ex.getMessage()).build();
        }
    }

    /** GET /api/detalles-pedido/por-pedido/{idPedido} */
    @GET
    @Path("/por-pedido/{idPedido}")
    public Response listarPorPedido(@PathParam("idPedido") int idPedido) {
        try {
            List<DetallePedido> detalles = detallePedidoBo.listarPorPedido(idPedido);
            return Response.ok(detalles).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar detalles del pedido: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            DetallePedido detalle = detallePedidoBo.buscarPorID(id);
            if (detalle != null) {
                return Response.ok(detalle).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Detalle de pedido con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar detalle de pedido: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(DetallePedido detalle) {
        try {
            int idGenerado = detallePedidoBo.insertar(detalle);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el detalle de pedido.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar detalle de pedido: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(DetallePedido detalle) {
        try {
            int filasAfectadas = detallePedidoBo.modificar(detalle);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar detalle de pedido: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = detallePedidoBo.eliminar(id);
            return Response.ok("Detalle eliminado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar detalle de pedido: " + ex.getMessage()).build();
        }
    }
}
