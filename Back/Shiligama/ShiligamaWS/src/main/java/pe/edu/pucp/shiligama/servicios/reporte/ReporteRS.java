package pe.edu.pucp.shiligama.servicios.reporte;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.model.operacion.Devolucion;
import pe.edu.pucp.model.operacion.MovimientoInventario;
import pe.edu.pucp.model.venta.TopProductoDto;
import pe.edu.pucp.model.venta.VentaReporteDto;
import pe.edu.pucp.operacion.bo.DevolucionBO;
import pe.edu.pucp.operacion.bo.MovimientoInventarioBO;
import pe.edu.pucp.operacion.impl.DevolucionBoImpl;
import pe.edu.pucp.operacion.impl.MovimientoInventarioBoImpl;
import pe.edu.pucp.venta.bo.VentaBo;
import pe.edu.pucp.venta.impl.VentaBoImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteRS {

    private final VentaBo ventaBo = new VentaBoImpl();
    private final DevolucionBO devolucionBo = new DevolucionBoImpl();
    private final MovimientoInventarioBO movimientoBo = new MovimientoInventarioBoImpl();

    // GET /api/reportes/ventas?fechaInicio=2025-01-01&fechaFin=2025-12-31
    @GET
    @Path("/ventas")
    public Response reporteVentas(@QueryParam("fechaInicio") String fechaInicio,
                                  @QueryParam("fechaFin") String fechaFin) {
        try {
            List<VentaReporteDto> reporte = ventaBo.reporteVentasPorPeriodo(fechaInicio, fechaFin);
            return Response.ok(reporte).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al generar reporte de ventas: " + ex.getMessage()).build();
        }
    }

    // GET /api/reportes/devoluciones?fechaInicio=2025-01-01&fechaFin=2025-12-31
    @GET
    @Path("/devoluciones")
    public Response reporteDevoluciones(@QueryParam("fechaInicio") String fechaInicio,
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
                    .entity("Error al generar reporte de devoluciones: " + ex.getMessage()).build();
        }
    }

    // GET /api/reportes/movimientos?fechaInicio=2025-01-01T00:00:00&fechaFin=2025-12-31T23:59:59
    @GET
    @Path("/movimientos")
    public Response reporteMovimientos(@QueryParam("fechaInicio") String fechaInicio,
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
                    .entity("Error al generar reporte de movimientos: " + ex.getMessage()).build();
        }
    }

    // GET /api/reportes/movimientos/producto/12
    @GET
    @Path("/movimientos/producto/{idProducto}")
    public Response movimientosPorProducto(@PathParam("idProducto") int idProducto) {
        try {
            List<MovimientoInventario> lista = movimientoBo.listarPorProducto(idProducto);
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al obtener movimientos por producto: " + ex.getMessage()).build();
        }
    }

    // GET /api/reportes/top-productos — top 5 más vendidos por unidades
    @GET
    @Path("/top-productos")
    public Response topProductos() {
        try {
            List<TopProductoDto> lista = ventaBo.topProductosVendidos();
            return Response.ok(lista).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener top productos: " + ex.getMessage()).build();
        }
    }
}
