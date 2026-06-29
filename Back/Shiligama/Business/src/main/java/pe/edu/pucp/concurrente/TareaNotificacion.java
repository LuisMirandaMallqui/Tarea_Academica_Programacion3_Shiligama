package pe.edu.pucp.concurrente;

import pe.edu.pucp.model.enums.ReferenciaNotificacion;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.notificacion.impl.NotificacionHelper;

/**
 * Tarea concurrente que registra una Notificacion en BD
 * cuando un pedido es confirmado.
 *
 * Implementa {@link Runnable} para separar la tarea del hilo que la ejecuta
 * (patron equivalente a Tarea / Deposito del ejemplo HILOS).
 *
 * El hilo que la ejecuta es creado y lanzado por {@link GestorPostConfirmacion}.
 */
public class TareaNotificacion implements Runnable {

    private final int idCliente;
    private final int idPedido;
    private final int idVenta;
    private final double montoTotal;

    public TareaNotificacion(int idCliente, int idPedido, int idVenta, double montoTotal) {
        this.idCliente = idCliente;
        this.idPedido = idPedido;
        this.idVenta = idVenta;
        this.montoTotal = montoTotal;
    }

    @Override
    public void run() {
        String nombreHilo = Thread.currentThread().getName();
        System.out.println("[" + nombreHilo + "] Iniciando registro de notificacion para cliente " + idCliente);

        try {
            NotificacionHelper.notificar(
                    "Pedido #" + idPedido + " confirmado",
                    "Tu pedido #" + idPedido + " fue confirmado. "
                            + "Venta generada: #" + idVenta
                            + ". Monto: S/ " + String.format("%.2f", montoTotal),
                    TipoNotificacion.VENTA_REGISTRADA,
                    idCliente,
                    ReferenciaNotificacion.PEDIDO,
                    idPedido);
            System.out.println("[" + nombreHilo + "] Notificacion registrada para pedido " + idPedido);

        } catch (Exception ex) {
            // Los errores de notificacion NO deben abortar la confirmacion del pedido
            System.err.println("[" + nombreHilo + "] Error al registrar notificacion: " + ex.getMessage());
        }
    }
}
