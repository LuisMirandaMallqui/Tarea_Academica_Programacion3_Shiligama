package pe.edu.pucp.persistance.dao.notificacion.dao;

import pe.edu.pucp.model.notificacion.Notificacion;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface NotificacionDao extends IDAO<Notificacion> {
    List<Notificacion> listarPorUsuario(int idUsuario);
    int marcarLeida(int idNotificacion);
    int contarNoLeidas(int idUsuario);

    // Notificaciones visibles para ADMIN: todo menos lo dirigido a un CLIENTE.
    // Incluye broadcasts, lo de trabajadores y lo de otros administradores.
    List<Notificacion> listarParaAdmin();
}
