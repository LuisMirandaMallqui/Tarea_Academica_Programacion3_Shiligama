package pe.edu.pucp.shiligama.repository;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

public class JasperRepository {
    private Connection connection;

    public JasperRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Carga el reporte compilado (.jasper) desde el classpath.
     * Los .jasper ya están validados y funcionando, se usan directamente.
     */
    public JasperReport loadReport(String reportName) throws JRException {
        InputStream reportStream = getClass().getResourceAsStream("/Reportes/" + reportName + ".jasper");
        if (reportStream == null) {
            throw new JRException("Reporte no encontrado: /Reportes/" + reportName + ".jasper");
        }
        return (JasperReport) JRLoader.loadObject(reportStream);
    }

    public JasperPrint fillReport(JasperReport report, Map<String, Object> parameters) throws JRException {
        return JasperFillManager.fillReport(report, parameters, connection);
    }
}
