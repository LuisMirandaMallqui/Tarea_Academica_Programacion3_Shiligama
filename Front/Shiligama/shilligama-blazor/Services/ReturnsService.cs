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
// ReturnsService — lee y gestiona devoluciones desde /api/devoluciones.
//
// GET  /api/devoluciones          → lista de devoluciones
// POST /api/devoluciones          → nueva devolución
// DELETE /api/devoluciones/{id}   → eliminar
// ============================================================================
public class ReturnsService
{
    public string? UltimoErrorDevolucion { get; private set; }
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;
    private readonly List<Return> _returns = new();
    private bool _cargado = false;

    public ReturnsService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;

        try
        {
            var lista = await _http.GetFromJsonAsync<List<DevolucionApi>>("devoluciones", _json);
            _returns.Clear();
            if (lista != null)
                _returns.AddRange(lista.Where(d => d.Activo).Select(d => d.ToReturn()));
        }
        catch { /* backend no disponible */ }

        _cargado = true;
    }

    public List<Return> GetReturns() => _returns;

    public Return? GetReturnById(string id) => _returns.FirstOrDefault(r => r.Id == id);

    // Crea devolución en el backend.
    public async Task<bool> AddReturnAsync(Return returnItem, int idProducto, int idUsuarioRegistra)
    {
        UltimoErrorDevolucion = null;

        var dto = DevolucionApi.FromReturn(returnItem, idProducto, idUsuarioRegistra);

        try
        {
            Console.WriteLine("JSON DEVOLUCIÓN ENVIADO:");
            Console.WriteLine(JsonSerializer.Serialize(dto, _json));

            var resp = await _http.PostAsJsonAsync("devoluciones", dto, _json);

            if (resp.IsSuccessStatusCode)
            {
                int idGenerado = await resp.Content.ReadFromJsonAsync<int>();
                returnItem.Id = $"DEV-{idGenerado:D3}";
                returnItem.Date = DateTime.Now;
                _returns.Insert(0, returnItem);
                return true;
            }

            string errorBody = await resp.Content.ReadAsStringAsync();

            UltimoErrorDevolucion =
                $"HTTP {(int)resp.StatusCode}: {errorBody}";

            Console.WriteLine("ERROR AL REGISTRAR DEVOLUCIÓN");
            Console.WriteLine(UltimoErrorDevolucion);
            Console.WriteLine($"IdProducto enviado: {idProducto}");
            Console.WriteLine($"IdUsuario enviado: {idUsuarioRegistra}");
            Console.WriteLine($"IdPedido enviado: {returnItem.IdPedido}");
            Console.WriteLine($"IdVenta enviado: {returnItem.IdVenta}");
            Console.WriteLine($"Estado enviado: {returnItem.Estado}");
        }
        catch (Exception ex)
        {
            UltimoErrorDevolucion = $"Error de red: {ex.Message}";
            Console.WriteLine($"Error de red al registrar devolución: {ex.Message}");
        }

        return false;
    }

    // Versión síncrona de compatibilidad
    public void AddReturn(Return returnItem)
    {
        var localId = _returns.Count + 1;
        returnItem.Id   = $"DEV-{localId:D3}";
        returnItem.Date = DateTime.Now;
        _returns.Insert(0, returnItem);

        _ = _http.PostAsJsonAsync("devoluciones", DevolucionApi.FromReturn(returnItem, 0, 0));
    }

    public async Task<bool> UpdateReturnAsync(Return returnItem, int idUsuarioRegistra)
    {
        var dto = DevolucionApi.FromReturn(returnItem, 0, idUsuarioRegistra);
        try
        {
            var resp = await _http.PutAsJsonAsync("devoluciones", dto);
            if (resp.IsSuccessStatusCode)
            {
                var existing = GetReturnById(returnItem.Id);
                if (existing != null)
                {
                    existing.Product      = returnItem.Product;
                    existing.ProductCode  = returnItem.ProductCode;
                    existing.Quantity     = returnItem.Quantity;
                    existing.Reason       = returnItem.Reason;
                    existing.Amount       = returnItem.Amount;
                    existing.Observations = returnItem.Observations;
                    existing.IdPedido     = returnItem.IdPedido;
                    existing.Detalles     = returnItem.Detalles;
                }
                return true;
            }
        }
        catch { /* error de red */ }
        return false;
    }

    public void UpdateReturn(Return returnItem)
    {
        var existing = GetReturnById(returnItem.Id);
        if (existing == null) return;
        existing.Product     = returnItem.Product;
        existing.ProductCode = returnItem.ProductCode;
        existing.Quantity    = returnItem.Quantity;
        existing.Reason      = returnItem.Reason;
        existing.RegisteredBy = returnItem.RegisteredBy;
        existing.Amount      = returnItem.Amount;
        existing.Observations = returnItem.Observations;
        existing.IdPedido     = returnItem.IdPedido;
        existing.Detalles     = returnItem.Detalles;
    }

    public void DeleteReturn(string id)
    {
        var existing = GetReturnById(id);
        if (existing == null) return;
        _returns.Remove(existing);

        // Intentar borrar en el back (DEV-001 → 1)
        if (int.TryParse(id.Replace("DEV-", ""), out var numId))
            _ = _http.DeleteAsync($"devoluciones/{numId}");
    }


    public async Task<bool> CambiarEstadoAsync(Return returnItem, string nuevoEstado, int idUsuarioRegistra)
    {
        returnItem.Estado = nuevoEstado.ToLower();

        // Obtener idProducto del primer detalle si existe
        int idProducto = returnItem.Detalles != null && returnItem.Detalles.Any()
            ? returnItem.Detalles.First().IdProducto
            : 1; // fallback mínimo para pasar validación

        var dto = DevolucionApi.FromReturn(returnItem, idProducto, idUsuarioRegistra);
            
        try
        {
            var resp = await _http.PutAsJsonAsync("devoluciones", dto);
            return resp.IsSuccessStatusCode;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error: {ex.Message}");
            return false;
        }
    }   
}

// ── DevolucionApi se encuentra en Models/Api/DevolucionApi.cs ──
