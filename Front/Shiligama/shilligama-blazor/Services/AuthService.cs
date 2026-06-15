using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.JSInterop;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// Login unificado: POST /api/auth/login
// El back devuelve el Usuario con su "rol" (CLIENTE/TRABAJADOR/ADMINISTRADOR).
// También persiste la sesión en localStorage para que RoleGate funcione al recargar.
public class AuthService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;
    private readonly IJSRuntime _js;

    private const string StorageKey = "shiligama-auth";
    private bool _initialized = false;

    public User? CurrentUser { get; private set; }
    public event Action? OnChange;

    public AuthService(HttpClient http, JsonSerializerOptions json, IJSRuntime js)
    {
        _http = http;
        _json = json;
        _js   = js;
    }

    // Restaura la sesión desde localStorage (lo llama RoleGate al montar).
    // Si la sesión ya está en memoria (el usuario acaba de hacer login), no hace nada.
    public async Task InitializeAsync()
    {
        if (_initialized) return;
        _initialized = true;

        if (CurrentUser != null) return;  // ya autenticado en esta sesión

        try
        {
            var jsonStr = await _js.InvokeAsync<string?>("localStorageHelper.getItem", StorageKey);
            if (!string.IsNullOrEmpty(jsonStr))
            {
                CurrentUser = JsonSerializer.Deserialize<User>(jsonStr, _json);
                NotifyStateChanged();
            }
        }
        catch { /* prerender o circuito no listo */ }
    }

    // POST /api/auth/login — llama al SP AUTENTICAR_USUARIO del back.
    public string LastError { get; private set; } = string.Empty;

    public async Task<bool> LoginAsync(string email, string password)
    {
        LastError = string.Empty;
        try
        {
            var resp = await _http.PostAsJsonAsync("auth/login",
                new { correo = email, contrasena = password });

            if (!resp.IsSuccessStatusCode)
            {
                LastError = $"HTTP {(int)resp.StatusCode}: credenciales incorrectas";
                return false;
            }

            CurrentUser = await resp.Content.ReadFromJsonAsync<User>(_json);
            if (CurrentUser == null)
            {
                LastError = "El servidor respondió 200 pero no se pudo leer el usuario (JSON inválido)";
                return false;
            }

            await PersistSessionAsync();
            NotifyStateChanged();
            return true;
        }
        catch (Exception ex)
        {
            LastError = $"Excepción: {ex.Message}";
            return false;
        }
    }

    // Registro de cliente: POST /api/clientes + login automático.
    // dni DEBE ser exactamente 8 dígitos numéricos (lo valida el backend).
    public async Task<bool> RegisterAsync(string nombres, string apellidos, string dni,
                                          string email, string password)
    {
        try
        {
            var nuevoCliente = new
            {
                correo           = email,
                contrasena       = password,
                nombres          = nombres,
                apellidos        = apellidos,
                dni              = dni,
                telefono         = string.Empty,
                direccionEntrega = string.Empty
            };

            var resp = await _http.PostAsJsonAsync("clientes", nuevoCliente);
            if (!resp.IsSuccessStatusCode) return false;

            return await LoginAsync(email, password);
        }
        catch { return false; }
    }

    // Invitado: crea un usuario temporal sin llamar al API (para LoginRequiredModal).
    public void LoginAsGuest()
    {
        CurrentUser = new User { Correo = "invitado@shiligama.com", Nombres = "Invitado", Rol = "CLIENTE" };
        _ = PersistSessionAsync();
        NotifyStateChanged();
    }

    public void Logout()
    {
        CurrentUser = null;
        _initialized = false;
        _ = ClearSessionAsync();
        NotifyStateChanged();
    }

    public bool IsLoggedIn() => CurrentUser != null;
    public bool IsAdmin()    => CurrentUser?.Role == "administrador";
    public bool IsWorker()   => CurrentUser?.Role == "trabajador";
    public bool IsCliente()  => CurrentUser?.Role == "cliente";

    // Sincroniza el CurrentUser en memoria con localStorage.
    // Llamar desde fuera cuando se actualiza el perfil (ej: Configuracion.razor)
    // para que los datos persistan tras un refresh.
    public Task SyncSessionAsync() => PersistSessionAsync();

    private async Task PersistSessionAsync()
    {
        if (CurrentUser is null) return;
        try
        {
            var json = JsonSerializer.Serialize(CurrentUser, _json);
            await _js.InvokeVoidAsync("localStorageHelper.setItem", StorageKey, json);
        }
        catch { }
    }

    private async Task ClearSessionAsync()
    {
        try { await _js.InvokeVoidAsync("localStorageHelper.removeItem", StorageKey); }
        catch { }
    }

    private void NotifyStateChanged() => OnChange?.Invoke();
}
