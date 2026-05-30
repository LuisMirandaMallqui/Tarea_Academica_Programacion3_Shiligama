using System;

namespace shilligama_blazor.Models;

public class Return
{
    public string Id { get; set; } = string.Empty;
    public DateTime Date { get; set; } = DateTime.Now;
    public string Product { get; set; } = string.Empty;
    public string ProductCode { get; set; } = string.Empty;
    public int Quantity { get; set; }
    public string Reason { get; set; } = "Otro"; // "Vencido" | "Dañado" | "Error de pedido" | "Otro"
    public string RegisteredBy { get; set; } = string.Empty;
    public decimal Amount { get; set; }
    public string Observations { get; set; } = string.Empty;
}
