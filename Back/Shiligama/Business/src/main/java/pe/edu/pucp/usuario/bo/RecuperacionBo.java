package pe.edu.pucp.usuario.bo;

/**
 * Flujo de recuperación de contraseña por correo.
 *
 * <p>1) {@link #solicitarRecuperacion} genera un token de un solo uso, lo
 * guarda y envía un correo con el enlace de restablecimiento.
 * <br>2) {@link #restablecerContrasena} valida el token (vigente y no usado)
 * y actualiza la contraseña del usuario.
 */
public interface RecuperacionBo {

    /**
     * Inicia la recuperación para el correo dado. Por seguridad devuelve
     * {@code true} aunque el correo no exista (no se revela si está registrado);
     * el correo solo se envía si la cuenta existe.
     */
    boolean solicitarRecuperacion(String correo) throws Exception;

    /**
     * Restablece la contraseña a partir de un token válido.
     * @return true si se actualizó correctamente.
     */
    boolean restablecerContrasena(String token, String nuevaContrasena) throws Exception;

    /**
     * Cambia la contraseña directamente dado el ID del usuario (flujo desde perfil).
     * La verificación de la contraseña actual se hace en la capa de servicio (RS)
     * usando AuthBo antes de llamar a este método.
     * @return true si se actualizó correctamente.
     */
    boolean cambiarContrasena(int idUsuario, String nuevaContrasena) throws Exception;
}
