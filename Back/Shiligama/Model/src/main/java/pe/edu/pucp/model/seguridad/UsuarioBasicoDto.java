package pe.edu.pucp.model.seguridad;

/**
 * Datos mínimos de un usuario (cualquier rol) usados por el flujo de
 * recuperación de contraseña. No expone la contraseña.
 */
public class UsuarioBasicoDto {
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;

    public UsuarioBasicoDto() {
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
