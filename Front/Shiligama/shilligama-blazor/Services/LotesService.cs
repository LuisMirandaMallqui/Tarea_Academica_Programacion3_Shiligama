using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

public class LotesService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;
    private readonly List<LoteApi> _lotes = new();
    private bool _cargado = false;

    public LotesService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;

        try
        {
            var lista = await _http.GetFromJsonAsync<List<LoteApi>>("lotes", _json);
            _lotes.Clear();
            if (lista != null)
                _lotes.AddRange(lista.Where(l => l.Activo));
        }
        catch { /* backend no disponible */ }

        _cargado = true;
    }

    public List<LoteApi> GetLotes() => _lotes;

    public LoteApi? GetLoteById(int id) => _lotes.FirstOrDefault(l => l.IdLote == id);

    public async Task<int> AddLoteAsync(LoteApi lote)
    {
        try
        {
            var resp = await _http.PostAsJsonAsync("lotes", lote);
            if (resp.IsSuccessStatusCode)
            {
                int idGenerado = await resp.Content.ReadFromJsonAsync<int>();
                lote.IdLote = idGenerado;
                lote.Activo = true;
                _lotes.Insert(0, lote);
                return idGenerado;
            }
        }
        catch { /* error de red */ }
        return 0;
    }

    public async Task<bool> UpdateLoteAsync(LoteApi lote)
    {
        try
        {
            var resp = await _http.PutAsJsonAsync("lotes", lote);
            if (resp.IsSuccessStatusCode)
            {
                var existing = _lotes.FirstOrDefault(l => l.IdLote == lote.IdLote);
                if (existing != null)
                {
                    existing.CantidadActual = lote.CantidadActual;
                    existing.FechaVencimiento = lote.FechaVencimiento;
                    existing.NumeroLote = lote.NumeroLote;
                }
                return true;
            }
        }
        catch { /* error de red */ }
        return false;
    }

    public async Task<bool> DeleteLoteAsync(int id)
    {
        try
        {
            var resp = await _http.DeleteAsync($"lotes/{id}");
            if (resp.IsSuccessStatusCode)
            {
                var existing = _lotes.FirstOrDefault(l => l.IdLote == id);
                if (existing != null) existing.Activo = false;
                return true;
            }
        }
        catch { /* error de red */ }
        return false;
    }

    public async Task<List<LoteApi>> GetLotesByProductoAsync(int idProducto)
    {
        try
        {
            var lista = await _http.GetFromJsonAsync<List<LoteApi>>(
                $"lotes/por-producto/{idProducto}", _json);
            return lista?.Where(l => l.Activo).ToList() ?? new();
        }
        catch { /* backend no disponible */ }
        return new();
    }

    public async Task<List<LoteApi>> GetLotesProximosAVencerAsync(int dias = 30)
    {
        try
        {
            var lista = await _http.GetFromJsonAsync<List<LoteApi>>(
                $"lotes/proximos-vencer?dias={dias}", _json);
            return lista?.Where(l => l.Activo).ToList() ?? new();
        }
        catch { /* backend no disponible */ }
        return new();
    }
}
