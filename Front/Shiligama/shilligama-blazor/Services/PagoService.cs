using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

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

    /// <param name="monto">Total del pedido. Se envía explícitamente porque el SP
    /// INSERTAR_PEDIDO no guarda MONTO_TOTAL en la BD, por lo que pedido.getMontoTotal()
    /// devolvería 0 y el backend rechazaría el pago.</param>
    public async Task<RespuestaIniciarPago?> IniciarPagoAsync(int idPedido, string email, decimal monto = 0)
    {
        try
        {
            var body = new
            {
                idPedido,
                email,
                monto = monto > 0 ? (double)monto : 0
            };
            var resp = await _http.PostAsJsonAsync("pagos/iniciar", body);
            if (resp.IsSuccessStatusCode)
            {
                return await resp.Content.ReadFromJsonAsync<RespuestaIniciarPago>(_json);
            }
        }
        catch { /* backend o pasarela no disponible */ }
        return null;
    }

    // Demo: confirma el pago en el backend (simula el IPN de Izipay).
    // Solo funciona cuando izipay.demo=true en el backend.
    public async Task<bool> ConfirmarDemoAsync(int idPedido)
    {
        try
        {
            var resp = await _http.PostAsync($"pagos/confirmar-demo/{idPedido}", null);
            return resp.IsSuccessStatusCode;
        }
        catch { return false; }
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

// ── RespuestaIniciarPago y RespuestaPagoEstado se encuentran en Models/RespuestaPago.cs ──
