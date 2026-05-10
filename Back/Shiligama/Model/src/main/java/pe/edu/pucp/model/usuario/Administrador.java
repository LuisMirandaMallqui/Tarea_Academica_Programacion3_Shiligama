package pe.edu.pucp.model.usuario;

import java.time.LocalDateTime;

public class Administrador extends Usuario {
    private int idAdministrador;

    // Constructor vacío
    public Administrador() {
        super();
    }

    // Constructor completo
    public Administrador(int idUsuario, String nombreUsuario, String contrasena,
                         String nombres, String apellidos, String dni,
                         String telefono, String email, String direccion,
                         boolean estado, LocalDateTime fechaCreacion,
                         int idAdministrador) {
        super(idUsuario, email, contrasena, nombres, apellidos, dni, telefono);
        this.idAdministrador = idAdministrador;
    }

    // Getters y Setters
    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    // Implementación del método abstracto (polimorfismo)
    @Override
    public String obtenerRol() {
        return "ADMINISTRADOR";
    }
}