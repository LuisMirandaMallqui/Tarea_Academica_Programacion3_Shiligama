package pe.edu.pucp.model.usuario;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Usuario")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Usuario {
    @XmlElement(name = "idUsuario")
    @JsonbProperty("idUsuario")
    protected int idUsuario;
    @XmlElement(name = "correo")
    @JsonbProperty("correo")
    protected String correo; //Será usado como usuario para inicio de sesion
    @XmlElement(name = "contrasena")
    @JsonbProperty("contrasena")
    protected String contrasena;
    @XmlElement(name = "nombres")
    @JsonbProperty("nombres")
    protected String nombres;
    @XmlElement(name = "apellidos")
    @JsonbProperty("apellidos")
    protected String apellidos;
    @XmlElement(name = "dni")
    @JsonbProperty("dni")
    protected String dni;
    @XmlElement(name = "telefono")
    @JsonbProperty("telefono")
    protected String telefono;

    // Constructor vacío (necesario para persistencia)
    public Usuario() {
    }

    // Constructor completo
    public Usuario(int idUsuario, String correo, String contrasena,
                   String nombres, String apellidos, String dni,
                   String telefono) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.correo = correo;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }


    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }


    // Polimórfico: cada subtipo devuelve "CLIENTE", "TRABAJADOR" o "ADMINISTRADOR".
    // Es getter para que Jackson lo serialice como "rol" en el JSON.
    public abstract String getRol();
}