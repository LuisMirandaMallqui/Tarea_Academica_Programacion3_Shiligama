namespace ShiligamaWA.Models;

public class Producto
{
    public int Id { get; set; }
    public string Codigo { get; set; } = string.Empty;
    public string Nombre { get; set; } = string.Empty;
    public string Descripcion { get; set; } = string.Empty;
    public decimal Precio { get; set; }
    public decimal? PrecioOriginal { get; set; }   // Si esta en oferta
    public string Imagen { get; set; } = string.Empty;
    public string CategoriaCodigo { get; set; } = string.Empty;
    public string CategoriaNombre { get; set; } = string.Empty;
    public int Stock { get; set; }
    public int StockMinimo { get; set; } = 10;
    public bool EsPromocion { get; set; }
    public bool Activo { get; set; } = true;
    public DateTime FechaCreacion { get; set; } = DateTime.Now;
    public string? Proveedor { get; set; }
    public string UnidadMedida { get; set; } = "unidad";

    // Helpers
    public bool TieneStockBajo => Stock <= StockMinimo && Stock > 0;
    public bool SinStock => Stock <= 0;
    public decimal Descuento => PrecioOriginal.HasValue && PrecioOriginal.Value > Precio
        ? PrecioOriginal.Value - Precio
        : 0;
    public int DescuentoPorcentaje => PrecioOriginal.HasValue && PrecioOriginal.Value > 0
        ? (int)Math.Round((1 - (Precio / PrecioOriginal.Value)) * 100)
        : 0;
}
