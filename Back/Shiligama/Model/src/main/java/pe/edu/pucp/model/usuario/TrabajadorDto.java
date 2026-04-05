package pe.edu.pucp.model.usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TrabajadorDto extends UsuarioDto {
    private int idTrabajador;
    private LocalDate fechaIngreso;

    public TrabajadorDto() {
    }

    public TrabajadorDto(int idUsuario, String nombreUsuario, String contrasena, String nombres, String apellidos, String dni, String telefono, String email, String direccion, boolean estado, LocalDateTime fechaCreacion, int idTrabajador, LocalDate fechaIngreso) {
        super(idUsuario, nombreUsuario, contrasena, nombres, apellidos, dni, telefono, email, direccion, estado, fechaCreacion);
        this.idTrabajador = idTrabajador;
        this.fechaIngreso = fechaIngreso;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @Override
    public String toString() {
        return "TrabajadorDto{" +
                "idTrabajador=" + idTrabajador +
                ", fechaIngreso=" + fechaIngreso +
                ", idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }

    @Override
    public String obtenerRol() {
        return "TRABAJADOR";
    }
}
