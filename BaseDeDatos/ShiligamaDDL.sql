-- =====================================================================
-- SHILIGAMA - Sistema de Gestión de Minimarket
-- DDL: Definición de tablas (MySQL 8+)
-- Equipo: Team Script — Programación 3 (PUCP) 2026-1
-- Versión: 2.0 — Requisitos reducidos según retroalimentación Lab 03
-- =====================================================================
-- CAMBIOS vs versión anterior (nuevo2):
--   1. Se eliminó tabla alertas_stock (RF006 eliminado, demasiado complejo de probar)
--   2. Se eliminó tabla turnos (RF fuera de alcance)
--   3. Se eliminó tabla boletas (sincronización Microsoft POS eliminada)
--   4. Se simplificó pedidos (sin chatbot WhatsApp, solo web)
--   5. Se agregó tabla auditoria para trazabilidad (RNF014)
--   6. Se renumeraron los RF para coherencia
-- =====================================================================

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema shiligama
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `shiligama`;
CREATE SCHEMA IF NOT EXISTS `shiligama` DEFAULT CHARACTER SET utf8mb4;
USE `shiligama`;

-- =============================================================
-- MÓDULO 1: GESTIÓN DE USUARIOS (Herencia Table-per-Subclass)
-- RF01: Gestión de usuarios (registrar, modificar, eliminar, consultar)
-- =============================================================

