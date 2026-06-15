package pe.edu.pucp.shiligama.servicios.operacion;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.operacion.MovimientoInventario;
import pe.edu.pucp.operacion.bo.MovimientoInventarioBO;
import pe.edu.pucp.operacion.impl.MovimientoInventarioBoImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/movimientos-inventario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovimientoInventarioRS {

    private final MovimientoInventarioBO movimientoBo = new MovimientoInventarioBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<MovimientoInventario> movimientos = movimientoBo.listarTodos();
            return Response.ok(movimientos).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar movimientos: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            MovimientoInventario mov = movimientoBo.buscarPorId(id);
            if (mov != null) {
                return Response.ok(mov).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Movimiento con ID " + id + " no encontrado.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar movimiento: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(MovimientoInventario mov) {
        try {
            int idGenerado = movimientoBo.insertar(mov);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar el movimiento.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar movimiento: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(MovimientoInventario mov) {
        try {
            int filasAfectadas = movimientoBo.modificar(mov);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar movimiento: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = movimientoBo.eliminar(id);
            return Response.ok("Movimiento eliminado. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar movimiento: " + ex.getMessage()).build();
        }
    }

    // Listar movimientos por producto
    // GET /api/movimientos-inventario/por-producto/12
    @GET
    @Path("/por-producto/{idProducto}")
    public Response listarPorProducto(@PathParam("idProducto") int idProducto) {
        try {
            List<MovimientoInventario> lista = movimientoBo.listarPorProducto(idProducto);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar movimientos por producto: " + ex.getMessage()).build();
        }
    }

    // Listar movimientos por rango de fechas
    // GET /api/movimientos-inventario/por-fechas?fechaInicio=2025-01-01T00:00:00&fechaFin=2025-12-31T23:59:59
    @GET
    @Path("/por-fechas")
    public Response listarPorFechas(@QueryParam("fechaInicio") String fechaInicio,
                                    @QueryParam("fechaFin") String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);
            List<MovimientoInventario> lista = movimientoBo.listarPorFechas(inicio, fin);
            return Response.ok(lista).build();
        } catch (DateTimeParseException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Usar yyyy-MM-ddTHH:mm:ss.").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar movimientos por fechas: " + ex.getMessage()).build();
        }
    }
}
