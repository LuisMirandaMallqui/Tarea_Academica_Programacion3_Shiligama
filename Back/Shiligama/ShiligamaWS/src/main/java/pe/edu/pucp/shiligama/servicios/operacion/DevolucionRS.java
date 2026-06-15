package pe.edu.pucp.shiligama.servicios.operacion;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.operacion.Devolucion;
import pe.edu.pucp.operacion.bo.DevolucionBO;
import pe.edu.pucp.operacion.impl.DevolucionBoImpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/devoluciones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DevolucionRS {

    private final DevolucionBO devolucionBo = new DevolucionBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Devolucion> devoluciones = devolucionBo.listarTodos();
            return Response.ok(devoluciones).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar devoluciones: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Devolucion devolucion = devolucionBo.buscarPorId(id);
            if (devolucion != null) {
                return Response.ok(devolucion).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Devolución con ID " + id + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar devolución: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Devolucion devolucion) {
        try {
            int idGenerado = devolucionBo.insertar(devolucion);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar la devolución.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar devolución: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Devolucion devolucion) {
        try {
            int filasAfectadas = devolucionBo.modificar(devolucion);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar devolución: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = devolucionBo.eliminar(id);
            return Response.ok("Devolución eliminada. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar devolución: " + ex.getMessage()).build();
        }
    }

    // Listar devoluciones en un rango de fechas
    // GET /api/devoluciones/por-fechas?fechaInicio=2025-01-01&fechaFin=2025-12-31
    @GET
    @Path("/por-fechas")
    public Response listarPorFechas(@QueryParam("fechaInicio") String fechaInicio,
                                    @QueryParam("fechaFin") String fechaFin) {
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            List<Devolucion> lista = devolucionBo.listarPorFechas(inicio, fin);
            return Response.ok(lista).build();
        } catch (DateTimeParseException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Usar yyyy-MM-dd.").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al listar devoluciones por fechas: " + ex.getMessage()).build();
        }
    }
}