CREATE TABLE IF NOT EXISTS `usuarios` (
    `USUARIO_ID`      INT          NOT NULL AUTO_INCREMENT,
    `NOMBRE_USUARIO`  VARCHAR(50)  NOT NULL,
    `CONTRASENA`      VARCHAR(255) NOT NULL,
    `NOMBRES`         VARCHAR(100) NOT NULL,
    `APELLIDOS`       VARCHAR(100) NOT NULL,
    `DNI`             VARCHAR(8)   NOT NULL,
    `TELEFONO`        VARCHAR(15)  NULL DEFAULT NULL,
    `EMAIL`           VARCHAR(100) NOT NULL,
    `DIRECCION`       VARCHAR(255) NULL DEFAULT NULL,
    `ACTIVO`          TINYINT      NOT NULL DEFAULT 1,
    `FECHA_CREACION`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`USUARIO_ID`),
    UNIQUE INDEX `uq_usuarios_nombre_usuario` (`NOMBRE_USUARIO`),
    UNIQUE INDEX `uq_usuarios_email` (`EMAIL`),
    UNIQUE INDEX `uq_usuarios_dni` (`DNI`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `clientes` (
    `CLIENTE_ID`        INT          NOT NULL AUTO_INCREMENT,
    `USUARIO_ID`        INT          NOT NULL,
    `TELEFONO_WHATSAPP` VARCHAR(15)  NULL DEFAULT NULL,
    `DIRECCION_ENTREGA` VARCHAR(255) NULL DEFAULT NULL,
    `FECHA_REGISTRO`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`CLIENTE_ID`),
    UNIQUE INDEX `uq_clientes_usuario_id` (`USUARIO_ID`),
    CONSTRAINT `fk_clientes_usuarios`
        FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `administradores` (
    `ADMINISTRADOR_ID` INT NOT NULL AUTO_INCREMENT,
    `USUARIO_ID`       INT NOT NULL,
    PRIMARY KEY (`ADMINISTRADOR_ID`),
    UNIQUE INDEX `uq_administradores_usuario_id` (`USUARIO_ID`),
    CONSTRAINT `fk_administradores_usuarios`
        FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `trabajadores` (
    `TRABAJADOR_ID` INT          NOT NULL AUTO_INCREMENT,
    `USUARIO_ID`    INT          NOT NULL,
    `CARGO`         VARCHAR(100) NULL DEFAULT NULL,
    `FECHA_INGRESO` DATE         NULL DEFAULT NULL,
    PRIMARY KEY (`TRABAJADOR_ID`),
    UNIQUE INDEX `uq_trabajadores_usuario_id` (`USUARIO_ID`),
    CONSTRAINT `fk_trabajadores_usuarios`
        FOREIGN KEY (`USUARIO_ID`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 2: GESTIÓN DE CATÁLOGO Y CATEGORÍAS
-- RF02: Gestión de categorías
-- RF03: Gestión de productos (catálogo)
-- =============================================================

CREATE TABLE IF NOT EXISTS `categorias` (
    `CATEGORIA_ID`       INT          NOT NULL AUTO_INCREMENT,
    `CATEGORIA_PADRE_ID` INT          NULL DEFAULT NULL,
    `NOMBRE`             VARCHAR(100) NOT NULL,
    `DESCRIPCION`        VARCHAR(255) NULL DEFAULT NULL,
    `ACTIVO`             TINYINT      NOT NULL DEFAULT 1,
    PRIMARY KEY (`CATEGORIA_ID`),
    INDEX `fk_categorias_padre_idx` (`CATEGORIA_PADRE_ID`),
    CONSTRAINT `fk_categorias_categorias`
        FOREIGN KEY (`CATEGORIA_PADRE_ID`) REFERENCES `categorias` (`CATEGORIA_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `productos` (
    `PRODUCTO_ID`    INT            NOT NULL AUTO_INCREMENT,
    `CATEGORIA_ID`   INT            NOT NULL,
    `NOMBRE`         VARCHAR(150)   NOT NULL,
    `DESCRIPCION`    VARCHAR(500)   NULL DEFAULT NULL,
    `PRECIO_UNITARIO` DECIMAL(10,2) NOT NULL,
    `STOCK`          INT            NOT NULL DEFAULT 0,
    `STOCK_MINIMO`   INT            NOT NULL DEFAULT 5,
    `UNIDAD_MEDIDA`  VARCHAR(30)    NULL DEFAULT NULL,
    `CODIGO_BARRAS`  VARCHAR(50)    NULL DEFAULT NULL,
    `IMAGEN_URL`     VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`         TINYINT        NOT NULL DEFAULT 1,
    `FECHA_REGISTRO` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`PRODUCTO_ID`),
    INDEX `fk_productos_categorias_idx` (`CATEGORIA_ID`),
    CONSTRAINT `fk_productos_categorias`
        FOREIGN KEY (`CATEGORIA_ID`) REFERENCES `categorias` (`CATEGORIA_ID`)
        ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 3: GESTIÓN DE INVENTARIO
-- RF04: Control de inventario y actualización de stock
-- RF05: Consulta de historial de movimientos de inventario
-- =============================================================

CREATE TABLE IF NOT EXISTS `movimientos_inventario` (
    `MOVIMIENTO_ID`    INT                                              NOT NULL AUTO_INCREMENT,
    `PRODUCTO_ID`      INT                                              NOT NULL,
    `TRABAJADOR_ID`    INT                                              NULL DEFAULT NULL,
    `TIPO_MOVIMIENTO`  ENUM('ENTRADA','SALIDA','AJUSTE','DEVOLUCION')   NOT NULL,
    `CANTIDAD`         INT                                              NOT NULL,
    `STOCK_ANTERIOR`   INT                                              NOT NULL,
    `STOCK_RESULTANTE` INT                                              NOT NULL,
    `MOTIVO`           VARCHAR(255)                                     NULL DEFAULT NULL,
    `FECHA_HORA`       DATETIME                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`MOVIMIENTO_ID`),
    INDEX `fk_movimientos_productos_idx` (`PRODUCTO_ID`),
    INDEX `fk_movimientos_trabajadores_idx` (`TRABAJADOR_ID`),
    CONSTRAINT `fk_movimientos_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_movimientos_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 4: GESTIÓN DE VENTAS
-- RF06: Gestión de métodos de pago
-- RF07: Registro de ventas (presencial)
-- =============================================================

CREATE TABLE IF NOT EXISTS `metodos_pago` (
    `METODO_PAGO_ID` INT         NOT NULL AUTO_INCREMENT,
    `NOMBRE`         VARCHAR(50) NOT NULL,
    `DESCRIPCION`    VARCHAR(255) NULL DEFAULT NULL,
    `ACTIVO`         TINYINT     NOT NULL DEFAULT 1,
    PRIMARY KEY (`METODO_PAGO_ID`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `ventas` (
    `VENTA_ID`        INT            NOT NULL AUTO_INCREMENT,
    `CLIENTE_ID`      INT            NULL DEFAULT NULL,
    `TRABAJADOR_ID`   INT            NULL DEFAULT NULL,
    `METODO_PAGO_ID`  INT            NOT NULL,
    `FECHA_HORA`      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MONTO_TOTAL`     DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    `MONTO_DESCUENTO` DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    `CANAL_VENTA`     ENUM('PRESENCIAL','WEB') NOT NULL DEFAULT 'PRESENCIAL',
    `ESTADO_VENTA`    ENUM('REGISTRADA','COMPLETADA','ANULADA') NOT NULL DEFAULT 'REGISTRADA',
    `OBSERVACIONES`   VARCHAR(500)   NULL DEFAULT NULL,
    PRIMARY KEY (`VENTA_ID`),
    INDEX `fk_ventas_clientes_idx` (`CLIENTE_ID`),
    INDEX `fk_ventas_trabajadores_idx` (`TRABAJADOR_ID`),
    INDEX `fk_ventas_metodos_pago_idx` (`METODO_PAGO_ID`),
    INDEX `idx_ventas_fecha` (`FECHA_HORA`),
    CONSTRAINT `fk_ventas_clientes`
        FOREIGN KEY (`CLIENTE_ID`) REFERENCES `clientes` (`CLIENTE_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_ventas_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_ventas_metodos_pago`
        FOREIGN KEY (`METODO_PAGO_ID`) REFERENCES `metodos_pago` (`METODO_PAGO_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `detalles_venta` (
    `DETALLE_VENTA_ID` INT            NOT NULL AUTO_INCREMENT,
    `VENTA_ID`         INT            NOT NULL,
    `PRODUCTO_ID`      INT            NOT NULL,
    `CANTIDAD`         INT            NOT NULL,
    `PRECIO_UNITARIO`  DECIMAL(10,2)  NOT NULL,
    `SUBTOTAL`         DECIMAL(10,2)  NOT NULL,
    PRIMARY KEY (`DETALLE_VENTA_ID`),
    INDEX `fk_det_venta_ventas_idx` (`VENTA_ID`),
    INDEX `fk_det_venta_productos_idx` (`PRODUCTO_ID`),
    CONSTRAINT `fk_det_venta_ventas`
        FOREIGN KEY (`VENTA_ID`) REFERENCES `ventas` (`VENTA_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_det_venta_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 5: GESTIÓN DE PEDIDOS (Portal web)
-- RF08: Gestión de pedidos del portal web
-- RF09: Verificación de stock en checkout
-- RF10: Historial de pedidos del cliente
-- =============================================================

CREATE TABLE IF NOT EXISTS `pedidos` (
    `PEDIDO_ID`          INT            NOT NULL AUTO_INCREMENT,
    `CLIENTE_ID`         INT            NOT NULL,
    `VENTA_ID`           INT            NULL DEFAULT NULL,
    `FECHA_HORA`         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MONTO_TOTAL`        DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    `ESTADO_PEDIDO`      ENUM('RECIBIDO','EN_PROCESO','ATENDIDO','RECHAZADO','CANCELADO') NOT NULL DEFAULT 'RECIBIDO',
    `PRIORIDAD`          INT            NOT NULL DEFAULT 0,
    `DIRECCION_ENTREGA`  VARCHAR(255)   NULL DEFAULT NULL,
    `MODALIDAD_ENTREGA`  ENUM('DELIVERY','RECOJO_TIENDA') NOT NULL DEFAULT 'DELIVERY',
    `OBSERVACIONES`      VARCHAR(500)   NULL DEFAULT NULL,
    PRIMARY KEY (`PEDIDO_ID`),
    INDEX `fk_pedidos_clientes_idx` (`CLIENTE_ID`),
    INDEX `fk_pedidos_ventas_idx` (`VENTA_ID`),
    INDEX `idx_pedidos_estado` (`ESTADO_PEDIDO`),
    CONSTRAINT `fk_pedidos_clientes`
        FOREIGN KEY (`CLIENTE_ID`) REFERENCES `clientes` (`CLIENTE_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_pedidos_ventas`
        FOREIGN KEY (`VENTA_ID`) REFERENCES `ventas` (`VENTA_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `detalles_pedido` (
    `DETALLE_PEDIDO_ID` INT            NOT NULL AUTO_INCREMENT,
    `PEDIDO_ID`         INT            NOT NULL,
    `PRODUCTO_ID`       INT            NOT NULL,
    `CANTIDAD`          INT            NOT NULL,
    `PRECIO_UNITARIO`   DECIMAL(10,2)  NOT NULL,
    `SUBTOTAL`          DECIMAL(10,2)  NOT NULL,
    `DISPONIBLE`        TINYINT        NOT NULL DEFAULT 1,
    PRIMARY KEY (`DETALLE_PEDIDO_ID`),
    INDEX `fk_det_pedido_pedidos_idx` (`PEDIDO_ID`),
    INDEX `fk_det_pedido_productos_idx` (`PRODUCTO_ID`),
    CONSTRAINT `fk_det_pedido_pedidos`
        FOREIGN KEY (`PEDIDO_ID`) REFERENCES `pedidos` (`PEDIDO_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_det_pedido_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 6: GESTIÓN DE PROVEEDORES Y REABASTECIMIENTO
-- RF11: Gestión de proveedores
-- RF12: Gestión de órdenes de reabastecimiento
-- =============================================================

CREATE TABLE IF NOT EXISTS `proveedores` (
    `PROVEEDOR_ID` INT          NOT NULL AUTO_INCREMENT,
    `RAZON_SOCIAL` VARCHAR(150) NOT NULL,
    `RUC`          VARCHAR(11)  NOT NULL,
    `TELEFONO`     VARCHAR(15)  NULL DEFAULT NULL,
    `EMAIL`        VARCHAR(100) NULL DEFAULT NULL,
    `DIRECCION`    VARCHAR(255) NULL DEFAULT NULL,
    `CONTACTO`     VARCHAR(100) NULL DEFAULT NULL,
    `ACTIVO`       TINYINT      NOT NULL DEFAULT 1,
    PRIMARY KEY (`PROVEEDOR_ID`),
    UNIQUE INDEX `uq_proveedores_ruc` (`RUC`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `productos_proveedores` (
    `PRODUCTO_ID`    INT            NOT NULL,
    `PROVEEDOR_ID`   INT            NOT NULL,
    `PRECIO_COMPRA`  DECIMAL(10,2)  NOT NULL,
    `TIEMPO_ENTREGA` INT            NULL DEFAULT NULL,
    PRIMARY KEY (`PRODUCTO_ID`, `PROVEEDOR_ID`),
    INDEX `fk_pp_proveedores_idx` (`PROVEEDOR_ID`),
    CONSTRAINT `fk_pp_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_pp_proveedores`
        FOREIGN KEY (`PROVEEDOR_ID`) REFERENCES `proveedores` (`PROVEEDOR_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `ordenes_reabastecimiento` (
    `ORDEN_ID`               INT          NOT NULL AUTO_INCREMENT,
    `PROVEEDOR_ID`           INT          NOT NULL,
    `TRABAJADOR_ID`          INT          NOT NULL,
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `FECHA_ENTREGA_ESTIMADA` DATE         NULL DEFAULT NULL,
    `ESTADO_ORDEN`           ENUM('PENDIENTE','ENVIADA','RECIBIDA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
    `OBSERVACIONES`          VARCHAR(500) NULL DEFAULT NULL,
    PRIMARY KEY (`ORDEN_ID`),
    INDEX `fk_ordenes_proveedores_idx` (`PROVEEDOR_ID`),
    INDEX `fk_ordenes_trabajadores_idx` (`TRABAJADOR_ID`),
    CONSTRAINT `fk_ordenes_proveedores`
        FOREIGN KEY (`PROVEEDOR_ID`) REFERENCES `proveedores` (`PROVEEDOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_ordenes_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `detalles_orden_reabastecimiento` (
    `DETALLE_ORDEN_ID`   INT            NOT NULL AUTO_INCREMENT,
    `ORDEN_ID`           INT            NOT NULL,
    `PRODUCTO_ID`        INT            NOT NULL,
    `CANTIDAD_SOLICITADA` INT           NOT NULL,
    `CANTIDAD_RECIBIDA`  INT            NOT NULL DEFAULT 0,
    `PRECIO_COMPRA`      DECIMAL(10,2)  NOT NULL,
    PRIMARY KEY (`DETALLE_ORDEN_ID`),
    INDEX `fk_det_orden_ordenes_idx` (`ORDEN_ID`),
    INDEX `fk_det_orden_productos_idx` (`PRODUCTO_ID`),
    CONSTRAINT `fk_det_orden_ordenes`
        FOREIGN KEY (`ORDEN_ID`) REFERENCES `ordenes_reabastecimiento` (`ORDEN_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_det_orden_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 7: PROMOCIONES
-- RF13: Gestión de promociones y descuentos
-- =============================================================

CREATE TABLE IF NOT EXISTS `promociones` (
    `PROMOCION_ID`    INT            NOT NULL AUTO_INCREMENT,
    `NOMBRE`          VARCHAR(100)   NOT NULL,
    `DESCRIPCION`     VARCHAR(500)   NULL DEFAULT NULL,
    `TIPO_DESCUENTO`  ENUM('PORCENTAJE','MONTO_FIJO') NOT NULL,
    `VALOR_DESCUENTO` DECIMAL(10,2)  NOT NULL,
    `FECHA_INICIO`    DATE           NOT NULL,
    `FECHA_FIN`       DATE           NOT NULL,
    `CONDICIONES`     VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`          TINYINT        NOT NULL DEFAULT 1,
    PRIMARY KEY (`PROMOCION_ID`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `promociones_productos` (
    `PROMOCION_ID` INT NOT NULL,
    `PRODUCTO_ID`  INT NOT NULL,
    PRIMARY KEY (`PROMOCION_ID`, `PRODUCTO_ID`),
    INDEX `fk_promprod_productos_idx` (`PRODUCTO_ID`),
    CONSTRAINT `fk_promprod_promociones`
        FOREIGN KEY (`PROMOCION_ID`) REFERENCES `promociones` (`PROMOCION_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_promprod_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 8: DEVOLUCIONES Y MERMAS
-- RF14: Registro de devoluciones
-- =============================================================

CREATE TABLE IF NOT EXISTS `devoluciones` (
    `DEVOLUCION_ID`    INT                                           NOT NULL AUTO_INCREMENT,
    `PRODUCTO_ID`      INT                                           NOT NULL,
    `TRABAJADOR_ID`    INT                                           NOT NULL,
    `TIPO_DEVOLUCION`  ENUM('CLIENTE','MERMA','VENCIMIENTO','DEFECTO') NOT NULL,
    `CANTIDAD`         INT                                           NOT NULL,
    `MOTIVO`           VARCHAR(500)                                  NOT NULL,
    `FECHA_HORA`       DATETIME                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`DEVOLUCION_ID`),
    INDEX `fk_devoluciones_productos_idx` (`PRODUCTO_ID`),
    INDEX `fk_devoluciones_trabajadores_idx` (`TRABAJADOR_ID`),
    CONSTRAINT `fk_devoluciones_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_devoluciones_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MÓDULO 9: AUDITORÍA (RNF014 - Trazabilidad)
-- =============================================================

CREATE TABLE IF NOT EXISTS `auditoria` (
    `AUDITORIA_ID`  INT          NOT NULL AUTO_INCREMENT,
    `TABLA`         VARCHAR(50)  NOT NULL,
    `OPERACION`     ENUM('INSERT','UPDATE','DELETE') NOT NULL,
    `REGISTRO_ID`   INT          NOT NULL,
    `USUARIO_ID`    INT          NULL DEFAULT NULL,
    `DATOS_ANTES`   JSON         NULL DEFAULT NULL,
    `DATOS_DESPUES` JSON         NULL DEFAULT NULL,
    `FECHA_HORA`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`AUDITORIA_ID`),
    INDEX `idx_auditoria_tabla` (`TABLA`),
    INDEX `idx_auditoria_fecha` (`FECHA_HORA`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- RESTAURAR CONFIGURACIÓN
-- =============================================================
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
