package pe.edu.pucp.concurrente;

import pe.edu.pucp.model.venta.Pedido;

/**
 * Gestor de tareas concurrentes post-confirmacion de pedido.
 *
 * Cuando un pedido se confirma y se genera la venta, este gestor lanza
 * en paralelo dos hilos independientes:
 *
 *   Hilo 1 — TareaNotificacion : registra la notificacion en BD para el cliente.
 *   Hilo 2 — TareaCorreo       : envia el correo de confirmacion al cliente.
 *
 * Patron equivalente al Main / Programa3 del ejemplo HILOS:
 *   - Crea los Runnables con los datos necesarios.
 *   - Envuelve cada uno en un Thread con nombre descriptivo.
 *   - Llama start() en ambos -> ejecucion asincrona (concurrente).
 *
 * FIRE-AND-FORGET: no se usa join() porque el WS ya devolvio el idVenta
 * al cliente; estas tareas son secundarias y no deben retrasar la respuesta.
 *
 * Si en algun momento se necesitara esperar (p.ej. para auditoria sincrona),
 * se usaria join() despues de ambos start() dentro de un bloque try/catch
 * de InterruptedException, exactamente como en el ejemplo HILOS.
 */
public class GestorPostConfirmacion {

    private GestorPostConfirmacion() { }

    /**
     * Lanza los hilos de notificacion y correo de forma concurrente.
     *
     * @param pedido  el pedido que acaba de confirmarse (con datos del cliente)
     * @param idVenta el ID de la venta recien generada por el SP
     */
    public static void lanzarTareasPostConfirmacion(Pedido pedido, int idVenta) {
        if (pedido == null || pedido.getCliente() == null) {
            System.out.println("[GestorPostConfirmacion] Pedido o cliente nulo, no se lanzan hilos.");
            return;
        }

        int idCliente      = pedido.getCliente().getIdUsuario();
        double montoTotal  = pedido.getMontoTotal();
        int idPedido       = pedido.getIdPedido();
        String correo      = pedido.getCliente().getCorreo();
        String nombres     = pedido.getCliente().getNombres() != null
                             ? pedido.getCliente().getNombres() : "Cliente";

        // --- Hilo 1: registrar notificacion en BD (Runnable via clase) ---
        Runnable tareaNotif = new TareaNotificacion(idCliente, idPedido, idVenta, montoTotal);
        Thread hiloNotificacion = new Thread(tareaNotif, "Hilo-Notificacion-Pedido" + idPedido);

        // --- Hilo 2: enviar correo de confirmacion (Runnable via clase) ---
        Runnable tareaCorreo = new TareaCorreo(correo, nombres, idPedido, idVenta, montoTotal);
        Thread hiloCorreo = new Thread(tareaCorreo, "Hilo-Correo-Pedido" + idPedido);

        System.out.println("[GestorPostConfirmacion] Lanzando hilos concurrentes para pedido #" + idPedido
                + " -> venta #" + idVenta);

        hiloNotificacion.start(); // asincrono: no bloquea el hilo principal (el del request HTTP)
        hiloCorreo.start();       // asincrono: corre en paralelo con hiloNotificacion

        // Fire-and-forget: el WS ya retorno. Si se quisiera esperar:
        // try {
        //     hiloNotificacion.join();
        //     hiloCorreo.join();
        //     System.out.println("[GestorPostConfirmacion] Ambos hilos terminaron.");
        // } catch (InterruptedException ex) {
        //     Thread.currentThread().interrupt();
        // }
    }
}
