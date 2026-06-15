package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.venta.MetodoPago;
import pe.edu.pucp.persistance.dao.venta.Impl.MetodoPagoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.MetodoPagoDao;
import pe.edu.pucp.venta.bo.MetodoPagoBo;

public class MetodoPagoBoImpl implements MetodoPagoBo {
    private final MetodoPagoDao daoMetodoPago;

    public MetodoPagoBoImpl() {
        daoMetodoPago = new MetodoPagoDaoImpl();
    }

    @Override
    public int insertar(MetodoPago metodoPago) throws Exception {
        validar(metodoPago, false);
        return daoMetodoPago.insertar(metodoPago);
    }

    @Override
    public int modificar(MetodoPago metodoPago) throws Exception {
        validar(metodoPago, true);
        return daoMetodoPago.modificar(metodoPago);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del metodo de pago debe ser mayor que cero.");
        }
        return daoMetodoPago.eliminar(id);
    }

    @Override
    public MetodoPago buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del metodo de pago debe ser mayor que cero.");
        }
        return daoMetodoPago.buscarPorId(id);
    }

    @Override
    public List<MetodoPago> listarTodos() throws Exception {
        return daoMetodoPago.listarTodos();
    }

    private void validar(MetodoPago metodoPago, boolean esModificacion) throws Exception {
        if (metodoPago == null) {
            throw new Exception("El metodo de pago no puede ser nulo.");
        }
        if (esModificacion && metodoPago.getIdMetodoPago() <= 0) {
            throw new Exception("El ID del metodo de pago es obligatorio para la modificacion.");
        }
        if (metodoPago.getNombre() == null || metodoPago.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del metodo de pago es obligatorio.");
        }
    }
}
