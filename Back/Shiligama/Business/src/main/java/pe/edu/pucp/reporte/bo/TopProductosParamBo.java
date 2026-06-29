package pe.edu.pucp.reporte.bo;

import pe.edu.pucp.model.reporte.TopProductosParamDTO;

public interface TopProductosParamBo {
    TopProductosParamDTO obtenerYValidarParametros(String fechaInicio, String fechaFin, Integer limite);
}
