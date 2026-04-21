-- =====================================================================
-- SHILIGAMA - Procedimientos Almacenados
-- Estilo: Profesor Freddy Paz (CallableStatement + parámetros OUT)
-- =====================================================================
USE `shiligama`;

DELIMITER $$

-- =============================================================
-- MÓDULO 1: USUARIOS
-- =============================================================

-- ---------------------------------------------------------------
-- INSERTAR_USUARIO: Inserta en tabla padre y devuelve el ID generado
-- Usado por: todas las subclases antes de insertar en su tabla
-- ---------------------------------------------------------------
CREATE PROCEDURE INSERTAR_USUARIO(
    OUT _usuario_id INT,
    IN  _nombre_usuario VARCHAR(50),
    IN  _contrasena VARCHAR(255),
    IN  _nombres VARCHAR(100),
    IN  _apellidos VARCHAR(100),
    IN  _dni VARCHAR(8),
    IN  _telefono VARCHAR(15),
    IN  _email VARCHAR(100),
    IN  _direccion VARCHAR(255)
)
BEGIN
    INSERT INTO usuarios(NOMBRE_USUARIO, CONTRASENA, NOMBRES, APELLIDOS,
                         DNI, TELEFONO, EMAIL, DIRECCION)
    VALUES(_nombre_usuario, _contrasena, _nombres, _apellidos,
           _dni, _telefono, _email, _direccion);
    SET _usuario_id = LAST_INSERT_ID();
END$$

