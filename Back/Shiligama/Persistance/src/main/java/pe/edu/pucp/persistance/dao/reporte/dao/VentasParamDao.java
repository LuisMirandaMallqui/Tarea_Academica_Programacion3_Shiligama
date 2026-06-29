package pe.edu.pucp.persistance.dao.reporte.dao;

public interface VentasParamDao {
    String obtenerLogo();
    String obtenerAgrupacionDefecto();
    Double obtenerSumaPorCanal(String fechaInicio, String fechaFin, String canal);
}
