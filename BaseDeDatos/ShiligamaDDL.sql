-- =====================================================================
-- SHILIGAMA - Sistema de Gestion de Minimarket
-- DDL: Definicion de tablas (MySQL 8+)
-- Equipo: Team Script - Programacion 3 (PUCP) 2026-1
-- =====================================================================
--   -Cada tabla maestra agrega: ACTIVO, FECHA_CREACION, FECHA_MODIFICACION,
--   -Las tablas de detalle (detalles_venta, detalles_pedido, etc.)
--   heredan la trazabilidad de su tabla padre y NO llevan columnas
--   de auditoria propias.
-- =====================================================================

-- SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
-- SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
-- SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema shiligama
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `shiligama`;
CREATE SCHEMA IF NOT EXISTS `shiligama`;
-- DEFAULT CHARACTER SET utf8mb4;
USE `shiligama`;

-- =============================================================
-- MODULO 1: GESTION DE USUARIOS 
-- Gestion de usuarios
-- Autenticacion y control de sesion
-- =============================================================
-- -----------------------------------------------------
-- Tabla Usuarios
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`usuarios` ;
CREATE TABLE IF NOT EXISTS `usuarios` (
	-- Primary Keys
    `USUARIO_ID`             INT          NOT NULL AUTO_INCREMENT COMMENT 'Identificador único de la persona.',
	-- Atributos
    `NOMBRES`                VARCHAR(100) NOT NULL,
    `APELLIDOS`              VARCHAR(100) NOT NULL,
    `DNI`                    VARCHAR(8)   NOT NULL,
    `TELEFONO`               VARCHAR(15)  NULL DEFAULT NULL,
    `CORREO`                  VARCHAR(100) NOT NULL,
    -- `DIRECCION`              VARCHAR(255) NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
	`CONTRASENA`             VARCHAR(255) NOT NULL,
    PRIMARY KEY (`USUARIO_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena información general de los usuarios registrados';
-- -----------------------------------------------------
-- Tabla Clientes
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`clientes` ;
CREATE TABLE IF NOT EXISTS `clientes` (
    `CLIENTE_ID`             INT          NOT NULL AUTO_INCREMENT,
    `USUARIO_ID`             INT          NOT NULL,
    `DIRECCION_ENTREGA`      VARCHAR(255) NULL DEFAULT NULL,
    `FECHA_REGISTRO`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`CLIENTE_ID`),
	-- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario 
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena los Clientes';
-- -----------------------------------------------------
-- Tabla Administradores
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`administradores` ;
CREATE TABLE IF NOT EXISTS `administradores` (
    `ADMINISTRADOR_ID`       INT          NOT NULL AUTO_INCREMENT,
    `USUARIO_ID`             INT          NOT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
    PRIMARY KEY (`ADMINISTRADOR_ID`),
	-- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario 
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena datos de los administradores';
-- -----------------------------------------------------
-- Tabla Trabajadores
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`trabajadores` ;
CREATE TABLE IF NOT EXISTS `trabajadores` (
    `TRABAJADOR_ID`          INT          NOT NULL AUTO_INCREMENT,
    `USUARIO_ID`             INT          NOT NULL,
    `CARGO`                  VARCHAR(100) NULL DEFAULT NULL,
    `FECHA_INGRESO`          DATE         NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
     PRIMARY KEY (`TRABAJADOR_ID`),
	-- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario 
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena datos de los trabajadores';


-- =============================================================
-- MODULO 2: CATALOGO Y PROMOCIONES
-- Gestion de categorias
-- Gestion de productos
-- Gestion de promociones
-- =============================================================

CREATE TABLE IF NOT EXISTS `categorias` (
    `CATEGORIA_ID`           INT          NOT NULL AUTO_INCREMENT,
    `CATEGORIA_PADRE_ID`     INT          NULL DEFAULT NULL,
    `NOMBRE`                 VARCHAR(100) NOT NULL,
    `DESCRIPCION`            VARCHAR(255) NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USUARIO_CREACION`       INT          NULL DEFAULT NULL,
    `USUARIO_MODIFICACION`   INT          NULL DEFAULT NULL,
    PRIMARY KEY (`CATEGORIA_ID`),
    INDEX `fk_categorias_padre_idx` (`CATEGORIA_PADRE_ID`),
    INDEX `fk_cat_usu_creacion_idx` (`USUARIO_CREACION`),
    INDEX `fk_cat_usu_modificacion_idx` (`USUARIO_MODIFICACION`),
    CONSTRAINT `fk_categorias_categorias`
        FOREIGN KEY (`CATEGORIA_PADRE_ID`) REFERENCES `categorias` (`CATEGORIA_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_cat_usu_creacion`
        FOREIGN KEY (`USUARIO_CREACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_cat_usu_modificacion`
        FOREIGN KEY (`USUARIO_MODIFICACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `productos` (
    `PRODUCTO_ID`            INT            NOT NULL AUTO_INCREMENT,
    `CATEGORIA_ID`           INT            NOT NULL,
    `NOMBRE`                 VARCHAR(150)   NOT NULL,
    `DESCRIPCION`            VARCHAR(500)   NULL DEFAULT NULL,
    `PRECIO_UNITARIO`        DECIMAL(10,2)  NOT NULL,
    `STOCK`                  INT            NOT NULL DEFAULT 0,
    `STOCK_MINIMO`           INT            NOT NULL DEFAULT 5,
    `UNIDAD_MEDIDA`          VARCHAR(30)    NULL DEFAULT NULL,
    `CODIGO_BARRAS`          VARCHAR(50)    NULL DEFAULT NULL,
    `IMAGEN_URL`             VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT        NOT NULL DEFAULT 1,
    `FECHA_CREACION`         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `FECHA_MODIFICACION`     DATETIME       NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USUARIO_CREACION`       INT            NULL DEFAULT NULL,
    `USUARIO_MODIFICACION`   INT            NULL DEFAULT NULL,
    PRIMARY KEY (`PRODUCTO_ID`),
    INDEX `fk_productos_categorias_idx` (`CATEGORIA_ID`),
    INDEX `fk_prod_usu_creacion_idx` (`USUARIO_CREACION`),
    INDEX `fk_prod_usu_modificacion_idx` (`USUARIO_MODIFICACION`),
    CONSTRAINT `fk_productos_categorias`
        FOREIGN KEY (`CATEGORIA_ID`) REFERENCES `categorias` (`CATEGORIA_ID`)
        ON UPDATE CASCADE,
    CONSTRAINT `fk_prod_usu_creacion`
        FOREIGN KEY (`USUARIO_CREACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_prod_usu_modificacion`
        FOREIGN KEY (`USUARIO_MODIFICACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `promociones` (
    `PROMOCION_ID`           INT            NOT NULL AUTO_INCREMENT,
    `NOMBRE`                 VARCHAR(100)   NOT NULL,
    `DESCRIPCION`            VARCHAR(500)   NULL DEFAULT NULL,
    `TIPO_DESCUENTO`         ENUM('PORCENTAJE','MONTO_FIJO') NOT NULL,
    `VALOR_DESCUENTO`        DECIMAL(10,2)  NOT NULL,
    `FECHA_INICIO`           DATE           NOT NULL,
    `FECHA_FIN`              DATE           NOT NULL,
    `CONDICIONES`            VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT        NOT NULL DEFAULT 1,
    `FECHA_CREACION`         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `FECHA_MODIFICACION`     DATETIME       NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USUARIO_CREACION`       INT            NULL DEFAULT NULL,
    `USUARIO_MODIFICACION`   INT            NULL DEFAULT NULL,
    PRIMARY KEY (`PROMOCION_ID`),
    INDEX `fk_prom_usu_creacion_idx` (`USUARIO_CREACION`),
    INDEX `fk_prom_usu_modificacion_idx` (`USUARIO_MODIFICACION`),
    CONSTRAINT `fk_prom_usu_creacion`
        FOREIGN KEY (`USUARIO_CREACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_prom_usu_modificacion`
        FOREIGN KEY (`USUARIO_MODIFICACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- Tabla de detalle (sin auditoria propia)
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
-- MODULO 3: INVENTARIO Y DEVOLUCIONES
-- RF07: Actualizacion de stock
-- RF08: Historial de movimientos
-- RF09: Devoluciones y mermas
-- =============================================================

-- Los movimientos de inventario son un LOG inmutable: no llevan
-- columnas de modificacion (no se editan una vez registrados).
CREATE TABLE IF NOT EXISTS `movimientos_inventario` (
    `MOVIMIENTO_ID`          INT                                              NOT NULL AUTO_INCREMENT,
    `PRODUCTO_ID`            INT                                              NOT NULL,
    `TRABAJADOR_ID`          INT                                              NULL DEFAULT NULL,
    `TIPO_MOVIMIENTO`        ENUM('ENTRADA','SALIDA','AJUSTE','DEVOLUCION')   NOT NULL,
    `CANTIDAD`               INT                                              NOT NULL,
    `STOCK_ANTERIOR`         INT                                              NOT NULL,
    `STOCK_RESULTANTE`       INT                                              NOT NULL,
    `MOTIVO`                 VARCHAR(255)                                     NULL DEFAULT NULL,
    `FECHA_HORA`             DATETIME                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `USUARIO_CREACION`       INT                                              NULL DEFAULT NULL,
    PRIMARY KEY (`MOVIMIENTO_ID`),
    INDEX `fk_movimientos_productos_idx` (`PRODUCTO_ID`),
    INDEX `fk_movimientos_trabajadores_idx` (`TRABAJADOR_ID`),
    INDEX `fk_mov_usu_creacion_idx` (`USUARIO_CREACION`),
    INDEX `idx_mov_fecha` (`FECHA_HORA`),
    CONSTRAINT `fk_movimientos_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_movimientos_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_mov_usu_creacion`
        FOREIGN KEY (`USUARIO_CREACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `devoluciones` (
    `DEVOLUCION_ID`          INT                                              NOT NULL AUTO_INCREMENT,
    `PRODUCTO_ID`            INT                                              NOT NULL,
    `TRABAJADOR_ID`          INT                                              NOT NULL,
    `TIPO_DEVOLUCION`        ENUM('CLIENTE','MERMA','VENCIMIENTO','DEFECTO')  NOT NULL,
    `CANTIDAD`               INT                                              NOT NULL,
    `MOTIVO`                 VARCHAR(500)                                     NOT NULL,
    `FECHA_HORA`             DATETIME                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ACTIVO`                 TINYINT                                          NOT NULL DEFAULT 1,
    `FECHA_CREACION`         DATETIME                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `FECHA_MODIFICACION`     DATETIME                                         NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USUARIO_CREACION`       INT                                              NULL DEFAULT NULL,
    `USUARIO_MODIFICACION`   INT                                              NULL DEFAULT NULL,
    PRIMARY KEY (`DEVOLUCION_ID`),
    INDEX `fk_devoluciones_productos_idx` (`PRODUCTO_ID`),
    INDEX `fk_devoluciones_trabajadores_idx` (`TRABAJADOR_ID`),
    INDEX `fk_dev_usu_creacion_idx` (`USUARIO_CREACION`),
    INDEX `fk_dev_usu_modificacion_idx` (`USUARIO_MODIFICACION`),
    CONSTRAINT `fk_devoluciones_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_devoluciones_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_dev_usu_creacion`
        FOREIGN KEY (`USUARIO_CREACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_dev_usu_modificacion`
        FOREIGN KEY (`USUARIO_MODIFICACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MODULO 4: VENTAS Y PEDIDOS
-- Metodos de pago
-- Ventas presenciales
-- Pedidos del portal web
-- Verificacion de stock en checkout
-- =============================================================
-- -----------------------------------------------------
-- Tabla Metodos Pago
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`metodos_pago` ;
CREATE TABLE IF NOT EXISTS `metodos_pago` (
    -- Primary Key
    `METODO_PAGO_ID`         INT          NOT NULL AUTO_INCREMENT,
    -- Atributos
    `NOMBRE`                 VARCHAR(50)  NOT NULL,
    `DESCRIPCION`            VARCHAR(255) NULL DEFAULT NULL, -- SE DEBERIA IR?
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
	PRIMARY KEY (`METODO_PAGO_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena los distintos metodos de pago';

-- -----------------------------------------------------
-- Tabla Ventas BOLETA?? WAAA
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`ventas` ;
CREATE TABLE IF NOT EXISTS `ventas` (
    -- Primary Key
    `VENTA_ID`               INT            NOT NULL AUTO_INCREMENT,
    -- Atributos
    `CLIENTE_ID`             INT            NULL DEFAULT NULL,
    `TRABAJADOR_ID`          INT            NULL DEFAULT NULL,
    `METODO_PAGO_ID`         INT            NOT NULL,
    `FECHA_HORA`             DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MONTO_TOTAL`            DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    `MONTO_DESCUENTO`        DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    `CANAL_VENTA`            ENUM('PRESENCIAL','WEB') NOT NULL DEFAULT 'PRESENCIAL',
    `ESTADO_VENTA`           ENUM('REGISTRADA','COMPLETADA','ANULADA') NOT NULL DEFAULT 'REGISTRADA',
    `OBSERVACIONES`          VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT        NOT NULL DEFAULT 1, -- CONSIDERAR PARA BORRADO LOGICO, lo añado en DTO?
	PRIMARY KEY (`VENTA_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
    INDEX `fk_ventas_clientes_idx` (`CLIENTE_ID`),
    INDEX `fk_ventas_trabajadores_idx` (`TRABAJADOR_ID`),
    INDEX `fk_ventas_metodos_pago_idx` (`METODO_PAGO_ID`),
    CONSTRAINT `fk_ventas_clientes`
        FOREIGN KEY (`CLIENTE_ID`) REFERENCES `clientes` (`CLIENTE_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_ventas_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_ventas_metodos_pago`
        FOREIGN KEY (`METODO_PAGO_ID`) REFERENCES `metodos_pago` (`METODO_PAGO_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena la cabecera de los comprobantes de pago (ventas/alquileres).';

-- -----------------------------------------------------
-- Tabla DetallesVenta (sin auditoria propia porque hereda de ventas)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`detalles_venta` ;
CREATE TABLE IF NOT EXISTS `detalles_venta` (
    -- Primary Key
    `DETALLE_VENTA_ID` INT            NOT NULL AUTO_INCREMENT,
    -- Atributos
    `VENTA_ID`         INT            NOT NULL,
    `PRODUCTO_ID`      INT            NOT NULL,
	`DESCRIPCION` VARCHAR(100) NOT NULL COMMENT 'Descripción de la línea de detalle.',
    `PRECIO_UNITARIO`  DECIMAL(10,2)  NOT NULL,
    `CANTIDAD`         INT            NOT NULL,
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

-- -----------------------------------------------------
-- Tabla Pedidos 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`pedidos` ;
CREATE TABLE IF NOT EXISTS `pedidos` (
	-- Primary Key
    `PEDIDO_ID`              INT            NOT NULL AUTO_INCREMENT,
    -- Atributos 
    `CLIENTE_ID`             INT            NOT NULL,
    `VENTA_ID`               INT            NULL DEFAULT NULL,
    `FECHA_HORA`             DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MONTO_TOTAL`            DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    `ESTADO_PEDIDO`          ENUM('RECIBIDO','EN_PROCESO','ATENDIDO','RECHAZADO','CANCELADO') NOT NULL DEFAULT 'RECIBIDO',
    `PRIORIDAD`              INT            NOT NULL DEFAULT 0,
    `DIRECCION_ENTREGA`      VARCHAR(255)   NULL DEFAULT NULL,
    `MODALIDAD_ENTREGA`      ENUM('DELIVERY','RECOJO_TIENDA') NOT NULL DEFAULT 'DELIVERY',
    `OBSERVACIONES`          VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT        NOT NULL DEFAULT 1,
	PRIMARY KEY (`PEDIDO_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
    INDEX `fk_pedidos_clientes_idx` (`CLIENTE_ID`),
    INDEX `fk_pedidos_ventas_idx` (`VENTA_ID`),
    -- INDEX `idx_pedidos_estado` (`ESTADO_PEDIDO`),
    CONSTRAINT `fk_pedidos_clientes`
        FOREIGN KEY (`CLIENTE_ID`) REFERENCES `clientes` (`CLIENTE_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_pedidos_ventas`
        FOREIGN KEY (`VENTA_ID`) REFERENCES `ventas` (`VENTA_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- -----------------------------------------------------
-- Tabla Detalles Pedidos
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`detalles_pedido` ;
CREATE TABLE IF NOT EXISTS `detalles_pedido` (
    -- Primary Key
    `DETALLE_PEDIDO_ID` INT            NOT NULL AUTO_INCREMENT,
    -- Atributos
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
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `productos` (`PRODUCTO_ID`) 
        ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- =============================================================
-- MODULO 5: PROVEEDORES Y REABASTECIMIENTO
-- RF13: Proveedores
-- RF14: Ordenes de reabastecimiento
-- =============================================================

CREATE TABLE IF NOT EXISTS `proveedores` (
    `PROVEEDOR_ID`           INT          NOT NULL AUTO_INCREMENT,
    `RAZON_SOCIAL`           VARCHAR(150) NOT NULL,
    `RUC`                    VARCHAR(11)  NOT NULL,
    `TELEFONO`               VARCHAR(15)  NULL DEFAULT NULL,
    `EMAIL`                  VARCHAR(100) NULL DEFAULT NULL,
    `DIRECCION`              VARCHAR(255) NULL DEFAULT NULL,
    `CONTACTO`               VARCHAR(100) NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USUARIO_CREACION`       INT          NULL DEFAULT NULL,
    `USUARIO_MODIFICACION`   INT          NULL DEFAULT NULL,
    PRIMARY KEY (`PROVEEDOR_ID`),
    UNIQUE INDEX `uq_proveedores_ruc` (`RUC`),
    INDEX `fk_prov_usu_creacion_idx` (`USUARIO_CREACION`),
    INDEX `fk_prov_usu_modificacion_idx` (`USUARIO_MODIFICACION`),
    CONSTRAINT `fk_prov_usu_creacion`
        FOREIGN KEY (`USUARIO_CREACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_prov_usu_modificacion`
        FOREIGN KEY (`USUARIO_MODIFICACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- Tabla de relacion (sin auditoria propia)
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
    `FECHA_ENTREGA_ESTIMADA` DATE         NULL DEFAULT NULL,
    `ESTADO_ORDEN`           ENUM('PENDIENTE','ENVIADA','RECIBIDA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
    `OBSERVACIONES`          VARCHAR(500) NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `USUARIO_CREACION`       INT          NULL DEFAULT NULL,
    `USUARIO_MODIFICACION`   INT          NULL DEFAULT NULL,
    PRIMARY KEY (`ORDEN_ID`),
    INDEX `fk_ordenes_proveedores_idx` (`PROVEEDOR_ID`),
    INDEX `fk_ordenes_trabajadores_idx` (`TRABAJADOR_ID`),
    INDEX `fk_ord_usu_creacion_idx` (`USUARIO_CREACION`),
    INDEX `fk_ord_usu_modificacion_idx` (`USUARIO_MODIFICACION`),
    CONSTRAINT `fk_ordenes_proveedores`
        FOREIGN KEY (`PROVEEDOR_ID`) REFERENCES `proveedores` (`PROVEEDOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_ordenes_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajadores` (`TRABAJADOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_ord_usu_creacion`
        FOREIGN KEY (`USUARIO_CREACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_ord_usu_modificacion`
        FOREIGN KEY (`USUARIO_MODIFICACION`) REFERENCES `usuarios` (`USUARIO_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;

-- Tabla de detalle (sin auditoria propia; hereda de ordenes_reabastecimiento)
CREATE TABLE IF NOT EXISTS `detalles_orden_reabastecimiento` (
    `DETALLE_ORDEN_ID`    INT            NOT NULL AUTO_INCREMENT,
    `ORDEN_ID`            INT            NOT NULL,
    `PRODUCTO_ID`         INT            NOT NULL,
    `CANTIDAD_SOLICITADA` INT            NOT NULL,
    `CANTIDAD_RECIBIDA`   INT            NOT NULL DEFAULT 0,
    `PRECIO_COMPRA`       DECIMAL(10,2)  NOT NULL,
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
-- RESTAURAR CONFIGURACION
-- =============================================================
-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- =============================================================
-- RESUMEN DE TABLAS (18 total)
-- =============================================================
-- Maestras con auditoria completa (12):
--   usuarios, clientes, administradores, trabajadores,
--   categorias, productos, promociones, metodos_pago,
--   ventas, pedidos, proveedores, ordenes_reabastecimiento,
--   devoluciones
-- Log inmutable (1):
--   movimientos_inventario
-- Detalles / relaciones N:M (4):
--   detalles_venta, detalles_pedido,
--   detalles_orden_reabastecimiento,
--   promociones_productos, productos_proveedores
-- =============================================================
