package pe.edu.pucp.ventas.impl;

import java.util.List;
import pe.edu.pucp.model.venta.PedidoDto;
import pe.edu.pucp.persistance.dao.venta.Impl.PedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;
import pe.edu.pucp.ventas.bo.PedidoBo;

public class PedidoBoImpl implements PedidoBo {
    private final PedidoDao daoPedido;

    public PedidoBoImpl() {
        daoPedido = new PedidoDaoImpl();
    }

    @Override
    public int insertar(PedidoDto pedido) throws Exception {
        validar(pedido, false);
        return daoPedido.insertar(pedido);
    }

    @Override
    public int modificar(PedidoDto pedido) throws Exception {
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
    public PedidoDto buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoPedido.buscarPorID(id);
    }

    @Override
    public List<PedidoDto> listarTodos() throws Exception {
        return daoPedido.listarTodos();
    }

    private void validar(PedidoDto pedido, boolean esModificacion) throws Exception {
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
