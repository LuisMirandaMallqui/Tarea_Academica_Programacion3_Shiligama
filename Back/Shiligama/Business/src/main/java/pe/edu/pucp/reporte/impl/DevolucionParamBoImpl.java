package pe.edu.pucp.reporte.impl;

import pe.edu.pucp.model.reporte.DevolucionParamDTO;
import pe.edu.pucp.persistance.dao.reporte.dao.DevolucionParamDao;
import pe.edu.pucp.persistance.dao.reporte.impl.DevolucionParamDaoImpl;
import pe.edu.pucp.reporte.bo.DevolucionParamBo;

import java.math.BigDecimal;

public class DevolucionParamBoImpl implements DevolucionParamBo {

    private final DevolucionParamDao dao = new DevolucionParamDaoImpl();

    @Override
    public DevolucionParamDTO obtenerYValidarParametros(String fechaInicio, String fechaFin, String estado, Integer diasAlerta) {
        String  validEstado = (estado != null && !estado.trim().isEmpty()) ? estado.trim() : "TODOS";
        Integer validDias   = (diasAlerta != null && diasAlerta > 0) ? diasAlerta : dao.obtenerDiasAlertaDefecto();

        // Logo se carga en JasperService (ShiligamaWS) donde existe el recurso /images/logo.png
        DevolucionParamDTO dto = new DevolucionParamDTO(fechaInicio, fechaFin, validEstado, validDias, null);

        // Calcular resúmenes para la cabecera del reporte
        BigDecimal perdida = dao.obtenerTotalPerdidaReal(fechaInicio, fechaFin);
        BigDecimal riesgo  = dao.obtenerTotalRiesgo(validDias);
        Integer aprobados  = dao.obtenerContadorPorEstado(fechaInicio, fechaFin, "APROBADO");
        Integer pendientes = dao.obtenerContadorPorEstado(fechaInicio, fechaFin, "PENDIENTE");
        Integer totalDev   = dao.obtenerContadorPorEstado(fechaInicio, fechaFin, "TODOS");

        dto.setTotalPerdidaReal(perdida);
        dto.setTotalRiesgo(riesgo);
        dto.setAprobados(aprobados);
        dto.setPendientes(pendientes);
        dto.setTotalDev(totalDev);
        dto.setTotalDevSec1(totalDev);

        return dto;
    }
}
