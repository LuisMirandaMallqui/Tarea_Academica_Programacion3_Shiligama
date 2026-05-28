using ShiligamaWA.Models;

namespace ShiligamaWA.Services.Mock;

public class PedidoServiceMock : IPedidoService
{
    private readonly List<Pedido> _data = SeedData.Pedidos();
    private readonly object _lock = new();
    private int _nextId;
    private int _nextCodNum;

    public PedidoServiceMock()
    {
        _nextId = _data.Max(p => p.Id) + 1;
        _nextCodNum = _data.Max(p => int.TryParse(p.Codigo.Replace("PED-", ""), out var n) ? n : 0) + 1;
    }

    public IReadOnlyList<Pedido> Listar()
    {
        lock (_lock) return _data.OrderByDescending(p => p.Fecha).ToList();
    }

    public IReadOnlyList<Pedido> ListarPorCliente(int clienteId)
    {
        lock (_lock) return _data.Where(p => p.ClienteId == clienteId).OrderByDescending(p => p.Fecha).ToList();
    }

    public IReadOnlyList<Pedido> ListarPorEstado(EstadoPedido estado)
    {
        lock (_lock) return _data.Where(p => p.Estado == estado).OrderByDescending(p => p.Fecha).ToList();
    }

    public Pedido? Obtener(int id)
    {
        lock (_lock) return _data.FirstOrDefault(p => p.Id == id);
    }

    public Pedido Crear(Pedido p)
    {
        lock (_lock)
        {
            p.Id = _nextId++;
            p.Codigo = $"PED-{_nextCodNum++:D4}";
            p.Fecha = DateTime.Now;
            _data.Add(p);
            return p;
        }
    }

    public void CambiarEstado(int id, EstadoPedido nuevo)
    {
        lock (_lock)
        {
            var p = _data.FirstOrDefault(x => x.Id == id);
            if (p == null) return;
            p.Estado = nuevo;
            if (nuevo == EstadoPedido.Entregado) p.FechaEntrega = DateTime.Now;
        }
    }
}

public class VentaServiceMock : IVentaService
{
    private readonly List<Venta> _data = SeedData.Ventas();
    private readonly object _lock = new();
    private int _nextId;
    private int _nextCodNum;

    public VentaServiceMock()
    {
        _nextId = _data.Max(v => v.Id) + 1;
        _nextCodNum = _data.Max(v => int.TryParse(v.Codigo.Replace("VTA-", ""), out var n) ? n : 0) + 1;
    }

    public IReadOnlyList<Venta> Listar()
    {
        lock (_lock) return _data.OrderByDescending(v => v.Fecha).ToList();
    }

    public Venta? Obtener(int id)
    {
        lock (_lock) return _data.FirstOrDefault(v => v.Id == id);
    }

    public Venta RegistrarVenta(Venta v)
    {
        lock (_lock)
        {
            v.Id = _nextId++;
            if (string.IsNullOrEmpty(v.Codigo))
                v.Codigo = $"VTA-{_nextCodNum++:D4}";
            else
                _nextCodNum++;
            v.Fecha = DateTime.Now;
            v.Estado = EstadoVenta.Confirmada;
            _data.Add(v);
            return v;
        }
    }

    public string SiguienteCodigo() => $"VTA-{_nextCodNum:D4}";
}

public class DevolucionServiceMock : IDevolucionService
{
    private readonly List<Devolucion> _data = SeedData.Devoluciones();
    private readonly object _lock = new();
    private int _nextId;
    private int _nextCodNum;

    public DevolucionServiceMock()
    {
        _nextId = _data.Max(d => d.Id) + 1;
        _nextCodNum = _data.Max(d => int.TryParse(d.Codigo.Replace("DEV-", ""), out var n) ? n : 0) + 1;
    }

    public IReadOnlyList<Devolucion> Listar()
    {
        lock (_lock) return _data.OrderByDescending(d => d.FechaSolicitud).ToList();
    }

    public IReadOnlyList<Devolucion> ListarPorTrabajador(int trabajadorId)
    {
        lock (_lock) return _data
            .Where(d => d.TrabajadorAtendioId == trabajadorId)
            .OrderByDescending(d => d.FechaSolicitud).ToList();
    }

    public Devolucion? Obtener(int id)
    {
        lock (_lock) return _data.FirstOrDefault(d => d.Id == id);
    }

    public Devolucion Crear(Devolucion d)
    {
        lock (_lock)
        {
            d.Id = _nextId++;
            d.Codigo = $"DEV-{_nextCodNum++:D4}";
            d.FechaSolicitud = DateTime.Now;
            _data.Add(d);
            return d;
        }
    }

    public void CambiarEstado(int id, EstadoDevolucion nuevo, int? trabajadorId, string? trabajadorNombre)
    {
        lock (_lock)
        {
            var d = _data.FirstOrDefault(x => x.Id == id);
            if (d == null) return;
            d.Estado = nuevo;
            if (trabajadorId.HasValue)
            {
                d.TrabajadorAtendioId = trabajadorId;
                d.TrabajadorAtendioNombre = trabajadorNombre;
            }
            if (nuevo == EstadoDevolucion.Aprobada ||
                nuevo == EstadoDevolucion.Rechazada ||
                nuevo == EstadoDevolucion.Completada)
            {
                d.FechaResolucion = DateTime.Now;
            }
        }
    }
}
