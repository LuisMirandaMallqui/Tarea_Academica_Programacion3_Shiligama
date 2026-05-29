using ShiligamaWA.Models;

namespace ShiligamaWA.Services.Mock;

internal static class SeedData
{
    public static List<Categoria> Categorias() => new()
    {
        new() { Id = 1, Codigo = "abarrotes", Nombre = "Abarrotes", Icono = "fa-solid fa-wheat-awn", Productos = 4 },
        new() { Id = 2, Codigo = "bebidas",   Nombre = "Bebidas",   Icono = "fa-solid fa-bottle-water", Productos = 3 },
        new() { Id = 3, Codigo = "lacteos",   Nombre = "Lácteos",   Icono = "fa-solid fa-cheese", Productos = 3 },
        new() { Id = 4, Codigo = "snacks",    Nombre = "Snacks",    Icono = "fa-solid fa-cookie", Productos = 2 },
        new() { Id = 5, Codigo = "limpieza",  Nombre = "Limpieza",  Icono = "fa-solid fa-soap", Productos = 3 },
        new() { Id = 6, Codigo = "panaderia", Nombre = "Panadería", Icono = "fa-solid fa-bread-slice", Productos = 2 }
    };

    public static List<Producto> Productos() => new()
    {
        new() { Id = 1, Codigo = "ARR-001", Nombre = "Arroz Extra Costeño 5kg", Precio = 24.90m,
                Imagen = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400&h=400&fit=crop",
                CategoriaCodigo = "abarrotes", CategoriaNombre = "Abarrotes", Stock = 45, StockMinimo = 15,
                Descripcion = "Arroz de grano largo de la más alta calidad. Ideal para todo tipo de preparaciones.",
                UnidadMedida = "bolsa 5kg", Proveedor = "Costeño S.A." },

            new() { Id = 2, Codigo = "ACE-001", Nombre = "Aceite Primor 1L", Precio = 12.50m, PrecioOriginal = 15.90m, EsPromocion = true,
                Imagen = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400&h=400&fit=crop",
                CategoriaCodigo = "abarrotes", CategoriaNombre = "Abarrotes", Stock = 32, StockMinimo = 10,
                Descripcion = "Aceite vegetal premium para cocinar. Bajo en grasas saturadas.",
                UnidadMedida = "botella 1L", Proveedor = "Alicorp" },

            new() { Id = 3, Codigo = "BEB-001", Nombre = "Inca Kola 1.5L", Precio = 6.50m,
                Imagen = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=400&h=400&fit=crop",
                CategoriaCodigo = "bebidas", CategoriaNombre = "Bebidas", Stock = 78, StockMinimo = 20,
                Descripcion = "La bebida del sabor nacional. Botella familiar de 1.5 litros.",
                UnidadMedida = "botella 1.5L", Proveedor = "Coca-Cola Perú" },

            new() { Id = 4, Codigo = "LAC-001", Nombre = "Leche Gloria Entera 1L", Precio = 5.20m,
                Imagen = "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=400&h=400&fit=crop",
                CategoriaCodigo = "lacteos", CategoriaNombre = "Lácteos", Stock = 8, StockMinimo = 30,
                Descripcion = "Leche entera evaporada. Rica en calcio y vitaminas.",
                UnidadMedida = "caja 1L", Proveedor = "Gloria S.A." },

            new() { Id = 5, Codigo = "LAC-002", Nombre = "Yogurt Laive Fresa 1L", Precio = 8.90m, PrecioOriginal = 10.50m, EsPromocion = true,
                Imagen = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400&h=400&fit=crop",
                CategoriaCodigo = "lacteos", CategoriaNombre = "Lácteos", Stock = 25, StockMinimo = 10,
                Descripcion = "Yogurt cremoso sabor fresa con probióticos.",
                UnidadMedida = "botella 1L", Proveedor = "Laive" },

            new() { Id = 6, Codigo = "SNK-001", Nombre = "Galletas Oreo x6", Precio = 4.50m,
                Imagen = "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400&h=400&fit=crop",
                CategoriaCodigo = "snacks", CategoriaNombre = "Snacks", Stock = 65, StockMinimo = 20,
                Descripcion = "Galletas de chocolate con relleno de crema de vainilla. Pack de 6.",
                UnidadMedida = "pack x6", Proveedor = "Mondelez" },

            new() { Id = 7, Codigo = "LIM-001", Nombre = "Detergente Bolivar 2.6kg", Precio = 28.90m,
                Imagen = "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=400&h=400&fit=crop",
                CategoriaCodigo = "limpieza", CategoriaNombre = "Limpieza", Stock = 18, StockMinimo = 8,
                Descripcion = "Detergente en polvo de alta eficiencia.",
                UnidadMedida = "bolsa 2.6kg", Proveedor = "Intradevco" },

            new() { Id = 8, Codigo = "LIM-002", Nombre = "Jabón Bolivar Pack x3", Precio = 9.90m, PrecioOriginal = 12.90m, EsPromocion = true,
                Imagen = "https://images.unsplash.com/photo-1600857544200-b2f666a9a2ec?w=400&h=400&fit=crop",
                CategoriaCodigo = "limpieza", CategoriaNombre = "Limpieza", Stock = 42, StockMinimo = 15,
                Descripcion = "Jabón de lavar en barra. Pack de 3 unidades.",
                UnidadMedida = "pack x3", Proveedor = "Intradevco" },

            new() { Id = 9, Codigo = "BEB-002", Nombre = "Agua San Mateo 2.5L", Precio = 4.20m,
                Imagen = "https://images.unsplash.com/photo-1548839140-29a749e7b8e7?w=400&h=400&fit=crop",
                CategoriaCodigo = "bebidas", CategoriaNombre = "Bebidas", Stock = 56, StockMinimo = 20,
                Descripcion = "Agua mineral natural. Botella familiar de 2.5 litros.",
                UnidadMedida = "botella 2.5L", Proveedor = "Backus" },

            new() { Id = 10, Codigo = "PAN-001", Nombre = "Pan Bimbo Blanco 600g", Precio = 7.90m,
                Imagen = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400&h=400&fit=crop",
                CategoriaCodigo = "panaderia", CategoriaNombre = "Panadería", Stock = 22, StockMinimo = 10,
                Descripcion = "Pan de molde blanco, suave y fresco.",
                UnidadMedida = "bolsa 600g", Proveedor = "Bimbo" },

            new() { Id = 11, Codigo = "ABA-002", Nombre = "Azúcar Rubia 1kg", Precio = 4.80m,
                Imagen = "https://images.unsplash.com/photo-1568051243851-f9b136146e97?w=400&h=400&fit=crop",
                CategoriaCodigo = "abarrotes", CategoriaNombre = "Abarrotes", Stock = 95, StockMinimo = 25,
                Descripcion = "Azúcar rubia de caña, granulada.",
                UnidadMedida = "bolsa 1kg", Proveedor = "Casagrande" },

            new() { Id = 12, Codigo = "ABA-003", Nombre = "Fideo Don Vittorio 500g", Precio = 3.90m,
                Imagen = "https://images.unsplash.com/photo-1551462147-37885acc36f1?w=400&h=400&fit=crop",
                CategoriaCodigo = "abarrotes", CategoriaNombre = "Abarrotes", Stock = 70, StockMinimo = 20,
                Descripcion = "Fideos largos de calidad premium.",
                UnidadMedida = "bolsa 500g", Proveedor = "Alicorp" },

            new() { Id = 13, Codigo = "LAC-003", Nombre = "Queso Bonlé Edam 200g", Precio = 14.50m,
                Imagen = "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=400&h=400&fit=crop",
                CategoriaCodigo = "lacteos", CategoriaNombre = "Lácteos", Stock = 14, StockMinimo = 8,
                Descripcion = "Queso edam cremoso, ideal para sándwiches.",
                UnidadMedida = "paquete 200g", Proveedor = "Gloria S.A." },

            new() { Id = 14, Codigo = "SNK-002", Nombre = "Chocolate Sublime 28g x10", Precio = 11.90m,
                Imagen = "https://images.unsplash.com/photo-1623660053975-cf75a8be0908?w=400&h=400&fit=crop",
                CategoriaCodigo = "snacks", CategoriaNombre = "Snacks", Stock = 4, StockMinimo = 12,
                Descripcion = "Chocolate con maní Sublime. Pack de 10 unidades.",
                UnidadMedida = "pack x10", Proveedor = "Nestlé" },

            new() { Id = 15, Codigo = "PAN-002", Nombre = "Tostadas Integrales 200g", Precio = 6.50m,
                Imagen = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=400&h=400&fit=crop",
                CategoriaCodigo = "panaderia", CategoriaNombre = "Panadería", Stock = 28, StockMinimo = 10,
                Descripcion = "Tostadas integrales crocantes, ideales para el desayuno.",
                UnidadMedida = "paquete 200g", Proveedor = "Bimbo" }
    };

