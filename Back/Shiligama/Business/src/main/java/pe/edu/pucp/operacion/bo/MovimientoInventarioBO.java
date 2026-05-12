package pe.edu.pucp.operacion.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.operacion.MovimientoInventario;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoInventarioBO extends BaseBo<MovimientoInventario> {
    List<MovimientoInventario> listarPorProducto(int idProducto) throws Exception;
    List<MovimientoInventario> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws Exception;
}
