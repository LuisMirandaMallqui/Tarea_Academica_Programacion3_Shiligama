USE shiligama;

-- =============================================================
-- 1. Insertar categoría (si no existe)
-- =============================================================
INSERT IGNORE INTO categorias(CATEGORIA_ID, NOMBRE, ACTIVO)
VALUES(1, 'General', 1);

-- =============================================================
-- 2. Insertar usuario (para trabajador)
-- =============================================================
INSERT IGNORE INTO usuarios(USUARIO_ID, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA, ACTIVO)
VALUES(1, 'Trabajador', 'Test', '12345678', '987654321', 'trabajador@test.com', '123456', 1);

-- =============================================================
-- 3. Insertar trabajador (usa USUARIO_ID = 1)
-- =============================================================
INSERT IGNORE INTO trabajadores(TRABAJADOR_ID, USUARIO_ID, CARGO, ACTIVO)
VALUES(1, 1, 'Cajero', 1);

-- =============================================================
-- 4. Insertar producto (usa CATEGORIA_ID = 1)
-- =============================================================
INSERT IGNORE INTO productos(PRODUCTO_ID, CATEGORIA_ID, NOMBRE, PRECIO_UNITARIO, STOCK, ACTIVO)
VALUES(1, 1, 'Producto Test', 25.50, 100, 1);

-- =============================================================
-- 5. PROBAR DEVOLUCIÓN (usa PRODUCTO_ID=1, TRABAJADOR_ID=1)
-- =============================================================
CALL INSERTAR_DEVOLUCION(
    @devolucion_id,
    1,               -- _id_producto (el que acabamos de insertar)
    1,               -- _id_trabajador (el que acabamos de insertar)
    'APROBADO',
    1,
    'Prueba de devolución',
    NOW()
);

-- =============================================================
-- 6. VERIFICAR
-- =============================================================
SELECT @devolucion_id AS DEVOLUCION_ID_GENERADA;
SELECT * FROM devoluciones WHERE DEVOLUCION_ID = @devolucion_id;
SHOW CREATE PROCEDURE BUSCAR_PEDIDO_POR_ID;