using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;

namespace shilligama_blazor.Services;

// DTO que mapea TopProductoDto del backend Java
public class TopProductoItem
{
    public int    IdProducto    { get; set; }
    public string Nombre        { get; set; } = string.Empty;
    public string? ImagenUrl    { get; set; }
    public int    TotalUnidades { get; set; }
    public double TotalIngresos { get; set; }
}

// ============================================================================
// ReporteService — consultas analíticas del panel de administración.
//
// Endpoints:
//   GET /api/reportes/top-productos  → top 5 productos más vendidos
// ============================================================================
public class ReporteService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;

    public ReporteService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    // Top 5 productos por unidades vendidas (en ventas completadas).
    public async Task<List<TopProductoItem>> GetTopProductosAsync()
    {
        try
        {
            var lista = await _http.GetFromJsonAsync<List<TopProductoItem>>(
                "reportes/top-productos", _json);
            return lista ?? new();
        }
        catch { return new(); }
    }
}
