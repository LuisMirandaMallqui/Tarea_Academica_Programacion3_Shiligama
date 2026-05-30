using System;

namespace shilligama_blazor.Models;

public class Staff
{
    public string Id { get; set; } = string.Empty;
    public string Nombre { get; set; } = string.Empty;
    public string Dni { get; set; } = string.Empty;
    public string Rol { get; set; } = string.Empty; // "administrador" | "trabajador"
    public string Telefono { get; set; } = string.Empty;
    public string Correo { get; set; } = string.Empty;
    public string Avatar { get; set; } = string.Empty;
    public DateTime FechaIngreso { get; set; } = DateTime.Now;
    public string Estado { get; set; } = "activo"; // "activo" | "inactivo"
}
