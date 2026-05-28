namespace ShiligamaWA.Models;

public class Oferta
{
    public int Id { get; set; }
    public string Titulo { get; set; } = string.Empty;
    public string Descripcion { get; set; } = string.Empty;
    public string Imagen { get; set; } = string.Empty;
    public decimal? DescuentoPorcentaje { get; set; }
    public decimal? DescuentoMonto { get; set; }
    public DateTime FechaInicio { get; set; }
    public DateTime FechaFin { get; set; }
    public bool Activa { get; set; } = true;
    public List<int> ProductosIds { get; set; } = new();
    public string EtiquetaPromo { get; set; } = "OFERTA";
    public bool EstaVigente => Activa && DateTime.Now >= FechaInicio && DateTime.Now <= FechaFin;
}