-- ---------------------------------------------------------------
-- INSERTAR_CLIENTE: Primero inserta usuario, luego cliente
-- ---------------------------------------------------------------
CREATE PROCEDURE INSERTAR_CLIENTE(
    OUT _cliente_id INT,
    IN  _nombre_usuario VARCHAR(50),
    IN  _contrasena VARCHAR(255),
    IN  _nombres VARCHAR(100),
    IN  _apellidos VARCHAR(100),
    IN  _dni VARCHAR(8),
    IN  _telefono VARCHAR(15),
    IN  _email VARCHAR(100),
    IN  _direccion VARCHAR(255),
    IN  _telefono_whatsapp VARCHAR(15),
    IN  _direccion_entrega VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;

    -- Insertar en tabla padre
    INSERT INTO usuarios(NOMBRE_USUARIO, CONTRASENA, NOMBRES, APELLIDOS,
                         DNI, TELEFONO, EMAIL, DIRECCION)
    VALUES(_nombre_usuario, _contrasena, _nombres, _apellidos,
           _dni, _telefono, _email, _direccion);
    SET v_usuario_id = LAST_INSERT_ID();

    -- Insertar en tabla hija
    INSERT INTO clientes(USUARIO_ID, TELEFONO_WHATSAPP, DIRECCION_ENTREGA)
    VALUES(v_usuario_id, _telefono_whatsapp, _direccion_entrega);
    SET _cliente_id = LAST_INSERT_ID();
END$$

-- ---------------------------------------------------------------
-- INSERTAR_TRABAJADOR
-- ---------------------------------------------------------------
CREATE PROCEDURE INSERTAR_TRABAJADOR(
    OUT _trabajador_id INT,
    IN  _nombre_usuario VARCHAR(50),
    IN  _contrasena VARCHAR(255),
    IN  _nombres VARCHAR(100),
    IN  _apellidos VARCHAR(100),
    IN  _dni VARCHAR(8),
    IN  _telefono VARCHAR(15),
    IN  _email VARCHAR(100),
    IN  _direccion VARCHAR(255),
    IN  _cargo VARCHAR(100),
    IN  _fecha_ingreso DATE
)
BEGIN
    DECLARE v_usuario_id INT;

    INSERT INTO usuarios(NOMBRE_USUARIO, CONTRASENA, NOMBRES, APELLIDOS,
                         DNI, TELEFONO, EMAIL, DIRECCION)
    VALUES(_nombre_usuario, _contrasena, _nombres, _apellidos,
           _dni, _telefono, _email, _direccion);
    SET v_usuario_id = LAST_INSERT_ID();

    INSERT INTO trabajadores(USUARIO_ID, CARGO, FECHA_INGRESO)
    VALUES(v_usuario_id, _cargo, _fecha_ingreso);
    SET _trabajador_id = LAST_INSERT_ID();
END$$

-- ---------------------------------------------------------------
-- INSERTAR_ADMINISTRADOR
-- ---------------------------------------------------------------
CREATE PROCEDURE INSERTAR_ADMINISTRADOR(
    OUT _administrador_id INT,
    IN  _nombre_usuario VARCHAR(50),
    IN  _contrasena VARCHAR(255),
    IN  _nombres VARCHAR(100),
    IN  _apellidos VARCHAR(100),
    IN  _dni VARCHAR(8),
    IN  _telefono VARCHAR(15),
    IN  _email VARCHAR(100),
    IN  _direccion VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;

    INSERT INTO usuarios(NOMBRE_USUARIO, CONTRASENA, NOMBRES, APELLIDOS,
                         DNI, TELEFONO, EMAIL, DIRECCION)
    VALUES(_nombre_usuario, _contrasena, _nombres, _apellidos,
           _dni, _telefono, _email, _direccion);
    SET v_usuario_id = LAST_INSERT_ID();

    INSERT INTO administradores(USUARIO_ID)
    VALUES(v_usuario_id);
    SET _administrador_id = LAST_INSERT_ID();
END$$

-- ---------------------------------------------------------------
-- MODIFICAR_CLIENTE
-- ---------------------------------------------------------------
CREATE PROCEDURE MODIFICAR_CLIENTE(
    IN _cliente_id INT,
    IN _nombres VARCHAR(100),
    IN _apellidos VARCHAR(100),
    IN _telefono VARCHAR(15),
    IN _email VARCHAR(100),
    IN _direccion VARCHAR(255),
    IN _telefono_whatsapp VARCHAR(15),
    IN _direccion_entrega VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;

    -- Obtener el usuario_id del cliente
    SELECT USUARIO_ID INTO v_usuario_id
    FROM clientes WHERE CLIENTE_ID = _cliente_id;

    -- Actualizar tabla padre
    UPDATE usuarios SET
        NOMBRES = _nombres,
        APELLIDOS = _apellidos,
        TELEFONO = _telefono,
        EMAIL = _email,
        DIRECCION = _direccion
    WHERE USUARIO_ID = v_usuario_id;

    -- Actualizar tabla hija
    UPDATE clientes SET
        TELEFONO_WHATSAPP = _telefono_whatsapp,
        DIRECCION_ENTREGA = _direccion_entrega
    WHERE CLIENTE_ID = _cliente_id;
END$$

-- ---------------------------------------------------------------
-- ELIMINAR_USUARIO (eliminación lógica - soft delete)
-- ---------------------------------------------------------------
CREATE PROCEDURE ELIMINAR_USUARIO(
    IN _usuario_id INT
)
BEGIN
    UPDATE usuarios SET ACTIVO = 0 WHERE USUARIO_ID = _usuario_id;
END$$

-- ---------------------------------------------------------------
-- LISTAR_CLIENTES
-- ---------------------------------------------------------------
CREATE PROCEDURE LISTAR_CLIENTES()
BEGIN
    SELECT c.CLIENTE_ID, u.USUARIO_ID, u.NOMBRE_USUARIO, u.NOMBRES,
           u.APELLIDOS, u.DNI, u.TELEFONO, u.EMAIL, u.DIRECCION,
           c.TELEFONO_WHATSAPP, c.DIRECCION_ENTREGA, c.FECHA_REGISTRO
    FROM clientes c
    INNER JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.ACTIVO = 1;
END$$

-- ---------------------------------------------------------------
-- BUSCAR_CLIENTE_POR_ID
-- ---------------------------------------------------------------
CREATE PROCEDURE BUSCAR_CLIENTE_POR_ID(
    IN _cliente_id INT
)
BEGIN
    SELECT c.CLIENTE_ID, u.USUARIO_ID, u.NOMBRE_USUARIO, u.NOMBRES,
           u.APELLIDOS, u.DNI, u.TELEFONO, u.EMAIL, u.DIRECCION,
           c.TELEFONO_WHATSAPP, c.DIRECCION_ENTREGA, c.FECHA_REGISTRO
    FROM clientes c
    INNER JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE c.CLIENTE_ID = _cliente_id AND u.ACTIVO = 1;
END$$

-- =============================================================
-- MÓDULO 2: CATEGORÍAS Y PRODUCTOS
-- =============================================================

CREATE PROCEDURE INSERTAR_CATEGORIA(
    OUT _categoria_id INT,
    IN  _nombre VARCHAR(100),
    IN  _descripcion VARCHAR(255),
    IN  _categoria_padre_id INT
)
BEGIN
    INSERT INTO categorias(NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID)
    VALUES(_nombre, _descripcion, _categoria_padre_id);
    SET _categoria_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_CATEGORIA(
    IN _categoria_id INT,
    IN _nombre VARCHAR(100),
    IN _descripcion VARCHAR(255),
    IN _categoria_padre_id INT
)
BEGIN
    UPDATE categorias SET
        NOMBRE = _nombre,
        DESCRIPCION = _descripcion,
        CATEGORIA_PADRE_ID = _categoria_padre_id
    WHERE CATEGORIA_ID = _categoria_id;
END$$

CREATE PROCEDURE ELIMINAR_CATEGORIA(
    IN _categoria_id INT
)
BEGIN
    UPDATE categorias SET ACTIVO = 0 WHERE CATEGORIA_ID = _categoria_id;
END$$

CREATE PROCEDURE LISTAR_CATEGORIAS()
BEGIN
    SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO
    FROM categorias WHERE ACTIVO = 1;
END$$


CREATE PROCEDURE BUSCAR_CATEGORIA_POR_ID(
    IN _categoria_id INT
)
BEGIN
    SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO
    FROM categorias 
    WHERE CATEGORIA_ID = _categoria_id;
END$$


CREATE PROCEDURE INSERTAR_PRODUCTO(
    OUT _producto_id INT,
    IN  _categoria_id INT,
    IN  _nombre VARCHAR(150),
    IN  _descripcion VARCHAR(500),
    IN  _precio_unitario DECIMAL(10,2),
    IN  _stock INT,
    IN  _stock_minimo INT,
    IN  _unidad_medida VARCHAR(30),
    IN  _codigo_barras VARCHAR(50),
    IN  _imagen_url VARCHAR(500)
)
BEGIN
    INSERT INTO productos(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO,
                          STOCK, STOCK_MINIMO, UNIDAD_MEDIDA, CODIGO_BARRAS, IMAGEN_URL)
    VALUES(_categoria_id, _nombre, _descripcion, _precio_unitario,
           _stock, _stock_minimo, _unidad_medida, _codigo_barras, _imagen_url);
    SET _producto_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_PRODUCTO(
    IN _producto_id INT,
    IN _categoria_id INT,
    IN _nombre VARCHAR(150),
    IN _descripcion VARCHAR(500),
    IN _precio_unitario DECIMAL(10,2),
    IN _stock_minimo INT,
    IN _unidad_medida VARCHAR(30),
    IN _codigo_barras VARCHAR(50),
    IN _imagen_url VARCHAR(500)
)
BEGIN
    UPDATE productos SET
        CATEGORIA_ID = _categoria_id,
        NOMBRE = _nombre,
        DESCRIPCION = _descripcion,
        PRECIO_UNITARIO = _precio_unitario,
        STOCK_MINIMO = _stock_minimo,
        UNIDAD_MEDIDA = _unidad_medida,
        CODIGO_BARRAS = _codigo_barras,
        IMAGEN_URL = _imagen_url
    WHERE PRODUCTO_ID = _producto_id;
END$$

CREATE PROCEDURE ELIMINAR_PRODUCTO(
    IN _producto_id INT
)
BEGIN
    UPDATE productos SET ACTIVO = 0 WHERE PRODUCTO_ID = _producto_id;
END$$

CREATE PROCEDURE LISTAR_PRODUCTOS()
BEGIN
    SELECT p.PRODUCTO_ID, p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE,
           p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
           p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS,
           p.IMAGEN_URL, p.ACTIVO, p.FECHA_REGISTRO
    FROM productos p
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.ACTIVO = 1;
END$$

CREATE PROCEDURE BUSCAR_PRODUCTO_POR_ID(
    IN _producto_id INT
)
BEGIN
    SELECT p.PRODUCTO_ID, p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE,
           p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
           p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS,
           p.IMAGEN_URL, p.ACTIVO, p.FECHA_REGISTRO
    FROM productos p
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.PRODUCTO_ID = _producto_id;
END$$

-- =============================================================
-- MÓDULO 3: INVENTARIO
-- =============================================================

-- ---------------------------------------------------------------
-- REGISTRAR_MOVIMIENTO_INVENTARIO
-- Actualiza stock del producto y registra el movimiento
-- ---------------------------------------------------------------
CREATE PROCEDURE REGISTRAR_MOVIMIENTO_INVENTARIO(
    OUT _movimiento_id INT,
    IN  _producto_id INT,
    IN  _trabajador_id INT,
    IN  _tipo_movimiento VARCHAR(20),
    IN  _cantidad INT,
    IN  _motivo VARCHAR(255)
)
BEGIN
    DECLARE v_stock_actual INT;

    -- Obtener stock actual
    SELECT STOCK INTO v_stock_actual
    FROM productos WHERE PRODUCTO_ID = _producto_id;

    -- Actualizar stock según tipo
    IF _tipo_movimiento = 'ENTRADA' OR _tipo_movimiento = 'DEVOLUCION' THEN
        UPDATE productos SET STOCK = STOCK + _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    ELSEIF _tipo_movimiento = 'SALIDA' THEN
        UPDATE productos SET STOCK = STOCK - _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    ELSEIF _tipo_movimiento = 'AJUSTE' THEN
        UPDATE productos SET STOCK = _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    END IF;

    -- Registrar movimiento
    INSERT INTO movimientos_inventario(PRODUCTO_ID, TRABAJADOR_ID,
        TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO)
    VALUES(_producto_id, _trabajador_id, _tipo_movimiento,
           _cantidad, v_stock_actual,
           CASE
               WHEN _tipo_movimiento IN ('ENTRADA','DEVOLUCION') THEN v_stock_actual + _cantidad
               WHEN _tipo_movimiento = 'SALIDA' THEN v_stock_actual - _cantidad
               ELSE _cantidad
           END,
           _motivo);
    SET _movimiento_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE LISTAR_MOVIMIENTOS_POR_PRODUCTO(
    IN _producto_id INT
)
BEGIN
    SELECT m.MOVIMIENTO_ID, m.PRODUCTO_ID, p.NOMBRE AS PRODUCTO_NOMBRE,
           m.TRABAJADOR_ID, u.NOMBRES AS TRABAJADOR_NOMBRES,
           m.TIPO_MOVIMIENTO, m.CANTIDAD, m.STOCK_ANTERIOR,
           m.STOCK_RESULTANTE, m.MOTIVO, m.FECHA_HORA
    FROM movimientos_inventario m
    INNER JOIN productos p ON m.PRODUCTO_ID = p.PRODUCTO_ID
    LEFT JOIN trabajadores t ON m.TRABAJADOR_ID = t.TRABAJADOR_ID
    LEFT JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE m.PRODUCTO_ID = _producto_id
    ORDER BY m.FECHA_HORA DESC;
END$$

-- =============================================================
-- MÓDULO 4: VENTAS
-- =============================================================

-- ---------------------------------------------------------------
-- INSERTAR_VENTA: Crea cabecera de venta
-- ---------------------------------------------------------------
CREATE PROCEDURE INSERTAR_VENTA(
    OUT _venta_id INT,
    IN  _cliente_id INT,
    IN  _trabajador_id INT,
    IN  _metodo_pago_id INT,
    IN  _canal_venta VARCHAR(20),
    IN  _observaciones VARCHAR(500)
)
BEGIN
    INSERT INTO ventas(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID,
                       CANAL_VENTA, OBSERVACIONES)
    VALUES(_cliente_id, _trabajador_id, _metodo_pago_id,
           _canal_venta, _observaciones);
    SET _venta_id = LAST_INSERT_ID();
END$$

-- ---------------------------------------------------------------
-- INSERTAR_DETALLE_VENTA: Agrega línea y descuenta stock
-- ---------------------------------------------------------------
CREATE PROCEDURE INSERTAR_DETALLE_VENTA(
    OUT _detalle_venta_id INT,
    IN  _venta_id INT,
    IN  _producto_id INT,
    IN  _cantidad INT
)
BEGIN
    DECLARE v_precio DECIMAL(10,2);
    DECLARE v_subtotal DECIMAL(10,2);

    -- Obtener precio actual del producto
    SELECT PRECIO_UNITARIO INTO v_precio
    FROM productos WHERE PRODUCTO_ID = _producto_id;

    SET v_subtotal = v_precio * _cantidad;

    -- Insertar detalle
    INSERT INTO detalles_venta(VENTA_ID, PRODUCTO_ID, CANTIDAD,
                               PRECIO_UNITARIO, SUBTOTAL)
    VALUES(_venta_id, _producto_id, _cantidad, v_precio, v_subtotal);
    SET _detalle_venta_id = LAST_INSERT_ID();

    -- Descontar stock
    UPDATE productos SET STOCK = STOCK - _cantidad
    WHERE PRODUCTO_ID = _producto_id;

    -- Actualizar monto total de la venta
    UPDATE ventas SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_venta WHERE VENTA_ID = _venta_id
    ) WHERE VENTA_ID = _venta_id;
END$$

-- ---------------------------------------------------------------
-- COMPLETAR_VENTA: Cambia estado a COMPLETADA
-- ---------------------------------------------------------------
CREATE PROCEDURE COMPLETAR_VENTA(
    IN _venta_id INT
)
BEGIN
    UPDATE ventas SET ESTADO_VENTA = 'COMPLETADA' WHERE VENTA_ID = _venta_id;
END$$

-- ---------------------------------------------------------------
-- ANULAR_VENTA: Anula venta y devuelve stock
-- ---------------------------------------------------------------
CREATE PROCEDURE ANULAR_VENTA(
    IN _venta_id INT
)
BEGIN
    -- Devolver stock de cada detalle
    UPDATE productos p
    INNER JOIN detalles_venta dv ON p.PRODUCTO_ID = dv.PRODUCTO_ID
    SET p.STOCK = p.STOCK + dv.CANTIDAD
    WHERE dv.VENTA_ID = _venta_id;

    -- Marcar como anulada
    UPDATE ventas SET ESTADO_VENTA = 'ANULADA' WHERE VENTA_ID = _venta_id;
END$$

CREATE PROCEDURE LISTAR_VENTAS()
BEGIN
    SELECT v.VENTA_ID, v.CLIENTE_ID,
           CONCAT(uc.NOMBRES, ' ', uc.APELLIDOS) AS CLIENTE_NOMBRE,
           v.TRABAJADOR_ID,
           CONCAT(ut.NOMBRES, ' ', ut.APELLIDOS) AS TRABAJADOR_NOMBRE,
           v.METODO_PAGO_ID, mp.NOMBRE AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES
    FROM ventas v
    LEFT JOIN clientes c ON v.CLIENTE_ID = c.CLIENTE_ID
    LEFT JOIN usuarios uc ON c.USUARIO_ID = uc.USUARIO_ID
    LEFT JOIN trabajadores t ON v.TRABAJADOR_ID = t.TRABAJADOR_ID
    LEFT JOIN usuarios ut ON t.USUARIO_ID = ut.USUARIO_ID
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    ORDER BY v.FECHA_HORA DESC;
END$$

-- =============================================================
-- MÓDULO 5: PEDIDOS
-- =============================================================

CREATE PROCEDURE INSERTAR_PEDIDO(
    OUT _pedido_id INT,
    IN  _cliente_id INT,
    IN  _direccion_entrega VARCHAR(255),
    IN  _modalidad_entrega VARCHAR(20),
    IN  _observaciones VARCHAR(500)
)
BEGIN
    DECLARE v_prioridad INT;
    -- Prioridad FIFO: siguiente número
    SELECT COALESCE(MAX(PRIORIDAD), 0) + 1 INTO v_prioridad FROM pedidos
    WHERE ESTADO_PEDIDO IN ('RECIBIDO', 'EN_PROCESO');

    INSERT INTO pedidos(CLIENTE_ID, DIRECCION_ENTREGA, MODALIDAD_ENTREGA,
                        PRIORIDAD, OBSERVACIONES)
    VALUES(_cliente_id, _direccion_entrega, _modalidad_entrega,
           v_prioridad, _observaciones);
    SET _pedido_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE INSERTAR_DETALLE_PEDIDO(
    OUT _detalle_pedido_id INT,
    IN  _pedido_id INT,
    IN  _producto_id INT,
    IN  _cantidad INT
)
BEGIN
    DECLARE v_precio DECIMAL(10,2);
    DECLARE v_stock INT;
    DECLARE v_disponible TINYINT;

    SELECT PRECIO_UNITARIO, STOCK INTO v_precio, v_stock
    FROM productos WHERE PRODUCTO_ID = _producto_id;

    SET v_disponible = IF(v_stock >= _cantidad, 1, 0);

    INSERT INTO detalles_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD,
                                PRECIO_UNITARIO, SUBTOTAL, DISPONIBLE)
    VALUES(_pedido_id, _producto_id, _cantidad,
           v_precio, v_precio * _cantidad, v_disponible);
    SET _detalle_pedido_id = LAST_INSERT_ID();

    -- Actualizar monto total del pedido
    UPDATE pedidos SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_pedido WHERE PEDIDO_ID = _pedido_id
    ) WHERE PEDIDO_ID = _pedido_id;
