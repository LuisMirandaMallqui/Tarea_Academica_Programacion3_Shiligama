package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.venta.DetalleVenta;
import pe.edu.pucp.persistance.dao.venta.Impl.DetalleVentaDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.DetalleVentaDao;
import pe.edu.pucp.venta.bo.DetalleVentaBo;

public class DetalleVentaBoImpl implements DetalleVentaBo {
    private final DetalleVentaDao daoDetalleVenta;

    public DetalleVentaBoImpl() {
        daoDetalleVenta = new DetalleVentaDaoImpl();
    }

    @Override
    public int insertar(DetalleVenta detalleVenta) throws Exception {
        validar(detalleVenta, false);
        return daoDetalleVenta.insertar(detalleVenta);
    }

    @Override
    public int modificar(DetalleVenta detalleVenta) throws Exception {
        validar(detalleVenta, true);
        return daoDetalleVenta.modificar(detalleVenta);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del detalle de venta debe ser mayor que cero.");
        }
        return daoDetalleVenta.eliminar(id);
    }

    @Override
    public DetalleVenta buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del detalle de venta debe ser mayor que cero.");
        }
        return daoDetalleVenta.buscarPorID(id);
    }

    @Override
    public List<DetalleVenta> listarTodos() throws Exception {
        return daoDetalleVenta.listarTodos();
    }

    private void validar(DetalleVenta detalleVenta, boolean esModificacion) throws Exception {
        if (detalleVenta == null) {
            throw new Exception("El detalle de venta no puede ser nulo.");
        }
        if (esModificacion && detalleVenta.getIdDetalleVenta() <= 0) {
            throw new Exception("El ID del detalle de venta es obligatorio para la modificacion.");
        }
        if (detalleVenta.getProducto() == null) {
            throw new Exception("El detalle de venta debe tener un producto asignado.");
        }
        if (detalleVenta.getCantidad() <= 0) {
            throw new Exception("La cantidad debe ser mayor que cero.");
        }
        if (detalleVenta.getPrecioUnitario() < 0) {
            throw new Exception("El precio unitario no puede ser negativo.");
        }
        if (detalleVenta.getIdPadreVenta() <= 0) {
            throw new Exception("El detalle de venta debe estar asociado a una venta valida.");
        }
    }
}
