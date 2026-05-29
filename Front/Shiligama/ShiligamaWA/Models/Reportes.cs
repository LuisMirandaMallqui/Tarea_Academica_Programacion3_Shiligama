namespace ShiligamaWA.Models;

public class KpiDashboard
{
    public string Titulo { get; set; } = string.Empty;
    public string Valor { get; set; } = string.Empty;
    public string Cambio { get; set; } = string.Empty;
    public bool TendenciaPositiva { get; set; } = true;
    public string Icono { get; set; } = "fa-solid fa-chart-line";
    public string Descripcion { get; set; } = string.Empty;
}

public class PuntoSerie
{
    public string Etiqueta { get; set; } = string.Empty;
    public decimal Valor { get; set; }
}

public class ResumenVentas
{
    public decimal VentasHoy { get; set; }
    public decimal VentasMes { get; set; }
    public int PedidosPendientes { get; set; }
    public int ProductosStockBajo { get; set; }
    public List<PuntoSerie> SerieDiaria { get; set; } = new();
    public List<PuntoSerie> SerieMensual { get; set; } = new();
}

public class ProductoTopVendido
{
    public int ProductoId { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public string Categoria { get; set; } = string.Empty;
    public int UnidadesVendidas { get; set; }
    public decimal IngresoGenerado { get; set; }
}

public class ClienteReporte
{
    public int ClienteId { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public int Pedidos { get; set; }
    public decimal MontoTotal { get; set; }
    public DateTime UltimaCompra { get; set; }
}