END$$

CREATE PROCEDURE MODIFICAR_ESTADO_PEDIDO(
    IN _pedido_id INT,
    IN _estado VARCHAR(20)
)
BEGIN
    UPDATE pedidos SET ESTADO_PEDIDO = _estado WHERE PEDIDO_ID = _pedido_id;
END$$

CREATE PROCEDURE LISTAR_PEDIDOS_POR_CLIENTE(
    IN _cliente_id INT
)
BEGIN
    SELECT p.PEDIDO_ID, p.CLIENTE_ID, p.FECHA_HORA, p.MONTO_TOTAL,
           p.ESTADO_PEDIDO, p.PRIORIDAD, p.DIRECCION_ENTREGA,
           p.MODALIDAD_ENTREGA, p.OBSERVACIONES
    FROM pedidos p
    WHERE p.CLIENTE_ID = _cliente_id
    ORDER BY p.FECHA_HORA DESC;
END$$

-- =============================================================
-- MÓDULO 6: PROVEEDORES
-- =============================================================

CREATE PROCEDURE INSERTAR_PROVEEDOR(
    OUT _proveedor_id INT,
    IN  _razon_social VARCHAR(150),
    IN  _ruc VARCHAR(11),
    IN  _telefono VARCHAR(15),
    IN  _email VARCHAR(100),
    IN  _direccion VARCHAR(255),
    IN  _contacto VARCHAR(100)
)
BEGIN
    INSERT INTO proveedores(RAZON_SOCIAL, RUC, TELEFONO, EMAIL, DIRECCION, CONTACTO)
    VALUES(_razon_social, _ruc, _telefono, _email, _direccion, _contacto);
    SET _proveedor_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_PROVEEDOR(
    IN _proveedor_id INT,
    IN _razon_social VARCHAR(150),
    IN _ruc VARCHAR(11),
    IN _telefono VARCHAR(15),
    IN _email VARCHAR(100),
    IN _direccion VARCHAR(255),
    IN _contacto VARCHAR(100)
)
BEGIN
    UPDATE proveedores SET
        RAZON_SOCIAL = _razon_social, RUC = _ruc, TELEFONO = _telefono,
        EMAIL = _email, DIRECCION = _direccion, CONTACTO = _contacto
    WHERE PROVEEDOR_ID = _proveedor_id;
