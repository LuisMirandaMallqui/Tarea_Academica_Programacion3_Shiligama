namespace ShiligamaWA.Models;

public class SesionActual
{
    public bool Autenticado { get; set; }
    public int UsuarioId { get; set; }
    public string Email { get; set; } = string.Empty;
    public string NombreCompleto { get; set; } = string.Empty;
    public string Iniciales { get; set; } = string.Empty;
    public RolUsuario Rol { get; set; }

    public bool EsCliente => Rol == RolUsuario.Cliente;
    public bool EsTrabajador => Rol == RolUsuario.Trabajador;
    public bool EsAdministrador => Rol == RolUsuario.Administrador;

    public string RolLabel => Rol switch
    {
        RolUsuario.Administrador => "Administrador",
        RolUsuario.Trabajador => "Trabajador",
        _ => "Cliente"
    };
}
