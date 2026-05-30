using System;
using System.Collections.Generic;

namespace shilligama_blazor.Models;

public class Sale
{
    public string Id { get; set; } = string.Empty;
    public DateTime Fecha { get; set; } = DateTime.Now;
    public string Cliente { get; set; } = string.Empty;
    public string Canal { get; set; } = "web"; // "presencial" | "web" | "whatsapp"
    public List<CartItem> Productos { get; set; } = new();
    public decimal Total { get; set; }
    public string MetodoPago { get; set; } = "tarjeta"; // "yape" | "plin" | "tarjeta" | "efectivo"
    public string Comprobante { get; set; } = "boleta"; // "boleta" | "factura" | "ninguno"
    public string Estado { get; set; } = "completado"; // "completado" | "pendiente" | "cancelado" | "reembolsado"
}
