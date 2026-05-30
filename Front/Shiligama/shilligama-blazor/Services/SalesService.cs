using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using shilligama_blazor.Models;

namespace shilligama_blazor.Services;

// ============================================================================
// SalesService — lee ventas y pedidos desde el API REST del back.
//
// Patrón igual que ProductService: EnsureLoadedAsync() llena la caché una vez,
// los métodos síncronos GetSales() / GetRecentOrders() devuelven desde la caché
// para no romper las páginas que los llaman síncronamente.
//
// Endpoints:
//   GET  /api/ventas            → lista de ventas (mapea a Sale)
//   GET  /api/pedidos           → lista de pedidos (mapea a Order)
//   POST /api/ventas            → nueva venta presencial
//   PUT  /api/pedidos           → actualizar estado de pedido
// ============================================================================
public class SalesService
{
    private readonly HttpClient _http;
    private readonly JsonSerializerOptions _json;

    private readonly List<Sale>  _sales  = new();
    private readonly List<Order> _orders = new();
    private bool _cargado = false;

    // Diagnóstico: último error al cargar pedidos (null = sin error)
    public string? UltimoErrorPedidos { get; private set; }

    public SalesService(HttpClient http, JsonSerializerOptions json)
    {
        _http = http;
        _json = json;
    }

    // Llama al API y llena la caché. Las páginas deben hacer await antes de
    // leer GetSales() / GetRecentOrders() si quieren datos reales.
    public async Task EnsureLoadedAsync(bool recargar = false)
    {
        if (_cargado && !recargar) return;

        try
        {
            var ventas = await _http.GetFromJsonAsync<List<VentaApi>>("ventas", _json);
            _sales.Clear();
            if (ventas != null)
                _sales.AddRange(ventas.Select(v => v.ToSale()));
        }
        catch { /* backend no disponible — mantener lista vacía */ }

        try
        {
            // Primero probar si el endpoint responde (para ver el texto crudo en caso de error)
            var response = await _http.GetAsync("pedidos");
            if (response.IsSuccessStatusCode)
            {
                var pedidos = await response.Content.ReadFromJsonAsync<List<PedidoApi>>(_json);
                _orders.Clear();
                if (pedidos != null)
                    _orders.AddRange(pedidos.Select(p => p.ToOrder()));
                UltimoErrorPedidos = null;
            }
            else
            {
                var body = await response.Content.ReadAsStringAsync();
                UltimoErrorPedidos = $"HTTP {(int)response.StatusCode}: {body}";
            }
        }
        catch (Exception ex)
        {
            UltimoErrorPedidos = ex.Message;
        }

        _cargado = true;
    }

    // ----- Getters síncronos (sobre la caché) -----
    public List<Sale>  GetSales()        => _sales;
    public List<Order> GetRecentOrders() => _orders;

    // Pedidos filtrados por cliente — para la pantalla "Mis Pedidos".
    // Llama directamente al API para obtener solo los del cliente logueado.
    public async Task<List<Order>> GetClientOrdersAsync(int idCliente)
    {
        var orders = new List<Order>();
        if (idCliente <= 0) return orders;
        try
        {
            var pedidos = await _http.GetFromJsonAsync<List<PedidoApi>>(
                $"pedidos/por-cliente/{idCliente}", _json);
            if (pedidos != null)
                orders.AddRange(pedidos.Select(p => p.ToOrder()));
        }
        catch { /* backend no disponible */ }
        return orders;
    }

    // ----- Mutaciones -----

