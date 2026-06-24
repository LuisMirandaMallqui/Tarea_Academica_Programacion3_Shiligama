package pe.edu.pucp.operacion.impl;

import pe.edu.pucp.model.operacion.Lote;
import pe.edu.pucp.operacion.bo.LoteBo;
import pe.edu.pucp.persistance.dao.operacion.Impl.LoteDaoImpl;
import pe.edu.pucp.persistance.dao.operacion.dao.LoteDao;

import java.util.List;

public class LoteBoImpl implements LoteBo {
    private final LoteDao daoLote;

    public LoteBoImpl() {
        this.daoLote = new LoteDaoImpl();
    }

    @Override
    public int insertar(Lote lote) throws Exception {
        validar(lote, false);
        return daoLote.insertar(lote);
    }

    @Override
    public int modificar(Lote lote) throws Exception {
        validar(lote, true);
        return daoLote.modificar(lote);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del lote debe ser mayor que cero.");
        }
        return daoLote.eliminar(id);
    }

    @Override
    public Lote buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del lote debe ser mayor que cero.");
        }
        return daoLote.buscarPorId(id);
    }

    @Override
    public List<Lote> listarTodos() throws Exception {
        return daoLote.listarTodos();
    }

    @Override
    public List<Lote> listarPorProducto(int idProducto) throws Exception {
        if (idProducto <= 0) {
            throw new Exception("El ID del producto debe ser mayor que cero.");
        }
        return daoLote.listarPorProducto(idProducto);
    }

    @Override
    public List<Lote> listarProximosAVencer(int dias) throws Exception {
        if (dias <= 0) {
            throw new Exception("Los días deben ser mayores que cero.");
        }
        return daoLote.listarProximosAVencer(dias);
    }

    private void validar(Lote lote, boolean esModificacion) throws Exception {
        if (lote == null) {
            throw new Exception("El lote no puede ser nulo.");
        }
        if (esModificacion && lote.getIdLote() <= 0) {
            throw new Exception("El ID del lote es obligatorio para modificar.");
        }
        if (lote.getIdProducto() <= 0) {
            throw new Exception("El ID del producto es obligatorio.");
        }
        if (!esModificacion && lote.getCantidadInicial() <= 0) {
            throw new Exception("La cantidad inicial debe ser mayor que cero.");
        }
        if (esModificacion && lote.getCantidadActual() < 0) {
            throw new Exception("La cantidad actual no puede ser negativa.");
        }
    }
}
