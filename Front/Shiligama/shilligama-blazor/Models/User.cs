namespace shilligama_blazor.Models;

// Mapea la respuesta de /api/auth/login.
// PropertyNameCaseInsensitive = true (configurado en Program.cs) hace que
// "idUsuario", "correo", "nombres", "rol", etc. del JSON Java mapeen
// automáticamente a las propiedades C# sin atributos adicionales.
public class User
{
    public int IdUsuario { get; set; }
    public string Correo { get; set; } = string.Empty;
    public string Nombres { get; set; } = string.Empty;
    public string Apellidos { get; set; } = string.Empty;
    public string? Dni { get; set; }
    public string? Telefono { get; set; }
    public string? DireccionEntrega { get; set; }   // subtipo Cliente
    public string? Cargo { get; set; }              // subtipo Trabajador

    // El backend envía "rol" en mayúsculas: CLIENTE / TRABAJADOR / ADMINISTRADOR
    public string Rol { get; set; } = string.Empty;

    // Alias usados por el resto de la app
    public int    Id    => IdUsuario;
    public string Name  => $"{Nombres} {Apellidos}".Trim();
    public string Email => Correo;

    // El front compara contra "cliente" / "trabajador" / "administrador"
    public string Role => Rol.ToLowerInvariant();
}
