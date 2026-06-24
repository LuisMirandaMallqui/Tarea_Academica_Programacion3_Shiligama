using System.Text.Json.Serialization;

namespace shilligama_blazor.Models;

/// <summary>
/// Parámetros globales del sistema, sincronizados con la tabla `configuracion` del back.
/// </summary>
public class ConfiguracionDto
{
    [JsonPropertyName("nombreTienda")]
    public string NombreTienda { get; set; } = "Shiligama Minimarket";

    [JsonPropertyName("moneda")]
    public string Moneda { get; set; } = "PEN (S/.)";

    [JsonPropertyName("igv")]
    public decimal Igv { get; set; } = 18m;

    [JsonPropertyName("tarifaEnvio")]
    public decimal TarifaEnvio { get; set; } = 5m;

    [JsonPropertyName("minimoEnvioGratis")]
    public decimal MinimoEnvioGratis { get; set; } = 50m;
}
