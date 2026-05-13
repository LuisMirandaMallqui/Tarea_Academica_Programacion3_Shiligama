package pe.edu.pucp.persistance.dao.venta.dao;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.model.venta.VentaReporteDto;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface VentaDao extends IDAO<Venta> {

    List<VentaReporteDto> reporteVentasPorPeriodo(String fechaInicio, String fechaFin);
}
