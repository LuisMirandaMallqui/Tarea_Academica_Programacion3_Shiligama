package pe.edu.pucp.persistance.dao.reporte.dao;

public interface TopProductosParamDao {
    String obtenerLogo();
    Integer obtenerLimiteDefecto();
    Double obtenerSumaPorCanal(String fechaInicio, String fechaFin, String canal);
}
