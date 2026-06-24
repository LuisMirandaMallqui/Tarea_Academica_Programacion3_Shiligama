package pe.edu.pucp.model.seguridad;

/**
 * DTO para el endpoint PUT /api/trabajadores/{id}/contrasena
 * El front envía la contraseña actual (para verificar) y la nueva.
 */
public class CambiarContrasenaDto {

    private String contrasenaActual;
    private String nuevaContrasena;

    public CambiarContrasenaDto() {}

    public String getContrasenaActual() { return contrasenaActual; }
    public void setContrasenaActual(String contrasenaActual) { this.contrasenaActual = contrasenaActual; }

    public String getNuevaContrasena() { return nuevaContrasena; }
    public void setNuevaContrasena(String nuevaContrasena) { this.nuevaContrasena = nuevaContrasena; }
}
