using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// ============================================================================
// PromocionService
//
// GET  /api/promociones/con-productos  → todas las promos CON sus productos
//                                        en UNA sola llamada (sin N+1)
// POST /api/promociones                → crear
// PUT  /api/promociones                → modificar
// POST /api/promociones/{id}/productos/{pid}  → asociar
// DELETE /api/promociones/{id}/productos/{pid} → desasociar
// ============================================================================
public class PromocionService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;

    private readonly List<Promocion> _promociones = new();
    private bool _cargado = false;

    public PromocionService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    // Carga todas las promos con sus productos en UNA sola llamada HTTP.
    // Antes: 1 llamada para promos + N llamadas para productos = muy lento.
    // Ahora: 1 sola llamada a /api/promociones/con-productos.
    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;
        try
        {
            var lista = await _http.GetFromJsonAsync<List<PromocionConProductosApi>>(
                "promociones/con-productos", _json);
            _promociones.Clear();
            if (lista != null)
                _promociones.AddRange(lista.Select(p => p.ToPromocion()));
        }
        catch { /* backend no disponible */ }
        _cargado = true;
    }

    public List<Promocion> GetPromociones() => _promociones;
    public List<Promocion> GetVigentes() => _promociones.Where(p => p.EsVigente).ToList();

    public async Task<bool> AddPromocionAsync(Promocion promo)
    {
        var dto = PromocionApi.FromPromocion(promo);
        try
        {
            var resp = await _http.PostAsJsonAsync("promociones", dto);
            if (resp.IsSuccessStatusCode)
            {
                int id = await resp.Content.ReadFromJsonAsync<int>();
                promo.Id = id;
                _promociones.Insert(0, promo);
                return true;
            }
        }
        catch { }
        _promociones.Insert(0, promo);
        return false;
    }

    public async Task<bool> UpdatePromocionAsync(Promocion promo)
    {
        var dto = PromocionApi.FromPromocion(promo);
        try
        {
            var resp = await _http.PutAsJsonAsync("promociones", dto);
            if (resp.IsSuccessStatusCode)
            {
                var existing = _promociones.FirstOrDefault(p => p.Id == promo.Id);
                if (existing != null)
                {
                    existing.Nombre = promo.Nombre;
                    existing.Descripcion = promo.Descripcion;
                    existing.TipoDescuento = promo.TipoDescuento;
                    existing.ValorDescuento = promo.ValorDescuento;
                    existing.FechaInicio = promo.FechaInicio;
                    existing.FechaFin = promo.FechaFin;
                    existing.Condiciones = promo.Condiciones;
                    existing.Activo = promo.Activo;
                    existing.ProductoIds = new List<int>(promo.ProductoIds);
                }
                return true;
            }
        }
        catch { }
        return false;
    }

    public async Task<bool> DeletePromocionAsync(int id)
    {
        try
        {
            var resp = await _http.DeleteAsync($"promociones/{id}");
            if (resp.IsSuccessStatusCode)
            {
                _promociones.RemoveAll(p => p.Id == id);
                return true;
            }
        }
        catch { }
        _promociones.RemoveAll(p => p.Id == id);
        return false;
    }

    public async Task<bool> AsociarProductoAsync(int idPromocion, int idProducto)
    {
        try
        {
            var resp = await _http.PostAsync($"promociones/{idPromocion}/productos/{idProducto}", null);
            return resp.IsSuccessStatusCode;
        }
        catch { return false; }
    }

    public async Task<bool> DesasociarProductoAsync(int idPromocion, int idProducto)
    {
        try
        {
            var resp = await _http.DeleteAsync($"promociones/{idPromocion}/productos/{idProducto}");
            return resp.IsSuccessStatusCode;
        }
        catch { return false; }
    }

    // Se mantiene por compatibilidad pero ya no se llama en el loop N+1
    public async Task<List<int>> GetProductosDePromocionAsync(int idPromocion)
    {
        try
        {
            return await _http.GetFromJsonAsync<List<int>>(
                $"promociones/{idPromocion}/productos", _json) ?? new List<int>();
        }
        catch { return new List<int>(); }
    }
}

// DTO que mapea la respuesta de /api/promociones/con-productos
// El back devuelve la lista de productos como objetos con idProducto
internal class PromocionConProductosApi
{
    public int IdPromocion { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string? Descripcion { get; set; }
    public string TipoDescuento { get; set; } = "PORCENTAJE";
    public double ValorDescuento { get; set; }
    public string FechaInicio { get; set; } = string.Empty;
    public string FechaFin { get; set; } = string.Empty;
    public string? Condiciones { get; set; }
    public bool Activo { get; set; } = true;

    // Lista de productos con solo idProducto (el back devuelve objetos mínimos)
    [JsonPropertyName("productos")]
    public List<ProductoIdRef>? Productos { get; set; }

    public Promocion ToPromocion()
    {
        var promo = new Promocion
        {
            Id = IdPromocion,
            Nombre = Nombre,
            Descripcion = Descripcion,
            TipoDescuento = TipoDescuento,
            ValorDescuento = ValorDescuento,
            FechaInicio = DateTime.TryParse(FechaInicio, out var fi) ? fi : DateTime.Today,
            FechaFin = DateTime.TryParse(FechaFin, out var ff) ? ff : DateTime.Today,
            Condiciones = Condiciones,
            Activo = Activo
        };
        // Extraer solo los IDs de los productos vinculados
        if (Productos != null)
            promo.ProductoIds = Productos
                .Where(p => p.IdProducto > 0)
                .Select(p => p.IdProducto)
                .ToList();
        return promo;
    }
}

internal class ProductoIdRef
{
    public int IdProducto { get; set; }
}
