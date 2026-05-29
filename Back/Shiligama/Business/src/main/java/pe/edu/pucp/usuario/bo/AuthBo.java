package pe.edu.pucp.usuario.bo;

import pe.edu.pucp.model.usuario.Usuario;

/**
 * BO unificado de autenticación.
 *
 * Reemplaza los 3 métodos login() duplicados que tenían ClienteWS,
 * TrabajadorWS y AdministradorWS por un único punto de entrada para el
 * login del sistema.
 *
 * Devuelve el subtipo concreto de Usuario (Cliente, Trabajador o
 * Administrador) — el rol se obtiene polimórficamente vía getRol().
 */
public interface AuthBo {
    Usuario autenticar(String correo, String contrasena) throws Exception;
}
