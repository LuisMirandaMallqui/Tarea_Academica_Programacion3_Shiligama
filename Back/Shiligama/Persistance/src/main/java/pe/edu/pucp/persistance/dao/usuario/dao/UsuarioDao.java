package pe.edu.pucp.persistance.dao.usuario.dao;

import pe.edu.pucp.model.usuario.Usuario;
import pe.edu.pucp.persistance.dao.IDAO;

public interface UsuarioDao<T extends Usuario> extends IDAO<T> {
    T buscarPorCorreo(String correo);
    T obtenerPorDNI(String dni);
    Boolean existeUsuarioEnBD(T usuario);
}
