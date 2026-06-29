package pe.edu.pucp.concurrente;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.ReferenciaNotificacion;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.notificacion.impl.NotificacionHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Revisa promociones activas que estan por vencer (proximos DIAS_LIMITE
 * dias) o que ya vencieron hace como maximo DIAS_LIMITE dias, y notifica
 * a los administradores (broadcast).
 *
 * Se apoya directamente en el SP LISTAR_PROMOCIONES_POR_VENCER via
 * DBManager, igual patron que usan los DAO existentes.
 *
 * Tiene DOS formas de ejecutarse:
 *   1) Automatica: lanzada cada 24h por JobNotificacionesProgramadas
 *      (ScheduledExecutorService) como Runnable normal.
 *   2) Manual/inmediata: ejecutarAhora() es un metodo estatico que hace
 *      exactamente lo mismo pero se puede invocar al instante desde un
 *      endpoint REST (ver NotificacionRS.revisarPromociones()), util
 *      para pruebas y demos sin tener que esperar el ciclo de 24h ni
 *      reiniciar el servidor.
 */
public class TareaPromocionesPorVencer implements Runnable {

    private static final int DIAS_LIMITE = 3;

    @Override
    public void run() {
        ejecutarAhora();
    }

    /** Ejecuta la revision inmediatamente, sin pasar por el scheduler. */
    public static int ejecutarAhora() {
        int notificacionesGeneradas = 0;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, DIAS_LIMITE);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_PROMOCIONES_POR_VENCER", parametrosEntrada)) {
            if (resultado == null) return 0;

            ResultSet rs = resultado.getRs();
            while (rs.next()) {
                int idPromocion = rs.getInt("id_promocion");
                String nombre = rs.getString("nombre");
                LocalDate fechaFin = rs.getDate("fecha_fin").toLocalDate();
                boolean yaVencida = rs.getBoolean("ya_vencida");

                String titulo = yaVencida
                        ? "Promoción vencida: " + nombre
                        : "Promoción por vencer: " + nombre;
                String mensaje = yaVencida
                        ? "La promoción \"" + nombre + "\" venció el " + fechaFin + "."
                        : "La promoción \"" + nombre + "\" vence el " + fechaFin + ".";

                NotificacionHelper.notificarBroadcast(
                        titulo, mensaje, TipoNotificacion.PROMOCION_POR_VENCER,
                        ReferenciaNotificacion.PROMOCION, idPromocion);
                notificacionesGeneradas++;
            }
        } catch (SQLException ex) {
            System.err.println("[TareaPromocionesPorVencer] Error: " + ex.getMessage());
        }
        return notificacionesGeneradas;
    }
}
