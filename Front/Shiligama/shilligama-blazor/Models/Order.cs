using System;

namespace shilligama_blazor.Models;

public class Order
{
    public string Id { get; set; } = string.Empty;
    public string Customer { get; set; } = string.Empty;
    public DateTime Date { get; set; } = DateTime.Now;
    public decimal Subtotal { get; set; }
    public decimal DeliveryFee { get; set; }
    public decimal Total { get; set; }
    public int Items { get; set; }
    public string Status { get; set; } = "pendiente";
    // "delivery" | "pickup"
    public string DeliveryMethod { get; set; } = "delivery";
    // "yape" | "plin" | "tarjeta" | "efectivo"
    public string PaymentMethod { get; set; } = "efectivo";
    public string Address { get; set; } = string.Empty;
    // Lista de productos del pedido
    public List<CartItem> Products { get; set; } = new();
    // Fechas del timeline de seguimiento
    public DateTime? TimelinePedidoRecibido { get; set; }
    public DateTime? TimelineEnPreparacion { get; set; }
    public DateTime? TimelineListo { get; set; }
    public DateTime? TimelineEnCamino { get; set; }
    public DateTime? TimelineEntregado { get; set; }
    public DateTime? TimelineCancelado { get; set; }
    public string Channel { get; set; } = "Online"; // "Online" | "Presencial"
}
