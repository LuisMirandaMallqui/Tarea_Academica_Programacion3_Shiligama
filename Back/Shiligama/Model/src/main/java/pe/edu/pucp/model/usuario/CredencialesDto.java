package pe.edu.pucp.model.usuario;

/**
 * DTO de entrada para el login unificado.
 * Lo recibe LoginWS desde el cuerpo del POST /api/auth/login.
 */
public class CredencialesDto {
    private String correo;
    private String contrasena;

    public CredencialesDto() {
    }

    public CredencialesDto(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
