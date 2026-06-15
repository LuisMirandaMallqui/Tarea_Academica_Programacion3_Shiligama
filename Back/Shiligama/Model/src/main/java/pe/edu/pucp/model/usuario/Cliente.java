package pe.edu.pucp.model.usuario;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Cliente")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cliente extends Usuario {
    @XmlElement(name = "direccionEntrega")
    @JsonbProperty("direccionEntrega")
    private String direccionEntrega;

    // Constructor vacío
    public Cliente() {
        super();
    }

    // Constructor completo — usa idUsuario del padre, NO tiene ID propio
    public Cliente(int idUsuario, String correo, String contrasena,
                   String nombres, String apellidos, String dni,
                   String telefono, String direccionEntrega) {
        super(idUsuario, correo, contrasena, nombres, apellidos, dni, telefono);
        this.direccionEntrega = direccionEntrega;
    }

    // Getters y Setters — solo atributos propios del hijo
    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    // Implementación del método abstracto (polimorfismo)
    @Override
    public String getRol() {
        return "CLIENTE";
    }
}
