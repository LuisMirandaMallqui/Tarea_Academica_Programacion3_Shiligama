using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// ============================================================================
// NotificacionService — lee y gestiona notificaciones desde el API REST.
//
// NotificacionItem se encuentra en Models/NotificacionItem.cs
//
// Endpoints:
//   GET  /api/notificaciones                     → todas las notificaciones
//   GET  /api/notificaciones/por-usuario/{id}    → notificaciones del usuario
//   GET  /api/notificaciones/contar-no-leidas/{id}
//   PUT  /api/notificaciones/{id}/marcar-leida
//   DELETE /api/notificaciones/{id}
// ============================================================================
public class NotificacionService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;

    public NotificacionService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    // Trae todas las notificaciones (panel admin).
    public async Task<List<NotificacionItem>> GetAllAsync()
    {
        try
        {
            var lista = await _http.GetFromJsonAsync<List<NotificacionItem>>("notificaciones", _json);
            return lista ?? new();
        }
        catch { return new(); }
    }

    // Trae notificaciones de un usuario específico (incluye broadcasts).
    public async Task<List<NotificacionItem>> GetByUsuarioAsync(int idUsuario)
    {
        try
        {
            var lista = await _http.GetFromJsonAsync<List<NotificacionItem>>(
                $"notificaciones/por-usuario/{idUsuario}", _json);
            return lista ?? new();
        }
        catch { return new(); }
    }

    // Cuenta no leídas — para el badge del icono de campana.
    public async Task<int> ContarNoLeidasAsync(int idUsuario)
    {
        try
        {
            return await _http.GetFromJsonAsync<int>(
                $"notificaciones/contar-no-leidas/{idUsuario}", _json);
        }
        catch { return 0; }
    }

    // Marca una notificación como leída.
    public async Task<bool> MarcarLeidaAsync(int idNotificacion)
    {
        try
        {
            var resp = await _http.PutAsync(
                $"notificaciones/{idNotificacion}/marcar-leida", null);
            return resp.IsSuccessStatusCode;
        }
        catch { return false; }
    }

    // Elimina (soft-delete) una notificación.
    public async Task<bool> EliminarAsync(int idNotificacion)
    {
        try
        {
            var resp = await _http.DeleteAsync($"notificaciones/{idNotificacion}");
            return resp.IsSuccessStatusCode;
        }
        catch { return false; }
    }
}
