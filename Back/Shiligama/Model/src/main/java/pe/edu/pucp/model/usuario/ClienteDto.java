package pe.edu.pucp.model.usuario;

import java.time.LocalDateTime;

public class ClienteDto extends UsuarioDto {
    private int idCliente;
    private String direccionEntrega;


    // Constructor vacío
    public ClienteDto() {
        super();
    }

    // Constructor completo
    public ClienteDto(int idUsuario, String email, String contrasena,
                   String nombres, String apellidos, String dni,
                   String telefono,
                   int idCliente,
                   String direccionEntrega) {
        super(idUsuario, email, contrasena, nombres, apellidos,
                dni, telefono);
        this.idCliente = idCliente;
        this.direccionEntrega = direccionEntrega;
    }

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }


    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }


    // Implementación del método abstracto (polimorfismo)
    @Override
    public String obtenerRol() {
        return "CLIENTE";
    }
}