package pe.edu.pucp.persistance.dao.operacion.dao;

import pe.edu.pucp.persistance.dao.IDAO;
import pe.edu.pucp.model.operacion.Lote;

import java.time.LocalDate;
import java.util.List;

public interface LoteDao extends IDAO<Lote> {
    List<Lote> listarPorProducto(int idProducto);
    List<Lote> listarProximosAVencer(int dias);
}
