package pe.edu.pucp.shiligama.servicios.reporte;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.operacion.Devolucion;
import pe.edu.pucp.model.operacion.MovimientoInventario;
import pe.edu.pucp.model.reporte.DevolucionParamDTO;
import pe.edu.pucp.model.reporte.TopProductosParamDTO;
import pe.edu.pucp.model.reporte.VentasParamDTO;
import pe.edu.pucp.model.venta.TopProductoDto;
import pe.edu.pucp.model.venta.VentaReporteDto;
import pe.edu.pucp.operacion.bo.DevolucionBO;
import pe.edu.pucp.operacion.bo.MovimientoInventarioBO;
import pe.edu.pucp.operacion.impl.DevolucionBoImpl;
import pe.edu.pucp.operacion.impl.MovimientoInventarioBoImpl;
import pe.edu.pucp.reporte.bo.DevolucionParamBo;
import pe.edu.pucp.reporte.bo.TopProductosParamBo;
import pe.edu.pucp.reporte.bo.VentasParamBo;
import pe.edu.pucp.reporte.impl.DevolucionParamBoImpl;
import pe.edu.pucp.reporte.impl.TopProductosParamBoImpl;
import pe.edu.pucp.reporte.impl.VentasParamBoImpl;
import pe.edu.pucp.shiligama.service.JasperService;
import pe.edu.pucp.venta.bo.VentaBo;
import pe.edu.pucp.venta.impl.VentaBoImpl;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReporteRS {

    @jakarta.ws.rs.core.Context
    private jakarta.servlet.ServletContext servletContext;

    private final VentaBo ventaBo = new VentaBoImpl();
    private final DevolucionBO devolucionBo = new DevolucionBoImpl();
    private final MovimientoInventarioBO movimientoBo = new MovimientoInventarioBoImpl();

    // =========================================================
    // ENDPOINTS JSON EXISTENTES (se mantienen sin cambios)
    // =========================================================

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

    // =========================================================
    // NUEVOS ENDPOINTS PDF (JasperReports)
    // =========================================================

    /**
     * GET /api/reportes/pdf/devoluciones
     * Parámetros: fechaInicio, fechaFin, estado, diasAlerta
     * Ejemplo: /api/reportes/pdf/devoluciones?fechaInicio=2026-04-19&fechaFin=2026-07-19&estado=APROBADO&diasAlerta=30
     */
    @GET
    @Path("/pdf/devoluciones")
    @Produces("application/pdf")
    public Response pdfDevoluciones(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin")    String fechaFin,
            @QueryParam("estado")      String estado,
            @QueryParam("diasAlerta")  Integer diasAlerta) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            DevolucionParamBo bo = new DevolucionParamBoImpl();
            DevolucionParamDTO dto = bo.obtenerYValidarParametros(fechaInicio, fechaFin, estado, diasAlerta);

            String rutaReal = servletContext.getRealPath("/");
            byte[] pdf = new JasperService(con, rutaReal).generateDevolucionesReport(dto);

            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=\"reporte_devoluciones.pdf\"")
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("C:/PROGRA3/TA_ULTIMA_VERSION/Tarea_Academica_Programacion3_Shiligama/error_log.txt", true))) {
                pw.println("--- ERROR AL GENERAR PDF DEVOLUCIONES ---");
                ex.printStackTrace(pw);
                pw.println();
            } catch (Exception ioEx) {
                // ignore
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar PDF de devoluciones: " + ex.getMessage()).build();
        }
    }

    /**
     * GET /api/reportes/pdf/ventas-agrupadas
     * Parámetros: fechaInicio, fechaFin, agrupacion
     * Ejemplo: /api/reportes/pdf/ventas-agrupadas?fechaInicio=2026-04-19&fechaFin=2026-08-19&agrupacion=DIA
     */
    @GET
    @Path("/pdf/ventas-agrupadas")
    @Produces("application/pdf")
    public Response pdfVentasAgrupadas(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin")    String fechaFin,
            @QueryParam("agrupacion")  String agrupacion) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            VentasParamBo bo = new VentasParamBoImpl();
            VentasParamDTO dto = bo.obtenerYValidarParametros(fechaInicio, fechaFin, agrupacion);

            String rutaReal = servletContext.getRealPath("/");
            byte[] pdf = new JasperService(con, rutaReal).generateVentasAgrupadasReport(dto);

            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=\"reporte_ventas_agrupadas.pdf\"")
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("C:/PROGRA3/TA_ULTIMA_VERSION/Tarea_Academica_Programacion3_Shiligama/error_log.txt", true))) {
                pw.println("--- ERROR AL GENERAR PDF VENTAS AGRUPADAS ---");
                ex.printStackTrace(pw);
                pw.println();
            } catch (Exception ioEx) {
                // ignore
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar PDF de ventas agrupadas: " + ex.getMessage()).build();
        }
    }

    /**
     * GET /api/reportes/pdf/top-productos
     * Parámetros: fechaInicio, fechaFin, limite
     * Ejemplo: /api/reportes/pdf/top-productos?fechaInicio=2026-04-19&fechaFin=2026-08-19&limite=20
     */
    @GET
    @Path("/pdf/top-productos")
    @Produces("application/pdf")
    public Response pdfTopProductos(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin")    String fechaFin,
            @QueryParam("limite")      Integer limite) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            TopProductosParamBo bo = new TopProductosParamBoImpl();
            TopProductosParamDTO dto = bo.obtenerYValidarParametros(fechaInicio, fechaFin, limite);

            String rutaReal = servletContext.getRealPath("/");
            byte[] pdf = new JasperService(con, rutaReal).generateTopProductosReport(dto);

            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=\"reporte_top_productos.pdf\"")
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter("C:/PROGRA3/TA_ULTIMA_VERSION/Tarea_Academica_Programacion3_Shiligama/error_log.txt", true))) {
                pw.println("--- ERROR AL GENERAR PDF TOP PRODUCTOS ---");
                ex.printStackTrace(pw);
                pw.println();
            } catch (Exception ioEx) {
                // ignore
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar PDF de top productos: " + ex.getMessage()).build();
        }
    }
}
