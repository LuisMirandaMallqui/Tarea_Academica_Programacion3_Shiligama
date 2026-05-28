namespace ShiligamaWA.Models;

public class Devolucion
{
    public int Id { get; set; }
    public string Codigo { get; set; } = string.Empty;      // DEV-0042
    public string PedidoCodigo { get; set; } = string.Empty; // PED-1024
    public int? PedidoId { get; set; }
    public int ClienteId { get; set; }
    public string ClienteNombre { get; set; } = string.Empty;
    public int? TrabajadorAtendioId { get; set; }
    public string? TrabajadorAtendioNombre { get; set; }
    public DateTime FechaSolicitud { get; set; } = DateTime.Now;
    public DateTime? FechaResolucion { get; set; }
    public EstadoDevolucion Estado { get; set; } = EstadoDevolucion.Solicitada;
    public string Motivo { get; set; } = string.Empty;
    public decimal MontoDevuelto { get; set; }
    public List<DetalleDevolucion> Detalles { get; set; } = new();
    public string? Observaciones { get; set; }
}

public class DetalleDevolucion
{
    public int Id { get; set; }
    public int DevolucionId { get; set; }
    public int ProductoId { get; set; }
    public string ProductoNombre { get; set; } = string.Empty;
    public int Cantidad { get; set; }
    public decimal PrecioUnitario { get; set; }
    public decimal Subtotal => Cantidad * PrecioUnitario;
}
