package pe.edu.pucp.persistance.dao.usuario.dao;

import pe.edu.pucp.model.usuario.Usuario;

/**
 * DAO unificado de autenticación.
 * Devuelve el subtipo concreto (Cliente / Trabajador / Administrador) según
 * la tabla hija en que aparezca el USUARIO_ID. El rol viaja en getRol()
 * polimórfico, sin necesidad de DTOs intermedios.
 */
public interface AuthDao {
    Usuario autenticar(String correo, String contrasena);
}
