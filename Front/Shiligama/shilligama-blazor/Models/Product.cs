using System.Text.Json.Serialization;

namespace shilligama_blazor.Models;

// Mapea directamente el "Producto" del back. Los nombres en español del JSON
// se enlazan con las propiedades en inglés que ya usa el front vía JsonPropertyName.
public class Product
{
    [JsonPropertyName("idProducto")] public int Id { get; set; }
    [JsonPropertyName("nombre")] public string Name { get; set; } = string.Empty;
    [JsonPropertyName("precioUnitario")] public decimal Price { get; set; }
    [JsonPropertyName("imagenUrl")] public string Image { get; set; } = string.Empty;
    [JsonPropertyName("stock")] public int Stock { get; set; }
    [JsonPropertyName("stockMinimo")] public int MinStock { get; set; } = 10;
    [JsonPropertyName("descripcion")] public string Description { get; set; } = string.Empty;
    [JsonPropertyName("unidadMedida")] public string? UnidadMedida { get; set; }
    [JsonPropertyName("codigoBarras")] public string? CodigoBarras { get; set; }
    [JsonPropertyName("estado")] public bool Estado { get; set; } = true;

    // El back aún NO envía estos campos de promoción (las promos viven en otra
    // entidad). Quedan en default; si algún día se exponen, mapean solos.
    // No se escriben al back cuando van en default para no romper el POST/PUT.
    [JsonPropertyName("precioOriginal")]
    [JsonIgnore(Condition = JsonIgnoreCondition.WhenWritingDefault)]
    public decimal? OriginalPrice { get; set; }

    [JsonPropertyName("esPromo")]
    [JsonIgnore(Condition = JsonIgnoreCondition.WhenWritingDefault)]
    public bool IsPromo { get; set; }

    // Categoría anidada tal cual la envía/recibe el back.
    [JsonPropertyName("categoria")]
    public Category? Categoria { get; set; }

    // Clave de categoría (string del id) usada por filtros y bindings del front.
    // Se mantiene sincronizada con la categoría anidada.
    [JsonIgnore]
    public string Category
    {
        get => Categoria?.Id ?? string.Empty;
        set
        {
            Categoria ??= new Category();
            Categoria.Id = value;
        }
    }
}