    // Venta presencial desde el POS del trabajador.
    // Construye el JSON que espera VentaWS.insertar() y lo envía.
    // Parámetros idTrabajador e idMetodoPago vienen del contexto de sesión.
    public async Task<int> AddPresencialSaleAsync(
        string customerName,
        List<CartItem> items,
        decimal total,
        string payMethod,
        string invoiceType,
        int idTrabajador,
        int idMetodoPago = 0)   // 0 = detectar por nombre
    {
        // Mapeo nombre de método → id (coincide con seeds de la BD)
        if (idMetodoPago == 0)
        {
            idMetodoPago = payMethod.ToLower() switch
            {
                "efectivo" => 1,
                "yape"     => 2,
                "plin"     => 3,
                "tarjeta"  => 4,
                _          => 1
            };
        }

        var detalles = items.Select(i => new
        {
            producto        = new { idProducto = i.Id },
            cantidad        = i.Quantity,
            precioUnitario  = (double)i.Price,
            subtotal        = (double)(i.Price * i.Quantity)
        }).ToList();

        var venta = new
        {
            canalVenta  = "PRESENCIAL",
            estadoVenta = "COMPLETADA",
            montoTotal  = (double)total,
            montoDescuento = 0.0,
            observaciones  = string.IsNullOrWhiteSpace(customerName)
                             ? null : $"Cliente: {customerName}",
            trabajador  = new { idUsuario = idTrabajador },
            metodoPago  = new { idMetodoPago = idMetodoPago },
            detalles    = detalles
        };

        try
        {
            var resp = await _http.PostAsJsonAsync("ventas", venta);
            if (resp.IsSuccessStatusCode)
            {
                int idGenerado = await resp.Content.ReadFromJsonAsync<int>();
                // Agrega a la caché local sin recargar toda la lista
                _sales.Insert(0, new Sale
                {
                    Id         = $"VTA-{idGenerado:D3}",
                    Fecha      = DateTime.Now,
                    Cliente    = string.IsNullOrWhiteSpace(customerName) ? "Público General" : customerName,
                    Canal      = "presencial",
                    Productos  = items,
                    Total      = total,
                    MetodoPago = payMethod,
                    Comprobante= invoiceType,
                    Estado     = "completado"
                });
                return idGenerado;
            }
        }
        catch { /* error de red → caemos al fallback */ }

        // Fallback local si el API no está disponible
        var localId = _sales.Count + 1;
        _sales.Insert(0, new Sale
        {
            Id         = $"VTA-{localId:D3}",
            Fecha      = DateTime.Now,
            Cliente    = string.IsNullOrWhiteSpace(customerName) ? "Público General" : customerName,
            Canal      = "presencial",
            Productos  = items,
            Total      = total,
            MetodoPago = payMethod,
            Comprobante= invoiceType,
            Estado     = "completado"
        });
        return localId;
    }

    // Mantener la firma legacy para que RegistrarVenta.razor no necesite cambio
    // inmediato (ahora es void síncrono pero se delega al async sin await).
    public void AddPresencialSale(string customerName, List<CartItem> items,
                                  decimal total, string payMethod, string invoiceType)
    {
        // Fire-and-forget con idTrabajador=0 (se asignará en ProcessSale async)
        _ = AddPresencialSaleAsync(customerName, items, total, payMethod, invoiceType, 0);

        // Actualiza caché local inmediatamente para feedback visual
        var localId = _sales.Count + 1;
        _sales.Insert(0, new Sale
        {
            Id         = $"VTA-{localId:D3}",
            Fecha      = DateTime.Now,
            Cliente    = string.IsNullOrWhiteSpace(customerName) ? "Público General" : customerName,
            Canal      = "presencial",
            Productos  = items,
            Total      = total,
            MetodoPago = payMethod,
            Comprobante= invoiceType,
            Estado     = "completado"
        });

        // También agrega a órdenes para el dashboard
        _orders.Insert(0, new Order
        {
            Id       = $"ORD-{_orders.Count + 1:D3}",
            Customer = string.IsNullOrWhiteSpace(customerName) ? "Público General" : customerName,
            Date     = DateTime.Now,
            Total    = total,
            Items    = items.Sum(i => i.Quantity),
            Status   = "entregado",
            Channel  = "Presencial"
        });
    }

    public void UpdateOrderStatus(string id, string status)
    {
        var order = _orders.FirstOrDefault(o => o.Id == id);
        if (order != null) order.Status = status;

        var sale = _sales.FirstOrDefault(s => s.Id == id.Replace("PED-", "VTA-"));
        if (sale != null) sale.Estado = status == "entregado" ? "completado" : status;
    }

