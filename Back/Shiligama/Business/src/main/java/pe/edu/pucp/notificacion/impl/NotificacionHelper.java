package pe.edu.pucp.notificacion.impl;

import pe.edu.pucp.model.enums.ReferenciaNotificacion;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.model.notificacion.Notificacion;

import java.time.LocalDateTime;

/**
 * Punto unico para registrar notificaciones desde cualquier BO o tarea
 * concurrente. Centraliza el try/catch: un fallo al notificar NUNCA debe
 * abortar la operacion de negocio que la origino (igual criterio que
 * TareaNotificacion ya usaba para VENTA_REGISTRADA).
 */
public final class NotificacionHelper {

    private NotificacionHelper() { }

    /**
     * Notificacion dirigida a un usuario especifico (cliente, trabajador o admin).
     *
     * @param idDestinatario USUARIO_ID del destinatario
     * @param referenciaTipo entidad a la que apunta (puede ser null)
     * @param referenciaId   PK de esa entidad (puede ser null)
     */
    public static void notificar(String titulo, String mensaje, TipoNotificacion tipo,
                                  Integer idDestinatario,
                                  ReferenciaNotificacion referenciaTipo, Integer referenciaId) {
        try {
            Notificacion notif = new Notificacion();
            notif.setTitulo(titulo);
            notif.setMensaje(mensaje);
            notif.setTipo(tipo);
            notif.setLeida(false);
            notif.setFechaCreacion(LocalDateTime.now());
            notif.setIdDestinatario(idDestinatario);
            notif.setReferenciaTipo(referenciaTipo);
            notif.setReferenciaId(referenciaId);

            new NotificacionBoImpl().insertar(notif);
        } catch (Exception ex) {
            // No relanzar: una notificacion fallida no debe tumbar la operacion principal.
            System.err.println("[NotificacionHelper] Error al registrar notificacion: " + ex.getMessage());
        }
    }

    /** Broadcast a todos los admins/trabajadores (ID_DESTINATARIO = NULL). */
    public static void notificarBroadcast(String titulo, String mensaje, TipoNotificacion tipo,
                                           ReferenciaNotificacion referenciaTipo, Integer referenciaId) {
        notificar(titulo, mensaje, tipo, null, referenciaTipo, referenciaId);
    }
}
