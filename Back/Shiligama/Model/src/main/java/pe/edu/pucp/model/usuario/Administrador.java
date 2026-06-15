package pe.edu.pucp.model.usuario;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Administrador")
@XmlAccessorType(XmlAccessType.FIELD)
public class Administrador extends Usuario {

    // Constructor vacío
    public Administrador() {
        super();
    }

    // Constructor completo — usa idUsuario del padre, NO tiene ID propio
    public Administrador(int idUsuario, String correo, String contrasena,
                         String nombres, String apellidos, String dni,
                         String telefono) {
        super(idUsuario, correo, contrasena, nombres, apellidos, dni, telefono);
    }

    // Implementación del método abstracto (polimorfismo)
    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }
}
