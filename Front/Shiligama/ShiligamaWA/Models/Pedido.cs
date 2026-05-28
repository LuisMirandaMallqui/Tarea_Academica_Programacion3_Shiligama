namespace ShiligamaWA.Models;

public class Pedido
{
    public int Id { get; set; }
    public string Codigo { get; set; } = string.Empty;   // PED-1024
    public int ClienteId { get; set; }
    public string ClienteNombre { get; set; } = string.Empty;
    public string ClienteTelefono { get; set; } = string.Empty;
    public string DireccionEntrega { get; set; } = string.Empty;
    public DateTime Fecha { get; set; } = DateTime.Now;
    public DateTime? FechaEntrega { get; set; }
    public EstadoPedido Estado { get; set; } = EstadoPedido.Pendiente;
    public MetodoPago MetodoPago { get; set; } = MetodoPago.Efectivo;
    public decimal Subtotal { get; set; }
    public decimal CostoEnvio { get; set; } = 5.00m;   // Tarifa unica (feedback JP)
    public decimal Total => Subtotal + CostoEnvio;
    public List<DetallePedido> Detalles { get; set; } = new();
    public string? Observaciones { get; set; }
    public int TotalProductos => Detalles.Sum(d => d.Cantidad);
}

public class DetallePedido
{
    public int Id { get; set; }
    public int PedidoId { get; set; }
    public int ProductoId { get; set; }
    public string ProductoNombre { get; set; } = string.Empty;
    public string ProductoImagen { get; set; } = string.Empty;
    public int Cantidad { get; set; }
    public decimal PrecioUnitario { get; set; }
    public decimal Subtotal => Cantidad * PrecioUnitario;
}
