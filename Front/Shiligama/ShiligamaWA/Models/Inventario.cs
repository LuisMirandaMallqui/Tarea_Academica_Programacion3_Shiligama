namespace ShiligamaWA.Models;

public class MovimientoInventario
{
    public int Id { get; set; }
    public int ProductoId { get; set; }
    public string ProductoNombre { get; set; } = string.Empty;
    public TipoMovimientoInventario Tipo { get; set; }
    public int Cantidad { get; set; }    // positiva siempre; el signo lo da el Tipo
    public int StockAnterior { get; set; }
    public int StockNuevo { get; set; }
    public DateTime Fecha { get; set; } = DateTime.Now;
    public string Motivo { get; set; } = string.Empty;
    public string? Referencia { get; set; }   // PED-1024, VTA-0014, etc.
    public int? UsuarioId { get; set; }
    public string? UsuarioNombre { get; set; }
}

public class StockSnapshot
{
    public int ProductoId { get; set; }
    public string Codigo { get; set; } = string.Empty;
    public string Nombre { get; set; } = string.Empty;
    public string CategoriaNombre { get; set; } = string.Empty;
    public int Stock { get; set; }
    public int StockMinimo { get; set; }
    public decimal Precio { get; set; }
    public decimal ValorInventario => Stock * Precio;
    public string EstadoStock => Stock <= 0
        ? "agotado"
        : Stock <= StockMinimo ? "bajo" : "ok";
}
