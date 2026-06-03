package pe.edu.pucp.model.seguridad;

import java.time.LocalDateTime;

/**
 * Token de un solo uso para el flujo de recuperación de contraseña.
 * Se genera al solicitar el restablecimiento, viaja por correo y se
 * invalida al usarse o al expirar.
 */
public class TokenRecuperacion {
    private int idToken;
    private int idUsuario;
    private String token;
    private LocalDateTime expiracion;
    private boolean usado;

    public TokenRecuperacion() {
    }

    public TokenRecuperacion(int idToken, int idUsuario, String token,
                             LocalDateTime expiracion, boolean usado) {
        this.idToken = idToken;
        this.idUsuario = idUsuario;
        this.token = token;
        this.expiracion = expiracion;
        this.usado = usado;
    }

    public int getIdToken() { return idToken; }
    public void setIdToken(int idToken) { this.idToken = idToken; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiracion() { return expiracion; }
    public void setExpiracion(LocalDateTime expiracion) { this.expiracion = expiracion; }

    public boolean isUsado() { return usado; }
    public void setUsado(boolean usado) { this.usado = usado; }

    /** true si el token sigue siendo válido (no usado y no expirado). */
    public boolean esValido() {
        return !usado && expiracion != null && expiracion.isAfter(LocalDateTime.now());
    }
}
