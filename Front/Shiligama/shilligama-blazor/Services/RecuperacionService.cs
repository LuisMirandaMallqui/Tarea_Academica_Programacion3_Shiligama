using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;

namespace shilligama_blazor.Services;

// ============================================================================
// RecuperacionService — flujo de recuperación de contraseña por correo.
//
// POST /api/recuperacion/solicitar    body { correo }
// POST /api/recuperacion/restablecer  body { token, nuevaContrasena }
// ============================================================================
public class RecuperacionService
{
    private readonly HttpClient _http;

    public RecuperacionService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
    }

    /// Solicita el envío del correo de recuperación. Devuelve true si la
    /// petición se procesó (por seguridad el backend responde 200 aunque el
    /// correo no exista).
    public async Task<bool> SolicitarAsync(string correo)
    {
        try
        {
            var resp = await _http.PostAsJsonAsync("recuperacion/solicitar", new { correo });
            return resp.IsSuccessStatusCode;
        }
        catch { return false; }
    }

    /// Restablece la contraseña con un token. Devuelve (ok, mensaje del backend).
    public async Task<(bool Ok, string Mensaje)> RestablecerAsync(string token, string nuevaContrasena)
    {
        try
        {
            var resp = await _http.PostAsJsonAsync("recuperacion/restablecer",
                new { token, nuevaContrasena });
            string body = await resp.Content.ReadAsStringAsync();
            return (resp.IsSuccessStatusCode, body);
        }
        catch
        {
            return (false, "No se pudo conectar con el servidor. Intenta nuevamente.");
        }
    }
}
