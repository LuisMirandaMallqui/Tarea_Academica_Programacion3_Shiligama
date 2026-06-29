package pe.edu.pucp.concurrente;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Gestor de tareas periodicas de notificacion (a diferencia de
 * GestorPostConfirmacion, que lanza hilos fire-and-forget puntuales,
 * este gestor mantiene un ScheduledExecutorService de fondo mientras
 * la aplicacion este desplegada).
 *
 * Hoy programa:
 *   - TareaPromocionesPorVencer: revisa promociones activas a punto
 *     de vencer y notifica a los administradores.
 *
 * Arrancado por AppLifecycleListener al desplegar el WAR, y detenido
 * al des-desplegar (evita hilos huerfanos en redeploys).
 */
public final class JobNotificacionesProgramadas {

    private static ScheduledExecutorService scheduler;

    private JobNotificacionesProgramadas() { }

    public static synchronized void iniciar() {
        if (scheduler != null && !scheduler.isShutdown()) {
            return; // ya iniciado
        }
        scheduler = Executors.newSingleThreadScheduledExecutor(r ->
                new Thread(r, "Hilo-Job-NotificacionesProgramadas"));

        // Revisa promociones por vencer cada 24 horas. El primer chequeo
        // se ejecuta 1 minuto despues del arranque del servidor (no en
        // el instante 0, para no competir con la inicializacion del contexto).
        scheduler.scheduleAtFixedRate(
                new TareaPromocionesPorVencer(),
                1, TimeUnit.HOURS.toMinutes(24), TimeUnit.MINUTES);

        System.out.println("[JobNotificacionesProgramadas] Scheduler iniciado.");
    }

    public static synchronized void detener() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
            System.out.println("[JobNotificacionesProgramadas] Scheduler detenido.");
        }
    }
}
