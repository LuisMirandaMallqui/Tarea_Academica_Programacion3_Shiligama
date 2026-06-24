package pe.edu.pucp.operacion.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.operacion.Lote;

import java.util.List;

public interface LoteBo extends BaseBo<Lote> {
    List<Lote> listarPorProducto(int idProducto) throws Exception;
    List<Lote> listarProximosAVencer(int dias) throws Exception;
}