END$$

CREATE PROCEDURE ELIMINAR_PROVEEDOR(IN _proveedor_id INT)
BEGIN
    UPDATE proveedores SET ACTIVO = 0 WHERE PROVEEDOR_ID = _proveedor_id;
END$$

CREATE PROCEDURE LISTAR_PROVEEDORES()
BEGIN
    SELECT PROVEEDOR_ID, RAZON_SOCIAL, RUC, TELEFONO, EMAIL,
           DIRECCION, CONTACTO, ACTIVO
    FROM proveedores WHERE ACTIVO = 1;
END$$

-- =============================================================
-- Procedimientos faltantes: Métodos de pago, Órdenes, Junctions
-- =============================================================

CREATE PROCEDURE INSERTAR_METODO_PAGO(
    OUT _metodo_pago_id INT,
    IN  _nombre VARCHAR(50),
    IN  _descripcion VARCHAR(255)
)
BEGIN
    INSERT INTO metodos_pago(NOMBRE, DESCRIPCION)
    VALUES(_nombre, _descripcion);
    SET _metodo_pago_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_METODO_PAGO(
    IN _metodo_pago_id INT,
    IN _nombre VARCHAR(50),
    IN _descripcion VARCHAR(255)
)
BEGIN
    UPDATE metodos_pago SET NOMBRE = _nombre, DESCRIPCION = _descripcion
    WHERE METODO_PAGO_ID = _metodo_pago_id;
