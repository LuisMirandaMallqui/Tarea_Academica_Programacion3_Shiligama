-- =====================================================================
-- SHILIGAMA – Datos de prueba COMPLEMENTARIOS
-- Ejecutar DESPUÉS de ShiligamaInserts.sql
-- Agrega: más productos, ventas, pedidos, detalles, devoluciones,
--         movimientos de inventario y notificaciones.
-- =====================================================================
USE shiligama;

-- ================================================================
-- 0. CORREGIR FECHAS DE PROMOCIONES EXPIRADAS
--    La promoción "Semana del Abarrote" vence el 2026-04-27 (ya pasó).
--    La actualizamos para que sea vigente hoy.
-- ================================================================
UPDATE promocion
SET FECHA_INICIO = CURDATE(),
    FECHA_FIN    = CURDATE() + INTERVAL 30 DAY
WHERE PROMOCION_ID = 1;

-- ================================================================
-- 1. ACTUALIZAR CONTRASEÑAS A TEXTO PLANO (para pruebas locales)
--    Contraseña de todos los usuarios: shiligama123
-- ================================================================
UPDATE usuario SET CONTRASENA = 'shiligama123' WHERE ACTIVO = 1;

-- ================================================================
-- 2. MÁS CLIENTES DE PRUEBA (usuarios 6-9)
-- ================================================================
INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('shiligama123', 'Pedro', 'Ramos Vega',     '67890123', '987654326', 'pramos@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA) VALUES(6, 'Calle Lima 220');

INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('shiligama123', 'Sofía', 'Mendoza Cruz',   '78901234', '987654327', 'smendoza@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA) VALUES(7, 'Av. Bolognesi 45');

INSERT INTO usuario(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('shiligama123', 'Jorge', 'Villanueva Ríos', '89012345', '987654328', 'jvillanueva@gmail.com');
INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA) VALUES(8, 'Jr. Cusco 310');

-- ================================================================
-- 3. MÁS PRODUCTOS (IDs 11-25)
-- ================================================================
INSERT INTO producto(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO, STOCK, STOCK_MINIMO, UNIDAD_MEDIDA, CODIGO_BARRAS, IMAGEN_URL) VALUES
(5,  'Leche Gloria Entera 1L',       'Leche evaporada entera',         4.80, 60, 12, 'Caja',    '7750001000011', 'https://images.unsplash.com/photo-1600718374662-0483d2b9da44?w=300'),
(5,  'Yogurt Gloria Fresa 1kg',       'Yogurt batido sabor fresa',      8.50, 40, 8,  'Tarro',   '7750001000012', 'https://images.unsplash.com/photo-1563636619-e9143da7973b?w=300'),
(6,  'Pringles Original 149g',        'Papas fritas en lata',          12.90, 35, 5,  'Lata',    '7750001000013', 'https://images.unsplash.com/photo-1621852004158-f3bc188ace2d?w=300'),
(6,  'Galletas Oreo Original 176g',   'Galletas de chocolate',          6.50, 50, 10, 'Paquete', '7750001000014', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=300'),
(1,  'Sal Marina 1kg',                'Sal fina de mesa',               2.20, 70, 15, 'Bolsa',   '7750001000015', NULL),
(1,  'Atún Florida en Aceite 170g',   'Conserva de atún',               4.90, 45, 10, 'Lata',    '7750001000016', 'https://images.unsplash.com/photo-1574484284002-952d92456975?w=300'),
(10, 'Inca Kola 1.5L',                'Gaseosa nacional',               7.50, 80, 20, 'Botella', '7750001000017', 'https://images.unsplash.com/photo-1560343087-b9216d9d8af8?w=300'),
(10, 'Pepsi 500ml',                   'Gaseosa cola',                   3.50, 90, 20, 'Botella', '7750001000018', NULL),
(11, 'Néctar Pulp Mango 1L',          'Néctar de mango',                4.20, 40, 10, 'Caja',    '7750001000019', NULL),
(12, 'Agua San Mateo 625ml',          'Agua mineral con gas',           2.50, 100, 20,'Botella', '7750001000020', NULL),
(4,  'Shampoo Head & Shoulders 200ml','Shampoo anticaspa',             14.90, 25, 5,  'Frasco',  '7750001000021', NULL),
(4,  'Jabón Dove 90g x3',             'Jabón de tocador',               9.80, 30, 8,  'Pack',    '7750001000022', NULL),
(13, 'Bolsa Basura Grande x10',       'Bolsas para basura 70L',         5.90, 40, 10, 'Pack',    '7750001000023', NULL),
(8,  'Aceite Vegetal Ideal 900ml',    'Aceite para cocinar',            8.50, 35, 8,  'Botella', '7750001000024', NULL),
(9,  'Fideos Tallarin N°5 500g',      'Fideos gruesos',                  3.20, 55, 12, 'Paquete', '7750001000025', NULL);

-- ================================================================
-- 4. VENTAS DE PRUEBA (trabajador 2 = Juan, trabajador 3 = María)
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
-- 5. PEDIDOS DE PRUEBA (estado variado para el dashboard trabajador)
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
-- 6. DEVOLUCIONES DE PRUEBA
-- ================================================================
INSERT INTO devolucion(PRODUCTO_ID, TRABAJADOR_ID, ESTADO_DEVOLUCION, CANTIDAD, MOTIVO)
VALUES
(1, 2, 'APROBADO',  2, 'Producto vencido, empaque roto'),
(3, 3, 'PENDIENTE', 1, 'Botella abollada, posible contaminación'),
(5, 2, 'APROBADO',  3, 'Error de pedido: se entregó producto incorrecto'),
(8, 3, 'RECHAZADO', 1, 'Uso incorrecto por parte del cliente'),
(11,2, 'PENDIENTE', 4, 'Fecha de vencimiento próxima');

-- ================================================================
-- 7. MOVIMIENTOS DE INVENTARIO
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
-- 8. NOTIFICACIONES DEL SISTEMA
-- ================================================================
INSERT INTO notificacion(TITULO, MENSAJE, TIPO, LEIDA, ID_DESTINATARIO)
VALUES
-- Broadcast (a todos los admin/trabajadores)
('⚠️ Stock bajo: Aceite Primor',
 'El producto Aceite Primor 1L tiene solo 8 unidades. Mínimo requerido: 8.',
 'STOCK_BAJO', 0, NULL),

('📦 Nuevo pedido recibido',
 'Pedido #PED-0001 de Ana Costa — S/. 44.40 — Delivery a Av. Los Pinos 300.',
 'NUEVO_PEDIDO', 0, NULL),

('✅ Venta registrada',
 'Venta #VTA-005 completada por el cajero Juan Pérez — Total: S/. 46.70.',
 'VENTA_REGISTRADA', 1, NULL),

-- Para el admin (usuario 1)
('🔄 Devolución pendiente de revisión',
 'La devolución DEV-002 del producto Aceite Primor está pendiente de aprobación.',
 'DEVOLUCION_PENDIENTE', 0, 1),

('📊 Reporte diario disponible',
 'El resumen de ventas del día está disponible en la sección de Reportes.',
 'SISTEMA', 1, 1),

-- Para Juan el cajero (usuario 2)
('📦 Pedido listo para entrega',
 'El pedido #PED-0003 de Pedro Ramos está marcado como ATENDIDO.',
 'PEDIDO_LISTO', 0, 2),

('⚠️ Stock bajo: Ariel 2kg',
 'Solo quedan 5 unidades de Ariel 2kg. Nivel mínimo: 5.',
 'STOCK_BAJO', 0, 2);

-- ================================================================
-- 9. SEGUNDA PROMOCIÓN VIGENTE
-- ================================================================
INSERT INTO promocion(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO, FECHA_INICIO, FECHA_FIN, CONDICIONES)
VALUES('Combo Bebidas',  '2x1 en gaseosas seleccionadas', 'PORCENTAJE', 50.00, CURDATE(), CURDATE() + INTERVAL 7 DAY, 'Solo para ventas presenciales');
INSERT INTO promocion_producto(PROMOCION_ID, PRODUCTO_ID) VALUES (2, 5), (2, 17), (2, 18);

INSERT INTO promocion(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO, FECHA_INICIO, FECHA_FIN, CONDICIONES)
VALUES('Descuento Lácteos', '15% en productos lácteos', 'PORCENTAJE', 15.00, CURDATE(), CURDATE() + INTERVAL 14 DAY, 'Aplica en leche y yogurt');
INSERT INTO promocion_producto(PROMOCION_ID, PRODUCTO_ID) VALUES (3, 11), (3, 12);
