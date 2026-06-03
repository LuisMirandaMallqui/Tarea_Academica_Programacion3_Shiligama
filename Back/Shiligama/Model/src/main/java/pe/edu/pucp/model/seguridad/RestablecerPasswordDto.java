package pe.edu.pucp.model.seguridad;

/** Cuerpo de la petición para restablecer la contraseña con un token. */
public class RestablecerPasswordDto {
    private String token;
    private String nuevaContrasena;

    public RestablecerPasswordDto() {
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNuevaContrasena() { return nuevaContrasena; }
    public void setNuevaContrasena(String nuevaContrasena) { this.nuevaContrasena = nuevaContrasena; }
}
