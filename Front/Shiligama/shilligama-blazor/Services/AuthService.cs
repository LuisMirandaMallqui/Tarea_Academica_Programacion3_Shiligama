using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// Login unificado: POST /api/auth/login
// El back devuelve el Usuario con su "rol" (CLIENTE/TRABAJADOR/ADMINISTRADOR).
public class AuthService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;

    public User? CurrentUser { get; private set; }
    public event Action? OnChange;

    public AuthService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    // POST /api/auth/login — endpoint unificado del LoginWS
    // Llama al SP AUTENTICAR_USUARIO y devuelve el usuario con su rol.
    // Último error para mostrar en la UI (opcional: inyectar ILogger)
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

    public void Logout()
    {
        CurrentUser = null;
        NotifyStateChanged();
    }

    public bool IsLoggedIn() => CurrentUser != null;
    public bool IsAdmin()    => CurrentUser?.Role == "administrador";
    public bool IsWorker()   => CurrentUser?.Role == "trabajador";

    private void NotifyStateChanged() => OnChange?.Invoke();
}
