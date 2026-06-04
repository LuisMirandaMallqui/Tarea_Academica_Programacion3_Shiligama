using System;

namespace shilligama_blazor.Models;

// DTO que mapea Promocion del backend Java
internal class PromocionApi
{
    public int     IdPromocion    { get; set; }
    public string  Nombre         { get; set; } = string.Empty;
    public string? Descripcion    { get; set; }
    public string  TipoDescuento  { get; set; } = "PORCENTAJE"; // PORCENTAJE | MONTO_FIJO | DOS_X_UNO
    public double  ValorDescuento { get; set; }
    public string  FechaInicio    { get; set; } = string.Empty; // yyyy-MM-dd
    public string  FechaFin       { get; set; } = string.Empty;
    public string? Condiciones    { get; set; }
    public bool    Activo         { get; set; } = true;

    public Promocion ToPromocion() => new Promocion
    {
        Id             = IdPromocion,
        Nombre         = Nombre,
        Descripcion    = Descripcion,
        TipoDescuento  = TipoDescuento,
        ValorDescuento = ValorDescuento,
        FechaInicio    = DateTime.TryParse(FechaInicio, out var fi) ? fi : DateTime.Today,
        FechaFin       = DateTime.TryParse(FechaFin,    out var ff) ? ff : DateTime.Today,
        Condiciones    = Condiciones,
        Activo         = Activo
    };

    public static PromocionApi FromPromocion(Promocion p) => new PromocionApi
    {
        IdPromocion    = p.Id,
        Nombre         = p.Nombre,
        Descripcion    = p.Descripcion,
        TipoDescuento  = p.TipoDescuento,
        ValorDescuento = p.ValorDescuento,
        FechaInicio    = p.FechaInicio.ToString("yyyy-MM-dd"),
        FechaFin       = p.FechaFin.ToString("yyyy-MM-dd"),
        Condiciones    = p.Condiciones,
        Activo         = p.Activo
    };
}
