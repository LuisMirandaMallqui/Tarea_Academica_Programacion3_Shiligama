package pe.edu.pucp.venta.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.venta.Pago;

/**
 * Lógica de negocio de pagos. Sobre el CRUD genérico añade las operaciones
 * del flujo de la pasarela: registrar un pago pendiente antes de redirigir y
 * confirmar el resultado cuando Izipay notifica.
 */
public interface PagoBo extends BaseBo<Pago> {

    /** Último pago asociado a un pedido (o null). */
    Pago buscarPorPedido(int idPedido) throws Exception;

    /** Pago localizado por el ORDER_ID enviado a la pasarela (o null). */
    Pago buscarPorOrder(String orderId) throws Exception;

    /**
     * Registra un pago en estado PENDIENTE para un pedido, antes de redirigir
     * al cliente a la pasarela. Devuelve el pago con su id ya generado.
     */
    Pago registrarPagoPendiente(int idPedido, int idMetodoPago, double monto,
                                String moneda, String orderId) throws Exception;

    /**
     * Procesa la notificación de la pasarela: actualiza el estado del pago y,
     * si fue autorizado, avanza el estado del pedido (queda listo para ser
     * procesado por la tienda).
     */
    void procesarResultadoPasarela(String orderId, boolean autorizado,
                                   String referencia) throws Exception;
}
