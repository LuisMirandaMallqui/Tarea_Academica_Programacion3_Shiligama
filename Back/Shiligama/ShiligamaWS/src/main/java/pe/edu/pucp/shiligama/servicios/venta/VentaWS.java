package pe.edu.pucp.shiligama.servicios.venta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.model.venta.VentaReporteDto;
import pe.edu.pucp.venta.bo.VentaBo;
import pe.edu.pucp.venta.impl.VentaBoImpl;

import java.util.List;

/**
 * Web Service REST para Ventas.
 * URL base: http://localhost:8080/shiligamaws-1.0-SNAPSHOT/api/ventas
 */
@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaWS {

    private final VentaBo ventaBo = new VentaBoImpl();

    @GET
    public Response listarTodos() {
        try {
            List<Venta> ventas = ventaBo.listarTodos();
            return Response.ok(ventas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al listar ventas: " + ex.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Venta venta = ventaBo.buscarPorID(id);
            if (venta != null) {
                return Response.ok(venta).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Venta con ID " + id + " no encontrada.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al buscar venta: " + ex.getMessage()).build();
        }
    }

    @POST
    public Response insertar(Venta venta) {
        try {
            int idGenerado = ventaBo.insertar(venta);
            if (idGenerado > 0) {
                return Response.status(Response.Status.CREATED).entity(idGenerado).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No se pudo insertar la venta.").build();
            }
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al insertar venta: " + ex.getMessage()).build();
        }
    }

    @PUT
    public Response modificar(Venta venta) {
        try {
            int filasAfectadas = ventaBo.modificar(venta);
            return Response.ok("Filas modificadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al modificar venta: " + ex.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            int filasAfectadas = ventaBo.eliminar(id);
            return Response.ok("Venta eliminada. Filas afectadas: " + filasAfectadas).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Error al eliminar venta: " + ex.getMessage()).build();
        }
    }

    // Reporte de ventas por período
    // GET /api/ventas/reporte?fechaInicio=2025-01-01&fechaFin=2025-12-31
    @GET
    @Path("/reporte")
    public Response reporteVentasPorPeriodo(@QueryParam("fechaInicio") String fechaInicio,
                                            @QueryParam("fechaFin") String fechaFin) {
        try {
            List<VentaReporteDto> reporte = ventaBo.reporteVentasPorPeriodo(fechaInicio, fechaFin);
            return Response.ok(reporte).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al generar reporte de ventas: " + ex.getMessage()).build();
        }
    }
}
