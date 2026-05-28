namespace ShiligamaWA.Models;

public class ItemCarrito
{
    public int ProductoId { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Imagen { get; set; } = string.Empty;
    public decimal Precio { get; set; }
    public int Cantidad { get; set; } = 1;
    public int StockDisponible { get; set; }
    public decimal Subtotal => Precio * Cantidad;
}
