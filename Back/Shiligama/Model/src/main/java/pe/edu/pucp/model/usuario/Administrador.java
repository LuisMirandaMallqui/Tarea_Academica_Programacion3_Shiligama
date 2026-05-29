package pe.edu.pucp.model.usuario;

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
