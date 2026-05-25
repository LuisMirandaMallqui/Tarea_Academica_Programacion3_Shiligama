package pe.edu.pucp.model.usuario;

public class Cliente extends Usuario {
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
    public String obtenerRol() {
        return "CLIENTE";
    }
}
