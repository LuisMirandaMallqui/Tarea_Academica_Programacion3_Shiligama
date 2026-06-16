-- =====================================================================
-- SHILIGAMA - Datos iniciales (catálogos y datos de prueba)
-- =====================================================================
USE shiligama;

-- =============================================================
-- DATOS MAESTROS / CATÁLOGOS
-- =============================================================

-- Métodos de pago
INSERT INTO metodo_pago(NOMBRE, DESCRIPCION) VALUES
('Efectivo', 'Pago en efectivo en tienda'),
('Yape', 'Pago mediante aplicación Yape'),
('Plin', 'Pago mediante aplicación Plin'),
('Tarjeta de débito', 'Pago con tarjeta de débito'),
('IZIPAY', 'Pago con tarjeta vía pasarela externa Izipay (modo pruebas)');

-- Categorías raíz
INSERT INTO categoria(NOMBRE, DESCRIPCION) VALUES
('Abarrotes', 'Productos de primera necesidad'),
('Bebidas', 'Bebidas con y sin alcohol'),
('Limpieza', 'Productos de limpieza del hogar'),
('Cuidado Personal', 'Productos de higiene y cuidado personal'),
('Lácteos', 'Leche, yogurt, quesos'),
('Snacks', 'Botanas, galletas, golosinas');

-- Subcategorías
INSERT INTO categoria(NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID) VALUES
('Arroz y Menestras', 'Arroz, lentejas, frejoles', 1),
('Aceites', 'Aceites vegetales y de oliva', 1),
('Fideos', 'Pastas y fideos', 1),
('Gaseosas', 'Bebidas gaseosas', 2),
('Jugos', 'Jugos y néctares', 2),
('Agua', 'Agua mineral y de mesa', 2),
('Detergentes', 'Detergentes y suavizantes', 3),
('Desinfectantes', 'Limpiadores y desinfectantes', 3);

-- =============================================================
-- DATOS DE PRUEBA
-- =============================================================

-- Administrador (contraseña: admin123 — en producción usar bcrypt)
INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES( '$2a$10$placeholder_bcrypt_hash', 'Carlos', 'García López', '12345678', '987654321', 'admin@shiligama.pe');
INSERT INTO administrador(USUARIO_ID) VALUES(1);

-- Trabajadores
INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('$2a$10$placeholder_bcrypt_hash', 'Juan', 'Pérez Quispe', '23456789', '987654322', 'jperez@shiligama.pe');
INSERT INTO trabajador(USUARIO_ID, CARGO, FECHA_INGRESO) VALUES(2, 'Cajero', '2025-01-15');

INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('$2a$10$placeholder_bcrypt_hash', 'María', 'Rodríguez Silva', '34567890', '987654323', 'mrodriguez@shiligama.pe');
INSERT INTO trabajador(USUARIO_ID, CARGO, FECHA_INGRESO) VALUES(3, 'Almacenero', '2025-03-01');

