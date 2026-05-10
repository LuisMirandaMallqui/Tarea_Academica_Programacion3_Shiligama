package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.persistance.dao.venta.Impl.PedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;
import pe.edu.pucp.venta.bo.PedidoBo;

public class PedidoBoImpl implements PedidoBo {
    private final PedidoDao daoPedido;

    public PedidoBoImpl() {
        daoPedido = new PedidoDaoImpl();
    }

    @Override
    public int insertar(Pedido pedido) throws Exception {
        validar(pedido, false);
        return daoPedido.insertar(pedido);
    }

    @Override
    public int modificar(Pedido pedido) throws Exception {
        validar(pedido, true);
        return daoPedido.modificar(pedido);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoPedido.eliminar(id);
    }

    @Override
    public Pedido buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoPedido.buscarPorID(id);
    }

    @Override
    public List<Pedido> listarTodos() throws Exception {
        return daoPedido.listarTodos();
    }

    private void validar(Pedido pedido, boolean esModificacion) throws Exception {
        if (pedido == null) {
            throw new Exception("El pedido no puede ser nulo.");
        }
        if (esModificacion && pedido.getIdPedido() <= 0) {
            throw new Exception("El ID del pedido es obligatorio para la modificacion.");
        }
        if (pedido.getCliente() == null) {
            throw new Exception("El pedido debe tener un cliente asignado.");
        }
    }
}
