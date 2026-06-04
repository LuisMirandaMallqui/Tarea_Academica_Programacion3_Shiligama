namespace shilligama_blazor.Models;

// DTO que mapea TopProductoDto del backend Java.
// Usado por ReporteService y Admin/Reportes.razor.
public class TopProductoItem
{
    public int    IdProducto    { get; set; }
    public string Nombre        { get; set; } = string.Empty;
    public string? ImagenUrl    { get; set; }
    public int    TotalUnidades { get; set; }
    public double TotalIngresos { get; set; }
}
