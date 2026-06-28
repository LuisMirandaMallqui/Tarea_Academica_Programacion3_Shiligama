using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;
using static System.Net.WebRequestMethods;

namespace shilligama_blazor.Models;

internal class DevolucionApi
{
    [JsonPropertyName("idDevolucion")]
    public int IdDevolucion { get; set; }

    [JsonPropertyName("idProducto")]
    public int IdProducto { get; set; }

    [JsonPropertyName("idPedido")]
    public int IdPedido { get; set; }

    [JsonPropertyName("idVenta")]
    public int? IdVenta { get; set; }

    [JsonPropertyName("idUsuarioRegistra")]
    public int IdUsuarioRegistra { get; set; }

    [JsonPropertyName("estadoDevolucion")]
    public string EstadoDevolucion { get; set; } = "PENDIENTE";

    [JsonPropertyName("cantidad")]
    public int Cantidad { get; set; }

    [JsonPropertyName("motivo")]
    public string Motivo { get; set; } = string.Empty;

    [JsonPropertyName("observaciones")]
    public string Observaciones { get; set; } = string.Empty;

    [JsonPropertyName("fechaHora")]
    public DateTime? FechaHora { get; set; }

    [JsonPropertyName("activo")]
    public bool Activo { get; set; } = true;

    [JsonPropertyName("nombreTrabajador")]
    public string NombreTrabajador { get; set; } = string.Empty;

    [JsonPropertyName("detalles")]
    public List<DetalleDevolucionApi> Detalles { get; set; } = new();

    public Return ToReturn()
    {
        return new Return
        {
            Id = $"DEV-{IdDevolucion:D3}",
            IdPedido = IdPedido,
            IdVenta = IdVenta,
            Quantity = Cantidad,
            Reason = Motivo,
            Observations = Observaciones,
            RegisteredBy = NombreTrabajador,
            Date = FechaHora ?? DateTime.Now,
            Estado = EstadoDevolucion?.ToUpper() switch
            {
                "APROBADO" => "aprobado",
                "RECHAZADO" => "rechazado",
                _ => "pendiente"
            },
            Detalles = Detalles,
            Product = string.Join(", ", Detalles.Select(d => d.NombreProducto)),
            ProductCode = string.Join(", ", Detalles.Select(d => $"PROD-{d.IdProducto:D3}")),
            Amount = (decimal)Detalles.Sum(d =>
            d.Cantidad * d.PrecioUnitario)
        };
    }

    public static DevolucionApi FromReturn(Return r, int idProducto, int idUsuarioRegistra) =>
        new DevolucionApi
        {
            IdDevolucion = int.TryParse(r.Id?.Replace("DEV-", ""), out var devId) ? devId : 0,
            IdProducto = r.Detalles != null && r.Detalles.Any() ? r.Detalles.First().IdProducto : idProducto,
            IdPedido = r.IdPedido ?? 0,
            IdVenta = r.IdVenta,
            IdUsuarioRegistra = idUsuarioRegistra,
            Cantidad = r.Detalles != null && r.Detalles.Any() ? r.Detalles.Sum(d => d.Cantidad) : r.Quantity,
            Motivo = r.Reason ?? "",
            Observaciones = r.Observations ?? "",
            EstadoDevolucion = r.Estado?.ToUpper() ?? "PENDIENTE",
            Activo = true,
            FechaHora = new DateTime(DateTime.Now.Ticks / 10_000_000 * 10_000_000), // sin nanosegundos
            Detalles = r.Detalles?.Select(d => new DetalleDevolucionApi
            {
                IdProducto = d.IdProducto,
                NombreProducto = d.NombreProducto,
                PrecioUnitario = d.PrecioUnitario,
                Cantidad = d.Cantidad
            }).ToList() ?? new()
        };

    
}