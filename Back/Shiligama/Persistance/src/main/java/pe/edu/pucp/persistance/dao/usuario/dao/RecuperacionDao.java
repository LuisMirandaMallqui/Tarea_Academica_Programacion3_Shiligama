package pe.edu.pucp.persistance.dao.usuario.dao;

import pe.edu.pucp.model.seguridad.TokenRecuperacion;
import pe.edu.pucp.model.seguridad.UsuarioBasicoDto;

/**
 * DAO del flujo de recuperación de contraseña.
 * Trabaja sobre la tabla `usuario` (actualizar contraseña / buscar por
 * correo) y sobre `token_recuperacion` (tokens de un solo uso).
 */
public interface RecuperacionDao {
    UsuarioBasicoDto buscarUsuarioPorCorreo(String correo);
    int actualizarContrasena(int idUsuario, String nuevaContrasena);
    int crearToken(TokenRecuperacion token);
    TokenRecuperacion buscarToken(String token);
    int marcarTokenUsado(int idToken);
}