END$$

CREATE PROCEDURE LISTAR_METODOS_PAGO()
BEGIN
    SELECT METODO_PAGO_ID, NOMBRE, DESCRIPCION, ACTIVO
    FROM metodos_pago WHERE ACTIVO = 1;
END$$

-- Órdenes de reabastecimiento
CREATE PROCEDURE INSERTAR_ORDEN_REABASTECIMIENTO(
    OUT _orden_id INT,
    IN  _proveedor_id INT,
    IN  _trabajador_id INT,
    IN  _fecha_entrega_estimada DATE,
    IN  _observaciones VARCHAR(500)
)
BEGIN
    INSERT INTO ordenes_reabastecimiento(PROVEEDOR_ID, TRABAJADOR_ID,
        FECHA_ENTREGA_ESTIMADA, OBSERVACIONES)
    VALUES(_proveedor_id, _trabajador_id, _fecha_entrega_estimada, _observaciones);
    SET _orden_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE INSERTAR_DETALLE_ORDEN_REABASTECIMIENTO(
    OUT _detalle_orden_id INT,
    IN  _orden_id INT,
    IN  _producto_id INT,
    IN  _cantidad_solicitada INT,
    IN  _precio_compra DECIMAL(10,2)
)
BEGIN
    INSERT INTO detalles_orden_reabastecimiento(ORDEN_ID, PRODUCTO_ID,
        CANTIDAD_SOLICITADA, PRECIO_COMPRA)
    VALUES(_orden_id, _producto_id, _cantidad_solicitada, _precio_compra);
    SET _detalle_orden_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_ESTADO_ORDEN(
    IN _orden_id INT,
    IN _estado VARCHAR(20)
)
BEGIN
    UPDATE ordenes_reabastecimiento SET ESTADO_ORDEN = _estado
    WHERE ORDEN_ID = _orden_id;
