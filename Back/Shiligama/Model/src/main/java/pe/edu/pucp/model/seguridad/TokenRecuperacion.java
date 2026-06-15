package pe.edu.pucp.model.seguridad;

import java.time.LocalDateTime;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

/**
 * Token de un solo uso para el flujo de recuperación de contraseña.
 * Se genera al solicitar el restablecimiento, viaja por correo y se
 * invalida al usarse o al expirar.
 */
@XmlType(name = "TokenRecuperacion")
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenRecuperacion {
    @XmlElement(name = "idToken")
    @JsonbProperty("idToken")
    private int idToken;
    @XmlElement(name = "idUsuario")
    @JsonbProperty("idUsuario")
    private int idUsuario;
    @XmlElement(name = "token")
    @JsonbProperty("token")
    private String token;
    @XmlElement(name = "expiracion")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("expiracion")
    private LocalDateTime expiracion;
    @XmlElement(name = "usado")
    @JsonbProperty("usado")
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
