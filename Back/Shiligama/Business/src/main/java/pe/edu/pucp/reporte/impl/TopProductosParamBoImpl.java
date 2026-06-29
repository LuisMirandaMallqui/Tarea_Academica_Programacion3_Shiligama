package pe.edu.pucp.reporte.impl;

import pe.edu.pucp.model.reporte.TopProductosParamDTO;
import pe.edu.pucp.persistance.dao.reporte.dao.TopProductosParamDao;
import pe.edu.pucp.persistance.dao.reporte.impl.TopProductosParamDaoImpl;
import pe.edu.pucp.reporte.bo.TopProductosParamBo;

public class TopProductosParamBoImpl implements TopProductosParamBo {

    private final TopProductosParamDao dao = new TopProductosParamDaoImpl();

    @Override
    public TopProductosParamDTO obtenerYValidarParametros(String fechaInicio, String fechaFin, Integer limite) {
        Integer validLimite = (limite != null && limite > 0) ? limite : dao.obtenerLimiteDefecto();

        // Logo se carga en JasperService (ShiligamaWS) donde existe el recurso /images/logo.png
        TopProductosParamDTO dto = new TopProductosParamDTO(fechaInicio, fechaFin, validLimite, null);

        // Calcular resúmenes para la cabecera del reporte
        Double presencial = dao.obtenerSumaPorCanal(fechaInicio, fechaFin, "PRESENCIAL");
        Double web        = dao.obtenerSumaPorCanal(fechaInicio, fechaFin, "WEB");
        Double total      = dao.obtenerSumaPorCanal(fechaInicio, fechaFin, null);

        dto.setSumaPresencial(presencial);
        dto.setSumaWeb(web);
        dto.setVentaTotal(total);

        return dto;
    }
}
