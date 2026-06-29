using System;
using System.Collections.Generic;

namespace shilligama_blazor.Models;

public class Return
{
    public string Id { get; set; } = string.Empty;
    public DateTime Date { get; set; } = DateTime.Now;
    public string Product { get; set; } = string.Empty;
    public string ProductCode { get; set; } = string.Empty;
    public int Quantity { get; set; }
    public string Reason { get; set; } = "OTRO"; // "VENCIDO" | "DAÑADO" | "ERROR_PEDIDO" | "OTRO"
    public string MotivoDetalle { get; set; } = string.Empty; // Texto libre cuando Reason = "OTRO"
    public string RegisteredBy { get; set; } = string.Empty;
    public decimal Amount { get; set; }
    public string Observations { get; set; } = string.Empty;
    public string Estado { get; set; } = "pendiente"; // "pendiente" | "aceptado" | "rechazado"
    public int? IdPedido { get; set; }
    public int? IdVenta { get; set; }
    public List<DetalleDevolucionApi> Detalles { get; set; } = new();
}
