namespace ShiligamaWA.Models;

public class Venta
{
    public int Id { get; set; }
    public string Codigo { get; set; } = string.Empty;   // VTA-0014
    public int? TrabajadorId { get; set; }
    public string TrabajadorNombre { get; set; } = string.Empty;
    public int? ClienteId { get; set; }
    public string? ClienteNombre { get; set; }
    public DateTime Fecha { get; set; } = DateTime.Now;
    public CanalVenta Canal { get; set; } = CanalVenta.Tienda;
    public EstadoVenta Estado { get; set; } = EstadoVenta.Confirmada;
    public MetodoPago MetodoPago { get; set; } = MetodoPago.Efectivo;
    public decimal Subtotal { get; set; }
    public decimal Igv => Math.Round(Subtotal * 0.18m, 2);
    public decimal Total => Subtotal + Igv;
    public decimal? MontoRecibido { get; set; }
    public decimal Vuelto => MontoRecibido.HasValue && MontoRecibido.Value > Total
        ? MontoRecibido.Value - Total
        : 0;
    public List<LineaVenta> Lineas { get; set; } = new();
    public bool BoletaGenerada { get; set; }
}

public class LineaVenta
{
    public int Id { get; set; }
    public int VentaId { get; set; }
    public int ProductoId { get; set; }
    public string ProductoCodigo { get; set; } = string.Empty;
    public string ProductoNombre { get; set; } = string.Empty;
    public int Cantidad { get; set; }
    public decimal PrecioUnitario { get; set; }
    public decimal Subtotal => Cantidad * PrecioUnitario;
}