    public void DeleteOrder(string id)
    {
        var order = _orders.FirstOrDefault(o => o.Id == id);
        if (order != null) _orders.Remove(order);
    }

    // Pedido online del cliente (Checkout.razor).
    // Hace POST /api/pedidos y guarda en caché local.
    // idCliente: ID del usuario logueado (0 = anónimo / público general).
    public void AddOrder(string customerName, List<CartItem> items,
                         decimal subtotal, decimal deliveryFee, decimal total,
                         string paymentMethod, string deliveryMethod, string address,
                         int idCliente = 0)
    {
        // Fire-and-forget hacia el API
        _ = AddOrderAsync(customerName, items, total, paymentMethod, deliveryMethod, address, idCliente);

        // Caché local inmediata
        var count  = _orders.Count + 1;
        var orderId = $"SHI-{DateTime.Now.Year}-{count:D4}";
        _orders.Insert(0, new Order
        {
            Id             = orderId,
            Customer       = customerName,
            Date           = DateTime.Now,
            Subtotal       = subtotal,
            DeliveryFee    = deliveryFee,
            Total          = total,
            Items          = items.Sum(i => i.Quantity),
            Status         = "pendiente",
            DeliveryMethod = deliveryMethod,
            PaymentMethod  = paymentMethod,
            Address        = address,
            Products       = new List<CartItem>(items),
            TimelinePedidoRecibido = DateTime.Now,
        });

        var saleId = $"VTA-{_sales.Count + 1:D3}";
        _sales.Insert(0, new Sale
        {
            Id         = saleId,
            Fecha      = DateTime.Now,
            Cliente    = customerName,
            Canal      = "web",
            Productos  = items,
            Total      = total,
            MetodoPago = paymentMethod,
            Comprobante= "boleta",
            Estado     = "pendiente"
        });
    }

    private async Task AddOrderAsync(string customerName, List<CartItem> items,
                                     decimal total, string payMethod,
                                     string deliveryMethod, string address, int idCliente)
    {
        int idMetodoPago = payMethod.ToLower() switch
        {
            "efectivo" => 1,
            "yape"     => 2,
            "plin"     => 3,
            "tarjeta"  => 4,
            _          => 1
        };

        var detalles = items.Select(i => new
        {
            producto       = new { idProducto = i.Id },
            cantidad       = i.Quantity,
            precioUnitario = (double)i.Price,
            subtotal       = (double)(i.Price * i.Quantity)
        }).ToList();

        var pedido = new
        {
            montoTotal     = (double)total,
            estadoPedido   = "RECIBIDO",
            direccionEntrega = address,
            modalidadVenta = deliveryMethod == "delivery" ? "DELIVERY" : "PRESENCIAL",
            observaciones  = (string?)null,
            cliente        = idCliente > 0 ? new { idUsuario = idCliente } : null,
            detalles       = detalles
        };

        try
        {
            var resp = await _http.PostAsJsonAsync("pedidos", pedido);
            if (resp.IsSuccessStatusCode)
            {
                int idPedido = await resp.Content.ReadFromJsonAsync<int>();
                // Insertar cada detalle — INSERTAR_DETALLE_PEDIDO recalcula MONTO_TOTAL
                foreach (var item in items)
                {
                    var detalle = new
                    {
                        idPadrePedido  = idPedido,
                        producto       = new { idProducto = item.Id },
                        cantidad       = item.Quantity,
                        precioUnitario = (double)item.Price,
                        subtotal       = (double)(item.Price * item.Quantity)
                    };
                    await _http.PostAsJsonAsync("detalles-pedido", detalle);
                }
            }
        }
        catch { /* red no disponible */ }
    }
}

// ── Clases que mapean el JSON del backend (antes en VentaApi.cs / PedidoApi.cs) ──

// Refleja Venta del backend Java
class VentaApi
{
    public int IdVenta { get; set; }
    public int IdPedido { get; set; }
    public DateTime? FechaHora { get; set; }
    public double MontoTotal { get; set; }
    public double MontoDescuento { get; set; }
    public string CanalVenta { get; set; } = string.Empty;
    public string EstadoVenta { get; set; } = string.Empty;
    public string? Observaciones { get; set; }
    public UserRef? Cliente { get; set; }
    public UserRef? Trabajador { get; set; }
    public MetodoPagoRef? MetodoPago { get; set; }
    public List<DetalleVentaApi>? Detalles { get; set; }

