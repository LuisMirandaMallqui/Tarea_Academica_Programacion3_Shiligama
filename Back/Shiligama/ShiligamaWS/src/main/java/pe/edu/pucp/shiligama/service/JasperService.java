package pe.edu.pucp.shiligama.service;

import pe.edu.pucp.model.reporte.DevolucionParamDTO;
import pe.edu.pucp.model.reporte.VentasParamDTO;
import pe.edu.pucp.model.reporte.TopProductosParamDTO;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRLoader;
// JasperReports 7 Exporters
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class JasperService {
    private Connection connection;
    private String rutaReal;  // Ruta física del servidor

    public JasperService(Connection connection, String rutaReal) {
        this.connection = connection;
        this.rutaReal = rutaReal;
    }

    private byte[] generatePDF(String reportName, Map<String, Object> params) throws Exception {
        // Asegurar que la ruta real termine en separador
        String baseDir = rutaReal;
        if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
            baseDir += File.separator;
        }
        
        // Cargar el .jasper desde la ruta física del servidor
        String rutaCompleta = baseDir + "reportes" + File.separator + reportName + ".jasper";
        System.out.println("📌 Cargando reporte desde: " + rutaCompleta);

        File reportFile = new File(rutaCompleta);
        if (!reportFile.exists()) {
            throw new JRException("Reporte no encontrado: " + rutaCompleta);
        }

        JasperReport report = (JasperReport) JRLoader.loadObject(reportFile);
        JasperPrint print = JasperFillManager.fillReport(report, params, connection);

        // Exportar a PDF usando las APIs de JasperReports 7
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.setConfiguration(new SimplePdfExporterConfiguration());
        exporter.exportReport();

        return baos.toByteArray();
    }

    // Cargar la imagen desde la ruta física
    private Image cargarLogo() {
        try {
            String baseDir = rutaReal;
            if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
                baseDir += File.separator;
            }
            String rutaImagen = baseDir + "images" + File.separator + "logo.png";
            File imagenFile = new File(rutaImagen);
            if (imagenFile.exists()) {
                return ImageIO.read(imagenFile);
            } else {
                System.err.println("⚠️ Logo no encontrado en: " + rutaImagen);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ============================================================
    // REPORTE 1: MERMAS / DEVOLUCIONES
    // ============================================================
    public byte[] generateDevolucionesReport(DevolucionParamDTO dto) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("FECHA_INICIO",       dto.getFechaInicio());
        params.put("FECHA_FIN",          dto.getFechaFin());
        params.put("ESTADO",             dto.getEstado());
        params.put("DIAS_ALERTA",        dto.getDiasAlerta());
        
        Image logo = cargarLogo();
        if (logo != null) {
            params.put("Logo", logo);
        }
        
        params.put("TOTAL_PERDIDA_REAL", dto.getTotalPerdidaReal());
        params.put("TOTAL_RIESGO",       dto.getTotalRiesgo());
        params.put("TOTAL_DEV",          dto.getTotalDev());
        params.put("Aprobados",          dto.getAprobados());
        params.put("Pendientes",         dto.getPendientes());
        params.put("TotalDev",           dto.getTotalDevSec1());
        
        return generatePDF("Reporte_Mermas", params);
    }

    // ============================================================
    // REPORTE 2: VENTAS AGRUPADAS
    // ============================================================
    public byte[] generateVentasAgrupadasReport(VentasParamDTO dto) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("fechaInicio",    dto.getFechaInicio());
        params.put("fechaFin",       dto.getFechaFin());
        params.put("agrupacion",     dto.getAgrupacion());
        
        Image logo = cargarLogo();
        if (logo != null) {
            params.put("Logo", logo);
        }
        
        params.put("SumaPresencial", dto.getSumaPresencial());
        params.put("SumaWeb",        dto.getSumaWeb());
        params.put("VentaTotal",     dto.getVentaTotal());
        
        return generatePDF("ReporteVentas", params);
    }

    // ============================================================
    // REPORTE 3: TOP PRODUCTOS
    // ============================================================
    public byte[] generateTopProductosReport(TopProductosParamDTO dto) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("fechaInicio",    dto.getFechaInicio());
        params.put("fechaFin",       dto.getFechaFin());
        params.put("limite",         dto.getLimite());
        
        Image logo = cargarLogo();
        if (logo != null) {
            params.put("Logo", logo);
        }
        
        params.put("SumaPresencial", dto.getSumaPresencial());
        params.put("SumaWeb",        dto.getSumaWeb());
        params.put("VentaTotal",     dto.getVentaTotal());
        
        return generatePDF("TopProductos", params);
    }
}
