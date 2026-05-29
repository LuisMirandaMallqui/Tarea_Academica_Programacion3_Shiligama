package pe.edu.pucp.venta.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.model.venta.VentaReporteDto;
import java.util.List;

public interface VentaBo extends BaseBo<Venta> {
    List<VentaReporteDto> reporteVentasPorPeriodo(String fechaInicio, String fechaFin) throws Exception;
}
