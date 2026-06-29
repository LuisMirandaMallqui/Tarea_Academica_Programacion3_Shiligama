package pe.edu.pucp.notificacion.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.notificacion.Notificacion;

import java.util.List;

public interface NotificacionBo extends BaseBo<Notificacion> {
    List<Notificacion> listarPorUsuario(int idUsuario) throws Exception;
    int marcarLeida(int idNotificacion) throws Exception;
    int contarNoLeidas(int idUsuario) throws Exception;

    // Notificaciones visibles para ADMIN: todo menos lo dirigido a un CLIENTE.
    List<Notificacion> listarParaAdmin() throws Exception;
}
