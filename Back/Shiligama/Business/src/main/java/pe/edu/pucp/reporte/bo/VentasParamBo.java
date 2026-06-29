package pe.edu.pucp.reporte.bo;

import pe.edu.pucp.model.reporte.VentasParamDTO;

public interface VentasParamBo {
    VentasParamDTO obtenerYValidarParametros(String fechaInicio, String fechaFin, String agrupacion);
}
