package pe.edu.pucp.notificacion.impl;

import pe.edu.pucp.model.notificacion.Notificacion;
import pe.edu.pucp.notificacion.bo.NotificacionBo;
import pe.edu.pucp.persistance.dao.notificacion.dao.NotificacionDao;
import pe.edu.pucp.persistance.dao.notificacion.impl.NotificacionDaoImpl;

import java.util.List;

public class NotificacionBoImpl implements NotificacionBo {
    private final NotificacionDao notifDao;

    public NotificacionBoImpl() {
        this.notifDao = new NotificacionDaoImpl();
    }

    @Override
    public int insertar(Notificacion notif) throws Exception {
        validar(notif, false);
        return notifDao.insertar(notif);
    }

    @Override
    public int modificar(Notificacion notif) throws Exception {
        validar(notif, true);
        return notifDao.modificar(notif);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la notificación debe ser mayor que cero.");
        }
        return notifDao.eliminar(id);
    }

    @Override
    public Notificacion buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la notificación debe ser mayor que cero.");
        }
        return notifDao.buscarPorID(id);
    }

    @Override
    public List<Notificacion> listarTodos() throws Exception {
        return notifDao.listarTodos();
    }

    @Override
    public List<Notificacion> listarPorUsuario(int idUsuario) throws Exception {
        if (idUsuario <= 0) {
            throw new Exception("El ID del usuario debe ser mayor que cero.");
        }
        return notifDao.listarPorUsuario(idUsuario);
    }

    @Override
    public int marcarLeida(int idNotificacion) throws Exception {
        if (idNotificacion <= 0) {
            throw new Exception("El ID de la notificación debe ser mayor que cero.");
        }
        return notifDao.marcarLeida(idNotificacion);
    }

    @Override
    public int contarNoLeidas(int idUsuario) throws Exception {
        if (idUsuario <= 0) {
            throw new Exception("El ID del usuario debe ser mayor que cero.");
        }
        return notifDao.contarNoLeidas(idUsuario);
    }

    private void validar(Notificacion notif, boolean esModificacion) throws Exception {
        if (notif == null) {
            throw new Exception("La notificación no puede ser nula.");
        }
        if (esModificacion && notif.getIdNotificacion() <= 0) {
            throw new Exception("El ID de la notificación es obligatorio para la modificación.");
        }
        if (notif.getTitulo() == null || notif.getTitulo().trim().isEmpty()) {
            throw new Exception("El título de la notificación es obligatorio.");
        }
        if (notif.getMensaje() == null || notif.getMensaje().trim().isEmpty()) {
            throw new Exception("El mensaje de la notificación es obligatorio.");
        }
        if (notif.getTipo() == null) {
            throw new Exception("El tipo de notificación es obligatorio.");
        }
    }
}
