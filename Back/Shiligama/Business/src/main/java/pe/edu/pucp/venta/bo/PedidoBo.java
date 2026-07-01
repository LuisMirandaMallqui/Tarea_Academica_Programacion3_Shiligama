package pe.edu.pucp.venta.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.venta.Pedido;

import java.util.List;

public interface PedidoBo extends BaseBo<Pedido> {
    List<Pedido> listarPorCliente(int idCliente) throws Exception;
    List<Pedido> listarPorEstado(EstadoPedido estado) throws Exception;
    /** Confirma el pedido creando la Venta atómicamente. Devuelve idVenta. */
    int confirmarPedido(int idPedido, int idTrabajador, int idMetodoPago) throws Exception;

    String reservarStockParaPago(int idPedido) throws Exception;

    int restaurarStockReservado(int idPedido) throws Exception;
}