    public static List<Usuario> Usuarios() => new()
    {
        // Test accounts del TS
        new() { Id = 1, Email = "test1234@admin.com", Password = "eldelegadolavandoselasmanos",
                Nombres = "Admin", Apellidos = "Principal", Telefono = "999-000-001",
                Rol = RolUsuario.Administrador, Dni = "10000001" },
        new() { Id = 2, Email = "trabajador@shiligama.com", Password = "trabajador123",
                Nombres = "Carlos", Apellidos = "Mendoza", Telefono = "999-000-002",
                Rol = RolUsuario.Trabajador, Dni = "10000002" },
        new() { Id = 3, Email = "cliente@shiligama.com", Password = "cliente123",
                Nombres = "Juan", Apellidos = "Pérez", Telefono = "999-000-003",
                Rol = RolUsuario.Cliente, Dni = "10000003",
                Direccion = "Av. La Marina 1234, San Miguel - Lima" },
        new() { Id = 4, Email = "maria@gmail.com", Password = "maria123",
                Nombres = "María", Apellidos = "García", Telefono = "999-000-004",
                Rol = RolUsuario.Cliente, Dni = "10000004",
                Direccion = "Jr. Cusco 543, Cercado de Lima" }
    };

    public static List<Trabajador> Trabajadores() => new()
    {
        new() { Id = 1, UsuarioId = 2, Nombres = "Carlos", Apellidos = "Mendoza",
                Email = "trabajador@shiligama.com", Telefono = "999-000-002",
                Dni = "10000002", Cargo = "Cajero",
                FechaIngreso = new DateTime(2024, 3, 15) },
        new() { Id = 2, UsuarioId = 0, Nombres = "Ana", Apellidos = "Rodríguez",
                Email = "ana.rodriguez@shiligama.com", Telefono = "999-000-005",
                Dni = "10000005", Cargo = "Almacenero",
                FechaIngreso = new DateTime(2023, 9, 1) },
        new() { Id = 3, UsuarioId = 0, Nombres = "Luis", Apellidos = "Fernández",
                Email = "luis.fernandez@shiligama.com", Telefono = "999-000-006",
                Dni = "10000006", Cargo = "Repartidor",
                FechaIngreso = new DateTime(2024, 1, 10) },
        new() { Id = 4, UsuarioId = 0, Nombres = "Patricia", Apellidos = "Vargas",
                Email = "patricia.vargas@shiligama.com", Telefono = "999-000-007",
                Dni = "10000007", Cargo = "Supervisor",
                FechaIngreso = new DateTime(2023, 5, 20) }
    };

