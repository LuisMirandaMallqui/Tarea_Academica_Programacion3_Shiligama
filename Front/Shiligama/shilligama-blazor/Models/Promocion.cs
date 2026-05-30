using System;

namespace shilligama_blazor.Models;

public class Promocion
{
    public int     Id             { get; set; }
    public string  Nombre         { get; set; } = string.Empty;
    public string? Descripcion    { get; set; }
    public string  TipoDescuento  { get; set; } = "PORCENTAJE"; // PORCENTAJE | MONTO_FIJO
    public double  ValorDescuento { get; set; }
    public DateTime FechaInicio   { get; set; } = DateTime.Today;
    public DateTime FechaFin      { get; set; } = DateTime.Today.AddDays(7);
    public string? Condiciones    { get; set; }
    public bool    Activo         { get; set; } = true;

    // Computed: true si hoy está dentro del rango de fechas
    public bool EsVigente => Activo
        && DateTime.Today >= FechaInicio.Date
        && DateTime.Today <= FechaFin.Date;

    // Descripción legible del descuento
    public string DescuentoLabel => TipoDescuento == "PORCENTAJE"
        ? $"{ValorDescuento:0.##}% off"
        : $"S/. {ValorDescuento:0.00} off";
}
