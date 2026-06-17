using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json.Serialization;

namespace shilligama_blazor.Models;

internal class DevolucionApi
{
    [JsonPropertyName("idDevolucion")]
    public int IdDevolucion { get; set; }

    [JsonPropertyName("idProducto")]
    public int IdProducto { get; set; }

    [JsonPropertyName("idPedido")]
    public int IdPedido { get; set; }

    [JsonPropertyName("idTrabajador")]
    public int IdTrabajador { get; set; }

    [JsonPropertyName("estadoDevolucion")]
    public string EstadoDevolucion { get; set; } = "PENDIENTE";

    [JsonPropertyName("cantidad")]
    public int Cantidad { get; set; }

    [JsonPropertyName("motivo")]
    public string Motivo { get; set; } = string.Empty;

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
            Quantity = Cantidad,
            Reason = Motivo,
            RegisteredBy = NombreTrabajador,
            Date = FechaHora ?? DateTime.Now,
            Detalles = Detalles,

            Amount = (decimal)Detalles.Sum(d =>
            d.Cantidad * d.PrecioUnitario)
        };
    }

    public static DevolucionApi FromReturn(Return r, int idProducto, int idTrabajador) =>
        new DevolucionApi
        {
            IdDevolucion = 0,
            IdProducto = r.Detalles != null && r.Detalles.Any() ? r.Detalles.First().IdProducto : idProducto,
            IdPedido = r.IdPedido ?? 0,
            IdTrabajador = idTrabajador,
            Cantidad = r.Detalles != null && r.Detalles.Any() ? r.Detalles.Sum(d => d.Cantidad) : r.Quantity,
            Motivo = r.Reason ?? "",
            EstadoDevolucion = "PENDIENTE",
            Activo = true,
            FechaHora = DateTime.Now,
            Detalles = r.Detalles?.Select(d => new DetalleDevolucionApi
            {
                IdProducto = d.IdProducto,
                NombreProducto = d.NombreProducto,
                PrecioUnitario = d.PrecioUnitario,
                Cantidad = d.Cantidad
            }).ToList() ?? new()
        };
}