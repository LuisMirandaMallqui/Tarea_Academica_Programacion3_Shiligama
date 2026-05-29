package pe.edu.pucp.model.usuario;

/**
 * @deprecated CLASE A ELIMINAR.
 *
 * Era un DTO espejo de Usuario solo para agregar el campo "rol". Sobraba:
 * el login ahora devuelve un Usuario polimórfico (Cliente / Trabajador /
 * Administrador) y el rol viene del método getRol() de cada subtipo.
 *
 * No se referencia en ningún archivo del proyecto. BORRAR ESTE ARCHIVO
 * MANUALMENTE desde IntelliJ (no se pudo borrar desde la sesión por
 * problemas del mount del sandbox).
 */
@Deprecated
public final class UsuarioAutenticadoDto {
    private UsuarioAutenticadoDto() {
        // No instanciable.
    }
}