-- Clientes
INSERT INTO usuario( CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES( '$2a$10$placeholder_bcrypt_hash', 'Ana', 'Costa Medina', '45678901', '987654324', 'acosta@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA)
VALUES(4, 'Av. Los Pinos 300');

INSERT INTO usuario( CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES( '$2a$10$placeholder_bcrypt_hash', 'Luis', 'Huamán Torres', '56789012', '987654325', 'lhuaman@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA)
VALUES(5, 'Jr. Arequipa 150');

INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('shiligama123', 'Pedro', 'Ramos Vega',     '67890123', '987654326', 'pramos@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA) 
VALUES(6, 'Calle Lima 220');

INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('shiligama123', 'Sofía', 'Mendoza Cruz',   '78901234', '987654327', 'smendoza@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA) 
VALUES(7, 'Av. Bolognesi 45');

INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('shiligama123', 'Jorge', 'Villanueva Ríos', '89012345', '987654328', 'jvillanueva@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA) 
VALUES(8, 'Jr. Cusco 310');

-- Cliente genérico para ventas POS sin identificación (Público General / consumidor final)
INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('publico123', 'Publico', 'General', '00000000', '000000000', 'publico@shiligama.local');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA)
VALUES(9, 'Sin direccion');

-- Productos de ejemplo (con IMAGEN_URL incluida)
INSERT INTO producto(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO, STOCK, STOCK_MINIMO, UNIDAD_MEDIDA, CODIGO_BARRAS, IMAGEN_URL) VALUES
(7,  'Arroz Costeño 5kg',              'Arroz extra graneado',            22.50,  50, 10, 'Bolsa',   '7750001000001', 'https://images.unsplash.com/photo-1586201375761-83865001e31c?w=300&h=300&fit=crop'),
(7,  'Lentejas La Costeña 500g',       'Lentejas secas',                   4.50,  30,  5, 'Bolsa',   '7750001000002', 'https://images.unsplash.com/photo-1510130387422-82bed34b37e9?w=300&h=300&fit=crop'),
(8,  'Aceite Primor 1L',               'Aceite vegetal',                   9.90,  40,  8, 'Botella', '7750001000003', 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=300&h=300&fit=crop'),
(9,  'Fideos Don Vittorio Spaghetti',  'Fideos largos 500g',               3.80,  60, 10, 'Paquete', '7750001000004', 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=300&h=300&fit=crop'),
(10, 'Coca-Cola 1.5L',                 'Gaseosa',                          7.50, 100, 20, 'Botella', '7750001000005', 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&h=300&fit=crop'),
(11, 'Frugos Durazno 1L',              'Néctar de durazno',                4.20,  45, 10, 'Caja',    '7750001000006', 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=300&h=300&fit=crop'),
(12, 'San Luis sin gas 625ml',         'Agua mineral',                     2.00,  80, 15, 'Botella', '7750001000007', 'https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=300&h=300&fit=crop'),
(13, 'Ariel 2kg',                      'Detergente en polvo',             18.90,  25,  5, 'Bolsa',   '7750001000008', 'https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=300&h=300&fit=crop'),
(14, 'Poett Lavanda 900ml',            'Limpiador desinfectante',          8.50,  35,  8, 'Botella', '7750001000009', 'https://images.unsplash.com/photo-1585421514738-01798e348b17?w=300&h=300&fit=crop'),
(1,  'Azúcar Rubia 1kg',               'Azúcar rubia',                     4.20,  55, 10, 'Bolsa',   '7750001000010', 'https://images.unsplash.com/photo-1621939514649-280e2ee25f60?w=300&h=300&fit=crop'),
(5,  'Leche Gloria Entera 1L',         'Leche evaporada entera',           4.80,  60, 12, 'Caja',    '7750001000011', 'https://images.unsplash.com/photo-1563636619-e9143da7973b?w=300&h=300&fit=crop'),
(5,  'Yogurt Gloria Fresa 1kg',        'Yogurt batido sabor fresa',        8.50,  40,  8, 'Tarro',   '7750001000012', 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=300&h=300&fit=crop'),
(6,  'Pringles Original 149g',         'Papas fritas en lata',            12.90,  35,  5, 'Lata',    '7750001000013', 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=300&h=300&fit=crop'),
(6,  'Galletas Oreo Original 176g',    'Galletas de chocolate',            6.50,  50, 10, 'Paquete', '7750001000014', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=300&h=300&fit=crop'),
(1,  'Sal Marina 1kg',                 'Sal fina de mesa',                 2.20,  70, 15, 'Bolsa',   '7750001000015', 'https://images.unsplash.com/photo-1619280984807-a27b4d7f0a72?w=300&h=300&fit=crop'),
(1,  'Atún Florida en Aceite 170g',    'Conserva de atún',                 4.90,  45, 10, 'Lata',    '7750001000016', 'https://images.unsplash.com/photo-1534482421-64566f976cfa?w=300&h=300&fit=crop'),
(10, 'Inca Kola 1.5L',                 'Gaseosa nacional',                 7.50,  80, 20, 'Botella', '7750001000017', 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&h=300&fit=crop'),
(10, 'Pepsi 500ml',                    'Gaseosa cola',                     3.50,  90, 20, 'Botella', '7750001000018', 'https://images.unsplash.com/photo-1629203432180-71e9f76a3e57?w=300&h=300&fit=crop'),
(11, 'Néctar Pulp Mango 1L',           'Néctar de mango',                  4.20,  40, 10, 'Caja',    '7750001000019', 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=300&h=300&fit=crop'),
(12, 'Agua San Mateo 625ml',           'Agua mineral con gas',             2.50, 100, 20, 'Botella', '7750001000020', 'https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=300&h=300&fit=crop'),
(4,  'Shampoo Head & Shoulders 200ml', 'Shampoo anticaspa',               14.90,  25,  5, 'Frasco',  '7750001000021', 'https://images.unsplash.com/photo-1522916819787-2605d47a50d1?w=300&h=300&fit=crop'),
(4,  'Jabón Dove 90g x3',              'Jabón de tocador',                 9.80,  30,  8, 'Pack',    '7750001000022', 'https://images.unsplash.com/photo-1600857544200-b2f666a9a2ec?w=300&h=300&fit=crop'),
(13, 'Bolsa Basura Grande x10',        'Bolsas para basura 70L',           5.90,  40, 10, 'Pack',    '7750001000023', 'https://images.unsplash.com/photo-1558449028-b53a39d100fc?w=300&h=300&fit=crop'),
(8,  'Aceite Vegetal Ideal 900ml',     'Aceite para cocinar',              8.50,  35,  8, 'Botella', '7750001000024', 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=300&h=300&fit=crop'),
(9,  'Fideos Tallarin N°5 500g',       'Fideos gruesos',                   3.20,  55, 12, 'Paquete', '7750001000025', 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=300&h=300&fit=crop');

-- ================================================================
-- PROMOCIONES
-- ================================================================
INSERT INTO promocion(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO, FECHA_INICIO, FECHA_FIN, CONDICIONES)
VALUES('Semana del Abarrote', 'Descuento en abarrotes seleccionados', 'PORCENTAJE', 10.00, CURDATE(), CURDATE() + INTERVAL 60 DAY, 'Aplica en productos seleccionados');
INSERT INTO promocion_producto(PROMOCION_ID, PRODUCTO_ID) VALUES (1, 1), (1, 2), (1, 4);

INSERT INTO promocion(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO, FECHA_INICIO, FECHA_FIN, CONDICIONES)
VALUES('Combo Bebidas',  '2x1 en gaseosas seleccionadas', 'PORCENTAJE', 50.00, CURDATE(), CURDATE() + INTERVAL 60 DAY, 'Solo para ventas presenciales');
INSERT INTO promocion_producto(PROMOCION_ID, PRODUCTO_ID) VALUES (2, 5), (2, 17), (2, 18);

INSERT INTO promocion(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO, FECHA_INICIO, FECHA_FIN, CONDICIONES)
VALUES('Descuento Lácteos', '15% en productos lácteos', 'PORCENTAJE', 15.00, CURDATE(), CURDATE() + INTERVAL 60 DAY, 'Aplica en leche y yogurt');
INSERT INTO promocion_producto(PROMOCION_ID, PRODUCTO_ID) VALUES (3, 11), (3, 12);
-- ================================================================
--    VENTAS DE PRUEBA (trabajador 2 = Juan, trabajador 3 = María)
--    Clientes: 4=Ana, 5=Luis, 6=Pedro
-- ================================================================

-- Venta 1: Presencial, Ana, Juan cajero, Yape
INSERT INTO venta(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID, FECHA_HORA, MONTO_TOTAL, CANAL_VENTA, ESTADO_VENTA)
VALUES(4, 2, 2, NOW() - INTERVAL 3 DAY, 39.60, 'PRESENCIAL', 'COMPLETADA');
INSERT INTO detalle_venta(VENTA_ID, PRODUCTO_ID, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL)
VALUES(1, 1, 22.50, 1, 22.50),
      (1, 5, 7.50,  1, 7.50),
      (1, 2, 4.50,  2, 9.00),
      (1, 7, 2.00,  1, 2.00);  -- 22.50+7.50+9.00+2.00 = 41.00 (ajuste leve)

-- Venta 2: Presencial, Luis, Juan cajero, Efectivo
INSERT INTO venta(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID, FECHA_HORA, MONTO_TOTAL, CANAL_VENTA, ESTADO_VENTA)
VALUES(5, 2, 1, NOW() - INTERVAL 2 DAY, 56.40, 'PRESENCIAL', 'COMPLETADA');
INSERT INTO detalle_venta(VENTA_ID, PRODUCTO_ID, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL)
VALUES(2, 8, 18.90, 1, 18.90),
      (2, 3, 9.90,  2, 19.80),
      (2, 9, 8.50,  2, 17.00);

-- Venta 3: Web, Pedro, María almacenera, Tarjeta
INSERT INTO venta(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID, FECHA_HORA, MONTO_TOTAL, CANAL_VENTA, ESTADO_VENTA)
VALUES(6, 3, 4, NOW() - INTERVAL 1 DAY, 34.00, 'WEB', 'COMPLETADA');
INSERT INTO detalle_venta(VENTA_ID, PRODUCTO_ID, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL)
VALUES(3, 11, 4.80,  2, 9.60),
      (3, 12, 8.50,  1, 8.50),
      (3, 13, 12.90, 1, 12.90);

-- Venta 4: Presencial, sin cliente asignado (público general), Efectivo
INSERT INTO venta(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID, FECHA_HORA, MONTO_TOTAL, CANAL_VENTA, ESTADO_VENTA)
VALUES(NULL, 2, 1, NOW() - INTERVAL 5 HOUR, 18.50, 'PRESENCIAL', 'COMPLETADA');
INSERT INTO detalle_venta(VENTA_ID, PRODUCTO_ID, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL)
VALUES(4, 14, 6.50, 2, 13.00),
      (4, 5,  2.20, 2,  4.40),
      (4, 7,  2.00, 1,  2.00);  -- aprox

-- Venta 5: Presencial hoy, Yape
INSERT INTO venta(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID, FECHA_HORA, MONTO_TOTAL, CANAL_VENTA, ESTADO_VENTA)
VALUES(4, 2, 3, NOW() - INTERVAL 2 HOUR, 46.70, 'PRESENCIAL', 'COMPLETADA');
INSERT INTO detalle_venta(VENTA_ID, PRODUCTO_ID, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL)
VALUES(5, 1, 22.50, 1, 22.50),
      (5, 4, 3.80,  3, 11.40),
      (5, 6, 4.20,  3, 12.60);

-- ================================================================
--    PEDIDOS DE PRUEBA (estado variado para el dashboard trabajador)
-- ================================================================

-- Pedido 1: Ana – delivery – pendiente
INSERT INTO pedido(CLIENTE_ID, FECHA_HORA, MONTO_TOTAL, ESTADO_PEDIDO, DIRECCION_ENTREGA, MODALIDAD_ENTREGA)
VALUES(4, NOW() - INTERVAL 45 MINUTE, 44.40, 'RECIBIDO', 'Av. Los Pinos 300, San Isidro', 'DELIVERY');
INSERT INTO detalle_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES(1, 1, 1, 22.50, 22.50),
      (1, 5, 2, 7.50,  15.00),
      (1, 7, 2, 2.00,   4.00);  -- aprox 41.50

-- Pedido 2: Luis – recojo – en proceso
INSERT INTO pedido(CLIENTE_ID, FECHA_HORA, MONTO_TOTAL, ESTADO_PEDIDO, MODALIDAD_ENTREGA)
VALUES(5, NOW() - INTERVAL 2 HOUR, 29.60, 'EN_PROCESO', 'RECOJO_TIENDA');
INSERT INTO detalle_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES(2, 3, 2, 9.90,  19.80),
      (2, 6, 1, 4.20,   4.20),
      (2, 7, 2, 2.00,   4.00),
      (2, 2, 1, 4.50,   4.50);  -- aprox 32.50

-- Pedido 3: Pedro – delivery – atendido (ya entregado)
INSERT INTO pedido(CLIENTE_ID, FECHA_HORA, MONTO_TOTAL, ESTADO_PEDIDO, DIRECCION_ENTREGA, MODALIDAD_ENTREGA)
VALUES(6, NOW() - INTERVAL 1 DAY, 53.90, 'ATENDIDO', 'Calle Lima 220, Miraflores', 'DELIVERY');
INSERT INTO detalle_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES(3, 8, 2, 18.90, 37.80),
      (3, 4, 4, 3.80,  15.20);  -- aprox 53.00

-- Pedido 4: Ana – delivery – recibido hoy (reciente)
INSERT INTO pedido(CLIENTE_ID, FECHA_HORA, MONTO_TOTAL, ESTADO_PEDIDO, DIRECCION_ENTREGA, MODALIDAD_ENTREGA)
VALUES(4, NOW() - INTERVAL 15 MINUTE, 25.10, 'RECIBIDO', 'Av. Los Pinos 300, San Isidro', 'DELIVERY');
INSERT INTO detalle_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES(4, 11, 3, 4.80, 14.40),
      (4, 17, 1, 7.50,  7.50),
      (4, 7,  1, 2.00,  2.00);  -- aprox 23.90

-- Pedido 5: Jorge (cliente 8) – recojo – cancelado
INSERT INTO pedido(CLIENTE_ID, FECHA_HORA, MONTO_TOTAL, ESTADO_PEDIDO, MODALIDAD_ENTREGA, OBSERVACIONES)
VALUES(8, NOW() - INTERVAL 3 HOUR, 18.90, 'CANCELADO', 'RECOJO_TIENDA', 'Cliente no recogió en plazo');
INSERT INTO detalle_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
VALUES(5, 8, 1, 18.90, 18.90);

-- ================================================================
--    DEVOLUCIONES DE PRUEBA
-- ================================================================
INSERT INTO devolucion(PRODUCTO_ID, TRABAJADOR_ID, ESTADO_DEVOLUCION, CANTIDAD, MOTIVO)
VALUES
(1, 2, 'APROBADO',  2, 'Producto vencido, empaque roto'),
(3, 3, 'PENDIENTE', 1, 'Botella abollada, posible contaminación'),
(5, 2, 'APROBADO',  3, 'Error de pedido: se entregó producto incorrecto'),
(8, 3, 'RECHAZADO', 1, 'Uso incorrecto por parte del cliente'),
(11,2, 'PENDIENTE', 4, 'Fecha de vencimiento próxima');

-- ================================================================
--    MOVIMIENTOS DE INVENTARIO
-- ================================================================
INSERT INTO movimiento_inventario(PRODUCTO_ID, TRABAJADOR_ID, TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO)
VALUES
(1, 2, 'ENTRADA',    20, 30, 50, 'Reabastecimiento semanal'),
(5, 3, 'ENTRADA',    50, 50, 100,'Compra proveedor Inca Kola'),
(8, 2, 'SALIDA',     5,  30, 25, 'Venta presencial'),
(3, 3, 'AJUSTE',     2,  38, 40, 'Corrección de inventario físico'),
(1, 2, 'DEVOLUCION', 2,  48, 50, 'Producto vencido devuelto al proveedor'),
(11,3, 'ENTRADA',    20, 40, 60, 'Reabastecimiento leche'),
(13,2, 'SALIDA',     3,  53, 50, 'Venta online procesada');

-- ================================================================
--    NOTIFICACIONES DE PRUEBA
--    ID_DESTINATARIO NULL = broadcast para todos los admins/trabajadores
-- ================================================================
INSERT INTO notificacion(TITULO, MENSAJE, TIPO, LEIDA, ID_DESTINATARIO) VALUES
('Stock crítico: Inca Kola 1.5L',
 'El producto "Inca Kola 1.5L" (PROD-017) tiene 0 unidades en stock. Requiere reabastecimiento urgente.',
 'STOCK_BAJO', 0, NULL),

('Nuevo pedido recibido',
 'El cliente Ana Costa (ID-4) realizó el pedido PED-0001 por S/. 44.40. Modalidad: Delivery a Av. Los Pinos 300.',
 'NUEVO_PEDIDO', 0, NULL),

('Pedido listo para despacho',
 'El pedido PED-0002 de Luis Huamán está listo para recojo en tienda. Estado: EN_PROCESO.',
 'PEDIDO_LISTO', 1, NULL),

('Solicitud de devolución pendiente',
 'El trabajador Juan Pérez registró una devolución de 2 unidades de "Arroz Costeño 5kg". Pendiente de aprobación.',
 'DEVOLUCION_PENDIENTE', 0, NULL),

('Stock bajo: Aceite Primor 1L',
 'El producto "Aceite Primor 1L" (PROD-003) tiene solo 8 unidades, por debajo del mínimo establecido (8).',
 'STOCK_BAJO', 1, NULL),

('Venta registrada: VTA-003',
 'Se registró la venta VTA-003 por S/. 34.00 (canal WEB) del cliente Pedro Ramos.',
 'VENTA_REGISTRADA', 1, NULL),

('Pedido cancelado',
 'El pedido PED-0005 de Jorge Villanueva fue cancelado. Motivo: Cliente no recogió en plazo.',
 'SISTEMA', 0, NULL);


-- ================================================================
-- INSERTS DE PRUEBA - DEVOLUCIONES
-- ================================================================

-- Devolución 1: Vencido - Arroz (2)
CALL INSERTAR_DEVOLUCION(@dev1, 1, NULL, 2, 'PENDIENTE', 2, 'Vencido', NOW());
CALL INSERTAR_DETALLE_DEVOLUCION(@dev1, 1, 2);

-- Devolución 2: Dañado - Aceite (1)
CALL INSERTAR_DEVOLUCION(@dev2, 3, NULL, 2, 'PENDIENTE', 1, 'Dañado', NOW());
CALL INSERTAR_DETALLE_DEVOLUCION(@dev2, 3, 1);

-- Devolución 3: Otro - Coca-Cola (3)
CALL INSERTAR_DEVOLUCION(@dev3, 5, NULL, 2, 'PENDIENTE', 3, 'Otro', NOW());
CALL INSERTAR_DETALLE_DEVOLUCION(@dev3, 5, 3);

-- Devolución 4: Dañado - Ariel (1)
CALL INSERTAR_DEVOLUCION(@dev4, 8, NULL, 2, 'RECHAZADO', 1, 'Dañado', NOW());
CALL INSERTAR_DETALLE_DEVOLUCION(@dev4, 8, 1);

-- Devolución 5: Vencido - Leche (4)
CALL INSERTAR_DEVOLUCION(@dev5, 11, NULL, 2, 'APROBADO', 4, 'Vencido', NOW());
CALL INSERTAR_DETALLE_DEVOLUCION(@dev5, 11, 4);

-- Devolución 6: Dañado - Múltiples (Arroz 1, Coca-Cola 2, Agua 1)
CALL INSERTAR_DEVOLUCION(@dev6, 1, 4, 2, 'PENDIENTE', 4, 'Dañado', NOW());
CALL INSERTAR_DETALLE_DEVOLUCION(@dev6, 1, 1);
CALL INSERTAR_DETALLE_DEVOLUCION(@dev6, 5, 2);
CALL INSERTAR_DETALLE_DEVOLUCION(@dev6, 7, 1);

-- Devolución 7: Otro - Múltiples (Lentejas 1, Fideos 2)
CALL INSERTAR_DEVOLUCION(@dev7, 2, 4, 3, 'PENDIENTE', 3, 'Otro', NOW());
CALL INSERTAR_DETALLE_DEVOLUCION(@dev7, 2, 1);
CALL INSERTAR_DETALLE_DEVOLUCION(@dev7, 4, 2);

-- ================================================================
-- INSERTS DE DE USUARIO GENERAL PARA LA BOLETA
-- ================================================================

INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
SELECT 'publico123', 'Publico', 'General', '00000000', '000000000', 'publico@shiligama.local'
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE DNI = '00000000');

INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA)
SELECT u.USUARIO_ID, 'Sin direccion'
FROM usuario u
WHERE u.DNI = '00000000'
  AND NOT EXISTS (SELECT 1 FROM cliente c WHERE c.USUARIO_ID = u.USUARIO_ID);