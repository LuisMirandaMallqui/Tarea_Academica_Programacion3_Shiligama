using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace shilligama_blazor.Models;

// ── DTOs que mapean el JSON del backend Java para Ventas y Pedidos ────────────

// Refleja Venta del backend Java.
// CORRECCIONES:
//   - "estado"     → el Java llama al campo "estado", no "estadoVenta".
//                    Sin [JsonPropertyName] el deserializador busca "estadoVenta" y no lo encuentra.
//   - "canalVenta" → este SÍ coincidía, pero se deja explícito para claridad.
internal class VentaApi
{
    public int IdVenta { get; set; }
    public int IdPedido { get; set; }
    public DateTime? FechaHora { get; set; }
    public double MontoTotal { get; set; }
    public double MontoDescuento { get; set; }

    // El Java serializa el enum CanalVenta como "PRESENCIAL" o "WEB" (mayúsculas).
    // El .ToLower() en ToSale() lo normaliza antes del switch.
    [JsonPropertyName("canalVenta")]
    public string CanalVenta { get; set; } = string.Empty;

    // FIX: el Java manda el campo como "estado" (no "estadoVenta").
    // PropertyNameCaseInsensitive solo ayuda con diferencias de case, no con nombres distintos.
    [JsonPropertyName("estado")]
    public string EstadoVenta { get; set; } = string.Empty;

    public string? Observaciones { get; set; }
    public string? NumeroBoleta { get; set; }
    public UserRef? Cliente { get; set; }
    public UserRef? Trabajador { get; set; }
    public MetodoPagoRef? MetodoPago { get; set; }
    public List<DetalleVentaApi>? Detalles { get; set; }


    public Sale ToSale() => new Sale
    {
        Id = $"VTA-{IdVenta:D3}",
        Fecha = FechaHora ?? DateTime.Now,
        Cliente = string.IsNullOrWhiteSpace($"{Cliente?.Nombres} {Cliente?.Apellidos}".Trim())
                 ? "Público General"
                 : $"{Cliente!.Nombres} {Cliente!.Apellidos}".Trim(),

        Canal = CanalVenta.ToLower() switch
        {
            "presencial" => "presencial",
            "web" => "web",
            "whatsapp" => "whatsapp",
            _ => "presencial"
        },

        Total = (decimal)MontoTotal,
        MetodoPago = MetodoPago?.Nombre?.ToLower() ?? "efectivo",
        Comprobante = "boleta",

        Estado = EstadoVenta.ToLower() switch
        {
            "completada" => "completado",
            "completado" => "completado",
            "anulada" => "cancelado",
            _ => "completado"
        },

        NumeroBoleta = NumeroBoleta,

        Productos = Detalles?.Select(d => new CartItem
        {
            Id = d.Producto?.IdProducto ?? d.IdProducto,
            Name = d.Producto?.Nombre ?? d.NombreProducto ?? "Producto",
            Price = (decimal)(d.PrecioUnitario > 0 ? d.PrecioUnitario : d.Producto?.PrecioUnitario ?? 0),
            Quantity = d.Cantidad,
            Image = ""
        }).ToList() ?? new List<CartItem>()
    };
}

// Refleja Pedido del backend Java
internal class PedidoApi
{
    public int IdPedido { get; set; }
    public DateTime? FechaHora { get; set; }
    public double MontoTotal { get; set; }

    // EstadoPedido es un enum Java → llega como "RECIBIDO", "EN_PROCESO", etc.
    // El .ToLower() en ToOrder() normaliza antes del switch.
    public string EstadoPedido { get; set; } = string.Empty;
    public string? DireccionEntrega { get; set; }
    public string? ModalidadVenta { get; set; }
    public string? Observaciones { get; set; }
    public UserRef? Cliente { get; set; }
    public List<DetallePedidoApi>? Detalles { get; set; }
    public int TotalItems { get; set; }

    public Order ToOrder() => new Order
    {
        Id = $"PED-{IdPedido:D4}",
        Customer = (Cliente != null &&
                    !string.IsNullOrWhiteSpace($"{Cliente.Nombres} {Cliente.Apellidos}".Trim()))
                   ? $"{Cliente.Nombres} {Cliente.Apellidos}".Trim()
                   : "Cliente",
        Date = FechaHora ?? DateTime.Now,
        Total = (decimal)MontoTotal,
        Items = TotalItems > 0 ? TotalItems : (Detalles?.Count ?? 0),
        Status = (EstadoPedido ?? "").ToLower() switch
        {
            "recibido" => "recibido",
            "en_proceso" => "en_proceso",
            "atendido" => "atendido",
            "rechazado" => "rechazado",
            "cancelado" => "cancelado",
            _ => "recibido"
        },
        DeliveryMethod = (ModalidadVenta ?? "").ToUpper() switch
        {
            "RECOJO_TIENDA" => "pickup",
            _ => "delivery"
        },
        Channel = "Online",
        Address = DireccionEntrega ?? string.Empty,
        Observaciones = Observaciones,
        TimelinePedidoRecibido = FechaHora ?? DateTime.Now,
    };
}

// Clases de referencia compartidas
internal class MetodoPagoRef
{
    public int IdMetodoPago { get; set; }
    public string Nombre { get; set; } = string.Empty;
}

internal class UserRef
{
    public int IdUsuario { get; set; }
    public string? Correo { get; set; }
    public string? Dni { get; set; }
    public string? Telefono { get; set; }
    public string? Cargo { get; set; }
    public string? Rol { get; set; }

    [JsonPropertyName("nombres")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("apellidos")]
    public string Apellidos { get; set; } = string.Empty;
}

internal class DetalleVentaApi
{
    public int IdDetalleVenta { get; set; }

    public int IdVenta { get; set; }

    public int IdProducto { get; set; }

    public string? NombreProducto { get; set; }

    public int Cantidad { get; set; }

    public double PrecioUnitario { get; set; }

    public double Subtotal { get; set; }

    public ProductoRef? Producto { get; set; }
}

internal class DetallePedidoApi
{
    public int IdDetallePedido { get; set; }
    public int Cantidad { get; set; }
    public double PrecioUnitario { get; set; }
    public double Subtotal { get; set; }
    public ProductoRef? Producto { get; set; }
}

internal class ProductoRef
{
    public int IdProducto { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public double PrecioUnitario { get; set; }
}

// DTO mínimo para consultar el estado del pago de un pedido
internal class PagoEstadoApi
{
    public int IdPago { get; set; }
    public int IdPedido { get; set; }
    public string? Estado { get; set; } // PENDIENTE | AUTORIZADO | RECHAZADO | CANCELADO
}

