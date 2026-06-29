create database if not exists shiligama;
use shiligama;

-- Eliminación de tablas (hijos primero)
DROP TABLE IF EXISTS detalle_devolucion;
DROP TABLE IF EXISTS devolucion;
DROP TABLE IF EXISTS pago;
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS pedido;
DROP TABLE IF EXISTS boleta_detalle;
DROP TABLE IF EXISTS boleta;
DROP TABLE IF EXISTS detalle_venta;
DROP TABLE IF EXISTS venta;
DROP TABLE IF EXISTS promocion_producto;
DROP TABLE IF EXISTS movimiento_inventario;
DROP TABLE IF EXISTS lote;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS notificacion;
DROP TABLE IF EXISTS token_recuperacion;
DROP TABLE IF EXISTS trabajador;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS administrador;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS promocion;
DROP TABLE IF EXISTS metodo_pago;
DROP TABLE IF EXISTS configuracion;

-- Creación de tablas (padres primero)

CREATE TABLE configuracion (
  CONFIG_ID int NOT NULL DEFAULT '1',
  NOMBRE_TIENDA varchar(100) NOT NULL DEFAULT 'Shiligama Minimarket',
  MONEDA varchar(20) NOT NULL DEFAULT 'PEN (S/.)',
  IGV decimal(5,2) NOT NULL DEFAULT '18.00',
  TARIFA_ENVIO decimal(10,2) NOT NULL DEFAULT '5.00',
  MINIMO_ENVIO_GRATIS decimal(10,2) NOT NULL DEFAULT '50.00',
  PRIMARY KEY (CONFIG_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Configuración global del sistema (fila única, CONFIG_ID=1)';

CREATE TABLE metodo_pago (
  METODO_PAGO_ID int NOT NULL AUTO_INCREMENT,
  NOMBRE varchar(50) NOT NULL,
  DESCRIPCION varchar(255) DEFAULT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (METODO_PAGO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Almacena los distintos metodos de pago';

CREATE TABLE promocion (
  PROMOCION_ID int NOT NULL AUTO_INCREMENT,
  NOMBRE varchar(100) NOT NULL,
  DESCRIPCION varchar(255) DEFAULT NULL,
  TIPO_DESCUENTO enum('PORCENTAJE','MONTO_FIJO') NOT NULL DEFAULT 'PORCENTAJE',
  VALOR_DESCUENTO decimal(10,2) NOT NULL,
  FECHA_INICIO date NOT NULL,
  FECHA_FIN date NOT NULL,
  CONDICIONES varchar(500) DEFAULT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  MOSTRAR_EN_CARRUSEL tinyint NOT NULL DEFAULT '0',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (PROMOCION_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tabla de Promociones de descuento para productos';

CREATE TABLE usuario (
  USUARIO_ID int NOT NULL AUTO_INCREMENT COMMENT 'Identificador único de la persona.',
  NOMBRES varchar(100) NOT NULL,
  APELLIDOS varchar(100) NOT NULL,
  DNI varchar(8) NOT NULL,
  TELEFONO varchar(15) DEFAULT NULL,
  CORREO varchar(100) NOT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  CONTRASENA varchar(255) NOT NULL,
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (USUARIO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Almacena información general de los usuarios registrados';

CREATE TABLE categoria (
  CATEGORIA_ID int NOT NULL AUTO_INCREMENT,
  CATEGORIA_PADRE_ID int DEFAULT NULL,
  NOMBRE varchar(100) NOT NULL,
  DESCRIPCION varchar(255) DEFAULT NULL,
  ICONO varchar(50) DEFAULT 'fa-folder',
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (CATEGORIA_ID),
  KEY fk_categoria_padre_idx (CATEGORIA_PADRE_ID),
  CONSTRAINT fk_categoria_categoria FOREIGN KEY (CATEGORIA_PADRE_ID) REFERENCES categoria (CATEGORIA_ID) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tabla que almacena las categorias con el relativo a la categoria mayor de exisitr';

CREATE TABLE administrador (
  USUARIO_ID int NOT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (USUARIO_ID),
  CONSTRAINT administrador_ibfk_1 FOREIGN KEY (USUARIO_ID) REFERENCES usuario (USUARIO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Almacena datos de los administradores';

CREATE TABLE cliente (
  USUARIO_ID int NOT NULL,
  DIRECCION_ENTREGA varchar(255) DEFAULT NULL,
  FECHA_REGISTRO datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (USUARIO_ID),
  CONSTRAINT cliente_ibfk_1 FOREIGN KEY (USUARIO_ID) REFERENCES usuario (USUARIO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Almacena los Clientes';

CREATE TABLE trabajador (
  USUARIO_ID int NOT NULL,
  CARGO varchar(100) DEFAULT NULL,
  FECHA_INGRESO datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (USUARIO_ID),
  CONSTRAINT trabajador_ibfk_1 FOREIGN KEY (USUARIO_ID) REFERENCES usuario (USUARIO_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Almacena datos de los trabajadores';

CREATE TABLE token_recuperacion (
  TOKEN_ID int NOT NULL AUTO_INCREMENT,
  USUARIO_ID int NOT NULL,
  TOKEN varchar(100) NOT NULL,
  EXPIRACION datetime NOT NULL,
  USADO tinyint NOT NULL DEFAULT '0',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (TOKEN_ID),
  UNIQUE KEY uq_token (TOKEN),
  KEY fk_token_usuario_idx (USUARIO_ID),
  CONSTRAINT fk_token_usuario FOREIGN KEY (USUARIO_ID) REFERENCES usuario (USUARIO_ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tokens de un solo uso para recuperación de contraseña.';

CREATE TABLE notificacion (
  NOTIFICACION_ID int NOT NULL AUTO_INCREMENT,
  TITULO varchar(150) NOT NULL,
  MENSAJE varchar(500) NOT NULL,
  TIPO enum('STOCK_BAJO','NUEVO_PEDIDO','PEDIDO_LISTO','PEDIDO_ATENDIDO','DEVOLUCION_PENDIENTE','DEVOLUCION_RESUELTA','VENTA_REGISTRADA','PROMOCION_POR_VENCER','SISTEMA') NOT NULL DEFAULT 'SISTEMA',
  LEIDA tinyint NOT NULL DEFAULT '0',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ID_DESTINATARIO int DEFAULT NULL,
  REFERENCIA_TIPO varchar(30) DEFAULT NULL COMMENT 'PEDIDO, PRODUCTO, DEVOLUCION, PROMOCION',
  REFERENCIA_ID int DEFAULT NULL COMMENT 'ID de la entidad referenciada segun REFERENCIA_TIPO',
  ACTIVO tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (NOTIFICACION_ID),
  KEY idx_notif_destinatario (ID_DESTINATARIO),
  CONSTRAINT fk_notif_destinatario FOREIGN KEY (ID_DESTINATARIO) REFERENCES usuario (USUARIO_ID) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Notificaciones del sistema (alertas de stock, nuevos pedidos, etc.)';

CREATE TABLE producto (
  PRODUCTO_ID int NOT NULL AUTO_INCREMENT,
  CATEGORIA_ID int NOT NULL,
  NOMBRE varchar(100) NOT NULL,
  DESCRIPCION varchar(255) DEFAULT NULL,
  PRECIO_UNITARIO decimal(10,2) NOT NULL,
  STOCK int NOT NULL DEFAULT '0',
  STOCK_MINIMO int NOT NULL DEFAULT '0',
  UNIDAD_MEDIDA varchar(20) DEFAULT NULL,
  CODIGO_BARRAS varchar(50) DEFAULT NULL,
  IMAGEN_URL varchar(500) DEFAULT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (PRODUCTO_ID),
  KEY fk_producto_categoria_idx (CATEGORIA_ID),
  CONSTRAINT fk_producto_categoria FOREIGN KEY (CATEGORIA_ID) REFERENCES categoria (CATEGORIA_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Almacena los Productos';

CREATE TABLE lote (
  LOTE_ID int NOT NULL AUTO_INCREMENT,
  PRODUCTO_ID int NOT NULL,
  TRABAJADOR_ID int DEFAULT NULL,
  CANTIDAD_INICIAL int NOT NULL,
  CANTIDAD_ACTUAL int NOT NULL,
  FECHA_VENCIMIENTO date DEFAULT NULL,
  NUMERO_LOTE varchar(50) DEFAULT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  USUARIO_CREACION varchar(100) DEFAULT NULL,
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL,
  PRIMARY KEY (LOTE_ID),
  KEY fk_lote_producto_idx (PRODUCTO_ID),
  KEY fk_lote_trabajador_idx (TRABAJADOR_ID),
  KEY idx_lote_vencimiento (FECHA_VENCIMIENTO),
  CONSTRAINT fk_lote_producto FOREIGN KEY (PRODUCTO_ID) REFERENCES producto (PRODUCTO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_lote_trabajador FOREIGN KEY (TRABAJADOR_ID) REFERENCES usuario (USUARIO_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Lotes de productos recibidos con fecha de vencimiento (logica FEFO)';

CREATE TABLE movimiento_inventario (
  MOVIMIENTO_ID int NOT NULL AUTO_INCREMENT,
  PRODUCTO_ID int NOT NULL,
  TRABAJADOR_ID int DEFAULT NULL,
  TIPO_MOVIMIENTO enum('ENTRADA','SALIDA','AJUSTE','DEVOLUCION') NOT NULL,
  CANTIDAD int NOT NULL,
  STOCK_ANTERIOR int NOT NULL,
  STOCK_RESULTANTE int NOT NULL,
  MOTIVO varchar(255) DEFAULT NULL,
  FECHA_HORA datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  PRIMARY KEY (MOVIMIENTO_ID),
  KEY fk_movimientos_productos_idx (PRODUCTO_ID),
  KEY fk_movimientos_trabajadores_idx (TRABAJADOR_ID),
  CONSTRAINT fk_movimientos_productos FOREIGN KEY (PRODUCTO_ID) REFERENCES producto (PRODUCTO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_movimientos_trabajadores FOREIGN KEY (TRABAJADOR_ID) REFERENCES usuario (USUARIO_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Auditoría de movimientos de inventario: entradas, salidas, ajustes y devoluciones de productos';

CREATE TABLE promocion_producto (
  PROMOCION_ID int NOT NULL,
  PRODUCTO_ID int NOT NULL,
  PRIMARY KEY (PROMOCION_ID,PRODUCTO_ID),
  KEY fk_pp_promocion_idx (PROMOCION_ID),
  KEY fk_pp_producto_idx (PRODUCTO_ID),
  CONSTRAINT fk_pp_producto FOREIGN KEY (PRODUCTO_ID) REFERENCES producto (PRODUCTO_ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_pp_promocion FOREIGN KEY (PROMOCION_ID) REFERENCES promocion (PROMOCION_ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Relacion N a M de Promocion a Productos';

CREATE TABLE venta (
  VENTA_ID int NOT NULL AUTO_INCREMENT,
  CLIENTE_ID int DEFAULT NULL,
  TRABAJADOR_ID int DEFAULT NULL,
  METODO_PAGO_ID int NOT NULL,
  FECHA_HORA datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  MONTO_TOTAL decimal(10,2) NOT NULL DEFAULT '0.00',
  MONTO_DESCUENTO decimal(10,2) NOT NULL DEFAULT '0.00',
  CANAL_VENTA enum('PRESENCIAL','WEB') NOT NULL DEFAULT 'PRESENCIAL',
  ESTADO_VENTA varchar(20) NOT NULL DEFAULT 'PENDIENTE',
  OBSERVACIONES varchar(500) DEFAULT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  NUMERO_BOLETA varchar(20) DEFAULT NULL COMMENT 'Número de comprobante',
  RUC_EMPRESA varchar(11) DEFAULT NULL COMMENT 'RUC de la empresa emisora',
  CONTACTO_CLIENTE varchar(20) DEFAULT NULL COMMENT 'Teléfono/contacto del cliente',
  MENSAJE_BOLETA varchar(255) DEFAULT NULL COMMENT 'Mensaje personalizado en boleta',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (VENTA_ID),
  KEY fk_venta_cliente_idx (CLIENTE_ID),
  KEY fk_venta_trabajador_idx (TRABAJADOR_ID),
  KEY fk_venta_metodo_pago_idx (METODO_PAGO_ID),
  CONSTRAINT fk_venta_cliente FOREIGN KEY (CLIENTE_ID) REFERENCES cliente (USUARIO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_venta_metodo_pago FOREIGN KEY (METODO_PAGO_ID) REFERENCES metodo_pago (METODO_PAGO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_venta_trabajador FOREIGN KEY (TRABAJADOR_ID) REFERENCES usuario (USUARIO_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Registro de ventas/comprobantes de pago. Contiene la información completa de cada transacción.';

CREATE TABLE detalle_venta (
  DETALLE_VENTA_ID int NOT NULL AUTO_INCREMENT,
  VENTA_ID int NOT NULL,
  PRODUCTO_ID int NOT NULL,
  DESCRIPCION varchar(100) DEFAULT NULL COMMENT 'Descripción opcional de la línea de detalle.',
  PRECIO_UNITARIO decimal(10,2) NOT NULL,
  CANTIDAD int NOT NULL,
  SUBTOTAL decimal(10,2) NOT NULL,
  PRIMARY KEY (DETALLE_VENTA_ID),
  KEY fk_det_venta_venta_idx (VENTA_ID),
  KEY fk_det_venta_producto_idx (PRODUCTO_ID),
  CONSTRAINT fk_det_venta_producto FOREIGN KEY (PRODUCTO_ID) REFERENCES producto (PRODUCTO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_det_venta_venta FOREIGN KEY (VENTA_ID) REFERENCES venta (VENTA_ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE boleta (
  id bigint NOT NULL AUTO_INCREMENT,
  venta_id int NOT NULL,
  serie varchar(4) NOT NULL COMMENT 'B001, B002, etc.',
  numero int NOT NULL COMMENT 'Correlativo',
  fecha_emision date NOT NULL,
  cliente_tipo_documento varchar(2) NOT NULL COMMENT '1=DNI, 6=RUC',
  cliente_numero_documento varchar(15) NOT NULL,
  cliente_denominacion varchar(100) NOT NULL,
  cliente_direccion varchar(100) DEFAULT NULL,
  cliente_email varchar(250) DEFAULT NULL,
  moneda int DEFAULT '1' COMMENT '1=Soles',
  porcentaje_igv decimal(5,2) DEFAULT '18.00',
  total_gravada decimal(12,2) DEFAULT '0.00',
  total_igv decimal(12,2) DEFAULT '0.00',
  total decimal(12,2) NOT NULL,
  nubefact_enlace varchar(500) DEFAULT NULL,
  nubefact_enlace_pdf varchar(500) DEFAULT NULL,
  nubefact_enlace_xml varchar(500) DEFAULT NULL,
  nubefact_enlace_cdr varchar(500) DEFAULT NULL,
  nubefact_cadena_qr text,
  nubefact_codigo_hash varchar(100) DEFAULT NULL,
  aceptada_por_sunat tinyint(1) DEFAULT '0',
  sunat_response_code varchar(10) DEFAULT NULL COMMENT '0=aceptada',
  sunat_description text,
  anulado tinyint(1) DEFAULT '0',
  anulacion_motivo varchar(100) DEFAULT NULL,
  fecha_registro timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  usuario_registro varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_serie_numero (serie,numero),
  KEY fk_boleta_venta (venta_id),
  KEY idx_cliente (cliente_numero_documento),
  KEY idx_fecha (fecha_emision),
  CONSTRAINT fk_boleta_venta FOREIGN KEY (venta_id) REFERENCES venta (VENTA_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Boletas electrónicas (versión simple)';

CREATE TABLE boleta_detalle (
  id bigint NOT NULL AUTO_INCREMENT,
  id_boleta bigint NOT NULL,
  id_producto bigint DEFAULT NULL,
  unidad_medida varchar(5) NOT NULL DEFAULT 'NIU',
  descripcion text NOT NULL,
  cantidad decimal(12,2) NOT NULL,
  valor_unitario decimal(12,2) NOT NULL COMMENT 'Sin IGV',
  precio_unitario decimal(12,2) NOT NULL COMMENT 'Con IGV',
  subtotal decimal(12,2) NOT NULL,
  igv decimal(12,2) NOT NULL,
  total decimal(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_boleta (id_boleta),
  CONSTRAINT boleta_detalle_ibfk_1 FOREIGN KEY (id_boleta) REFERENCES boleta (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE pedido (
  PEDIDO_ID int NOT NULL AUTO_INCREMENT,
  CLIENTE_ID int NOT NULL,
  VENTA_ID int DEFAULT NULL,
  FECHA_HORA datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  MONTO_TOTAL decimal(10,2) NOT NULL DEFAULT '0.00',
  ESTADO_PEDIDO enum('RECIBIDO','EN_PROCESO','ATENDIDO','RECHAZADO','CANCELADO') NOT NULL DEFAULT 'RECIBIDO',
  PRIORIDAD int NOT NULL DEFAULT '0',
  DIRECCION_ENTREGA varchar(255) DEFAULT NULL,
  MODALIDAD_ENTREGA enum('DELIVERY','RECOJO_TIENDA') NOT NULL DEFAULT 'DELIVERY',
  OBSERVACIONES varchar(500) DEFAULT NULL,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (PEDIDO_ID),
  KEY fk_pedido_cliente_idx (CLIENTE_ID),
  KEY fk_pedido_venta_idx (VENTA_ID),
  CONSTRAINT fk_pedido_cliente FOREIGN KEY (CLIENTE_ID) REFERENCES cliente (USUARIO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_pedido_venta FOREIGN KEY (VENTA_ID) REFERENCES venta (VENTA_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE detalle_pedido (
  DETALLE_PEDIDO_ID int NOT NULL AUTO_INCREMENT,
  PEDIDO_ID int NOT NULL,
  PRODUCTO_ID int NOT NULL,
  CANTIDAD int NOT NULL,
  PRECIO_UNITARIO decimal(10,2) NOT NULL,
  SUBTOTAL decimal(10,2) NOT NULL,
  DISPONIBLE tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (DETALLE_PEDIDO_ID),
  KEY fk_det_pedido_pedido_idx (PEDIDO_ID),
  KEY fk_det_pedido_producto_idx (PRODUCTO_ID),
  CONSTRAINT fk_det_pedido_pedido FOREIGN KEY (PEDIDO_ID) REFERENCES pedido (PEDIDO_ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_det_pedido_producto FOREIGN KEY (PRODUCTO_ID) REFERENCES producto (PRODUCTO_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE pago (
  PAGO_ID int NOT NULL AUTO_INCREMENT,
  PEDIDO_ID int NOT NULL,
  METODO_PAGO_ID int NOT NULL,
  MONTO decimal(10,2) NOT NULL DEFAULT '0.00',
  MONEDA varchar(3) NOT NULL DEFAULT 'PEN',
  ESTADO enum('PENDIENTE','AUTORIZADO','RECHAZADO','CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
  REFERENCIA varchar(100) DEFAULT NULL COMMENT 'ID de transacción del proveedor (vads_trans_id)',
  ORDER_ID varchar(50) DEFAULT NULL COMMENT 'Identificador de orden enviado a la pasarela (vads_order_id)',
  FECHA_PAGO datetime DEFAULT NULL COMMENT 'Momento en que la pasarela confirmó el pago',
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (PAGO_ID),
  KEY fk_pago_pedido_idx (PEDIDO_ID),
  KEY fk_pago_metodo_pago_idx (METODO_PAGO_ID),
  KEY idx_pago_order (ORDER_ID),
  CONSTRAINT fk_pago_metodo_pago FOREIGN KEY (METODO_PAGO_ID) REFERENCES metodo_pago (METODO_PAGO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_pago_pedido FOREIGN KEY (PEDIDO_ID) REFERENCES pedido (PEDIDO_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Transacciones de pago de los pedidos (integración con pasarela Izipay).';

CREATE TABLE devolucion (
  DEVOLUCION_ID int NOT NULL AUTO_INCREMENT,
  PRODUCTO_ID int DEFAULT NULL,
  PEDIDO_ID int DEFAULT NULL,
  VENTA_ID int DEFAULT NULL,
  USUARIO_REGISTRA_ID int DEFAULT NULL,
  ESTADO_DEVOLUCION enum('PENDIENTE','APROBADO','RECHAZADO') NOT NULL DEFAULT 'PENDIENTE',
  CANTIDAD int DEFAULT '0',
  MOTIVO varchar(500) DEFAULT NULL,
  OBSERVACIONES varchar(500) DEFAULT NULL,
  FECHA_HORA datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ACTIVO tinyint NOT NULL DEFAULT '1',
  FECHA_CREACION datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha automática de creación',
  FECHA_MODIFICACION datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha automática de última modificación',
  USUARIO_CREACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que creó',
  USUARIO_MODIFICACION varchar(100) DEFAULT NULL COMMENT 'Nombre del usuario que modificó',
  PRIMARY KEY (DEVOLUCION_ID),
  KEY fk_devolucion_producto_idx (PRODUCTO_ID),
  KEY fk_devolucion_pedido_idx (PEDIDO_ID),
  KEY fk_devolucion_venta_idx (VENTA_ID),
  KEY fk_devolucion_usuario_registra_idx (USUARIO_REGISTRA_ID),
  CONSTRAINT fk_devolucion_pedido FOREIGN KEY (PEDIDO_ID) REFERENCES pedido (PEDIDO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_devolucion_producto FOREIGN KEY (PRODUCTO_ID) REFERENCES producto (PRODUCTO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_devolucion_usuario_registra FOREIGN KEY (USUARIO_REGISTRA_ID) REFERENCES usuario (USUARIO_ID) ON UPDATE CASCADE,
  CONSTRAINT fk_devolucion_venta FOREIGN KEY (VENTA_ID) REFERENCES venta (VENTA_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tabla de devoluciones asociadas a ventas presenciales u online';

CREATE TABLE detalle_devolucion (
  DEVOLUCION_ID int NOT NULL,
  PRODUCTO_ID int NOT NULL,
  CANTIDAD int NOT NULL,
  PRIMARY KEY (DEVOLUCION_ID,PRODUCTO_ID),
  KEY fk_dd_devolucion_idx (DEVOLUCION_ID),
  KEY fk_dd_producto_idx (PRODUCTO_ID),
  KEY idx_detalle_devolucion (DEVOLUCION_ID),
  KEY idx_detalle_producto (PRODUCTO_ID),
  CONSTRAINT fk_dd_devolucion FOREIGN KEY (DEVOLUCION_ID) REFERENCES devolucion (DEVOLUCION_ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_dd_producto FOREIGN KEY (PRODUCTO_ID) REFERENCES producto (PRODUCTO_ID) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
