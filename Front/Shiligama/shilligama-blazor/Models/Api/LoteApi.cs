using System;
using System.Text.Json.Serialization;

namespace shilligama_blazor.Models;

public class LoteApi
{
    [JsonPropertyName("idLote")]
    public int IdLote { get; set; }

    [JsonPropertyName("idProducto")]
    public int IdProducto { get; set; }

    [JsonPropertyName("idTrabajador")]
    public int IdTrabajador { get; set; }

    [JsonPropertyName("cantidadInicial")]
    public int CantidadInicial { get; set; }

    [JsonPropertyName("cantidadActual")]
    public int CantidadActual { get; set; }

    [JsonPropertyName("fechaVencimiento")]
    public string? FechaVencimiento { get; set; }

    [JsonPropertyName("numeroLote")]
    public string? NumeroLote { get; set; }

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("nombreTrabajador")]
    public string NombreTrabajador { get; set; } = string.Empty;

    [JsonPropertyName("nombreProducto")]
    public string NombreProducto { get; set; } = string.Empty;
}
