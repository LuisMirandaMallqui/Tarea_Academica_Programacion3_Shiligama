namespace ShiligamaWA.Models;

public class Usuario
{
    public int Id { get; set; }
    public string Email { get; set; } = string.Empty;
    public string Password { get; set; } = string.Empty; // Mock: plano. En produccion: hashed
    public string Nombres { get; set; } = string.Empty;
    public string Apellidos { get; set; } = string.Empty;
    public string Telefono { get; set; } = string.Empty;
    public string? Dni { get; set; }
    public string? Direccion { get; set; }
    public RolUsuario Rol { get; set; } = RolUsuario.Cliente;
    public string Iniciales => $"{(Nombres.Length > 0 ? Nombres[0] : ' ')}{(Apellidos.Length > 0 ? Apellidos[0] : ' ')}".Trim().ToUpper();
    public string NombreCompleto => $"{Nombres} {Apellidos}".Trim();
    public DateTime FechaRegistro { get; set; } = DateTime.Now;
    public bool Activo { get; set; } = true;
}
