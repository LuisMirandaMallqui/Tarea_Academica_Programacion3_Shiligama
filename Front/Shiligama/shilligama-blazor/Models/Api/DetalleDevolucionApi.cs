using System.Text.Json.Serialization;

namespace shilligama_blazor.Models;

public class DetalleDevolucionApi
{
    [JsonPropertyName("idProducto")]
    public int IdProducto { get; set; }

    [JsonPropertyName("nombreProducto")]
    public string NombreProducto { get; set; } = string.Empty;

    [JsonPropertyName("precioUnitario")]
    public double PrecioUnitario { get; set; }

    [JsonPropertyName("cantidad")]
    public int Cantidad { get; set; }
}
