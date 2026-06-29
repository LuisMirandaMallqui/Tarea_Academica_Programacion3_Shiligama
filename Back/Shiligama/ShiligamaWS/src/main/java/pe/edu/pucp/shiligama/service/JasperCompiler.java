package pe.edu.pucp.shiligama.service;

import net.sf.jasperreports.engine.JasperCompileManager;
import java.io.File;

public class JasperCompiler {
    public static void main(String[] args) {
        try {
            String basePath = "src/main/webapp/reportes/";
            String[] reports = {"Reporte_Mermas", "ReporteVentas", "TopProductos"};
            
            for (String report : reports) {
                String jrxmlPath = basePath + report + ".jrxml";
                String jasperPath = basePath + report + ".jasper";
                
                File jrxmlFile = new File(jrxmlPath);
                if (jrxmlFile.exists()) {
                    System.out.println("Compiling: " + jrxmlPath);
                    JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
                    System.out.println("Generated: " + jasperPath);
                } else {
                    System.err.println("File not found: " + jrxmlPath);
                }
            }
            System.out.println("Compilation successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
