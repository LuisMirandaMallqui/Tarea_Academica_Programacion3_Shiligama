using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// ============================================================================
// PromocionService — gestiona promociones desde /api/promociones
//
// GET    /api/promociones              → lista de promociones
// GET    /api/promociones/vigentes     → solo las activas hoy
// POST   /api/promociones              → crear promoción
// PUT    /api/promociones              → modificar
// DELETE /api/promociones/{id}         → eliminar
// POST   /api/promociones/{id}/productos/{pid}   → asociar producto
// DELETE /api/promociones/{id}/productos/{pid}   → desasociar producto
// GET    /api/promociones/{id}/productos          → IDs de productos asociados
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

    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;
        try
        {
            var lista = await _http.GetFromJsonAsync<List<PromocionApi>>("promociones", _json);
            _promociones.Clear();
            if (lista != null)
                _promociones.AddRange(lista.Select(p => p.ToPromocion()));
        }
        catch { /* backend no disponible */ }
        _cargado = true;
    }

    public List<Promocion> GetPromociones() => _promociones;
    public List<Promocion> GetVigentes()    => _promociones.Where(p => p.EsVigente).ToList();

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
                    existing.Nombre        = promo.Nombre;
                    existing.Descripcion   = promo.Descripcion;
                    existing.TipoDescuento = promo.TipoDescuento;
                    existing.ValorDescuento= promo.ValorDescuento;
                    existing.FechaInicio   = promo.FechaInicio;
                    existing.FechaFin      = promo.FechaFin;
                    existing.Condiciones   = promo.Condiciones;
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

    public async Task<List<int>> GetProductosDePromocionAsync(int idPromocion)
    {
        try
        {
            return await _http.GetFromJsonAsync<List<int>>($"promociones/{idPromocion}/productos", _json)
                   ?? new List<int>();
        }
        catch { return new List<int>(); }
    }
}

// ── Modelo API interno (mapea JSON del backend Java) ──────────────────────

class PromocionApi
{
    public int     IdPromocion    { get; set; }
    public string  Nombre         { get; set; } = string.Empty;
    public string? Descripcion    { get; set; }
    public string  TipoDescuento  { get; set; } = "PORCENTAJE"; // PORCENTAJE | MONTO_FIJO
    public double  ValorDescuento { get; set; }
    public string  FechaInicio    { get; set; } = string.Empty; // yyyy-MM-dd
    public string  FechaFin       { get; set; } = string.Empty;
    public string? Condiciones    { get; set; }
    public bool    Activo         { get; set; } = true;

    public Promocion ToPromocion() => new Promocion
    {
        Id             = IdPromocion,
        Nombre         = Nombre,
        Descripcion    = Descripcion,
        TipoDescuento  = TipoDescuento,
        ValorDescuento = ValorDescuento,
        FechaInicio    = DateTime.TryParse(FechaInicio, out var fi) ? fi : DateTime.Today,
        FechaFin       = DateTime.TryParse(FechaFin,    out var ff) ? ff : DateTime.Today,
        Condiciones    = Condiciones,
        Activo         = Activo
    };

    public static PromocionApi FromPromocion(Promocion p) => new PromocionApi
    {
        IdPromocion    = p.Id,
        Nombre         = p.Nombre,
        Descripcion    = p.Descripcion,
        TipoDescuento  = p.TipoDescuento,
        ValorDescuento = p.ValorDescuento,
        FechaInicio    = p.FechaInicio.ToString("yyyy-MM-dd"),
        FechaFin       = p.FechaFin.ToString("yyyy-MM-dd"),
        Condiciones    = p.Condiciones,
        Activo         = p.Activo
    };
}
