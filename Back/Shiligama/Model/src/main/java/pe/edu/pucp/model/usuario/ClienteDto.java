package pe.edu.pucp.model.usuario;

import java.time.LocalDateTime;

public class ClienteDto extends UsuarioDto {
    private int idCliente;
    private String telefonoWhatsapp;
    private String direccionEntrega;
    private LocalDateTime fechaRegistro;

    // Constructor vacío
    public ClienteDto() {
        super();
    }

    // Constructor completo
    public ClienteDto(int idUsuario, String nombreUsuario, String contrasena,
                   String nombres, String apellidos, String dni,
                   String telefono, String email, String direccion,
                   boolean estado, LocalDateTime fechaCreacion,
                   int idCliente, String telefonoWhatsapp,
                   String direccionEntrega, LocalDateTime fechaRegistro) {
        super(idUsuario, nombreUsuario, contrasena, nombres, apellidos,
                dni, telefono, email, direccion, estado, fechaCreacion);
        this.idCliente = idCliente;
        this.telefonoWhatsapp = telefonoWhatsapp;
        this.direccionEntrega = direccionEntrega;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getTelefonoWhatsapp() { return telefonoWhatsapp; }
    public void setTelefonoWhatsapp(String telefonoWhatsapp) { this.telefonoWhatsapp = telefonoWhatsapp; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // Implementación del método abstracto (polimorfismo)
    @Override
    public String obtenerRol() {
        return "CLIENTE";
    }
}