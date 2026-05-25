package pe.edu.pucp.model.usuario;

import java.time.LocalDate;

public class Trabajador extends Usuario {
    private LocalDate fechaIngreso;

    public Trabajador() {
    }

    // Constructor completo — usa idUsuario del padre, NO tiene ID propio
    public Trabajador(int idUsuario, String correo, String contrasena,
                      String nombres, String apellidos, String dni,
                      String telefono, LocalDate fechaIngreso) {
        super(idUsuario, correo, contrasena, nombres, apellidos, dni, telefono);
        this.fechaIngreso = fechaIngreso;
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
                ", fechaIngreso=" + fechaIngreso +
                ", correo='" + correo + '\'' +
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
