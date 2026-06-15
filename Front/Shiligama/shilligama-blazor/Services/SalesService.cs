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
            var response = await _http.GetAsync("pedidos");
            if (response.IsSuccessStatusCode)
            {
                var pedidos = await response.Content.ReadFromJsonAsync<List<PedidoApi>>(_json);
                _orders.Clear();
                if (pedidos != null)
                {
                    // Fetch detalles en paralelo para que Items > 0 en la vista
                    var tareas = pedidos.Select(async p =>
                    {
                        var order = p.ToOrder();
                        try
                        {
                            var detalles = await _http.GetFromJsonAsync<List<DetallePedidoApi>>(
                                $"detalles-pedido/por-pedido/{p.IdPedido}", _json);
                            if (detalles != null && detalles.Count > 0)
                            {
                                order.Items    = detalles.Count;
                                order.Products = detalles.Select(d => new CartItem
                                {
                                    Id       = d.Producto?.IdProducto ?? 0,
                                    Name     = d.Producto?.Nombre ?? "Producto",
                                    Price    = (decimal)d.PrecioUnitario,
                                    Quantity = d.Cantidad,
                                    Image    = ""
                                }).ToList();
                            }
                        }
                        catch { /* si falla el detalle, el pedido igual aparece con Items=0 */ }
                        return order;
                    });
                    _orders.AddRange(await Task.WhenAll(tareas));
                }
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
    // Enriquece cada pedido con sus líneas de detalle (GET /detalles-pedido/por-pedido/{id})
    // para que "Mis Pedidos" muestre los productos y el método de pago correcto.
    public async Task<List<Order>> GetClientOrdersAsync(int idCliente)
    {
        var orders = new List<Order>();
        if (idCliente <= 0) return orders;
        try
        {
            var pedidos = await _http.GetFromJsonAsync<List<PedidoApi>>(
                $"pedidos/por-cliente/{idCliente}", _json);
            if (pedidos == null) return orders;

            foreach (var p in pedidos)
            {
                var order = p.ToOrder();

                // Cargar líneas de detalle para mostrar productos
                try
                {
                    var detalles = await _http.GetFromJsonAsync<List<DetallePedidoApi>>(
                        $"detalles-pedido/por-pedido/{p.IdPedido}", _json);

                    if (detalles != null && detalles.Count > 0)
                    {
                        order.Products = detalles.Select(d => new CartItem
                        {
                            Id       = d.Producto?.IdProducto ?? 0,
                            Name     = d.Producto?.Nombre ?? "Producto",
                            Price    = (decimal)(d.PrecioUnitario),
                            Quantity = d.Cantidad,
                            Image    = ""
                        }).ToList();
                        order.Items = order.Products.Count;

                        // Subtotal real desde los detalles
                        order.Subtotal = order.Products.Sum(x => x.Price * x.Quantity);
                    }
                }
                catch { /* si falla, Products queda vacío */ }

                orders.Add(order);
            }
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

    // Convierte el status interno del front al valor ENUM que espera el backend.
    public static string ToBackendEstado(string status) => status.ToLower() switch
    {
        "recibido"   => "RECIBIDO",
        "en_proceso" => "EN_PROCESO",
        "atendido"   => "ATENDIDO",
        "rechazado"  => "RECHAZADO",
        "cancelado"  => "CANCELADO",
        _            => status.ToUpper()
    };

    public async Task UpdateOrderStatusAsync(string id, string status)
    {
        // Actualizar caché local
        var order = _orders.FirstOrDefault(o => o.Id == id);
        if (order != null) order.Status = status;
        var sale = _sales.FirstOrDefault(s => s.Id == id.Replace("PED-", "VTA-"));
        if (sale != null) sale.Estado = status == "atendido" ? "completado" : status;

        // Persiste en el backend
        if (int.TryParse(id.StartsWith("PED-") ? id[4..] : id, out var numId) && numId > 0)
        {
            var dto = new { idPedido = numId, estadoPedido = ToBackendEstado(status) };
            try { await _http.PutAsJsonAsync("pedidos", dto); }
            catch { /* red no disponible */ }
        }
    }

    // Mantener versión síncrona legacy para no romper referencias existentes
    public void UpdateOrderStatus(string id, string status)
        => _ = UpdateOrderStatusAsync(id, status);

    public void DeleteOrder(string id)
    {
        var order = _orders.FirstOrDefault(o => o.Id == id);
        if (order != null) _orders.Remove(order);
    }

    // Cancela un pedido del cliente (solo si aún está en estado RECIBIDO y dentro del tiempo límite).
    // Llama a PUT /api/pedidos con estado CANCELADO.
    // Returns: true si se canceló en el backend, false si hubo error (se actualiza la caché de todas formas).
    public async Task<bool> CancelOrderAsync(int idPedido)
    {
        var pedido = new { idPedido, estadoPedido = "CANCELADO" };
        try
        {
            var resp = await _http.PutAsJsonAsync("pedidos", pedido);
            if (resp.IsSuccessStatusCode)
            {
                // Actualizar caché local
                var order = _orders.FirstOrDefault(o => o.Id == $"PED-{idPedido:D4}");
                if (order != null) order.Status = "cancelado";
                return true;
            }
        }
        catch { /* red no disponible */ }

        // Actualización optimista de la caché
        var localOrder = _orders.FirstOrDefault(o => o.Id == $"PED-{idPedido:D4}");
        if (localOrder != null) localOrder.Status = "cancelado";
        return false;
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

    // Versión pública y awaitable: devuelve el orderId string para mostrar en pantalla.
    // Checkout.razor llama directamente a este método.
    public async Task<string> PlaceOnlineOrderAsync(
        string customerName, List<CartItem> items,
        decimal subtotal, decimal deliveryFee, decimal total,
        string payMethod, string deliveryMethod, string address, int idCliente)
    {
        int idPedido = await AddOrderAsync(customerName, items, total, payMethod,
                                           deliveryMethod, address, idCliente);

        string orderId = idPedido > 0
            ? $"PED-{idPedido:D4}"
            : $"SHI-{DateTime.Now.Year}-{(_orders.Count + 1):D4}";

        // Actualizar caché local con el ID definitivo
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
            PaymentMethod  = payMethod,
            Address        = address,
            Products       = new List<CartItem>(items),
            TimelinePedidoRecibido = DateTime.Now,
        });
        _sales.Insert(0, new Sale
        {
            Id         = $"VTA-{_sales.Count + 1:D3}",
            Fecha      = DateTime.Now,
            Cliente    = customerName,
            Canal      = "web",
            Productos  = items,
            Total      = total,
            MetodoPago = payMethod,
            Comprobante= "boleta",
            Estado     = "pendiente"
        });

        return orderId;
    }

    private async Task<int> AddOrderAsync(string customerName, List<CartItem> items,
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
            montoTotal       = (double)total,
            estadoPedido     = "RECIBIDO",
            direccionEntrega = address,
            modalidadVenta   = deliveryMethod == "delivery" ? "DELIVERY" : "PRESENCIAL",
            observaciones    = (string?)null,
            cliente          = idCliente > 0 ? new { idUsuario = idCliente } : null,
            detalles         = detalles
        };

        try
        {
            var resp = await _http.PostAsJsonAsync("pedidos", pedido);
            if (resp.IsSuccessStatusCode)
            {
                int idPedido = await resp.Content.ReadFromJsonAsync<int>();
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
                return idPedido;
            }
        }
        catch { /* red no disponible */ }

        return 0; // 0 indica que no se pudo guardar en la BD
    }
}

// ── VentaApi, PedidoApi y clases de referencia se encuentran en Models/Api/SalesApiModels.cs ──
