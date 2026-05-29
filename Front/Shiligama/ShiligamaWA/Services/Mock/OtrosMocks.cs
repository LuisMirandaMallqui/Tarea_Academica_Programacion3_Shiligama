using ShiligamaWA.Models;

namespace ShiligamaWA.Services.Mock;

public class NotificacionServiceMock : INotificacionService
{
    private readonly List<Notificacion> _data = SeedData.Notificaciones();
    private readonly object _lock = new();

    public IReadOnlyList<Notificacion> Listar()
    {
        lock (_lock) return _data.OrderByDescending(n => n.Fecha).ToList();
    }

    public int CantidadNoLeidas()
    {
        lock (_lock) return _data.Count(n => !n.Leida);
    }

    public void MarcarLeida(int id)
    {
        lock (_lock)
        {
            var n = _data.FirstOrDefault(x => x.Id == id);
            if (n != null) n.Leida = true;
        }
    }

    public void MarcarTodasLeidas()
    {
        lock (_lock)
        {
            foreach (var n in _data) n.Leida = true;
        }
    }
}

public class ReporteServiceMock : IReporteService
{
    public ResumenVentas ObtenerResumen()
    {
        return new ResumenVentas
        {
            VentasHoy = 3459.00m,
            VentasMes = 87230.00m,
            PedidosPendientes = 18,
            ProductosStockBajo = 24,
            SerieDiaria = new()
            {
                new() { Etiqueta = "Lun", Valor = 1850 },
                new() { Etiqueta = "Mar", Valor = 2240 },
                new() { Etiqueta = "Mié", Valor = 1980 },
                new() { Etiqueta = "Jue", Valor = 2890 },
                new() { Etiqueta = "Vie", Valor = 3420 },
                new() { Etiqueta = "Sáb", Valor = 4150 },
                new() { Etiqueta = "Dom", Valor = 3459 }
            },
            SerieMensual = new()
            {
                new() { Etiqueta = "Ene", Valor = 72500 },
                new() { Etiqueta = "Feb", Valor = 68900 },
                new() { Etiqueta = "Mar", Valor = 75400 },
                new() { Etiqueta = "Abr", Valor = 81200 },
                new() { Etiqueta = "May", Valor = 87230 }
            }
        };
    }

    public List<KpiDashboard> ObtenerKpisDashboard() => new()
    {
        new() { Titulo = "Ventas hoy", Valor = "S/ 3,459.00", Cambio = "+12.5%",
                TendenciaPositiva = true, Icono = "fa-solid fa-bag-shopping",
                Descripcion = "vs. ayer" },
        new() { Titulo = "Pedidos pendientes", Valor = "18", Cambio = "+3",
                TendenciaPositiva = true, Icono = "fa-regular fa-clock",
                Descripcion = "nuevos hoy" },
        new() { Titulo = "Productos con bajo stock", Valor = "24", Cambio = "+5",
                TendenciaPositiva = false, Icono = "fa-solid fa-triangle-exclamation",
                Descripcion = "requieren atención" },
        new() { Titulo = "Ingresos del mes", Valor = "S/ 87,230.00", Cambio = "+8.2%",
                TendenciaPositiva = true, Icono = "fa-solid fa-sack-dollar",
                Descripcion = "vs. mes anterior" }
    };

    public List<ProductoTopVendido> TopProductos(int n = 5) => new()
    {
        new() { ProductoId = 4,  Nombre = "Leche Gloria Entera 1L", Categoria = "Lácteos", UnidadesVendidas = 320, IngresoGenerado = 1664.00m },
        new() { ProductoId = 3,  Nombre = "Inca Kola 1.5L", Categoria = "Bebidas", UnidadesVendidas = 215, IngresoGenerado = 1397.50m },
        new() { ProductoId = 1,  Nombre = "Arroz Extra Costeño 5kg", Categoria = "Abarrotes", UnidadesVendidas = 142, IngresoGenerado = 3535.80m },
        new() { ProductoId = 6,  Nombre = "Galletas Oreo x6", Categoria = "Snacks", UnidadesVendidas = 198, IngresoGenerado = 891.00m },
        new() { ProductoId = 11, Nombre = "Azúcar Rubia 1kg", Categoria = "Abarrotes", UnidadesVendidas = 175, IngresoGenerado = 840.00m }
    }.Take(n).ToList();

    public List<ClienteReporte> TopClientes(int n = 5) => new()
    {
        new() { ClienteId = 3, Nombre = "Juan Pérez", Pedidos = 24, MontoTotal = 1842.50m, UltimaCompra = DateTime.Now.AddDays(-1) },
        new() { ClienteId = 4, Nombre = "María García", Pedidos = 18, MontoTotal = 1320.80m, UltimaCompra = DateTime.Now.AddHours(-3) },
        new() { ClienteId = 0, Nombre = "Pedro Sánchez", Pedidos = 12, MontoTotal = 950.00m, UltimaCompra = DateTime.Now.AddDays(-5) },
        new() { ClienteId = 0, Nombre = "Lucía Torres", Pedidos = 9, MontoTotal = 720.40m, UltimaCompra = DateTime.Now.AddDays(-2) },
        new() { ClienteId = 0, Nombre = "Diego Ramírez", Pedidos = 7, MontoTotal = 645.30m, UltimaCompra = DateTime.Now.AddDays(-8) }
    }.Take(n).ToList();
}

