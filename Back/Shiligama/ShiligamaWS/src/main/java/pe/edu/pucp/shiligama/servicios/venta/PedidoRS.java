package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.venta.bo.PedidoBo;
import pe.edu.pucp.venta.impl.PedidoBoImpl;

import java.util.List;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoRS {

    private final PedidoBo pedidoBo = new PedidoBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Pedido> pedidos = pedidoBo.listarTodos();
            return Response.ok(pedidos).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar pedidos: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Pedido pedido = pedidoBo.buscarPorId(id);
            if (pedido != null) {
                return Response.ok(pedido).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Pedido con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar pedido: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Pedido pedido) {
        try {
            int idGenerado = pedidoBo.insertar(pedido);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el pedido.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar pedido: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Pedido pedido) {
        try {
            int filasAfectadas = pedidoBo.modificar(pedido);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar pedido: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = pedidoBo.eliminar(id);
            return Response.ok("Pedido eliminado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar pedido: " + ex.getMessage()).build();
        }
    }

    // GET /api/pedidos/por-cliente/15  — para pantalla "Mis pedidos" del cliente
    @GET
    @Path("/por-cliente/{idCliente}")
    public Response listarPorCliente(@PathParam("idCliente") int idCliente) {
        try {
            List<Pedido> lista = pedidoBo.listarPorCliente(idCliente);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar pedidos del cliente: " + ex.getMessage()).build();
        }
    }

    // GET /api/pedidos/por-estado/RECIBIDO  — panel pedidos del admin
    @GET
    @Path("/por-estado/{estado}")
    public Response listarPorEstado(@PathParam("estado") String estado) {
        try {
            EstadoPedido estadoEnum = EstadoPedido.valueOf(estado.toUpperCase());
            List<Pedido> lista = pedidoBo.listarPorEstado(estadoEnum);
            return Response.ok(lista).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Estado inválido. Valores: RECIBIDO, EN_PROCESO, ATENDIDO, RECHAZADO, CANCELADO.")
                    .build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar pedidos por estado: " + ex.getMessage()).build();
        }
    }
}
