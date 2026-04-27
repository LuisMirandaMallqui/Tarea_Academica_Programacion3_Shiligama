-- =====================================================================
-- SHILIGAMA - Triggers de Auditoria
-- Completan USUARIO_CREACION / USUARIO_MODIFICACION en cada tabla.
-- Las fechas (FECHA_CREACION, FECHA_MODIFICACION) ya se manejan
-- automaticamente con DEFAULT CURRENT_TIMESTAMP y ON UPDATE CURRENT_TIMESTAMP.
-- Se usa USER() para registrar el usuario de la conexion MySQL.
-- =====================================================================
USE `Proyecto_prueba`;

DELIMITER $$

-- =============================================================
-- USUARIOS
-- =============================================================
CREATE TRIGGER trg_usuarios_before_insert
BEFORE INSERT ON usuarios FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_usuarios_before_update
BEFORE UPDATE ON usuarios FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- CLIENTES
-- =============================================================
CREATE TRIGGER trg_clientes_before_insert
BEFORE INSERT ON clientes FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_clientes_before_update
BEFORE UPDATE ON clientes FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- ADMINISTRADORES
-- =============================================================
CREATE TRIGGER trg_administradores_before_insert
BEFORE INSERT ON administradores FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_administradores_before_update
BEFORE UPDATE ON administradores FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- TRABAJADORES
-- =============================================================
CREATE TRIGGER trg_trabajadores_before_insert
BEFORE INSERT ON trabajadores FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_trabajadores_before_update
BEFORE UPDATE ON trabajadores FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- CATEGORIAS
-- =============================================================
CREATE TRIGGER trg_categorias_before_insert
BEFORE INSERT ON categorias FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_categorias_before_update
BEFORE UPDATE ON categorias FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- PRODUCTOS
-- =============================================================
CREATE TRIGGER trg_productos_before_insert
BEFORE INSERT ON productos FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_productos_before_update
BEFORE UPDATE ON productos FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- PROMOCIONES
-- =============================================================
CREATE TRIGGER trg_promociones_before_insert
BEFORE INSERT ON promociones FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_promociones_before_update
BEFORE UPDATE ON promociones FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- METODOS DE PAGO
-- =============================================================
CREATE TRIGGER trg_metodos_pago_before_insert
BEFORE INSERT ON metodos_pago FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_metodos_pago_before_update
BEFORE UPDATE ON metodos_pago FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- VENTAS
-- =============================================================
CREATE TRIGGER trg_ventas_before_insert
BEFORE INSERT ON ventas FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_ventas_before_update
BEFORE UPDATE ON ventas FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- PEDIDOS
-- =============================================================
CREATE TRIGGER trg_pedidos_before_insert
BEFORE INSERT ON pedidos FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_pedidos_before_update
BEFORE UPDATE ON pedidos FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- DEVOLUCIONES
-- =============================================================
CREATE TRIGGER trg_devoluciones_before_insert
BEFORE INSERT ON devoluciones FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

CREATE TRIGGER trg_devoluciones_before_update
BEFORE UPDATE ON devoluciones FOR EACH ROW
BEGIN
    SET NEW.USUARIO_MODIFICACION = USER();
END$$

-- =============================================================
-- MOVIMIENTOS DE INVENTARIO (log inmutable: solo INSERT)
-- =============================================================
CREATE TRIGGER trg_movimientos_before_insert
BEFORE INSERT ON movimientos_inventario FOR EACH ROW
BEGIN
    SET NEW.USUARIO_CREACION = USER();
END$$

DELIMITER ;