public class PersonalServiceMock : IPersonalService
{
    private readonly List<Trabajador> _data = SeedData.Trabajadores();
    private readonly object _lock = new();
    private int _nextId;

    public PersonalServiceMock() => _nextId = _data.Max(t => t.Id) + 1;

    public IReadOnlyList<Trabajador> Listar()
    {
        lock (_lock) return _data.Where(t => t.Activo).OrderBy(t => t.NombreCompleto).ToList();
    }

    public Trabajador? Obtener(int id)
    {
        lock (_lock) return _data.FirstOrDefault(t => t.Id == id);
    }

    public Trabajador Crear(Trabajador t)
    {
        lock (_lock)
        {
            t.Id = _nextId++;
            t.FechaIngreso = DateTime.Now;
            _data.Add(t);
            return t;
        }
    }

    public Trabajador Actualizar(Trabajador t)
    {
        lock (_lock)
        {
            var ix = _data.FindIndex(x => x.Id == t.Id);
            if (ix < 0) throw new InvalidOperationException("Trabajador no existe");
            _data[ix] = t;
            return t;
        }
    }

    public void Eliminar(int id)
    {
        lock (_lock)
        {
            var t = _data.FirstOrDefault(x => x.Id == id);
            if (t != null) t.Activo = false;
        }
    }
}

public class InventarioServiceMock : IInventarioService
{
    private readonly List<MovimientoInventario> _movs = new();
    private readonly object _lock = new();
    private readonly IProductoService _productos;

    public InventarioServiceMock(IProductoService productos)
    {
        _productos = productos;
        // Movimientos seed (historial)
        var hoy = DateTime.Now;
        _movs.Add(new() { Id = 1, ProductoId = 4, ProductoNombre = "Leche Gloria Entera 1L",
            Tipo = TipoMovimientoInventario.Salida, Cantidad = 22, StockAnterior = 30, StockNuevo = 8,
            Fecha = hoy.AddHours(-4), Motivo = "Ventas del día", UsuarioNombre = "Sistema" });
        _movs.Add(new() { Id = 2, ProductoId = 14, ProductoNombre = "Chocolate Sublime 28g x10",
            Tipo = TipoMovimientoInventario.Salida, Cantidad = 8, StockAnterior = 12, StockNuevo = 4,
            Fecha = hoy.AddHours(-6), Motivo = "Venta mayorista", UsuarioNombre = "Carlos Mendoza" });
        _movs.Add(new() { Id = 3, ProductoId = 1, ProductoNombre = "Arroz Extra Costeño 5kg",
            Tipo = TipoMovimientoInventario.Ingreso, Cantidad = 30, StockAnterior = 15, StockNuevo = 45,
            Fecha = hoy.AddDays(-1), Motivo = "Reabastecimiento", Referencia = "OC-0085", UsuarioNombre = "Ana Rodríguez" });
        _movs.Add(new() { Id = 4, ProductoId = 11, ProductoNombre = "Azúcar Rubia 1kg",
            Tipo = TipoMovimientoInventario.Ingreso, Cantidad = 50, StockAnterior = 45, StockNuevo = 95,
            Fecha = hoy.AddDays(-2), Motivo = "Reabastecimiento", Referencia = "OC-0084", UsuarioNombre = "Ana Rodríguez" });
    }

    public IReadOnlyList<StockSnapshot> ObtenerSnapshot()
    {
        return _productos.Listar().Select(p => new StockSnapshot
        {
            ProductoId = p.Id,
            Codigo = p.Codigo,
            Nombre = p.Nombre,
            CategoriaNombre = p.CategoriaNombre,
            Stock = p.Stock,
            StockMinimo = p.StockMinimo,
            Precio = p.Precio
        }).ToList();
    }

    public IReadOnlyList<MovimientoInventario> ListarMovimientos(int? productoId = null)
    {
        lock (_lock)
        {
            var q = _movs.OrderByDescending(m => m.Fecha).AsEnumerable();
            if (productoId.HasValue) q = q.Where(m => m.ProductoId == productoId.Value);
            return q.ToList();
        }
    }

    public void RegistrarMovimiento(MovimientoInventario m)
    {
        lock (_lock)
        {
            m.Id = (_movs.Count == 0 ? 1 : _movs.Max(x => x.Id) + 1);
            m.Fecha = DateTime.Now;
            _movs.Add(m);
        }
    }
}

public class OfertaServiceMock : IOfertaService
{
    private readonly List<Oferta> _data = SeedData.Ofertas();

    public IReadOnlyList<Oferta> Listar() => _data;
    public IReadOnlyList<Oferta> ListarVigentes() => _data.Where(o => o.EstaVigente).ToList();
    public Oferta? Obtener(int id) => _data.FirstOrDefault(o => o.Id == id);
}
