using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;

namespace shilligama_blazor.Services;

// ============================================================================
// PagoService — inicia pagos contra la pasarela Izipay (modo redirección).
//
// POST /api/pagos/iniciar   body { idPedido, email }
//   → { exito, mensaje, redirectionUrl, orderId, idPago }
//
// El frontend debe redirigir al cliente a `redirectionUrl` (página segura de
// Izipay). La confirmación del pago la procesa el backend vía el callback IPN.
// ============================================================================
public class PagoService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;

    public PagoService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    public async Task<RespuestaIniciarPago?> IniciarPagoAsync(int idPedido, string email)
    {
        try
        {
            var body = new { idPedido = idPedido, email = email };
            var resp = await _http.PostAsJsonAsync("pagos/iniciar", body);
            if (resp.IsSuccessStatusCode)
            {
                return await resp.Content.ReadFromJsonAsync<RespuestaIniciarPago>(_json);
            }
        }
        catch { /* backend o pasarela no disponible */ }
        return null;
    }

    // Estado del pago de un pedido (para la pantalla de resultado).
    public async Task<RespuestaPagoEstado?> ConsultarPorPedidoAsync(int idPedido)
    {
        try
        {
            return await _http.GetFromJsonAsync<RespuestaPagoEstado>($"pagos/pedido/{idPedido}", _json);
        }
        catch { return null; }
    }
}

// Mapea pe.edu.pucp.model.venta.RespuestaIniciarPago
public class RespuestaIniciarPago
{
    public bool   Exito          { get; set; }
    public string? Mensaje        { get; set; }
    public string? RedirectionUrl { get; set; }
    public string? OrderId        { get; set; }
    public int    IdPago         { get; set; }
}

// Mapea pe.edu.pucp.model.venta.Pago (campos relevantes)
public class RespuestaPagoEstado
{
    public int     IdPago  { get; set; }
    public int     IdPedido{ get; set; }
    public double  Monto   { get; set; }
    public string? Estado  { get; set; } // PENDIENTE | AUTORIZADO | RECHAZADO | CANCELADO
    public string? Referencia { get; set; }
}
