using ShiligamaWA.Models;

namespace ShiligamaWA.Services.Mock;

public class CategoriaServiceMock : ICategoriaService
{
    private readonly List<Categoria> _data = SeedData.Categorias();

    public IReadOnlyList<Categoria> Listar() => _data;
    public Categoria? Obtener(string codigo) =>
        _data.FirstOrDefault(c => c.Codigo.Equals(codigo, StringComparison.OrdinalIgnoreCase));
}

public class ProductoServiceMock : IProductoService
{
    private readonly List<Producto> _data = SeedData.Productos();
    private readonly object _lock = new();
    private int _nextId;

    public ProductoServiceMock() => _nextId = _data.Max(p => p.Id) + 1;

    public IReadOnlyList<Producto> Listar()
    {
        lock (_lock) return _data.Where(p => p.Activo).OrderBy(p => p.Nombre).ToList();
    }

    public IReadOnlyList<Producto> ListarPorCategoria(string categoriaCodigo)
    {
        lock (_lock) return _data
            .Where(p => p.Activo && p.CategoriaCodigo.Equals(categoriaCodigo, StringComparison.OrdinalIgnoreCase))
            .ToList();
    }

    public IReadOnlyList<Producto> Buscar(string termino)
    {
        if (string.IsNullOrWhiteSpace(termino)) return Listar();
        var t = termino.Trim().ToLowerInvariant();
        lock (_lock) return _data
            .Where(p => p.Activo && (
                p.Nombre.ToLowerInvariant().Contains(t) ||
                p.Codigo.ToLowerInvariant().Contains(t) ||
                p.CategoriaNombre.ToLowerInvariant().Contains(t)))
            .ToList();
    }

    public IReadOnlyList<Producto> ListarOfertas()
    {
        lock (_lock) return _data.Where(p => p.Activo && p.EsPromocion).ToList();
    }

    public IReadOnlyList<Producto> ListarStockBajo()
    {
        lock (_lock) return _data.Where(p => p.Activo && p.Stock <= p.StockMinimo).ToList();
    }

    public Producto? Obtener(int id)
    {
        lock (_lock) return _data.FirstOrDefault(p => p.Id == id);
    }

    public Producto Crear(Producto p)
    {
        lock (_lock)
        {
            p.Id = _nextId++;
            p.FechaCreacion = DateTime.Now;
            _data.Add(p);
            return p;
        }
    }

    public Producto Actualizar(Producto p)
    {
        lock (_lock)
        {
            var ix = _data.FindIndex(x => x.Id == p.Id);
            if (ix < 0) throw new InvalidOperationException("Producto no existe");
            _data[ix] = p;
            return p;
        }
    }

    public void Eliminar(int id)
    {
        lock (_lock)
        {
            var p = _data.FirstOrDefault(x => x.Id == id);
            if (p != null) p.Activo = false;
        }
    }

    public void AjustarStock(int productoId, int delta, string motivo, string? referencia = null)
    {
        lock (_lock)
        {
            var p = _data.FirstOrDefault(x => x.Id == productoId);
            if (p == null) return;
            p.Stock = Math.Max(0, p.Stock + delta);
        }
    }
}