END$$

CREATE PROCEDURE RECIBIR_ORDEN_REABASTECIMIENTO(
    IN _orden_id INT
)
BEGIN
    -- Actualizar cantidades recibidas = cantidades solicitadas
    UPDATE detalles_orden_reabastecimiento
    SET CANTIDAD_RECIBIDA = CANTIDAD_SOLICITADA
    WHERE ORDEN_ID = _orden_id;

    -- Sumar stock de cada producto
    UPDATE productos p
    INNER JOIN detalles_orden_reabastecimiento d ON p.PRODUCTO_ID = d.PRODUCTO_ID
    SET p.STOCK = p.STOCK + d.CANTIDAD_SOLICITADA
    WHERE d.ORDEN_ID = _orden_id;

    -- Marcar orden como recibida
    UPDATE ordenes_reabastecimiento SET ESTADO_ORDEN = 'RECIBIDA'
    WHERE ORDEN_ID = _orden_id;
END$$

CREATE PROCEDURE LISTAR_ORDENES_REABASTECIMIENTO()
BEGIN
    SELECT o.ORDEN_ID, o.PROVEEDOR_ID, pr.RAZON_SOCIAL,
           o.TRABAJADOR_ID, CONCAT(u.NOMBRES, ' ', u.APELLIDOS) AS TRABAJADOR,
           o.FECHA_CREACION, o.FECHA_ENTREGA_ESTIMADA,
           o.ESTADO_ORDEN, o.OBSERVACIONES
    FROM ordenes_reabastecimiento o
    INNER JOIN proveedores pr ON o.PROVEEDOR_ID = pr.PROVEEDOR_ID
    INNER JOIN trabajadores t ON o.TRABAJADOR_ID = t.TRABAJADOR_ID
    INNER JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID
    ORDER BY o.FECHA_CREACION DESC;
