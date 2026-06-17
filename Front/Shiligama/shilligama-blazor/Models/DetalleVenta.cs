public class DetalleVenta
{
    public int idDetalleVenta { get; set; }
    public int idPadreVenta { get; set; }
    public Producto producto { get; set; }
    public string descripcion { get; set; }
    public int cantidad { get; set; }
    public double precioUnitario { get; set; }
    public double subtotal { get; set; }
}