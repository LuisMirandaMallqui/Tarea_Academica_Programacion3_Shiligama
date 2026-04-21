package pe.edu.pucp.model.usuario;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TrabajadorDto extends UsuarioDto {
    private int idTrabajador;
    private LocalDate fechaIngreso;

    public TrabajadorDto() {
    }

    public TrabajadorDto(int idUsuario, String email, String contrasena,
                      String nombres, String apellidos, String dni,
                      String telefono,
                      int idTrabajador,
                      LocalDate fechaIngreso) {
        super(idUsuario, email, contrasena, nombres, apellidos,
                dni, telefono);
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
                ", nombreUsuario='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    @Override
    public String obtenerRol() {
        return "TRABAJADOR";
    }
}
