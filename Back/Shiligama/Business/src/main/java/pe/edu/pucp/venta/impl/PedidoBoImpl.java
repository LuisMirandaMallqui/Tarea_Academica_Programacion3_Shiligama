package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.model.venta.DetallePedido;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.persistance.dao.producto.Impl.ProductoDaoImpl;
import pe.edu.pucp.persistance.dao.producto.dao.ProductoDao;
import pe.edu.pucp.persistance.dao.venta.Impl.DetallePedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.Impl.PedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.DetallePedidoDao;
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
        int resultado = daoPedido.modificar(pedido);

        // Al confirmar (ATENDIDO), descontar el stock de cada producto del pedido
        if (pedido.getEstadoPedido() == EstadoPedido.ATENDIDO) {
            DetallePedidoDao daoDetalle = new DetallePedidoDaoImpl();
            ProductoDao daoProducto = new ProductoDaoImpl();

            List<DetallePedido> detalles = daoDetalle.listarPorPedido(pedido.getIdPedido());
            for (DetallePedido detalle : detalles) {
                Producto producto = daoProducto.buscarPorId(detalle.getProducto().getIdProducto());
                if (producto != null) {
                    int nuevoStock = Math.max(0, producto.getStock() - detalle.getCantidad());
                    producto.setStock(nuevoStock);
                    daoProducto.modificar(producto);
                }
            }
        }

        return resultado;
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoPedido.eliminar(id);
    }

    @Override
    public Pedido buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoPedido.buscarPorId(id);
    }

    @Override
    public List<Pedido> listarTodos() throws Exception {
        return daoPedido.listarTodos();
    }

    @Override
    public List<Pedido> listarPorCliente(int idCliente) throws Exception {
        if (idCliente <= 0) {
            throw new Exception("El ID del cliente debe ser mayor que cero.");
        }
        return daoPedido.listarPorCliente(idCliente);
    }

    @Override
    public List<Pedido> listarPorEstado(EstadoPedido estado) throws Exception {
        if (estado == null) {
            throw new Exception("El estado del pedido es obligatorio.");
        }
        return daoPedido.listarPorEstado(estado);
    }

    private void validar(Pedido pedido, boolean esModificacion) throws Exception {
        if (pedido == null) {
            throw new Exception("El pedido no puede ser nulo.");
        }
        if (esModificacion && pedido.getIdPedido() <= 0) {
            throw new Exception("El ID del pedido es obligatorio para la modificacion.");
        }
        if (!esModificacion && pedido.getCliente() == null) {
            throw new Exception("El pedido debe tener un cliente asignado.");
        }
        if (!esModificacion && pedido.getMontoTotal() <= 0) {
            throw new Exception("El monto total del pedido debe ser mayor que cero.");
        }
    }
}
