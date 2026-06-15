using System;
using System.Collections.Generic;

namespace shilligama_blazor.Models;

// ── DTOs que mapean el JSON del backend Java para Ventas y Pedidos ────────────

// Refleja Venta del backend Java
internal class VentaApi
{
    public int IdVenta { get; set; }
    public int IdPedido { get; set; }
    public DateTime? FechaHora { get; set; }
    public double MontoTotal { get; set; }
    public double MontoDescuento { get; set; }
    public string CanalVenta { get; set; } = string.Empty;
    public string EstadoVenta { get; set; } = string.Empty;
    public string? Observaciones { get; set; }
    public UserRef? Cliente { get; set; }
    public UserRef? Trabajador { get; set; }
    public MetodoPagoRef? MetodoPago { get; set; }
    public List<DetalleVentaApi>? Detalles { get; set; }

    public Sale ToSale() => new Sale
    {
        Id         = $"VTA-{IdVenta:D3}",
        Fecha      = FechaHora ?? DateTime.Now,
        Cliente    = string.IsNullOrWhiteSpace($"{Cliente?.Nombres} {Cliente?.Apellidos}".Trim())
                     ? "Público General"
                     : $"{Cliente!.Nombres} {Cliente!.Apellidos}".Trim(),
        Canal      = CanalVenta.ToLower() switch
        {
            "presencial" => "presencial",
            "web"        => "web",
            "whatsapp"   => "whatsapp",
            _            => "presencial"
        },
        Total      = (decimal)MontoTotal,
        MetodoPago = MetodoPago?.Nombre?.ToLower() ?? "efectivo",
        Comprobante= "boleta",
        Estado     = EstadoVenta.ToLower() switch
        {
            "completada" => "completado",
            "anulada"    => "cancelado",
            _            => "completado"
        }
    };
}

// Refleja Pedido del backend Java
internal class PedidoApi
{
    public int IdPedido { get; set; }
    public DateTime? FechaHora { get; set; }
    public double MontoTotal { get; set; }
    public string EstadoPedido { get; set; } = string.Empty;
    public string? DireccionEntrega { get; set; }
    public string? ModalidadVenta { get; set; }
    public string? Observaciones { get; set; }
    public UserRef? Cliente { get; set; }
    public List<DetallePedidoApi>? Detalles { get; set; }

    public Order ToOrder() => new Order
    {
        Id       = $"PED-{IdPedido:D4}",
        Customer = (Cliente != null &&
                    !string.IsNullOrWhiteSpace($"{Cliente.Nombres} {Cliente.Apellidos}".Trim()))
                   ? $"{Cliente.Nombres} {Cliente.Apellidos}".Trim()
                   : "Cliente",
        Date     = FechaHora ?? DateTime.Now,
        Total    = (decimal)MontoTotal,
        Items    = Detalles?.Count ?? 0,
        // Estados canónicos: 1:1 con EstadoPedido del backend
        Status   = (EstadoPedido ?? "").ToLower() switch
        {
            "recibido"   => "recibido",
            "en_proceso" => "en_proceso",
            "atendido"   => "atendido",
            "rechazado"  => "rechazado",
            "cancelado"  => "cancelado",
            _            => "recibido"
        },
        DeliveryMethod = (ModalidadVenta ?? "").ToUpper() switch
        {
            "RECOJO_TIENDA" => "pickup",
            _               => "delivery"
        },
        Channel  = (ModalidadVenta ?? "").ToUpper() switch
        {
            "RECOJO_TIENDA" => "Presencial",
            _               => "Online"
        },
        Address  = DireccionEntrega ?? string.Empty,
        TimelinePedidoRecibido = FechaHora ?? DateTime.Now,
    };
}

// Clases de referencia compartidas (MetodoPago, Usuario, Producto)
internal class MetodoPagoRef
{
    public int IdMetodoPago { get; set; }
    public string Nombre { get; set; } = string.Empty;
}

internal class UserRef
{
    public int IdUsuario { get; set; }
    public string Nombres { get; set; } = string.Empty;
    public string Apellidos { get; set; } = string.Empty;
    public string? Correo { get; set; }
    public string? Dni { get; set; }
    public string? Telefono { get; set; }
    public string? Cargo { get; set; }
    public string? Rol { get; set; }
}

internal class DetalleVentaApi
{
    public int IdDetalleVenta { get; set; }
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
