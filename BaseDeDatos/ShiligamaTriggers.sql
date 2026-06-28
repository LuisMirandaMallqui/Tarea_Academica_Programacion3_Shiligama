-- =====================================================================
-- SHILIGAMA - Triggers de Auditoria (ACTUALIZADO A SINGULAR)
-- =====================================================================

USE `shiligama`;

DELIMITER $$

-- =============================================================
-- USUARIO
-- =============================================================
CREATE TRIGGER trg_usuario_before_insert
BEFORE INSERT ON usuario FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_usuario_before_update
BEFORE UPDATE ON usuario FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- CLIENTE
-- =============================================================
CREATE TRIGGER trg_cliente_before_insert
BEFORE INSERT ON cliente FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_cliente_before_update
BEFORE UPDATE ON cliente FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- ADMINISTRADOR
-- =============================================================
CREATE TRIGGER trg_administrador_before_insert
BEFORE INSERT ON administrador FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_administrador_before_update
BEFORE UPDATE ON administrador FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- TRABAJADOR
-- =============================================================
CREATE TRIGGER trg_trabajador_before_insert
BEFORE INSERT ON trabajador FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_trabajador_before_update
BEFORE UPDATE ON trabajador FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- CATEGORIA
-- =============================================================
CREATE TRIGGER trg_categoria_before_insert
BEFORE INSERT ON categoria FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_categoria_before_update
BEFORE UPDATE ON categoria FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- PRODUCTO
-- =============================================================
CREATE TRIGGER trg_producto_before_insert
BEFORE INSERT ON producto FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_producto_before_update
BEFORE UPDATE ON producto FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- PROMOCION
-- =============================================================
CREATE TRIGGER trg_promocion_before_insert
BEFORE INSERT ON promocion FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_promocion_before_update
BEFORE UPDATE ON promocion FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- METODO_PAGO
-- =============================================================
CREATE TRIGGER trg_metodo_pago_before_insert
BEFORE INSERT ON metodo_pago FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_metodo_pago_before_update
BEFORE UPDATE ON metodo_pago FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- PEDIDO 
-- =============================================================
CREATE TRIGGER trg_pedido_before_insert
BEFORE INSERT ON pedido FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_pedido_before_update
BEFORE UPDATE ON pedido FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- VENTA - Trigger para actualizar monto cuando se completa
-- =============================================================
CREATE TRIGGER trg_venta_before_insert
BEFORE INSERT ON venta FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_venta_before_update
BEFORE UPDATE ON venta FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- MOVIMIENTO_INVENTARIO
-- =============================================================
CREATE TRIGGER trg_movimiento_inventario_before_insert
BEFORE INSERT ON movimiento_inventario FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

-- =============================================================
-- DEVOLUCION
-- =============================================================
CREATE TRIGGER trg_devolucion_before_insert
BEFORE INSERT ON devolucion FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_devolucion_before_update
BEFORE UPDATE ON devolucion FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- LOTE
-- =============================================================
CREATE TRIGGER trg_lote_before_insert
BEFORE INSERT ON lote FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_lote_before_update
BEFORE UPDATE ON lote FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

DELIMITER ;