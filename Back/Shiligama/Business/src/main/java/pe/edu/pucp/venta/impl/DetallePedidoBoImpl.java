package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.venta.DetallePedido;
import pe.edu.pucp.persistance.dao.venta.Impl.DetallePedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.DetallePedidoDao;
import pe.edu.pucp.venta.bo.DetallePedidoBo;

public class DetallePedidoBoImpl implements DetallePedidoBo {
    private final DetallePedidoDao daoDetallePedido;

    public DetallePedidoBoImpl() {
        daoDetallePedido = new DetallePedidoDaoImpl();
    }

    @Override
    public int insertar(DetallePedido detallePedido) throws Exception {
        validar(detallePedido, false);
        return daoDetallePedido.insertar(detallePedido);
    }

    @Override
    public int modificar(DetallePedido detallePedido) throws Exception {
        validar(detallePedido, true);
        return daoDetallePedido.modificar(detallePedido);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del detalle de pedido debe ser mayor que cero.");
        }
        return daoDetallePedido.eliminar(id);
    }

    @Override
    public DetallePedido buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del detalle de pedido debe ser mayor que cero.");
        }
        return daoDetallePedido.buscarPorId(id);
    }

    @Override
    public List<DetallePedido> listarTodos() throws Exception {
        return daoDetallePedido.listarTodos();
    }

    @Override
    public List<DetallePedido> listarPorPedido(int idPedido) throws Exception {
        if (idPedido <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoDetallePedido.listarPorPedido(idPedido);
    }

    private void validar(DetallePedido detallePedido, boolean esModificacion) throws Exception {
        if (detallePedido == null) {
            throw new Exception("El detalle de pedido no puede ser nulo.");
        }
        if (esModificacion && detallePedido.getIdDetallePedido() <= 0) {
            throw new Exception("El ID del detalle de pedido es obligatorio para la modificacion.");
        }
        if (detallePedido.getProducto() == null) {
            throw new Exception("El detalle de pedido debe tener un producto asignado.");
        }
        if (detallePedido.getCantidad() <= 0) {
            throw new Exception("La cantidad debe ser mayor que cero.");
        }
        if (detallePedido.getPrecioUnitario() < 0) {
            throw new Exception("El precio unitario no puede ser negativo.");
        }
        if (detallePedido.getIdPadrePedido() <= 0) {
            throw new Exception("El detalle de pedido debe estar asociado a un pedido valido.");
        }
    }
}
