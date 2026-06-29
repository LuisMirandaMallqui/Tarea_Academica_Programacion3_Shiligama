package pe.edu.pucp.reporte.impl;

import pe.edu.pucp.model.reporte.VentasParamDTO;
import pe.edu.pucp.persistance.dao.reporte.dao.VentasParamDao;
import pe.edu.pucp.persistance.dao.reporte.impl.VentasParamDaoImpl;
import pe.edu.pucp.reporte.bo.VentasParamBo;

public class VentasParamBoImpl implements VentasParamBo {

    private final VentasParamDao dao = new VentasParamDaoImpl();

    @Override
    public VentasParamDTO obtenerYValidarParametros(String fechaInicio, String fechaFin, String agrupacion) {
        String validAgrupacion = (agrupacion != null && !agrupacion.trim().isEmpty()) ? agrupacion.trim() : dao.obtenerAgrupacionDefecto();

        // Logo se carga en JasperService (ShiligamaWS) donde existe el recurso /images/logo.png
        VentasParamDTO dto = new VentasParamDTO(fechaInicio, fechaFin, validAgrupacion, null);

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
