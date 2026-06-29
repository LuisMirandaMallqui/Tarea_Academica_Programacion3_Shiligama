using System;

namespace shilligama_blazor.Models;

public class Promocion
{
    public int     Id             { get; set; }
    public string  Nombre         { get; set; } = string.Empty;
    public string? Descripcion    { get; set; }
    public string  TipoDescuento  { get; set; } = "PORCENTAJE"; // PORCENTAJE | MONTO_FIJO | DOS_X_UNO
    public double  ValorDescuento { get; set; }
    public DateTime FechaInicio   { get; set; } = DateTime.Today;
    public DateTime FechaFin      { get; set; } = DateTime.Today.AddDays(7);
    public string? Condiciones    { get; set; }
    public bool    Activo              { get; set; } = true;
    public bool    MostrarEnCarrusel  { get; set; } = false;
    public string  ColorCarrusel      { get; set; } = "#1A6B3C";

    // IDs de productos vinculados (tabla promocion_producto). Se gestiona en el
    // cliente; la persistencia se hace vía PromocionService.Asociar/Desasociar.
    public System.Collections.Generic.List<int> ProductoIds { get; set; } = new();

    // Computed: true si hoy está dentro del rango de fechas
    public bool EsVigente => Activo
        && DateTime.Today >= FechaInicio.Date
        && DateTime.Today <= FechaFin.Date;

    // Descripción legible del descuento
    public string DescuentoLabel => TipoDescuento switch
    {
        "PORCENTAJE" => $"{ValorDescuento:0.##}% off",
        "DOS_X_UNO"  => "2×1",
        _            => $"S/. {ValorDescuento:0.00} off"
    };

    // True si es una promoción 2×1
    public bool Es2x1 => TipoDescuento == "DOS_X_UNO";
}
