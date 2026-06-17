using System;

namespace shilligama_blazor.Models;

// DTO que mapea Promocion del backend Java
// IMPORTANTE: Java espera LocalDateTime → formato "yyyy-MM-dd'T'HH:mm:ss"
// Si se envía solo "yyyy-MM-dd" (sin hora), Yasson falla silenciosamente
// y el campo queda null, rompiendo el SP INSERTAR_PROMOCION / MODIFICAR_PROMOCION.
internal class PromocionApi
{
    public int IdPromocion { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string? Descripcion { get; set; }
    public string TipoDescuento { get; set; } = "PORCENTAJE"; // PORCENTAJE | MONTO_FIJO
    public double ValorDescuento { get; set; }
    public string FechaInicio { get; set; } = string.Empty; // yyyy-MM-ddTHH:mm:ss
    public string FechaFin { get; set; } = string.Empty; // yyyy-MM-ddTHH:mm:ss
    public string? Condiciones { get; set; }
    public bool Activo { get; set; } = true;

    public Promocion ToPromocion() => new Promocion
    {
        Id = IdPromocion,
        Nombre = Nombre,
        Descripcion = Descripcion,
        TipoDescuento = TipoDescuento,
        ValorDescuento = ValorDescuento,
        FechaInicio = DateTime.TryParse(FechaInicio, out var fi) ? fi : DateTime.Today,
        FechaFin = DateTime.TryParse(FechaFin, out var ff) ? ff : DateTime.Today,
        Condiciones = Condiciones,
        Activo = Activo
    };

    public static PromocionApi FromPromocion(Promocion p) => new PromocionApi
    {
        IdPromocion = p.Id,
        Nombre = p.Nombre,
        Descripcion = p.Descripcion,
        TipoDescuento = p.TipoDescuento,
        ValorDescuento = p.ValorDescuento,
        // FIX: formato completo con hora para que Yasson lo deserialice en LocalDateTime
        FechaInicio = p.FechaInicio.ToString("yyyy-MM-ddTHH:mm:ss"),
        FechaFin = p.FechaFin.ToString("yyyy-MM-ddTHH:mm:ss"),
        Condiciones = p.Condiciones,
        Activo = p.Activo
    };
}
