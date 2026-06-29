package pe.edu.pucp.shiligama.controller;

import pe.edu.pucp.shiligama.service.JasperService;
import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.reporte.DevolucionParamDTO;
import pe.edu.pucp.model.reporte.VentasParamDTO;
import pe.edu.pucp.model.reporte.TopProductosParamDTO;
import pe.edu.pucp.reporte.bo.DevolucionParamBo;
import pe.edu.pucp.reporte.bo.VentasParamBo;
import pe.edu.pucp.reporte.bo.TopProductosParamBo;
import pe.edu.pucp.reporte.impl.DevolucionParamBoImpl;
import pe.edu.pucp.reporte.impl.VentasParamBoImpl;
import pe.edu.pucp.reporte.impl.TopProductosParamBoImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

/**
 * Servlet expuesto en /reportes/* (evita /api/* para no entrar en conflicto con JAX-RS/Jersey y causar 404 en GlassFish).
 */
@WebServlet("/reportes/*")
public class ReporteServlet extends HttpServlet {

    private JasperService jasperService;

    @Override
    public void init() throws ServletException {
        try {
            // 🔥 Obtener la ruta real física del servidor para los reportes y recursos
            String rutaReal = getServletContext().getRealPath("/");
            System.out.println("📌 Ruta real del servidor: " + rutaReal);

            Connection connection = DBManager.getInstance().getConnection();
            this.jasperService = new JasperService(connection, rutaReal);

        } catch (Exception e) {
            throw new ServletException("Error al inicializar ReporteServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta especificar la ruta del reporte");
            return;
        }

        try {
            byte[] pdfBytes = null;

            switch (path) {
                case "/devoluciones":
                    String fInicioDev = request.getParameter("fechaInicio");
                    String fFinDev = request.getParameter("fechaFin");
                    String estadoDev = request.getParameter("estado");
                    Integer diasDev = request.getParameter("diasAlerta") != null ? Integer.parseInt(request.getParameter("diasAlerta")) : null;

                    DevolucionParamBo devBo = new DevolucionParamBoImpl();
                    DevolucionParamDTO devDto = devBo.obtenerYValidarParametros(fInicioDev, fFinDev, estadoDev, diasDev);

                    pdfBytes = jasperService.generateDevolucionesReport(devDto);
                    break;

                case "/ventas-agrupadas":
                    String fInicioVta = request.getParameter("fechaInicio");
                    String fFinVta = request.getParameter("fechaFin");
                    String agrupacionVta = request.getParameter("agrupacion");

                    VentasParamBo vtaBo = new VentasParamBoImpl();
                    VentasParamDTO vtaDto = vtaBo.obtenerYValidarParametros(fInicioVta, fFinVta, agrupacionVta);

                    pdfBytes = jasperService.generateVentasAgrupadasReport(vtaDto);
                    break;

                case "/top-productos":
                    String fInicioTop = request.getParameter("fechaInicio");
                    String fFinTop = request.getParameter("fechaFin");
                    Integer limiteTop = request.getParameter("limite") != null ? Integer.parseInt(request.getParameter("limite")) : null;

                    TopProductosParamBo topBo = new TopProductosParamBoImpl();
                    TopProductosParamDTO topDto = topBo.obtenerYValidarParametros(fInicioTop, fFinTop, limiteTop);

                    pdfBytes = jasperService.generateTopProductosReport(topDto);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reporte no encontrado: " + path);
                    return;
            }

            if (pdfBytes == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo generar el PDF");
                return;
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");
            response.setContentLength(pdfBytes.length);
            response.getOutputStream().write(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
