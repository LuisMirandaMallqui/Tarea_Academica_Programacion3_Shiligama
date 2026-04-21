-- =====================================================================
-- SHILIGAMA - Triggers
-- Auditoría automática + lógica de negocio
-- =====================================================================
USE `shiligama`;

DELIMITER $$

-- =============================================================
-- TRIGGERS DE AUDITORÍA (RNF014)
-- Registran operaciones INSERT/UPDATE en tabla auditoria
-- =============================================================

-- ---- USUARIOS ----
CREATE TRIGGER trg_usuarios_after_insert
AFTER INSERT ON usuarios FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_DESPUES)
    VALUES('usuarios', 'INSERT', NEW.USUARIO_ID,
        JSON_OBJECT('nombre_usuario', NEW.NOMBRE_USUARIO,
                    'nombres', NEW.NOMBRES,
                    'apellidos', NEW.APELLIDOS,
                    'dni', NEW.DNI,
                    'email', NEW.EMAIL));
END$$

CREATE TRIGGER trg_usuarios_after_update
AFTER UPDATE ON usuarios FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_ANTES, DATOS_DESPUES)
    VALUES('usuarios', 'UPDATE', NEW.USUARIO_ID,
        JSON_OBJECT('nombres', OLD.NOMBRES, 'apellidos', OLD.APELLIDOS,
                    'email', OLD.EMAIL, 'activo', OLD.ACTIVO),
        JSON_OBJECT('nombres', NEW.NOMBRES, 'apellidos', NEW.APELLIDOS,
                    'email', NEW.EMAIL, 'activo', NEW.ACTIVO));
END$$

-- ---- PRODUCTOS ----
CREATE TRIGGER trg_productos_after_insert
AFTER INSERT ON productos FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_DESPUES)
    VALUES('productos', 'INSERT', NEW.PRODUCTO_ID,
        JSON_OBJECT('nombre', NEW.NOMBRE, 'precio', NEW.PRECIO_UNITARIO,
                    'stock', NEW.STOCK, 'categoria_id', NEW.CATEGORIA_ID));
END$$

CREATE TRIGGER trg_productos_after_update
AFTER UPDATE ON productos FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_ANTES, DATOS_DESPUES)
    VALUES('productos', 'UPDATE', NEW.PRODUCTO_ID,
        JSON_OBJECT('nombre', OLD.NOMBRE, 'precio', OLD.PRECIO_UNITARIO,
                    'stock', OLD.STOCK, 'activo', OLD.ACTIVO),
        JSON_OBJECT('nombre', NEW.NOMBRE, 'precio', NEW.PRECIO_UNITARIO,
                    'stock', NEW.STOCK, 'activo', NEW.ACTIVO));
END$$

-- ---- VENTAS ----
CREATE TRIGGER trg_ventas_after_insert
AFTER INSERT ON ventas FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_DESPUES)
    VALUES('ventas', 'INSERT', NEW.VENTA_ID,
        JSON_OBJECT('cliente_id', NEW.CLIENTE_ID, 'trabajador_id', NEW.TRABAJADOR_ID,
                    'canal', NEW.CANAL_VENTA, 'estado', NEW.ESTADO_VENTA));
END$$

CREATE TRIGGER trg_ventas_after_update
AFTER UPDATE ON ventas FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_ANTES, DATOS_DESPUES)
    VALUES('ventas', 'UPDATE', NEW.VENTA_ID,
        JSON_OBJECT('monto_total', OLD.MONTO_TOTAL, 'estado', OLD.ESTADO_VENTA),
        JSON_OBJECT('monto_total', NEW.MONTO_TOTAL, 'estado', NEW.ESTADO_VENTA));
END$$

-- ---- PEDIDOS ----
CREATE TRIGGER trg_pedidos_after_insert
AFTER INSERT ON pedidos FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_DESPUES)
    VALUES('pedidos', 'INSERT', NEW.PEDIDO_ID,
        JSON_OBJECT('cliente_id', NEW.CLIENTE_ID, 'estado', NEW.ESTADO_PEDIDO,
                    'prioridad', NEW.PRIORIDAD));
END$$

CREATE TRIGGER trg_pedidos_after_update
AFTER UPDATE ON pedidos FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_ANTES, DATOS_DESPUES)
    VALUES('pedidos', 'UPDATE', NEW.PEDIDO_ID,
        JSON_OBJECT('estado', OLD.ESTADO_PEDIDO, 'monto_total', OLD.MONTO_TOTAL),
        JSON_OBJECT('estado', NEW.ESTADO_PEDIDO, 'monto_total', NEW.MONTO_TOTAL));
END$$

-- ---- ORDENES DE REABASTECIMIENTO ----
CREATE TRIGGER trg_ordenes_after_update
AFTER UPDATE ON ordenes_reabastecimiento FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_ANTES, DATOS_DESPUES)
    VALUES('ordenes_reabastecimiento', 'UPDATE', NEW.ORDEN_ID,
        JSON_OBJECT('estado', OLD.ESTADO_ORDEN),
        JSON_OBJECT('estado', NEW.ESTADO_ORDEN));
END$$

-- ---- DEVOLUCIONES ----
CREATE TRIGGER trg_devoluciones_after_insert
AFTER INSERT ON devoluciones FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_DESPUES)
    VALUES('devoluciones', 'INSERT', NEW.DEVOLUCION_ID,
        JSON_OBJECT('producto_id', NEW.PRODUCTO_ID,
                    'estado', NEW.ESTADO_DEVOLUCION,
                    'cantidad', NEW.CANTIDAD,
                    'motivo', NEW.MOTIVO));
END$$

-- =============================================================
-- TRIGGERS DE LÓGICA DE NEGOCIO
-- =============================================================

-- Cuando se registra un movimiento de inventario tipo ENTRADA
-- (por recepción de orden de reabastecimiento), registrar en auditoría
CREATE TRIGGER trg_movimientos_after_insert
AFTER INSERT ON movimientos_inventario FOR EACH ROW
BEGIN
    INSERT INTO auditoria(TABLA, OPERACION, REGISTRO_ID, DATOS_DESPUES)
    VALUES('movimientos_inventario', 'INSERT', NEW.MOVIMIENTO_ID,
        JSON_OBJECT('producto_id', NEW.PRODUCTO_ID,
                    'tipo', NEW.TIPO_MOVIMIENTO,
                    'cantidad', NEW.CANTIDAD,
                    'stock_anterior', NEW.STOCK_ANTERIOR,
                    'stock_resultante', NEW.STOCK_RESULTANTE));
END$$

DELIMITER ;
