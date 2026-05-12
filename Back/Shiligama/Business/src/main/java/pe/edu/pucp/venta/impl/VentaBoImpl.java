package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.persistance.dao.venta.Impl.VentaDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDao;
import pe.edu.pucp.venta.bo.VentaBo;

public class VentaBoImpl implements VentaBo {
    private final VentaDao daoVenta;

    public VentaBoImpl() {
        daoVenta = new VentaDaoImpl();
    }

    @Override
    public int insertar(Venta venta) throws Exception {
        validar(venta, false);
        return daoVenta.insertar(venta);
    }

    @Override
    public int modificar(Venta venta) throws Exception {
        validar(venta, true);
        return daoVenta.modificar(venta);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        return daoVenta.eliminar(id);
    }

    @Override
    public Venta buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        return daoVenta.buscarPorID(id);
    }

    @Override
    public List<Venta> listarTodos() throws Exception {
        return daoVenta.listarTodos();
    }

    private void validar(Venta venta, boolean esModificacion) throws Exception {
        if (venta == null) {
            throw new Exception("La venta no puede ser nula.");
        }
        if (esModificacion && venta.getIdVenta() <= 0) {
            throw new Exception("El ID de la venta es obligatorio para la modificacion.");
        }
        if (venta.getCliente() == null) {
            throw new Exception("La venta debe tener un cliente asignado.");
        }
        if (venta.getMetodoPago() == null) {
            throw new Exception("La venta debe tener un metodo de pago asignado.");
        }
        if (venta.getCanalVenta() == null) {
            throw new Exception("El canal de venta es obligatorio.");
        }
        if (venta.getMontoTotal() < 0) {
            throw new Exception("El monto total no puede ser negativo.");
        }
        if (venta.getMontoDescuento() < 0) {
            throw new Exception("El monto de descuento no puede ser negativo.");
        }
    }
}
