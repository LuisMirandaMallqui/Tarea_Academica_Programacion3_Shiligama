using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// ============================================================================
// TopProductoItem se encuentra en Models/TopProductoItem.cs
// ============================================================================
// ReporteService — consultas analíticas del panel de administración.
//
// Endpoints:
//   GET /api/reportes/top-productos  → top 5 productos más vendidos
//   GET /api/reportes/pdf/*          → reportes en formato PDF (bytes)
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

    public async Task<byte[]> GetPdfDevolucionesAsync(string fechaInicio, string fechaFin, string estado, int diasAlerta)
    {
        var url = $"reportes/pdf/devoluciones?fechaInicio={fechaInicio}&fechaFin={fechaFin}&estado={estado}&diasAlerta={diasAlerta}";
        return await _http.GetByteArrayAsync(url);
    }

    public async Task<byte[]> GetPdfVentasAgrupadasAsync(string fechaInicio, string fechaFin, string agrupacion)
    {
        var url = $"reportes/pdf/ventas-agrupadas?fechaInicio={fechaInicio}&fechaFin={fechaFin}&agrupacion={agrupacion}";
        return await _http.GetByteArrayAsync(url);
    }

    public async Task<byte[]> GetPdfTopProductosAsync(string fechaInicio, string fechaFin, int limite)
    {
        var url = $"reportes/pdf/top-productos?fechaInicio={fechaInicio}&fechaFin={fechaFin}&limite={limite}";
        return await _http.GetByteArrayAsync(url);
    }
}
