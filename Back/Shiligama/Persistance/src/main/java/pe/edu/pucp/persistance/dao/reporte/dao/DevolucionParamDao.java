package pe.edu.pucp.persistance.dao.reporte.dao;

import java.math.BigDecimal;

public interface DevolucionParamDao {
    String obtenerLogo();
    Integer obtenerDiasAlertaDefecto();
    BigDecimal obtenerTotalPerdidaReal(String fechaInicio, String fechaFin);
    BigDecimal obtenerTotalRiesgo(Integer diasAlerta);
    Integer obtenerContadorPorEstado(String fechaInicio, String fechaFin, String estado);
}
