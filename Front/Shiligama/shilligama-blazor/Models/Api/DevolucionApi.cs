using System;

namespace shilligama_blazor.Models;

// DTO que mapea Devolucion del backend Java
internal class DevolucionApi
{
    public int IdDevolucion { get; set; }
    public int IdProducto { get; set; }
    public int IdTrabajador { get; set; }
    public string EstadoDevolucion { get; set; } = "PENDIENTE";
    public int Cantidad { get; set; }
    public string Motivo { get; set; } = string.Empty;
    public DateTime? FechaHora { get; set; }
    public bool Activo { get; set; } = true;

    public Return ToReturn() => new Return
    {
        Id           = $"DEV-{IdDevolucion:D3}",
        Date         = FechaHora ?? DateTime.Now,
        Product      = $"Producto #{IdProducto}",
        ProductCode  = $"PROD-{IdProducto:D3}",
        Quantity     = Cantidad,
        Reason       = Motivo,
        RegisteredBy = $"Trabajador #{IdTrabajador}",
        Amount       = 0,
        Observations = EstadoDevolucion
    };

    public static DevolucionApi FromReturn(Return r, int idProducto, int idTrabajador) =>
        new DevolucionApi
        {
            IdProducto       = idProducto,
            IdTrabajador     = idTrabajador,
            Cantidad         = r.Quantity,
            Motivo           = r.Reason,
            EstadoDevolucion = "PENDIENTE",
            Activo           = true
        };
}