END$$

-- Junction: productos-proveedores
CREATE PROCEDURE VINCULAR_PRODUCTO_PROVEEDOR(
    IN _producto_id INT,
    IN _proveedor_id INT,
    IN _precio_compra DECIMAL(10,2),
    IN _tiempo_entrega INT
)
BEGIN
    INSERT INTO productos_proveedores(PRODUCTO_ID, PROVEEDOR_ID, PRECIO_COMPRA, TIEMPO_ENTREGA)
    VALUES(_producto_id, _proveedor_id, _precio_compra, _tiempo_entrega)
    ON DUPLICATE KEY UPDATE PRECIO_COMPRA = _precio_compra, TIEMPO_ENTREGA = _tiempo_entrega;
END$$

-- Junction: promociones-productos
CREATE PROCEDURE VINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id INT
)
BEGIN
    INSERT IGNORE INTO promociones_productos(PROMOCION_ID, PRODUCTO_ID)
    VALUES(_promocion_id, _producto_id);
END$$

CREATE PROCEDURE DESVINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id INT
)
BEGIN
    DELETE FROM promociones_productos
    WHERE PROMOCION_ID = _promocion_id AND PRODUCTO_ID = _producto_id;
END$$

-- =============================================================
-- MÓDULO 7: PROMOCIONES
-- =============================================================

CREATE PROCEDURE INSERTAR_PROMOCION(
    OUT _promocion_id INT,
    IN  _nombre VARCHAR(100),
    IN  _descripcion VARCHAR(500),
    IN  _tipo_descuento VARCHAR(20),
    IN  _valor_descuento DECIMAL(10,2),
    IN  _fecha_inicio DATE,
    IN  _fecha_fin DATE,
    IN  _condiciones VARCHAR(500)
)
BEGIN
    INSERT INTO promociones(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO,
                            FECHA_INICIO, FECHA_FIN, CONDICIONES)
    VALUES(_nombre, _descripcion, _tipo_descuento, _valor_descuento,
           _fecha_inicio, _fecha_fin, _condiciones);
    SET _promocion_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE LISTAR_PROMOCIONES_VIGENTES()
BEGIN
    SELECT PROMOCION_ID, NOMBRE, DESCRIPCION, TIPO_DESCUENTO,
           VALOR_DESCUENTO, FECHA_INICIO, FECHA_FIN, CONDICIONES
    FROM promociones
    WHERE ACTIVO = 1 AND CURDATE() BETWEEN FECHA_INICIO AND FECHA_FIN;
END$$

-- =============================================================
-- MÓDULO 8: DEVOLUCIONES
-- =============================================================

