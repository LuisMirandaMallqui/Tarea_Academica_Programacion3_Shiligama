package pe.edu.pucp.persistance.dao.venta.dao;

import pe.edu.pucp.model.enums.EstadoPago;
import pe.edu.pucp.model.venta.Pago;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

/**
 * DAO de pagos. Además del CRUD genérico, permite localizar el pago por
 * pedido y por el ORDER_ID enviado a la pasarela (necesario en el callback
 * de Izipay, donde no conocemos el PAGO_ID sino el vads_order_id).
 */
public interface PagoDao extends IDAO<Pago> {
    Pago buscarPorPedido(int idPedido);
    Pago buscarPorOrder(String orderId);
    List<Pago> listarPorPedido(int idPedido);
    int modificarPorOrder(String orderId, EstadoPago estado, String referencia);
}