    public static List<Pedido> Pedidos()
    {
        var hoy = DateTime.Now;
        return new()
        {
            new() {
                Id = 1, Codigo = "PED-1024", ClienteId = 3, ClienteNombre = "Juan Pérez",
                ClienteTelefono = "999-000-003", DireccionEntrega = "Av. La Marina 1234, San Miguel - Lima",
                Fecha = hoy.AddMinutes(-12), Estado = EstadoPedido.Pendiente, MetodoPago = MetodoPago.Efectivo,
                Subtotal = 62.30m,
                Detalles = new() {
                    new() { ProductoId = 1, ProductoNombre = "Arroz Extra Costeño 5kg", Cantidad = 1, PrecioUnitario = 24.90m,
                            ProductoImagen = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=200&h=200&fit=crop" },
                    new() { ProductoId = 4, ProductoNombre = "Leche Gloria Entera 1L", Cantidad = 3, PrecioUnitario = 5.20m,
                            ProductoImagen = "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=200&h=200&fit=crop" },
                    new() { ProductoId = 3, ProductoNombre = "Inca Kola 1.5L", Cantidad = 1, PrecioUnitario = 6.50m,
                            ProductoImagen = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=200&h=200&fit=crop" }
                }
            },
            new() {
                Id = 2, Codigo = "PED-1023", ClienteId = 4, ClienteNombre = "María García",
                ClienteTelefono = "999-000-004", DireccionEntrega = "Jr. Cusco 543, Cercado de Lima",
                Fecha = hoy.AddMinutes(-45), Estado = EstadoPedido.EnPreparacion, MetodoPago = MetodoPago.Yape,
                Subtotal = 32.40m,
                Detalles = new() {
                    new() { ProductoId = 2, ProductoNombre = "Aceite Primor 1L", Cantidad = 1, PrecioUnitario = 12.50m,
                            ProductoImagen = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=200&h=200&fit=crop" },
                    new() { ProductoId = 5, ProductoNombre = "Yogurt Laive Fresa 1L", Cantidad = 1, PrecioUnitario = 8.90m,
                            ProductoImagen = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=200&h=200&fit=crop" },
                    new() { ProductoId = 11, ProductoNombre = "Azúcar Rubia 1kg", Cantidad = 2, PrecioUnitario = 4.80m,
                            ProductoImagen = "https://images.unsplash.com/photo-1568051243851-f9b136146e97?w=200&h=200&fit=crop" }
                }
            },
            new() {
                Id = 3, Codigo = "PED-1022", ClienteId = 3, ClienteNombre = "Juan Pérez",
                ClienteTelefono = "999-000-003", DireccionEntrega = "Av. La Marina 1234, San Miguel - Lima",
                Fecha = hoy.AddHours(-3), Estado = EstadoPedido.EnRuta, MetodoPago = MetodoPago.Tarjeta,
                Subtotal = 45.80m,
                Detalles = new() {
                    new() { ProductoId = 7, ProductoNombre = "Detergente Bolivar 2.6kg", Cantidad = 1, PrecioUnitario = 28.90m,
                            ProductoImagen = "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=200&h=200&fit=crop" },
                    new() { ProductoId = 6, ProductoNombre = "Galletas Oreo x6", Cantidad = 2, PrecioUnitario = 4.50m,
                            ProductoImagen = "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=200&h=200&fit=crop" },
                    new() { ProductoId = 10, ProductoNombre = "Pan Bimbo Blanco 600g", Cantidad = 1, PrecioUnitario = 7.90m,
                            ProductoImagen = "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=200&h=200&fit=crop" }
                }
            },
            new() {
                Id = 4, Codigo = "PED-1021", ClienteId = 4, ClienteNombre = "María García",
                ClienteTelefono = "999-000-004", DireccionEntrega = "Jr. Cusco 543, Cercado de Lima",
                Fecha = hoy.AddDays(-1), FechaEntrega = hoy.AddDays(-1).AddHours(2),
                Estado = EstadoPedido.Entregado, MetodoPago = MetodoPago.Yape,
                Subtotal = 23.80m,
                Detalles = new() {
                    new() { ProductoId = 9, ProductoNombre = "Agua San Mateo 2.5L", Cantidad = 2, PrecioUnitario = 4.20m,
                            ProductoImagen = "https://images.unsplash.com/photo-1548839140-29a749e7b8e7?w=400&h=400&fit=crop" },
                    new() { ProductoId = 8, ProductoNombre = "Jabón Bolivar Pack x3", Cantidad = 1, PrecioUnitario = 9.90m,
                            ProductoImagen = "https://images.unsplash.com/photo-1600857544200-b2f666a9a2ec?w=400&h=400&fit=crop" }
                }
            },
            new() {
                Id = 5, Codigo = "PED-1020", ClienteId = 3, ClienteNombre = "Juan Pérez",
                ClienteTelefono = "999-000-003", DireccionEntrega = "Av. La Marina 1234, San Miguel - Lima",
                Fecha = hoy.AddDays(-3), FechaEntrega = hoy.AddDays(-3).AddHours(1.5),
                Estado = EstadoPedido.Entregado, MetodoPago = MetodoPago.Efectivo,
                Subtotal = 78.20m
            }
        };
    }

    public static List<Devolucion> Devoluciones()
    {
        var hoy = DateTime.Now;
        return new()
        {
            new() {
                Id = 1, Codigo = "DEV-0042", PedidoCodigo = "PED-1018", PedidoId = 0,
                ClienteId = 4, ClienteNombre = "María García",
                FechaSolicitud = hoy.AddDays(-1), Estado = EstadoDevolucion.Solicitada,
                Motivo = "Producto en mal estado",
                MontoDevuelto = 12.50m,
                Detalles = new() {
                    new() { ProductoId = 2, ProductoNombre = "Aceite Primor 1L", Cantidad = 1, PrecioUnitario = 12.50m }
                }
            },
            new() {
                Id = 2, Codigo = "DEV-0041", PedidoCodigo = "PED-1015", PedidoId = 0,
                ClienteId = 3, ClienteNombre = "Juan Pérez",
                TrabajadorAtendioId = 1, TrabajadorAtendioNombre = "Carlos Mendoza",
                FechaSolicitud = hoy.AddDays(-3), FechaResolucion = hoy.AddDays(-2),
                Estado = EstadoDevolucion.Aprobada,
                Motivo = "Producto incorrecto entregado",
                MontoDevuelto = 24.90m,
                Detalles = new() {
                    new() { ProductoId = 1, ProductoNombre = "Arroz Extra Costeño 5kg", Cantidad = 1, PrecioUnitario = 24.90m }
                }
            },
            new() {
                Id = 3, Codigo = "DEV-0040", PedidoCodigo = "PED-1010", PedidoId = 0,
                ClienteId = 4, ClienteNombre = "María García",
                TrabajadorAtendioId = 1, TrabajadorAtendioNombre = "Carlos Mendoza",
                FechaSolicitud = hoy.AddDays(-7), FechaResolucion = hoy.AddDays(-6),
                Estado = EstadoDevolucion.Completada,
                Motivo = "Producto vencido",
                MontoDevuelto = 8.90m,
                Detalles = new() {
                    new() { ProductoId = 5, ProductoNombre = "Yogurt Laive Fresa 1L", Cantidad = 1, PrecioUnitario = 8.90m }
                }
            }
        };
    }

    public static List<Venta> Ventas()
    {
        var hoy = DateTime.Now;
        return new()
        {
            new() {
                Id = 1, Codigo = "VTA-0013", TrabajadorId = 1, TrabajadorNombre = "Carlos Mendoza",
                Fecha = hoy.AddHours(-1), Canal = CanalVenta.Tienda, Estado = EstadoVenta.Confirmada,
                MetodoPago = MetodoPago.Efectivo, Subtotal = 30.50m, MontoRecibido = 50m, BoletaGenerada = true,
                Lineas = new() {
                    new() { ProductoId = 6, ProductoCodigo = "SNK-001", ProductoNombre = "Galletas Oreo x6", Cantidad = 3, PrecioUnitario = 4.50m },
                    new() { ProductoId = 4, ProductoCodigo = "LAC-001", ProductoNombre = "Leche Gloria Entera 1L", Cantidad = 2, PrecioUnitario = 5.20m },
                    new() { ProductoId = 3, ProductoCodigo = "BEB-001", ProductoNombre = "Inca Kola 1.5L", Cantidad = 1, PrecioUnitario = 6.50m }
                }
            },
            new() {
                Id = 2, Codigo = "VTA-0012", TrabajadorId = 1, TrabajadorNombre = "Carlos Mendoza",
                Fecha = hoy.AddHours(-3), Canal = CanalVenta.Tienda, Estado = EstadoVenta.Confirmada,
                MetodoPago = MetodoPago.Yape, Subtotal = 18.40m, BoletaGenerada = true,
                Lineas = new() {
                    new() { ProductoId = 9, ProductoCodigo = "BEB-002", ProductoNombre = "Agua San Mateo 2.5L", Cantidad = 2, PrecioUnitario = 4.20m },
                    new() { ProductoId = 10, ProductoCodigo = "PAN-001", ProductoNombre = "Pan Bimbo Blanco 600g", Cantidad = 1, PrecioUnitario = 7.90m },
                    new() { ProductoId = 11, ProductoCodigo = "ABA-002", ProductoNombre = "Azúcar Rubia 1kg", Cantidad = 1, PrecioUnitario = 4.80m }
                }
            }
        };
    }

    public static List<Notificacion> Notificaciones()
    {
        var hoy = DateTime.Now;
        return new()
        {
            new() { Id = 1, Tipo = TipoNotificacion.Pedido, Titulo = "Nuevo pedido #1024",
                    Mensaje = "Juan Pérez realizó un pedido por S/ 67.30",
                    Fecha = hoy.AddMinutes(-12), Leida = false, EnlaceAccion = "/admin/pedidos" },
            new() { Id = 2, Tipo = TipoNotificacion.StockBajo, Titulo = "Stock crítico: Leche Gloria",
                    Mensaje = "Solo quedan 8 unidades (mínimo: 30)",
                    Fecha = hoy.AddHours(-1), Leida = false, EnlaceAccion = "/admin/inventario" },
            new() { Id = 3, Tipo = TipoNotificacion.Devolucion, Titulo = "Nueva solicitud de devolución",
                    Mensaje = "María García solicitó devolver Aceite Primor 1L",
                    Fecha = hoy.AddHours(-2), Leida = false, EnlaceAccion = "/admin/devoluciones" },
            new() { Id = 4, Tipo = TipoNotificacion.StockBajo, Titulo = "Stock crítico: Chocolate Sublime",
                    Mensaje = "Solo quedan 4 unidades (mínimo: 12)",
                    Fecha = hoy.AddHours(-5), Leida = false, EnlaceAccion = "/admin/inventario" },
            new() { Id = 5, Tipo = TipoNotificacion.Sistema, Titulo = "Backup completado",
                    Mensaje = "Respaldo automático de base de datos exitoso",
                    Fecha = hoy.AddHours(-12), Leida = true },
            new() { Id = 6, Tipo = TipoNotificacion.Promocion, Titulo = "Promo Yogurt Laive activa",
                    Mensaje = "Descuento del 15% durante esta semana",
                    Fecha = hoy.AddDays(-1), Leida = true, EnlaceAccion = "/admin/productos" }
        };
    }

    public static List<Oferta> Ofertas()
    {
        var hoy = DateTime.Now;
        return new()
        {
            new() {
                Id = 1, Titulo = "Mega oferta en lacteos",
                Descripcion = "Hasta 20% de descuento en productos seleccionados",
                Imagen = "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=800&h=300&fit=crop",
                DescuentoPorcentaje = 20, EtiquetaPromo = "20% OFF",
                FechaInicio = hoy.AddDays(-7), FechaFin = hoy.AddDays(7),
                ProductosIds = new() { 5, 13 }
            },
            new() {
                Id = 2, Titulo = "2x1 en limpieza",
                Descripcion = "Lleva dos jabones Bolivar al precio de uno",
                Imagen = "https://images.unsplash.com/photo-1583947215259-38e31be8751f?w=800&h=300&fit=crop",
                DescuentoMonto = 9.90m, EtiquetaPromo = "2x1",
                FechaInicio = hoy.AddDays(-3), FechaFin = hoy.AddDays(11),
                ProductosIds = new() { 8 }
            },
            new() {
                Id = 3, Titulo = "Combo desayuno",
                Descripcion = "Pan + leche + yogurt con descuento especial",
                Imagen = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&h=300&fit=crop",
                DescuentoPorcentaje = 15, EtiquetaPromo = "COMBO",
                FechaInicio = hoy.AddDays(-1), FechaFin = hoy.AddDays(13),
                ProductosIds = new() { 4, 5, 10 }
            }
        };
    }
}
