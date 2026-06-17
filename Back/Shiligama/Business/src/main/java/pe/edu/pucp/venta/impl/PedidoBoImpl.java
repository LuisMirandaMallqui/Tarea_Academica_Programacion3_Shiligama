package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.enums.EstadoPedido;
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

        // ATENDIDO solo se puede alcanzar via confirmarPedido(); bloquear aqui.
        if (pedido.getEstadoPedido() == EstadoPedido.ATENDIDO) {
            throw new Exception(
                "Para confirmar un pedido use el endpoint POST /pedidos/{id}/confirmar. " +
                "El estado ATENDIDO no puede asignarse mediante modificar().");
        }

        // Irreversibilidad: verificar que el pedido no esté ya confirmado.
        Pedido actual = daoPedido.buscarPorId(pedido.getIdPedido());
        if (actual != null && actual.getEstadoPedido() == EstadoPedido.ATENDIDO) {
            throw new Exception(
                "El pedido ya fue confirmado (ATENDIDO) y su estado no puede revertirse.");
        }

        return daoPedido.modificar(pedido);
    }

    @Override
    public int confirmarPedido(int idPedido, int idTrabajador, int idMetodoPago) throws Exception {
        if (idPedido <= 0) throw new Exception("El ID del pedido debe ser mayor que cero.");

        // Verificar estado actual antes de delegar al SP (error amigable en caso terminal)
        Pedido actual = daoPedido.buscarPorId(idPedido);
        if (actual == null) throw new Exception("Pedido con ID " + idPedido + " no encontrado.");

        EstadoPedido estado = actual.getEstadoPedido();
        if (estado == EstadoPedido.ATENDIDO || estado == EstadoPedido.RECHAZADO
                || estado == EstadoPedido.CANCELADO) {
            throw new Exception(
                "El pedido ya está en estado terminal (" + estado + ") y no puede confirmarse.");
        }

        // El SP CONFIRMAR_PEDIDO_A_VENTA se encarga de: crear venta, copiar detalles,
        // decrementar stock y marcar el pedido como ATENDIDO de forma atómica.
        int idVenta = daoPedido.confirmarPedidoAVenta(idPedido, idTrabajador, idMetodoPago);
        if (idVenta <= 0) throw new Exception("Error al crear la venta para el pedido " + idPedido + ".");
        return idVenta;
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
