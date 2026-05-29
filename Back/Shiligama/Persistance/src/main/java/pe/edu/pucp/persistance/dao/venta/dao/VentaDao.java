package pe.edu.pucp.persistance.dao.venta.dao;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.model.venta.VentaReporteDto;
import pe.edu.pucp.persistance.dao.IDAO;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaDao extends IDAO<Venta> {

    List<VentaReporteDto> reporteVentasPorPeriodo(String fechaInicio, String fechaFin);

    List<Venta> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Venta> listarPorTrabajador(int idTrabajador);
}
