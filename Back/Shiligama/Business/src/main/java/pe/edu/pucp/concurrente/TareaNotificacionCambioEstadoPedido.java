package pe.edu.pucp.concurrente;

import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ModalidadVenta;
import pe.edu.pucp.model.enums.ReferenciaNotificacion;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.notificacion.impl.NotificacionHelper;

/**
 * Tarea concurrente que notifica al CLIENTE cuando su pedido cambia de
 * estado (EN_PROCESO, ATENDIDO, RECHAZADO, CANCELADO).
 *
 * No existe seguimiento de delivery en este sistema (no hay repartidor,
 * tracking GPS ni estado "en camino" separado de ATENDIDO): el sistema
 * solo distingue ESTADO_PEDIDO y MODALIDAD_ENTREGA (DELIVERY/RECOJO_TIENDA).
 * Por eso el mensaje usa la modalidad solo para elegir el texto correcto
 * ("listo para recojo" vs "será entregado"), no para simular un estado
 * de tracking que la BD no soporta.
 *
 * Llamada desde PedidoBoImpl.modificar() (cambios EN_PROCESO/RECHAZADO/
 * CANCELADO) y desde PedidoBoImpl.confirmarPedido() (ATENDIDO).
 *
 * Patron Runnable, igual que TareaNotificacion / TareaCorreo.
 */
public class TareaNotificacionCambioEstadoPedido implements Runnable {

    private final int idCliente;
    private final int idPedido;
    private final EstadoPedido nuevoEstado;
    private final ModalidadVenta modalidad;

    public TareaNotificacionCambioEstadoPedido(int idCliente, int idPedido,
                                                EstadoPedido nuevoEstado, ModalidadVenta modalidad) {
        this.idCliente = idCliente;
        this.idPedido = idPedido;
        this.nuevoEstado = nuevoEstado;
        this.modalidad = modalidad;
    }

    @Override
    public void run() {
        String titulo;
        String mensaje;
        TipoNotificacion tipo;

        boolean esDelivery = modalidad == ModalidadVenta.DELIVERY;

        switch (nuevoEstado) {
            case EN_PROCESO:
                titulo = "Pedido #" + idPedido + " en proceso";
                mensaje = esDelivery
                        ? "Tu pedido #" + idPedido + " está siendo preparado para el delivery."
                        : "Tu pedido #" + idPedido + " está siendo preparado para recojo en tienda.";
                tipo = TipoNotificacion.PEDIDO_LISTO;
                break;
            case ATENDIDO:
                titulo = "Pedido #" + idPedido + " atendido";
                mensaje = esDelivery
                        ? "Tu pedido #" + idPedido + " fue atendido y será entregado a tu dirección."
                        : "Tu pedido #" + idPedido + " fue atendido. Ya puedes recogerlo en tienda.";
                tipo = TipoNotificacion.PEDIDO_ATENDIDO;
                break;
            case RECHAZADO:
                titulo = "Pedido #" + idPedido + " rechazado";
                mensaje = "Tu pedido #" + idPedido + " fue rechazado. Si tienes dudas, contáctanos.";
                tipo = TipoNotificacion.SISTEMA;
                break;
            case CANCELADO:
                titulo = "Pedido #" + idPedido + " cancelado";
                mensaje = "Tu pedido #" + idPedido + " fue cancelado.";
                tipo = TipoNotificacion.SISTEMA;
                break;
            default:
                // RECIBIDO no se notifica aqui: ya lo cubre la confirmacion del pedido en el checkout.
                return;
        }

        NotificacionHelper.notificar(titulo, mensaje, tipo, idCliente,
                ReferenciaNotificacion.PEDIDO, idPedido);
    }
}
