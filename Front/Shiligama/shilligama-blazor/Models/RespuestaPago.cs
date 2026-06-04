namespace shilligama_blazor.Models;

// Mapea pe.edu.pucp.model.venta.RespuestaIniciarPago (respuesta de POST /api/pagos/iniciar)
public class RespuestaIniciarPago
{
    public bool    Exito     { get; set; }
    public string? Mensaje   { get; set; }
    public string? FormToken { get; set; }  // token del formulario embebido Izipay
    public string? PublicKey { get; set; }  // "username:publicKey" para el SDK
    public string? JsBase    { get; set; }  // base del SDK Krypton
    public string? OrderId   { get; set; }
    public int     IdPago    { get; set; }
}

// Mapea pe.edu.pucp.model.venta.Pago (campos relevantes para consulta de estado)
public class RespuestaPagoEstado
{
    public int     IdPago  { get; set; }
    public int     IdPedido{ get; set; }
    public double  Monto   { get; set; }
    public string? Estado  { get; set; } // PENDIENTE | AUTORIZADO | RECHAZADO | CANCELADO
    public string? Referencia { get; set; }
}
