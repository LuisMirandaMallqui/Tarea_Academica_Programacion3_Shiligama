package pe.edu.pucp.venta.impl;

import pe.edu.pucp.model.venta.DetalleVenta;
import pe.edu.pucp.persistance.dao.venta.Impl.DetalleVentaDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.DetalleVentaDao;
import pe.edu.pucp.venta.bo.DetalleVentaBo;

import java.util.List;

public class DetalleVentaBoImpl implements DetalleVentaBo {

    private final DetalleVentaDao dao = new DetalleVentaDaoImpl();

    @Override
    public int insertar(DetalleVenta t) throws Exception {
        if (t.getCantidad() <= 0)
            throw new Exception("Cantidad inválida");

        t.setSubtotal(t.calcularSubtotal());

        return dao.insertar(t);
    }

    @Override
    public int modificar(DetalleVenta t) throws Exception {
        if (t.getIdDetalleVenta() <= 0)
            throw new Exception("ID inválido");

        t.setSubtotal(t.calcularSubtotal());

        return dao.modificar(t);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0)
            throw new Exception("ID inválido");

        return dao.eliminar(id);
    }

    @Override
    public DetalleVenta buscarPorId(int id) throws Exception {
        return dao.buscarPorId(id);
    }

    @Override
    public List<DetalleVenta> listarTodos() throws Exception {
        return dao.listarTodos();
    }

    @Override
    public List<DetalleVenta> listarPorVenta(int idVenta) throws Exception {
        if (idVenta <= 0)
            throw new Exception("ID venta inválido");

        return dao.listarPorVenta(idVenta);
    }
}