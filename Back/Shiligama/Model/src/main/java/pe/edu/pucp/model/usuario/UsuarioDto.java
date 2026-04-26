package pe.edu.pucp.model.usuario;

public abstract class UsuarioDto {
    protected int idUsuario;
    protected String correo; //Será usado como usuario para inicio de sesion
    protected String contrasena;
    protected String nombres;
    protected String apellidos;
    protected String dni;
    protected String telefono;

    // Constructor vacío (necesario para persistencia)
    public UsuarioDto() {
    }

    // Constructor completo
    public UsuarioDto(int idUsuario, String correo, String contrasena,
                      String nombres, String apellidos, String dni,
                      String telefono) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.correo = correo;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }


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

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }


    // Métodos de negocio (declarados, se implementan en capa de negocio)
    public abstract String obtenerRol();
}