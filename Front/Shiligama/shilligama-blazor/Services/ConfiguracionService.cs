using System.Net.Http.Json;
using System.Text.Json;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// ============================================================================
// ConfiguracionService — Singleton que lee la configuración global del back
// una sola vez y la expone en caché. CartService y Configuracion.razor
// consumen este servicio para obtener la tarifa de envío y otros parámetros.
//
// Endpoints:
//   GET /api/configuracion   → lee la configuración
//   PUT /api/configuracion   → guarda la configuración
// ============================================================================
public class ConfiguracionService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;

    private ConfiguracionDto _cache = new();    // valores por defecto mientras carga
    private bool _loaded = false;
    private readonly SemaphoreSlim _lock = new(1, 1);

    // Accesores rápidos (sin await) para CartService, labels, etc.
    public decimal TarifaEnvio       => _cache.TarifaEnvio;
    public decimal MinimoEnvioGratis => _cache.MinimoEnvioGratis;
    public string  NombreTienda      => _cache.NombreTienda;
    public string  Moneda            => _cache.Moneda;
    public decimal Igv               => _cache.Igv;

    public ConfiguracionService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    /// <summary>
    /// Carga la configuración desde el back la primera vez; no-op en llamadas posteriores.
    /// Llamar desde OnInitializedAsync de cualquier componente que necesite los valores.
    /// </summary>
    public async Task EnsureLoadedAsync()
    {
        if (_loaded) return;

        await _lock.WaitAsync();
        try
        {
            if (_loaded) return;  // doble check dentro del lock
            var dto = await _http.GetFromJsonAsync<ConfiguracionDto>(
                "configuracion", _json);
            if (dto != null) _cache = dto;
            _loaded = true;
        }
        catch (Exception ex)
        {
            // Si falla el back, nos quedamos con los valores por defecto del DTO
            Console.WriteLine($"[ConfiguracionService] No se pudo cargar configuración: {ex.Message}");
        }
        finally
        {
            _lock.Release();
        }
    }

    /// <summary>
    /// Devuelve una copia del DTO en caché para editar en el formulario.
    /// </summary>
    public ConfiguracionDto GetCopy() => new()
    {
        NombreTienda      = _cache.NombreTienda,
        Moneda            = _cache.Moneda,
        Igv               = _cache.Igv,
        TarifaEnvio       = _cache.TarifaEnvio,
        MinimoEnvioGratis = _cache.MinimoEnvioGratis
    };

    /// <summary>
    /// Persiste la configuración en el back y actualiza el caché local.
    /// </summary>
    public async Task GuardarAsync(ConfiguracionDto dto)
    {
        var response = await _http.PutAsJsonAsync("configuracion", dto, _json);
        response.EnsureSuccessStatusCode();
        _cache = dto;   // actualiza caché tras éxito
    }
}
