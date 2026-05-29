using ShiligamaWA.Models;

namespace ShiligamaWA.Services.Mock;

public class CarritoServiceMock : ICarritoService
{
    private readonly List<ItemCarrito> _items = new();

    public event Action? OnChange;

    public IReadOnlyList<ItemCarrito> Items => _items;
    public int Cantidad => _items.Sum(i => i.Cantidad);
    public decimal Subtotal => _items.Sum(i => i.Subtotal);

    // Feedback JP: costo de envio unico (tarifa global, no por distancia)
    // Envio gratis a partir de S/ 100 como gancho comercial
    public decimal CostoEnvio => Subtotal == 0
        ? 0m
        : (Subtotal >= 100m ? 0m : 5.00m);

    public decimal Total => Subtotal + CostoEnvio;

    public void Agregar(Producto p, int cantidad = 1)
    {
        if (cantidad <= 0) return;
        var existente = _items.FirstOrDefault(i => i.ProductoId == p.Id);
        if (existente != null)
        {
            existente.Cantidad = Math.Min(p.Stock, existente.Cantidad + cantidad);
        }
        else
        {
            _items.Add(new ItemCarrito
            {
                ProductoId = p.Id,
                Nombre = p.Nombre,
                Imagen = p.Imagen,
                Precio = p.Precio,
                Cantidad = Math.Min(p.Stock, cantidad),
                StockDisponible = p.Stock
            });
        }
        OnChange?.Invoke();
    }

    public void ActualizarCantidad(int productoId, int nuevaCantidad)
    {
        var i = _items.FirstOrDefault(x => x.ProductoId == productoId);
        if (i == null) return;
        i.Cantidad = Math.Clamp(nuevaCantidad, 1, i.StockDisponible);
        OnChange?.Invoke();
    }

    public void Incrementar(int productoId, int delta)
    {
        var i = _items.FirstOrDefault(x => x.ProductoId == productoId);
        if (i == null) return;
        i.Cantidad = Math.Clamp(i.Cantidad + delta, 1, i.StockDisponible);
        OnChange?.Invoke();
    }

    public void Quitar(int productoId)
    {
        var i = _items.FirstOrDefault(x => x.ProductoId == productoId);
        if (i == null) return;
        _items.Remove(i);
        OnChange?.Invoke();
    }

    public void Vaciar()
    {
        _items.Clear();
        OnChange?.Invoke();
    }
}
