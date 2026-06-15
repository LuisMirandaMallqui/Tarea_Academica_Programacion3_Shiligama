package pe.edu.pucp.model.usuario;

import java.time.LocalDate;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Trabajador")
@XmlAccessorType(XmlAccessType.FIELD)
public class Trabajador extends Usuario {
    @XmlElement(name = "cargo")
    @JsonbProperty("cargo")
    private String cargo;
    @XmlElement(name = "fechaIngreso")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaIngreso")
    private LocalDate fechaIngreso;

    public Trabajador() {
    }

    // Constructor completo — usa idUsuario del padre, NO tiene ID propio
    public Trabajador(int idUsuario, String correo, String contrasena,
                      String nombres, String apellidos, String dni,
                      String telefono, String cargo, LocalDate fechaIngreso) {
        super(idUsuario, correo, contrasena, nombres, apellidos, dni, telefono);
        this.cargo = cargo;
        this.fechaIngreso = fechaIngreso;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public String toString() {
        return "Trabajador{" +
                "idUsuario=" + idUsuario +
                ", cargo='" + cargo + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", correo='" + correo + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    @Override
    public String getRol() {
        return "TRABAJADOR";
    }
}