    public Sale ToSale() => new Sale
    {
        Id         = $"VTA-{IdVenta:D3}",
        Fecha      = FechaHora ?? DateTime.Now,
        Cliente    = Cliente != null
                     ? $"{Cliente.Nombres} {Cliente.Apellidos}".Trim()
                     : "Público General",
        Canal      = CanalVenta.ToLower() switch
        {
            "presencial" => "presencial",
            "web"        => "web",
            "whatsapp"   => "whatsapp",
            _            => "presencial"
        },
        Total      = (decimal)MontoTotal,
        MetodoPago = MetodoPago?.Nombre?.ToLower() ?? "efectivo",
        Comprobante= "boleta",
        Estado     = EstadoVenta.ToLower() switch
        {
            "completada" => "completado",
            "anulada"    => "cancelado",
            _            => "completado"
        }
    };
}

// Refleja Pedido del backend Java
class PedidoApi
{
    public int IdPedido { get; set; }
    public DateTime? FechaHora { get; set; }
    public double MontoTotal { get; set; }
    public string EstadoPedido { get; set; } = string.Empty;
    public string? DireccionEntrega { get; set; }
    public string? ModalidadVenta { get; set; }
    public string? Observaciones { get; set; }
    public UserRef? Cliente { get; set; }
    public List<DetallePedidoApi>? Detalles { get; set; }

    public Order ToOrder() => new Order
    {
        Id       = $"PED-{IdPedido:D4}",
        Customer = (Cliente != null &&
                    !string.IsNullOrWhiteSpace($"{Cliente.Nombres} {Cliente.Apellidos}".Trim()))
                   ? $"{Cliente.Nombres} {Cliente.Apellidos}".Trim()
                   : "Cliente",
        Date     = FechaHora ?? DateTime.Now,
        Total    = (decimal)MontoTotal,
        Items    = Detalles?.Count ?? 0,
        Status   = (EstadoPedido ?? "").ToUpper() switch
        {
            "RECIBIDO"   => "pendiente",
            "EN_PROCESO" => "preparando",
            "ATENDIDO"   => "entregado",
            "RECHAZADO"  => "cancelado",
            "CANCELADO"  => "cancelado",
            _            => "pendiente"
        },
        DeliveryMethod = (ModalidadVenta ?? "").ToUpper() switch
        {
            "RECOJO_TIENDA" => "pickup",
            _               => "delivery"
        },
        Channel  = (ModalidadVenta ?? "").ToUpper() switch
        {
            "RECOJO_TIENDA" => "Presencial",
            _               => "Online"
        },
        Address  = DireccionEntrega ?? string.Empty,
        TimelinePedidoRecibido = FechaHora ?? DateTime.Now,
    };
}

// Clases de referencia compartidas (MetodoPago, Usuario, Producto)
class MetodoPagoRef
{
    public int IdMetodoPago { get; set; }
    public string Nombre { get; set; } = string.Empty;
}

class UserRef
{
    public int IdUsuario { get; set; }
    public string Nombres { get; set; } = string.Empty;
    public string Apellidos { get; set; } = string.Empty;
    public string? Correo { get; set; }
    public string? Dni { get; set; }
    public string? Telefono { get; set; }
    public string? Cargo { get; set; }
    public string? Rol { get; set; }
}

class DetalleVentaApi
{
    public int IdDetalleVenta { get; set; }
    public int Cantidad { get; set; }
    public double PrecioUnitario { get; set; }
    public double Subtotal { get; set; }
    public ProductoRef? Producto { get; set; }
}

class DetallePedidoApi
{
    public int IdDetallePedido { get; set; }
    public int Cantidad { get; set; }
    public double PrecioUnitario { get; set; }
    public double Subtotal { get; set; }
    public ProductoRef? Producto { get; set; }
}

class ProductoRef
{
    public int IdProducto { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public double PrecioUnitario { get; set; }
}
