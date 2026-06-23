using System.Text.Json.Serialization;

namespace shilligama_blazor.Models;

// Mapea directamente la "Categoria" del back (idCategoria, nombre, ...).
public class Category
{
    // Clave string usada por el front como filtro. No se serializa hacia el back.
    [JsonIgnore] public string Id { get; set; } = string.Empty;

    [JsonPropertyName("idCategoria")]
    public int IdCategoria
    {
        get => int.TryParse(Id, out var n) ? n : 0;
        set => Id = value.ToString();
    }

    [JsonPropertyName("nombre")]
    public string Name { get; set; } = string.Empty;

    [JsonPropertyName("descripcion")]
    public string? Descripcion { get; set; }

    [JsonPropertyName("icono")]
    public string Icono { get; set; } = "fa-folder";

    [JsonPropertyName("estado")]
    public bool Estado { get; set; } = true;

    [JsonPropertyName("categoriaPadre")]
    public Category? CategoriaPadre { get; set; }
}
