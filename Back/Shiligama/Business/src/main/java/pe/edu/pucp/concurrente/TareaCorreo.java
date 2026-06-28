package pe.edu.pucp.concurrente;

import pe.edu.pucp.correo.EnvioCorreo;

import java.util.List;

/**
 * Tarea concurrente que envia el correo de confirmacion de pedido al cliente.
 *
 * Implementa {@link Runnable} (patron equivalente a Retiro / Deposito del ejemplo HILOS).
 *
 * AREA CRITICA: {@link EnvioCorreo#getInstance()} esta declarado como {@code synchronized},
 * lo que garantiza que solo un hilo a la vez obtiene (o crea) la instancia Singleton.
 * Esto es el equivalente al {@code synchronized} en CuentaBancaria#retirar / depositar.
 *
 * El hilo que la ejecuta es creado y lanzado por {@link GestorPostConfirmacion}.
 */
public class TareaCorreo implements Runnable {

    private final String correoCliente;
    private final String nombresCliente;
    private final int idPedido;
    private final int idVenta;
    private final double montoTotal;

    public TareaCorreo(String correoCliente, String nombresCliente,
                       int idPedido, int idVenta, double montoTotal) {
        this.correoCliente = correoCliente;
        this.nombresCliente = nombresCliente;
        this.idPedido = idPedido;
        this.idVenta = idVenta;
        this.montoTotal = montoTotal;
    }

    @Override
    public void run() {
        String nombreHilo = Thread.currentThread().getName();
        System.out.println("[" + nombreHilo + "] Iniciando envio de correo a " + correoCliente);

        if (correoCliente == null || correoCliente.isBlank()) {
            System.out.println("[" + nombreHilo + "] Cliente sin correo registrado, se omite el envio.");
            return;
        }

        try {
            String asunto = "Shiligama - Pedido #" + idPedido + " confirmado";
            String cuerpoHtml = "<h2>Hola, " + nombresCliente + "!</h2>"
                    + "<p>Tu pedido <strong>#" + idPedido + "</strong> fue confirmado exitosamente.</p>"
                    + "<p>Venta generada: <strong>#" + idVenta + "</strong> &nbsp;|&nbsp; "
                    + "Total: <strong>S/ " + String.format("%.2f", montoTotal) + "</strong></p>"
                    + "<p>Gracias por comprar en <strong>Shiligama</strong>.</p>";

            // getInstance() es synchronized -> area critica (equivalente al monitor de CuentaBancaria)
            EnvioCorreo correoService = EnvioCorreo.getInstance();
            boolean enviado = correoService.enviarEmail(List.of(correoCliente), asunto, cuerpoHtml);
            System.out.println("[" + nombreHilo + "] Correo enviado: " + enviado);

        } catch (Exception ex) {
            // Los errores de correo NO deben abortar la confirmacion del pedido
            System.err.println("[" + nombreHilo + "] Error al enviar correo: " + ex.getMessage());
        }
    }
}