CREATE PROCEDURE INSERTAR_DEVOLUCION(
    OUT _devolucion_id INT,
    IN  _producto_id INT,
    IN  _trabajador_id INT,
    IN  _tipo_devolucion VARCHAR(20),
    IN  _cantidad INT,
    IN  _motivo VARCHAR(500)
)
BEGIN
    INSERT INTO devoluciones(PRODUCTO_ID, TRABAJADOR_ID, TIPO_DEVOLUCION,
                             CANTIDAD, MOTIVO)
    VALUES(_producto_id, _trabajador_id, _tipo_devolucion, _cantidad, _motivo);
    SET _devolucion_id = LAST_INSERT_ID();

    -- Si es devolución de cliente, devolver stock
    IF _tipo_devolucion = 'CLIENTE' THEN
        UPDATE productos SET STOCK = STOCK + _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    END IF;
END$$

-- =============================================================
-- REPORTES (RF15, RF16, RF17)
-- =============================================================

-- RF15: Reporte de ventas por período
CREATE PROCEDURE REPORTE_VENTAS_POR_PERIODO(
    IN _fecha_inicio DATE,
    IN _fecha_fin DATE
)
BEGIN
    SELECT v.VENTA_ID, v.FECHA_HORA,
           CONCAT(uc.NOMBRES, ' ', uc.APELLIDOS) AS CLIENTE,
           mp.NOMBRE AS METODO_PAGO,
           v.CANAL_VENTA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.ESTADO_VENTA
    FROM ventas v
    LEFT JOIN clientes c ON v.CLIENTE_ID = c.CLIENTE_ID
    LEFT JOIN usuarios uc ON c.USUARIO_ID = uc.USUARIO_ID
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    WHERE DATE(v.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
      AND v.ESTADO_VENTA != 'ANULADA'
    ORDER BY v.FECHA_HORA;
END$$

-- RF16: Reporte de productos con bajo stock
CREATE PROCEDURE REPORTE_PRODUCTOS_BAJO_STOCK()
BEGIN
    SELECT p.PRODUCTO_ID, p.NOMBRE, c.NOMBRE AS CATEGORIA,
           p.STOCK, p.STOCK_MINIMO, p.UNIDAD_MEDIDA,
           (p.STOCK_MINIMO - p.STOCK) AS UNIDADES_FALTANTES
    FROM productos p
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.ACTIVO = 1 AND p.STOCK < p.STOCK_MINIMO
    ORDER BY (p.STOCK_MINIMO - p.STOCK) DESC;
END$$

-- RF17: Reporte de productos más vendidos
CREATE PROCEDURE REPORTE_PRODUCTOS_MAS_VENDIDOS(
    IN _fecha_inicio DATE,
    IN _fecha_fin DATE,
    IN _limite INT
)
BEGIN
    SELECT p.PRODUCTO_ID, p.NOMBRE, c.NOMBRE AS CATEGORIA,
           SUM(dv.CANTIDAD) AS TOTAL_VENDIDO,
           SUM(dv.SUBTOTAL) AS INGRESO_TOTAL,
           COUNT(DISTINCT dv.VENTA_ID) AS NUM_VENTAS
    FROM detalles_venta dv
    INNER JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    INNER JOIN ventas v ON dv.VENTA_ID = v.VENTA_ID
    WHERE DATE(v.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
      AND v.ESTADO_VENTA != 'ANULADA'
    GROUP BY p.PRODUCTO_ID, p.NOMBRE, c.NOMBRE
    ORDER BY TOTAL_VENDIDO DESC
    LIMIT _limite;
END$$

-- Reporte de mermas/pérdidas
CREATE PROCEDURE REPORTE_PERDIDAS_POR_PERIODO(
    IN _fecha_inicio DATE,
    IN _fecha_fin DATE
)
BEGIN
    SELECT d.DEVOLUCION_ID, p.NOMBRE AS PRODUCTO, d.TIPO_DEVOLUCION,
           d.CANTIDAD, d.MOTIVO, d.FECHA_HORA,
           CONCAT(u.NOMBRES, ' ', u.APELLIDOS) AS REGISTRADO_POR
    FROM devoluciones d
    INNER JOIN productos p ON d.PRODUCTO_ID = p.PRODUCTO_ID
    INNER JOIN trabajadores t ON d.TRABAJADOR_ID = t.TRABAJADOR_ID
    INNER JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE d.TIPO_DEVOLUCION IN ('MERMA','VENCIMIENTO','DEFECTO')
      AND DATE(d.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
    ORDER BY d.FECHA_HORA DESC;
END$$

DELIMITER ;
