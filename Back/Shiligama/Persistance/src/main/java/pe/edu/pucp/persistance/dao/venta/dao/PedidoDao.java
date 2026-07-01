package pe.edu.pucp.persistance.dao.venta.dao;

import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface PedidoDao extends IDAO<Pedido> {
    List<Pedido> listarPorCliente(int idCliente);
    List<Pedido> listarPorEstado(EstadoPedido estado);
    /** Confirma un pedido creando la Venta de forma atómica. Devuelve el idVenta generado. */
    int confirmarPedidoAVenta(int idPedido, int idTrabajador, int idMetodoPago);
    String reservarStockParaPago(int idPedido);

    int restaurarStockReservado(int idPedido);
}
