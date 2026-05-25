-- =====================================================================
-- SHILIGAMA - Sistema de Gestion de Minimarket
-- DDL: Definicion de tablas (MySQL 8+)
-- Equipo: Team Script - Programacion 3 (PUCP) 2026-1
-- =====================================================================
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
-- Tabla Usuario
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`usuario` ;
CREATE TABLE IF NOT EXISTS `usuario` (
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
-- Tabla Cliente
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`cliente` ;
CREATE TABLE IF NOT EXISTS `cliente` (
    `USUARIO_ID`             INT          PRIMARY KEY NOT NULL,
    `DIRECCION_ENTREGA`      VARCHAR(255) NULL DEFAULT NULL,
    `FECHA_REGISTRO`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (USUARIO_ID) REFERENCES usuario(USUARIO_ID),
	-- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario 
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena los Clientes';
-- -----------------------------------------------------
-- Tabla Administrador
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`administrador` ;
CREATE TABLE IF NOT EXISTS `administrador` (
    `USUARIO_ID`             INT          PRIMARY KEY NOT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
    FOREIGN KEY (USUARIO_ID) REFERENCES usuario(USUARIO_ID),
	-- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario 
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena datos de los administradores';
-- -----------------------------------------------------
-- Tabla Trabajador
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`trabajador` ;
CREATE TABLE IF NOT EXISTS `trabajador` (
    `USUARIO_ID`             INT          PRIMARY KEY NOT NULL,
    `CARGO`                  VARCHAR(100) NULL DEFAULT NULL,
    `FECHA_INGRESO`          DATE         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
     FOREIGN KEY (USUARIO_ID) REFERENCES usuario(USUARIO_ID),
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
-- -----------------------------------------------------
-- Tabla Categoria
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`categoria` ;
CREATE TABLE IF NOT EXISTS `categoria` (
    -- Primary Key
    `CATEGORIA_ID`           INT          NOT NULL AUTO_INCREMENT,
    -- Atributos
    `CATEGORIA_PADRE_ID`     INT          NULL DEFAULT NULL,
    `NOMBRE`                 VARCHAR(100) NOT NULL,
    `DESCRIPCION`            VARCHAR(255) NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT      NOT NULL DEFAULT 1,
	PRIMARY KEY (`CATEGORIA_ID`),
	-- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario 
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
    INDEX `fk_categoria_padre_idx` (`CATEGORIA_PADRE_ID`),
    CONSTRAINT `fk_categoria_categoria`
        FOREIGN KEY (`CATEGORIA_PADRE_ID`) REFERENCES `categoria` (`CATEGORIA_ID`)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Tabla que almacena las categorias con el relativo a la categoria mayor de exisitr';
-- -----------------------------------------------------
-- Tabla Producto
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`producto` ;
CREATE TABLE IF NOT EXISTS `producto` (
    -- Primary Key
    `PRODUCTO_ID`            INT            NOT NULL AUTO_INCREMENT,
    -- Atributos
    `CATEGORIA_ID`           INT            NOT NULL,
    `NOMBRE`                 VARCHAR(100)   NOT NULL,
    `DESCRIPCION`            VARCHAR(255)   NULL DEFAULT NULL,
    `PRECIO_UNITARIO`        DECIMAL(10,2)  NOT NULL,
    `STOCK`                  INT            NOT NULL DEFAULT 0,
    `STOCK_MINIMO`           INT            NOT NULL DEFAULT 0,
    `UNIDAD_MEDIDA`          VARCHAR(20)    NULL DEFAULT NULL,
    `CODIGO_BARRAS`          VARCHAR(50)    NULL DEFAULT NULL,
    `IMAGEN_URL`             VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT        NOT NULL DEFAULT 1,
	PRIMARY KEY (`PRODUCTO_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
    INDEX `fk_producto_categoria_idx` (`CATEGORIA_ID`),
    CONSTRAINT `fk_producto_categoria`
        FOREIGN KEY (`CATEGORIA_ID`) REFERENCES `categoria` (`CATEGORIA_ID`)
        ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Almacena los Productos';
-- -----------------------------------------------------
-- Tabla Promocion
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`promocion` ;
CREATE TABLE IF NOT EXISTS `promocion` (
    -- Primary Key
    `PROMOCION_ID`           INT            NOT NULL AUTO_INCREMENT,
    -- Atributos
    `NOMBRE`                 VARCHAR(100)   NOT NULL,
    `DESCRIPCION`            VARCHAR(255)   NULL DEFAULT NULL,
    `TIPO_DESCUENTO`         ENUM('PORCENTAJE','MONTO_FIJO') NOT NULL DEFAULT 'PORCENTAJE',
    `VALOR_DESCUENTO`        DECIMAL(10,2)  NOT NULL,
    `FECHA_INICIO`           DATE           NOT NULL,
    `FECHA_FIN`              DATE           NOT NULL,
    `CONDICIONES`            VARCHAR(500)   NULL DEFAULT NULL,
    `ACTIVO`                 TINYINT        NOT NULL DEFAULT 1,
	PRIMARY KEY (`PROMOCION_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó'
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Tabla de Promociones de descuento para productos';
-- -----------------------------------------------------
-- Tabla Promocion_Producto (relación N:M)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`promocion_producto` ;
CREATE TABLE IF NOT EXISTS `promocion_producto` (
    `PROMOCION_ID` INT NOT NULL,
    `PRODUCTO_ID`  INT NOT NULL,
    PRIMARY KEY (`PROMOCION_ID`, `PRODUCTO_ID`),
    INDEX `fk_pp_promocion_idx` (`PROMOCION_ID`),
    INDEX `fk_pp_producto_idx` (`PRODUCTO_ID`),
    CONSTRAINT `fk_pp_promocion`
        FOREIGN KEY (`PROMOCION_ID`) REFERENCES `promocion` (`PROMOCION_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_pp_producto`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `producto` (`PRODUCTO_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
COMMENT = 'Relacion N a M de Promocion a Productos';

-- -----------------------------------------------------
-- Tabla Movimientos Inventario
-- -----------------------------------------------------
-- Los movimientos de inventario son un LOG inmutable: no llevan
-- columnas de modificacion (no se editan una vez registrados).
DROP TABLE IF EXISTS `shiligama`.`movimiento_inventario` ;
CREATE TABLE IF NOT EXISTS `movimiento_inventario` (
    -- Primary Key
    `MOVIMIENTO_ID`          INT                                              NOT NULL AUTO_INCREMENT,
    -- Atributos
    `PRODUCTO_ID`            INT                                              NOT NULL,
    `TRABAJADOR_ID`          INT                                              NULL DEFAULT NULL,
    `TIPO_MOVIMIENTO`        ENUM('ENTRADA','SALIDA','AJUSTE','DEVOLUCION')   NOT NULL,
    `CANTIDAD`               INT                                              NOT NULL,
    `STOCK_ANTERIOR`         INT                                              NOT NULL,
    `STOCK_RESULTANTE`       INT                                              NOT NULL,
    `MOTIVO`                 VARCHAR(255)                                     NULL DEFAULT NULL,
    PRIMARY KEY (`MOVIMIENTO_ID`),
    -- Auditoría Automatica
	`FECHA_HORA`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    -- Auditoría de Usuario 
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    INDEX `fk_movimientos_productos_idx` (`PRODUCTO_ID`),
    INDEX `fk_movimientos_trabajadores_idx` (`TRABAJADOR_ID`),
    CONSTRAINT `fk_movimientos_productos`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `producto` (`PRODUCTO_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_movimientos_trabajadores`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajador` (`TRABAJADOR_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Auditoría de movimientos de inventario: entradas, salidas, ajustes y devoluciones de productos';


-- -----------------------------------------------------
-- Tabla Devolucion
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`devolucion` ;
CREATE TABLE IF NOT EXISTS `devolucion` (
    -- Primary Key
    `DEVOLUCION_ID`          INT            NOT NULL AUTO_INCREMENT,
    -- Atributos
    `PRODUCTO_ID`            INT            NOT NULL,
    `TRABAJADOR_ID`          INT            NULL DEFAULT NULL,
    `ESTADO_DEVOLUCION`      ENUM('PENDIENTE','APROBADO','RECHAZADO') NOT NULL DEFAULT 'PENDIENTE',
    `CANTIDAD`               INT            NOT NULL,
    `MOTIVO`                 VARCHAR(500)   NULL DEFAULT NULL,
    `FECHA_HORA`             DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ACTIVO`                 TINYINT        NOT NULL DEFAULT 1,
	PRIMARY KEY (`DEVOLUCION_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
    INDEX `fk_devolucion_producto_idx` (`PRODUCTO_ID`),
    INDEX `fk_devolucion_trabajador_idx` (`TRABAJADOR_ID`),
    CONSTRAINT `fk_devolucion_producto`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `producto` (`PRODUCTO_ID`)
        ON UPDATE CASCADE,
    CONSTRAINT `fk_devolucion_trabajador`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajador` (`TRABAJADOR_ID`)
        ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Tabla de Devolucion, baja de stock';


-- =============================================================
-- MODULO 3: GESTION DE VENTAS Y PEDIDOS
-- =============================================================
-- -----------------------------------------------------
-- Tabla Metodo_Pago
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`metodo_pago` ;
CREATE TABLE IF NOT EXISTS `metodo_pago` (
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
-- Tabla Venta
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`venta` ;
CREATE TABLE IF NOT EXISTS `venta` (
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
    -- Campos específicos de boleta
    `NUMERO_BOLETA`          VARCHAR(20)    NULL DEFAULT NULL COMMENT 'Número de comprobante',
    `RUC_EMPRESA`            VARCHAR(11)    NULL DEFAULT NULL COMMENT 'RUC de la empresa emisora',
    `CONTACTO_CLIENTE`       VARCHAR(20)    NULL DEFAULT NULL COMMENT 'Teléfono/contacto del cliente',
    `MENSAJE_BOLETA`         VARCHAR(255)   NULL DEFAULT NULL COMMENT 'Mensaje personalizado en boleta',
	PRIMARY KEY (`VENTA_ID`),
    -- Auditoría Automática
    `FECHA_CREACION`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
    `FECHA_MODIFICACION`     DATETIME     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
    -- Auditoría de Usuario
    `USUARIO_CREACION`       VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que creó',
    `USUARIO_MODIFICACION`   VARCHAR(100) NULL DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
    INDEX `fk_venta_cliente_idx` (`CLIENTE_ID`),
    INDEX `fk_venta_trabajador_idx` (`TRABAJADOR_ID`),
    INDEX `fk_venta_metodo_pago_idx` (`METODO_PAGO_ID`),
    CONSTRAINT `fk_venta_cliente`
        FOREIGN KEY (`CLIENTE_ID`) REFERENCES `cliente` (`CLIENTE_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_venta_trabajador`
        FOREIGN KEY (`TRABAJADOR_ID`) REFERENCES `trabajador` (`TRABAJADOR_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_venta_metodo_pago`
        FOREIGN KEY (`METODO_PAGO_ID`) REFERENCES `metodo_pago` (`METODO_PAGO_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4
COMMENT = 'Registro de ventas/comprobantes de pago. Contiene la información completa de cada transacción.';
-- -----------------------------------------------------
-- Tabla Detalle_Venta (sin auditoria propia porque hereda de venta)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`detalle_venta` ;
CREATE TABLE IF NOT EXISTS `detalle_venta` (
    -- Primary Key
    `DETALLE_VENTA_ID` INT            NOT NULL AUTO_INCREMENT,
    -- Atributos
    `VENTA_ID`         INT            NOT NULL,
    `PRODUCTO_ID`      INT            NOT NULL,
	`DESCRIPCION`      VARCHAR(100)   NULL DEFAULT NULL COMMENT 'Descripción opcional de la línea de detalle.',
    `PRECIO_UNITARIO`  DECIMAL(10,2)  NOT NULL,
    `CANTIDAD`         INT            NOT NULL,
    `SUBTOTAL`         DECIMAL(10,2)  NOT NULL,    
    PRIMARY KEY (`DETALLE_VENTA_ID`),
    INDEX `fk_det_venta_venta_idx` (`VENTA_ID`),
    INDEX `fk_det_venta_producto_idx` (`PRODUCTO_ID`),
    CONSTRAINT `fk_det_venta_venta`
        FOREIGN KEY (`VENTA_ID`) REFERENCES `venta` (`VENTA_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_det_venta_producto`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `producto` (`PRODUCTO_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;
-- -----------------------------------------------------
-- Tabla Pedido 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`pedido` ;
CREATE TABLE IF NOT EXISTS `pedido` (
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
    INDEX `fk_pedido_cliente_idx` (`CLIENTE_ID`),
    INDEX `fk_pedido_venta_idx` (`VENTA_ID`),
    -- INDEX `idx_pedido_estado` (`ESTADO_PEDIDO`),
    CONSTRAINT `fk_pedido_cliente`
        FOREIGN KEY (`CLIENTE_ID`) REFERENCES `cliente` (`CLIENTE_ID`) ON UPDATE CASCADE,
    CONSTRAINT `fk_pedido_venta`
        FOREIGN KEY (`VENTA_ID`) REFERENCES `venta` (`VENTA_ID`) ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;
-- -----------------------------------------------------
-- Tabla Detalle_Pedido
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shiligama`.`detalle_pedido` ;
CREATE TABLE IF NOT EXISTS `detalle_pedido` (
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
    INDEX `fk_det_pedido_pedido_idx` (`PEDIDO_ID`),
    INDEX `fk_det_pedido_producto_idx` (`PRODUCTO_ID`),
    CONSTRAINT `fk_det_pedido_pedido`
        FOREIGN KEY (`PEDIDO_ID`) REFERENCES `pedido` (`PEDIDO_ID`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_det_pedido_producto`
        FOREIGN KEY (`PRODUCTO_ID`) REFERENCES `producto` (`PRODUCTO_ID`) 
        ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4;