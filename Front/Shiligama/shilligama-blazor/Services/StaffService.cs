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
// StaffService — lee personal (trabajadores + administradores) desde el API.
//
// GET  /api/trabajadores          → lista de trabajadores
// GET  /api/administradores       → lista de administradores
// POST /api/trabajadores          → nuevo trabajador
// PUT  /api/trabajadores          → actualizar trabajador
// DELETE /api/trabajadores/{id}   → eliminar trabajador
// ============================================================================
public class StaffService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;
    private readonly List<Staff> _staff = new();
    private bool _cargado = false;

    public StaffService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;
        _staff.Clear();

        // Cargar trabajadores
        try
        {
            var trabajadores = await _http.GetFromJsonAsync<List<UserRef>>("trabajadores", _json);
            if (trabajadores != null)
            {
                _staff.AddRange(trabajadores.Select(t => new Staff
                {
                    Id          = $"{t.IdUsuario}",
                    Nombres     = t.Nombres ?? string.Empty,
                    Apellidos   = t.Apellidos ?? string.Empty,
                    Dni         = t.Dni ?? string.Empty,
                    Rol         = "trabajador",
                    Telefono    = t.Telefono ?? string.Empty,
                    Correo      = t.Correo ?? string.Empty,
                    Avatar      = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&h=100&fit=crop",
                    FechaIngreso = DateTime.Now,
                    Estado      = "activo"
                }));
            }
        }
        catch { /* backend no disponible */ }

        // Cargar administradores
        try
        {
            var admins = await _http.GetFromJsonAsync<List<UserRef>>("administradores", _json);
            if (admins != null)
            {
                _staff.AddRange(admins.Select(a => new Staff
                {
                    Id          = $"{a.IdUsuario}",
                    Nombres     = a.Nombres ?? string.Empty,
                    Apellidos   = a.Apellidos ?? string.Empty,
                    Dni         = a.Dni ?? string.Empty,
                    Rol         = "administrador",
                    Telefono    = a.Telefono ?? string.Empty,
                    Correo      = a.Correo ?? string.Empty,
                    Avatar      = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop",
                    FechaIngreso = DateTime.Now,
                    Estado      = "activo"
                }));
            }
        }
        catch { /* ídem */ }

        _cargado = true;
    }

    public List<Staff> GetStaff() => _staff;

    public Staff? GetStaffById(string id) => _staff.FirstOrDefault(s => s.Id == id);

    public async Task<bool> AddStaffAsync(Staff member)
    {
        var dto = new
        {
            nombres      = member.Nombres,
            apellidos    = member.Apellidos,
            dni          = member.Dni,
            correo       = member.Correo,
            contrasena   = "Shiligama2025!",
            telefono     = member.Telefono,
            cargo        = member.Rol == "administrador" ? "Administrador" : "Cajero",
            fechaIngreso = DateTime.Now.ToString("yyyy-MM-dd")
        };

        try
        {
            string endpoint = member.Rol == "administrador" ? "administradores" : "trabajadores";
            var resp = await _http.PostAsJsonAsync(endpoint, dto);
            if (resp.IsSuccessStatusCode)
            {
                int idGenerado = await resp.Content.ReadFromJsonAsync<int>();
                member.Id = $"{idGenerado}";
                if (string.IsNullOrWhiteSpace(member.Avatar))
                    member.Avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&h=100&fit=crop";
                member.FechaIngreso = DateTime.Now;
                _staff.Insert(0, member);
                return true;
            }
        }
        catch { /* error de red */ }

        // Fallback local
        member.Id = $"local-{_staff.Count + 1}";
        member.FechaIngreso = DateTime.Now;
        if (string.IsNullOrWhiteSpace(member.Avatar))
            member.Avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&h=100&fit=crop";
        _staff.Insert(0, member);
        return false;
    }

    // Versión sync de compatibilidad
    public void AddStaff(Staff member)
    {
        member.FechaIngreso = DateTime.Now;
        if (string.IsNullOrWhiteSpace(member.Avatar))
            member.Avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&h=100&fit=crop";
        member.Id = $"local-{_staff.Count + 1}";
        _staff.Insert(0, member);
        _ = AddStaffAsync(member);
    }

    public async Task<bool> UpdateStaffAsync(Staff member)
    {
        if (!int.TryParse(member.Id, out var idNum)) return false;

        var dto = new
        {
            idUsuario  = idNum,
            nombres    = member.Nombres,
            apellidos  = member.Apellidos,
            dni        = member.Dni,
            correo     = member.Correo,
            telefono   = member.Telefono,
            contrasena = "Shiligama2025!"
        };

        try
        {
            string endpoint = member.Rol == "administrador" ? "administradores" : "trabajadores";
            var resp = await _http.PutAsJsonAsync(endpoint, dto);
            if (resp.IsSuccessStatusCode)
            {
                UpdateStaff(member);
                return true;
            }
        }
        catch { /* error de red */ }

        UpdateStaff(member);
        return false;
    }

    public void UpdateStaff(Staff member)
    {
        var existing = GetStaffById(member.Id);
        if (existing == null) return;
        existing.Nombres   = member.Nombres;
        existing.Apellidos = member.Apellidos;
        existing.Dni      = member.Dni;
        existing.Rol      = member.Rol;
        existing.Telefono = member.Telefono;
        existing.Correo   = member.Correo;
        existing.Avatar   = member.Avatar;
        existing.Estado   = member.Estado;
    }

    public void DeleteStaff(string id)
    {
        var existing = GetStaffById(id);
        if (existing != null) _staff.Remove(existing);

        if (int.TryParse(id, out var idNum))
            _ = _http.DeleteAsync($"trabajadores/{idNum}");
    }

    public void ToggleStatus(string id)
    {
        var existing = GetStaffById(id);
        if (existing != null)
            existing.Estado = existing.Estado == "activo" ? "inactivo" : "activo";
    }
}
