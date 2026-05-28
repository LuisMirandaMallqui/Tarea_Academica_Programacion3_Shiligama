using ShiligamaWA.Models;

namespace ShiligamaWA.Services;

public interface IProductoService
{
    IReadOnlyList<Producto> Listar();
    IReadOnlyList<Producto> ListarPorCategoria(string categoriaCodigo);
    IReadOnlyList<Producto> Buscar(string termino);
    IReadOnlyList<Producto> ListarOfertas();
    IReadOnlyList<Producto> ListarStockBajo();
    Producto? Obtener(int id);
    Producto Crear(Producto p);
    Producto Actualizar(Producto p);
    void Eliminar(int id);
    void AjustarStock(int productoId, int delta, string motivo, string? referencia = null);
}

public interface ICategoriaService
{
    IReadOnlyList<Categoria> Listar();
    Categoria? Obtener(string codigo);
}

public interface IUsuarioService
{
    Usuario? AutenticarPorEmail(string email, string password, RolUsuario rolEsperado);
    Usuario? ObtenerPorEmail(string email);
    Usuario? Obtener(int id);
    Usuario Registrar(Usuario u);
    IReadOnlyList<Usuario> ListarClientes();
}

public interface ICarritoService
{
    event Action? OnChange;
    IReadOnlyList<ItemCarrito> Items { get; }
    int Cantidad { get; }
    decimal Subtotal { get; }
    decimal CostoEnvio { get; }   // tarifa unica
    decimal Total { get; }

    void Agregar(Producto p, int cantidad = 1);
    void ActualizarCantidad(int productoId, int nuevaCantidad);
    void Incrementar(int productoId, int delta);
    void Quitar(int productoId);
    void Vaciar();
}

public interface ISesionService
{
    event Action? OnChange;
    SesionActual Actual { get; }
    void IniciarSesion(Usuario u);
    void CerrarSesion();
}

public interface IPedidoService
{
    IReadOnlyList<Pedido> Listar();
    IReadOnlyList<Pedido> ListarPorCliente(int clienteId);
    IReadOnlyList<Pedido> ListarPorEstado(EstadoPedido estado);
    Pedido? Obtener(int id);
    Pedido Crear(Pedido p);
    void CambiarEstado(int id, EstadoPedido nuevo);
}

public interface IVentaService
{
    IReadOnlyList<Venta> Listar();
    Venta? Obtener(int id);
    Venta RegistrarVenta(Venta v);
    string SiguienteCodigo();
}

public interface IDevolucionService
{
    IReadOnlyList<Devolucion> Listar();
    IReadOnlyList<Devolucion> ListarPorTrabajador(int trabajadorId);
    Devolucion? Obtener(int id);
    Devolucion Crear(Devolucion d);
    void CambiarEstado(int id, EstadoDevolucion nuevo, int? trabajadorId, string? trabajadorNombre);
}

public interface INotificacionService
{
    IReadOnlyList<Notificacion> Listar();
    int CantidadNoLeidas();
    void MarcarLeida(int id);
    void MarcarTodasLeidas();
}

public interface IReporteService
{
    ResumenVentas ObtenerResumen();
    List<KpiDashboard> ObtenerKpisDashboard();
    List<ProductoTopVendido> TopProductos(int n = 5);
    List<ClienteReporte> TopClientes(int n = 5);
}

public interface IPersonalService
{
    IReadOnlyList<Trabajador> Listar();
    Trabajador? Obtener(int id);
    Trabajador Crear(Trabajador t);
    Trabajador Actualizar(Trabajador t);
    void Eliminar(int id);
}

public interface IInventarioService
{
    IReadOnlyList<StockSnapshot> ObtenerSnapshot();
    IReadOnlyList<MovimientoInventario> ListarMovimientos(int? productoId = null);
    void RegistrarMovimiento(MovimientoInventario m);
}

public interface IOfertaService
{
    IReadOnlyList<Oferta> ListarVigentes();
    IReadOnlyList<Oferta> Listar();
    Oferta? Obtener(int id);
}
