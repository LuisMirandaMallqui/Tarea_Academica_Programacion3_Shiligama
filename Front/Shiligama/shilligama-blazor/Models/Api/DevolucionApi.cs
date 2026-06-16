using System;
using System.Collections.Generic;
using System.Linq;

namespace shilligama_blazor.Models;

// DTO que mapea Devolucion del backend Java
internal class DevolucionApi
{
    public int IdDevolucion { get; set; }
    public int IdProducto { get; set; }
    public int IdPedido { get; set; }
    public int IdTrabajador { get; set; }
    public string EstadoDevolucion { get; set; } = "PENDIENTE";
    public int Cantidad { get; set; }
    public string Motivo { get; set; } = string.Empty;
    public DateTime? FechaHora { get; set; }
    public bool Activo { get; set; } = true;
    public string NombreTrabajador { get; set; } = string.Empty;
    public List<DetalleDevolucionApi> Detalles { get; set; } = new();

    public Return ToReturn()
    {
        decimal computedAmount = 0;
        string prodNames = "";
        string prodCodes = "";

        if (Detalles != null && Detalles.Any())
        {
            computedAmount = (decimal)Detalles.Sum(d => d.Cantidad * d.PrecioUnitario);
            prodNames = string.Join(", ", Detalles.Select(d => d.NombreProducto));
            prodCodes = string.Join(", ", Detalles.Select(d => $"PROD-{d.IdProducto:D3}"));
        }
        else
        {
            prodNames = $"Producto #{IdProducto}";
            prodCodes = $"PROD-{IdProducto:D3}";
        }

        return new Return
        {
            Id           = $"DEV-{IdDevolucion:D3}",
            Date         = FechaHora ?? DateTime.Now,
            Product      = prodNames,
            ProductCode  = prodCodes,
            Quantity     = Detalles != null && Detalles.Any() ? Detalles.Sum(d => d.Cantidad) : Cantidad,
            Reason       = Motivo,
            RegisteredBy = !string.IsNullOrEmpty(NombreTrabajador) ? NombreTrabajador : $"Trabajador #{IdTrabajador}",
            Amount       = computedAmount,
            Observations = EstadoDevolucion,
            IdPedido     = IdPedido,
            Detalles     = Detalles != null ? Detalles.Select(d => new DetalleDevolucionApi
            {
                IdProducto = d.IdProducto,
                NombreProducto = d.NombreProducto,
                PrecioUnitario = d.PrecioUnitario,
                Cantidad = d.Cantidad
            }).ToList() : new List<DetalleDevolucionApi>()
        };
    }

    public static DevolucionApi FromReturn(Return r, int idProducto, int idTrabajador) =>
        new DevolucionApi
        {
            IdDevolucion     = int.TryParse(r.Id.Replace("DEV-", ""), out var numId) ? numId : 0,
            IdProducto       = idProducto > 0 ? idProducto : (r.Detalles != null && r.Detalles.Any() ? r.Detalles.First().IdProducto : 0),
            IdPedido         = r.IdPedido ?? 0,
            IdTrabajador     = idTrabajador,
            Cantidad         = r.Detalles != null && r.Detalles.Any() ? r.Detalles.Sum(d => d.Cantidad) : r.Quantity,
            Motivo           = r.Reason,
            EstadoDevolucion = "PENDIENTE",
            Activo           = true,
            Detalles         = r.Detalles != null ? r.Detalles.Select(d => new DetalleDevolucionApi
            {
                IdProducto = d.IdProducto,
                NombreProducto = d.NombreProducto,
                PrecioUnitario = d.PrecioUnitario,
                Cantidad = d.Cantidad
            }).ToList() : new List<DetalleDevolucionApi>()
        };
}
