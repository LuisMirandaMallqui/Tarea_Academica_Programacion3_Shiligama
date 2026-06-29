package pe.edu.pucp.reporte.bo;

import pe.edu.pucp.model.reporte.DevolucionParamDTO;

public interface DevolucionParamBo {
    DevolucionParamDTO obtenerYValidarParametros(String fechaInicio, String fechaFin, String estado, Integer diasAlerta);
}
