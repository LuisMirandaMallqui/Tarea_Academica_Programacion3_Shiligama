using System;

namespace shilligama_blazor.Models;

public class Staff
{
    public string Id { get; set; } = string.Empty;
    public string Nombres { get; set; } = string.Empty;
    public string Apellidos { get; set; } = string.Empty;
    public string Nombre => $"{Nombres} {Apellidos}".Trim(); // display helper
    public string Dni { get; set; } = string.Empty;
    public string Rol { get; set; } = string.Empty; // "administrador" | "trabajador"
    public string Telefono { get; set; } = string.Empty;
    public string Correo { get; set; } = string.Empty;
    public string Avatar { get; set; } = string.Empty;
    public DateTime FechaIngreso { get; set; } = DateTime.Now;
    public string Estado { get; set; } = "activo"; // "activo" | "inactivo"
    public string Turno { get; set; } = "tiempo-completo"; // "mañana" | "tarde" | "noche" | "tiempo-completo"
}
