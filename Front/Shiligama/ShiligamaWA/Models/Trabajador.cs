namespace ShiligamaWA.Models;

public class Trabajador
{
    public int Id { get; set; }
    public int UsuarioId { get; set; }
    public string Nombres { get; set; } = string.Empty;
    public string Apellidos { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string Telefono { get; set; } = string.Empty;
    public string Dni { get; set; } = string.Empty;
    public string Cargo { get; set; } = "Cajero";   // Cajero, Almacenero, Repartidor, Supervisor
    public DateTime FechaIngreso { get; set; }
    public bool Activo { get; set; } = true;
    public string Iniciales => $"{(Nombres.Length > 0 ? Nombres[0] : ' ')}{(Apellidos.Length > 0 ? Apellidos[0] : ' ')}".Trim().ToUpper();
    public string NombreCompleto => $"{Nombres} {Apellidos}".Trim();
}
