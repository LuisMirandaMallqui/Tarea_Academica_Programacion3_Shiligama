package pe.edu.pucp.model.seguridad;

/** Cuerpo de la petición para solicitar el correo de recuperación. */
public class SolicitarRecuperacionDto {
    private String correo;

    public SolicitarRecuperacionDto() {
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
