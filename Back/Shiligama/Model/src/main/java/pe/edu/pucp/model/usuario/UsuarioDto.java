package pe.edu.pucp.model.usuario;

import java.time.LocalDateTime;

public abstract class UsuarioDto {
    protected int idUsuario;
    protected String nombreUsuario;
    protected String contrasena;
    protected String nombres;
    protected String apellidos;
    protected String dni;
    protected String telefono;
    protected String email;
    protected String direccion;
    protected boolean estado;
    protected LocalDateTime fechaCreacion;

    // Constructor vacío (necesario para persistencia)
    public UsuarioDto() {
    }

    // Constructor completo
    public UsuarioDto(int idUsuario, String nombreUsuario, String contrasena,
                   String nombres, String apellidos, String dni,
                   String telefono, String email, String direccion,
                   boolean estado, LocalDateTime fechaCreacion) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // Métodos de negocio (declarados, se implementan en capa de negocio)
    public abstract String obtenerRol();
}