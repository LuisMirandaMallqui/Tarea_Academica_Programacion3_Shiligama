-- =====================================================================
-- SHILIGAMA - Procedimientos Almacenados (CORREGIDOS)
-- Alineados con: DAOs Java + DTOs (MetodoPagoDto, VentaDto,
--   DetalleVentaDto, PedidoDto, DetallePedidoDto)
-- =====================================================================
USE `shiligama`;

DELIMITER $$

-- =============================================================
-- MÓDULO 1: USUARIOS  (sin cambios, se conservan igual)
-- =============================================================

CREATE PROCEDURE INSERTAR_USUARIO(
    OUT _usuario_id INT,
    IN  _nombre_usuario VARCHAR(50),
    IN  _contrasena     VARCHAR(255),
    IN  _nombres        VARCHAR(100),
    IN  _apellidos      VARCHAR(100),
    IN  _dni            VARCHAR(8),
    IN  _telefono       VARCHAR(15),
    IN  _email          VARCHAR(100),
    IN  _direccion      VARCHAR(255)
)
BEGIN
    INSERT INTO usuarios(NOMBRE_USUARIO, CONTRASENA, NOMBRES, APELLIDOS,
                         DNI, TELEFONO, EMAIL, DIRECCION)
    VALUES(_nombre_usuario, _contrasena, _nombres, _apellidos,
           _dni, _telefono, _email, _direccion);
    SET _usuario_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE INSERTAR_CLIENTE(
    OUT _cliente_id INT,
    IN  _nombre_usuario      VARCHAR(50),
    IN  _contrasena          VARCHAR(255),
    IN  _nombres             VARCHAR(100),
    IN  _apellidos           VARCHAR(100),
    IN  _dni                 VARCHAR(8),
    IN  _telefono            VARCHAR(15),
    IN  _email               VARCHAR(100),
    IN  _direccion           VARCHAR(255),
    IN  _telefono_whatsapp   VARCHAR(15),
    IN  _direccion_entrega   VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;

    INSERT INTO usuarios(NOMBRE_USUARIO, CONTRASENA, NOMBRES, APELLIDOS,
                         DNI, TELEFONO, EMAIL, DIRECCION)
    VALUES(_nombre_usuario, _contrasena, _nombres, _apellidos,
           _dni, _telefono, _email, _direccion);
    SET v_usuario_id = LAST_INSERT_ID();

    INSERT INTO clientes(USUARIO_ID, TELEFONO_WHATSAPP, DIRECCION_ENTREGA)
    VALUES(v_usuario_id, _telefono_whatsapp, _direccion_entrega);
    SET _cliente_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE INSERTAR_TRABAJADOR(
    OUT _trabajador_id INT,
    IN  _nombre_usuario VARCHAR(50),
    IN  _contrasena     VARCHAR(255),
    IN  _nombres        VARCHAR(100),
    IN  _apellidos      VARCHAR(100),
    IN  _dni            VARCHAR(8),
    IN  _telefono       VARCHAR(15),
    IN  _email          VARCHAR(100),
    IN  _direccion      VARCHAR(255),
    IN  _cargo          VARCHAR(100),
    IN  _fecha_ingreso  DATE
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

CREATE PROCEDURE INSERTAR_ADMINISTRADOR(
    OUT _administrador_id INT,
    IN  _nombre_usuario VARCHAR(50),
    IN  _contrasena     VARCHAR(255),
    IN  _nombres        VARCHAR(100),
    IN  _apellidos      VARCHAR(100),
    IN  _dni            VARCHAR(8),
    IN  _telefono       VARCHAR(15),
    IN  _email          VARCHAR(100),
    IN  _direccion      VARCHAR(255)
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

CREATE PROCEDURE MODIFICAR_CLIENTE(
    IN _cliente_id          INT,
    IN _nombres             VARCHAR(100),
    IN _apellidos           VARCHAR(100),
    IN _telefono            VARCHAR(15),
    IN _email               VARCHAR(100),
    IN _direccion           VARCHAR(255),
    IN _telefono_whatsapp   VARCHAR(15),
    IN _direccion_entrega   VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;

    SELECT USUARIO_ID INTO v_usuario_id
    FROM clientes WHERE CLIENTE_ID = _cliente_id;

    UPDATE usuarios SET
        NOMBRES  = _nombres,
        APELLIDOS = _apellidos,
        TELEFONO  = _telefono,
        EMAIL     = _email,
        DIRECCION = _direccion
    WHERE USUARIO_ID = v_usuario_id;

    UPDATE clientes SET
        TELEFONO_WHATSAPP = _telefono_whatsapp,
        DIRECCION_ENTREGA  = _direccion_entrega
    WHERE CLIENTE_ID = _cliente_id;
END$$

CREATE PROCEDURE ELIMINAR_USUARIO(
    IN _usuario_id INT
)
BEGIN
    UPDATE usuarios SET ACTIVO = 0 WHERE USUARIO_ID = _usuario_id;
END$$

CREATE PROCEDURE LISTAR_CLIENTES()
BEGIN
    SELECT c.CLIENTE_ID, u.USUARIO_ID, u.NOMBRE_USUARIO, u.NOMBRES,
           u.APELLIDOS, u.DNI, u.TELEFONO, u.EMAIL, u.DIRECCION,
           c.TELEFONO_WHATSAPP, c.DIRECCION_ENTREGA, c.FECHA_REGISTRO
    FROM clientes c
    INNER JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.ACTIVO = 1;
END$$

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
-- MÓDULO 2: CATEGORÍAS Y PRODUCTOS  (sin cambios)
-- =============================================================

CREATE PROCEDURE INSERTAR_CATEGORIA(
    OUT _categoria_id INT,
    IN  _nombre             VARCHAR(100),
    IN  _descripcion        VARCHAR(255),
    IN  _categoria_padre_id INT
)
BEGIN
    INSERT INTO categorias(NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID)
    VALUES(_nombre, _descripcion, _categoria_padre_id);
    SET _categoria_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_CATEGORIA(
    IN _categoria_id        INT,
    IN _nombre              VARCHAR(100),
    IN _descripcion         VARCHAR(255),
    IN _categoria_padre_id  INT
)
BEGIN
    UPDATE categorias SET
        NOMBRE             = _nombre,
        DESCRIPCION        = _descripcion,
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
    IN  _categoria_id     INT,
    IN  _nombre           VARCHAR(150),
    IN  _descripcion      VARCHAR(500),
    IN  _precio_unitario  DECIMAL(10,2),
    IN  _stock            INT,
    IN  _stock_minimo     INT,
    IN  _unidad_medida    VARCHAR(30),
    IN  _codigo_barras    VARCHAR(50),
    IN  _imagen_url       VARCHAR(500)
)
BEGIN
    INSERT INTO productos(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO,
                          STOCK, STOCK_MINIMO, UNIDAD_MEDIDA, CODIGO_BARRAS, IMAGEN_URL)
    VALUES(_categoria_id, _nombre, _descripcion, _precio_unitario,
           _stock, _stock_minimo, _unidad_medida, _codigo_barras, _imagen_url);
    SET _producto_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_PRODUCTO(
    IN _producto_id    INT,
    IN _categoria_id   INT,
    IN _nombre         VARCHAR(150),
    IN _descripcion    VARCHAR(500),
    IN _precio_unitario DECIMAL(10,2),
    IN _stock_minimo   INT,
    IN _unidad_medida  VARCHAR(30),
    IN _codigo_barras  VARCHAR(50),
    IN _imagen_url     VARCHAR(500)
)
BEGIN
    UPDATE productos SET
        CATEGORIA_ID    = _categoria_id,
        NOMBRE          = _nombre,
        DESCRIPCION     = _descripcion,
        PRECIO_UNITARIO = _precio_unitario,
        STOCK_MINIMO    = _stock_minimo,
        UNIDAD_MEDIDA   = _unidad_medida,
        CODIGO_BARRAS   = _codigo_barras,
        IMAGEN_URL      = _imagen_url
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
-- MÓDULO 3: INVENTARIO  (sin cambios)
-- =============================================================

CREATE PROCEDURE REGISTRAR_MOVIMIENTO_INVENTARIO(
    OUT _movimiento_id INT,
    IN  _producto_id      INT,
    IN  _trabajador_id    INT,
    IN  _tipo_movimiento  VARCHAR(20),
    IN  _cantidad         INT,
    IN  _motivo           VARCHAR(255)
)
BEGIN
    DECLARE v_stock_actual INT;

    SELECT STOCK INTO v_stock_actual
    FROM productos WHERE PRODUCTO_ID = _producto_id;

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

    INSERT INTO movimientos_inventario(PRODUCTO_ID, TRABAJADOR_ID,
        TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO)
    VALUES(_producto_id, _trabajador_id, _tipo_movimiento,
           _cantidad, v_stock_actual,
           CASE
               WHEN _tipo_movimiento IN ('ENTRADA','DEVOLUCION') THEN v_stock_actual + _cantidad
               WHEN _tipo_movimiento = 'SALIDA'                  THEN v_stock_actual - _cantidad
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
    LEFT  JOIN trabajadores t ON m.TRABAJADOR_ID = t.TRABAJADOR_ID
    LEFT  JOIN usuarios u     ON t.USUARIO_ID    = u.USUARIO_ID
    WHERE m.PRODUCTO_ID = _producto_id
    ORDER BY m.FECHA_HORA DESC;
END$$

-- =============================================================
-- MÓDULO 4: MÉTODOS DE PAGO
-- CORRECCIÓN: se eliminó _descripcion de INSERTAR y MODIFICAR
--   porque MetodoPagoDto solo tiene: idMetodoPago, nombre, estado
--   El DAO solo pasa getNombre() → 1 parámetro IN
-- CORRECCIÓN: LISTAR y BUSCAR ya no retornan DESCRIPCION
-- =============================================================

CREATE PROCEDURE INSERTAR_METODO_PAGO(
    OUT _metodo_pago_id INT,
    IN  _nombre         VARCHAR(50)
    -- Se eliminó _descripcion: MetodoPagoDto no tiene ese atributo
)
BEGIN
    INSERT INTO metodos_pago(NOMBRE, ACTIVO)
    VALUES(_nombre, 1);
    SET _metodo_pago_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_METODO_PAGO(
    IN _metodo_pago_id INT,
    IN _nombre         VARCHAR(50)
    -- Se eliminó _descripcion: MetodoPagoDto no tiene ese atributo
)
BEGIN
    UPDATE metodos_pago
    SET NOMBRE = _nombre
    WHERE METODO_PAGO_ID = _metodo_pago_id;
END$$

CREATE PROCEDURE ELIMINAR_METODO_PAGO(
    IN _metodo_pago_id INT
)
BEGIN
    UPDATE metodos_pago SET ACTIVO = 0
    WHERE METODO_PAGO_ID = _metodo_pago_id;
END$$

CREATE PROCEDURE BUSCAR_METODO_PAGO_POR_ID(
    IN _metodo_pago_id INT
)
BEGIN
    -- Se eliminó DESCRIPCION: no existe en MetodoPagoDto
    -- El DAO lee: METODO_PAGO_ID → setIdMetodoPago
    --             NOMBRE         → setNombre
    --             ACTIVO         → setEstado
    SELECT METODO_PAGO_ID, NOMBRE, ACTIVO
    FROM metodos_pago
    WHERE METODO_PAGO_ID = _metodo_pago_id AND ACTIVO = 1;
END$$

CREATE PROCEDURE LISTAR_METODOS_PAGO()
BEGIN
    -- Se eliminó DESCRIPCION: no existe en MetodoPagoDto
    -- El DAO lee: METODO_PAGO_ID, NOMBRE, ACTIVO
    SELECT METODO_PAGO_ID, NOMBRE, ACTIVO
    FROM metodos_pago
    WHERE ACTIVO = 1;
END$$

-- =============================================================
-- MÓDULO 5: VENTAS
-- CORRECCIÓN en INSERTAR_VENTA: se quitaron MONTO_TOTAL,
--   MONTO_DESCUENTO y ESTADO_VENTA del INSERT porque la BD
--   los gestiona con defaults/triggers; el DAO no los envía.
-- CORRECCIÓN en LISTAR_VENTAS y BUSCAR_VENTA_POR_ID:
--   Se eliminaron CLIENTE_NOMBRE y TRABAJADOR_NOMBRE porque
--   VentaDto solo guarda ClienteDto con idCliente y
--   TrabajadorDto con idTrabajador; el DAO no llama setNombre
--   para estos objetos → los JOINs a usuarios eran innecesarios.
-- =============================================================

CREATE PROCEDURE INSERTAR_VENTA(
    OUT _venta_id       INT,
    IN  _cliente_id     INT,
    IN  _trabajador_id  INT,
    IN  _metodo_pago_id INT,
    IN  _canal_venta    VARCHAR(20),
    IN  _observaciones  VARCHAR(500)
    -- El DAO envía exactamente estos 5 parámetros IN
)
BEGIN
    INSERT INTO ventas(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID,
                       CANAL_VENTA, OBSERVACIONES)
    VALUES(_cliente_id, _trabajador_id, _metodo_pago_id,
           _canal_venta, _observaciones);
    SET _venta_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE COMPLETAR_VENTA(
    IN _venta_id INT
)
BEGIN
    UPDATE ventas SET ESTADO_VENTA = 'COMPLETADA'
    WHERE VENTA_ID = _venta_id;
END$$

CREATE PROCEDURE ANULAR_VENTA(
    IN _venta_id INT
)
BEGIN
    -- Devuelve stock de cada producto del detalle
    UPDATE productos p
    INNER JOIN detalles_venta dv ON p.PRODUCTO_ID = dv.PRODUCTO_ID
    SET p.STOCK = p.STOCK + dv.CANTIDAD
    WHERE dv.VENTA_ID = _venta_id;

    UPDATE ventas SET ESTADO_VENTA = 'ANULADA'
    WHERE VENTA_ID = _venta_id;
END$$

CREATE PROCEDURE BUSCAR_VENTA_POR_ID(
    IN _venta_id INT
)
BEGIN
    -- El DAO lee estas columnas exactamente:
    --   VENTA_ID, CLIENTE_ID, TRABAJADOR_ID,
    --   METODO_PAGO_ID, METODO_PAGO_NOMBRE,
    --   FECHA_HORA, MONTO_TOTAL, MONTO_DESCUENTO,
    --   CANAL_VENTA, ESTADO_VENTA, OBSERVACIONES
    -- Se eliminaron CLIENTE_NOMBRE y TRABAJADOR_NOMBRE:
    --   VentaDto solo almacena los IDs de cliente y trabajador
    SELECT v.VENTA_ID,
           v.CLIENTE_ID,
           v.TRABAJADOR_ID,
           v.METODO_PAGO_ID,
           mp.NOMBRE      AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA,
           v.MONTO_TOTAL,
           v.MONTO_DESCUENTO,
           v.CANAL_VENTA,
           v.ESTADO_VENTA,
           v.OBSERVACIONES
    FROM ventas v
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    WHERE v.VENTA_ID = _venta_id AND v.ACTIVO = 1;
END$$

CREATE PROCEDURE LISTAR_VENTAS()
BEGIN
    -- Misma lógica que BUSCAR: sin CLIENTE_NOMBRE ni TRABAJADOR_NOMBRE
    SELECT v.VENTA_ID,
           v.CLIENTE_ID,
           v.TRABAJADOR_ID,
           v.METODO_PAGO_ID,
           mp.NOMBRE      AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA,
           v.MONTO_TOTAL,
           v.MONTO_DESCUENTO,
           v.CANAL_VENTA,
           v.ESTADO_VENTA,
           v.OBSERVACIONES
    FROM ventas v
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    WHERE v.ACTIVO = 1
    ORDER BY v.FECHA_HORA DESC;
END$$

-- =============================================================
-- MÓDULO 6: DETALLES DE VENTA  (sin cambios lógicos)
-- =============================================================

CREATE PROCEDURE INSERTAR_DETALLE_VENTA(
    OUT _detalle_venta_id INT,
    IN  _venta_id         INT,
    IN  _producto_id      INT,
    IN  _cantidad         INT
)
BEGIN
    DECLARE v_precio   DECIMAL(10,2);
    DECLARE v_subtotal DECIMAL(10,2);

    SELECT PRECIO_UNITARIO INTO v_precio
    FROM productos WHERE PRODUCTO_ID = _producto_id;

    SET v_subtotal = v_precio * _cantidad;

    INSERT INTO detalles_venta(VENTA_ID, PRODUCTO_ID, CANTIDAD,
                               PRECIO_UNITARIO, SUBTOTAL)
    VALUES(_venta_id, _producto_id, _cantidad, v_precio, v_subtotal);
    SET _detalle_venta_id = LAST_INSERT_ID();

    -- Descontar stock
    UPDATE productos SET STOCK = STOCK - _cantidad
    WHERE PRODUCTO_ID = _producto_id;

    -- Recalcular monto total de la venta
    UPDATE ventas SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_venta WHERE VENTA_ID = _venta_id
    ) WHERE VENTA_ID = _venta_id;
END$$

-- Actualiza la cantidad de un detalle y recalcula subtotal y monto total de la venta
CREATE PROCEDURE MODIFICAR_DETALLE_VENTA(
    IN _detalle_venta_id INT,
    IN _cantidad         INT
)
BEGIN
    DECLARE v_precio   DECIMAL(10,2);
    DECLARE v_venta_id INT;

    SELECT PRECIO_UNITARIO, VENTA_ID INTO v_precio, v_venta_id
    FROM detalles_venta WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    -- Ajustar stock: devolver cantidad anterior y descontar la nueva
    UPDATE productos p
    INNER JOIN detalles_venta dv ON p.PRODUCTO_ID = dv.PRODUCTO_ID
    SET p.STOCK = p.STOCK + dv.CANTIDAD - _cantidad
    WHERE dv.DETALLE_VENTA_ID = _detalle_venta_id;

    UPDATE detalles_venta
    SET CANTIDAD = _cantidad,
        SUBTOTAL = v_precio * _cantidad
    WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    UPDATE ventas SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_venta WHERE VENTA_ID = v_venta_id
    ) WHERE VENTA_ID = v_venta_id;
END$$

CREATE PROCEDURE ELIMINAR_DETALLE_VENTA(
    IN _detalle_venta_id INT
)
BEGIN
    DECLARE v_venta_id INT;
    DECLARE v_producto_id INT;
    DECLARE v_cantidad INT;

    SELECT VENTA_ID, PRODUCTO_ID, CANTIDAD INTO v_venta_id, v_producto_id, v_cantidad
    FROM detalles_venta WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    -- Devolver stock
    UPDATE productos SET STOCK = STOCK + v_cantidad
    WHERE PRODUCTO_ID = v_producto_id;

    DELETE FROM detalles_venta WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    -- Recalcular monto total
    UPDATE ventas SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_venta WHERE VENTA_ID = v_venta_id
    ) WHERE VENTA_ID = v_venta_id;
END$$

CREATE PROCEDURE LISTAR_DETALLES_VENTA()
BEGIN
    SELECT dv.DETALLE_VENTA_ID,
           dv.VENTA_ID,
           dv.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dv.CANTIDAD,
           dv.PRECIO_UNITARIO,
           dv.SUBTOTAL
    FROM detalles_venta dv
    INNER JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID;
END$$

CREATE PROCEDURE BUSCAR_DETALLE_VENTA_POR_ID(
    IN _detalle_venta_id INT
)
BEGIN
    -- El DAO lee: DETALLE_VENTA_ID, VENTA_ID, PRODUCTO_ID,
    --   PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL
    SELECT dv.DETALLE_VENTA_ID,
           dv.VENTA_ID,
           dv.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dv.CANTIDAD,
           dv.PRECIO_UNITARIO,
           dv.SUBTOTAL
    FROM detalles_venta dv
    INNER JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dv.DETALLE_VENTA_ID = _detalle_venta_id;
END$$

CREATE PROCEDURE LISTAR_DETALLES_POR_VENTA(
    IN _venta_id INT
)
BEGIN
    SELECT dv.DETALLE_VENTA_ID,
           dv.VENTA_ID,
           dv.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dv.CANTIDAD,
           dv.PRECIO_UNITARIO,
           dv.SUBTOTAL
    FROM detalles_venta dv
    INNER JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dv.VENTA_ID = _venta_id;
END$$

-- =============================================================
-- MÓDULO 7: PEDIDOS
-- CORRECCIÓN en INSERTAR_PEDIDO: se eliminó PRIORIDAD del OUT
--   y de los parámetros IN porque PedidoDto no tiene ese atributo
--   y el DAO no lo envía. La lógica FIFO se mantiene internamente.
-- CORRECCIÓN en BUSCAR_PEDIDO_POR_ID y LISTAR_PEDIDOS:
--   Se eliminaron PRIORIDAD (no existe en PedidoDto),
--   VENTA_ID (el DAO no lo lee), CLIENTE_NOMBRE (el DAO solo
--   setea cliente.setIdCliente → no necesita nombre).
--   Se añadió MONTO_DESCUENTO que sí existe en PedidoDto y
--   el DAO sí llama setMontoDescuento.
-- CORRECCIÓN en MODIFICAR_ESTADO_PEDIDO: se renombró el
--   parámetro _estado → _estado_pedido para que coincida
--   exactamente con el nombre que usa el DAO Java.
-- =============================================================

CREATE PROCEDURE INSERTAR_PEDIDO(
    OUT _pedido_id         INT,
    IN  _cliente_id        INT,
    IN  _direccion_entrega VARCHAR(255),
    IN  _modalidad_entrega VARCHAR(20),
    IN  _observaciones     VARCHAR(500)
    -- El DAO envía exactamente estos 4 parámetros IN
)
BEGIN
    DECLARE v_prioridad INT;

    -- Lógica FIFO interna; PedidoDto no expone prioridad
    SELECT COALESCE(MAX(PRIORIDAD), 0) + 1 INTO v_prioridad
    FROM pedidos
    WHERE ESTADO_PEDIDO IN ('RECIBIDO', 'EN_PROCESO');

    INSERT INTO pedidos(CLIENTE_ID, DIRECCION_ENTREGA, MODALIDAD_ENTREGA,
                        PRIORIDAD, OBSERVACIONES)
    VALUES(_cliente_id, _direccion_entrega, _modalidad_entrega,
           v_prioridad, _observaciones);
    SET _pedido_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_ESTADO_PEDIDO(
    IN _pedido_id     INT,
    IN _estado_pedido VARCHAR(20)
    -- Renombrado de _estado → _estado_pedido para coincidir con el DAO
)
BEGIN
    UPDATE pedidos SET ESTADO_PEDIDO = _estado_pedido
    WHERE PEDIDO_ID = _pedido_id;
END$$

CREATE PROCEDURE ELIMINAR_PEDIDO(
    IN _pedido_id INT
)
BEGIN
    UPDATE pedidos SET ACTIVO = 0
    WHERE PEDIDO_ID = _pedido_id;
END$$

CREATE PROCEDURE BUSCAR_PEDIDO_POR_ID(
    IN _pedido_id INT
)
BEGIN
    -- El DAO lee: PEDIDO_ID, CLIENTE_ID, FECHA_HORA, MONTO_TOTAL,
    --   MONTO_DESCUENTO, ESTADO_PEDIDO, DIRECCION_ENTREGA,
    --   MODALIDAD_ENTREGA, OBSERVACIONES
    -- Se eliminaron: PRIORIDAD (no en PedidoDto),
    --   VENTA_ID (DAO no lo lee), CLIENTE_NOMBRE (DAO solo setea ID)
    SELECT p.PEDIDO_ID,
           p.CLIENTE_ID,
           p.FECHA_HORA,
           p.MONTO_TOTAL,
           p.MONTO_DESCUENTO,
           p.ESTADO_PEDIDO,
           p.DIRECCION_ENTREGA,
           p.MODALIDAD_ENTREGA,
           p.OBSERVACIONES
    FROM pedidos p
    WHERE p.PEDIDO_ID = _pedido_id AND p.ACTIVO = 1;
END$$

CREATE PROCEDURE LISTAR_PEDIDOS()
BEGIN
    -- Mismas columnas que BUSCAR_PEDIDO_POR_ID
    -- Sin PRIORIDAD, sin VENTA_ID, sin CLIENTE_NOMBRE
    -- ORDER BY FECHA_HORA ASC: el DAO no ordena, dejamos cronológico
    SELECT p.PEDIDO_ID,
           p.CLIENTE_ID,
           p.FECHA_HORA,
           p.MONTO_TOTAL,
           p.MONTO_DESCUENTO,
           p.ESTADO_PEDIDO,
           p.DIRECCION_ENTREGA,
           p.MODALIDAD_ENTREGA,
           p.OBSERVACIONES
    FROM pedidos p
    WHERE p.ACTIVO = 1
    ORDER BY p.FECHA_HORA ASC;
END$$

-- =============================================================
-- MÓDULO 8: DETALLES DE PEDIDO
-- CORRECCIÓN en BUSCAR y LISTAR: se eliminó DISPONIBLE porque
--   DetallePedidoDto no tiene ese atributo y el DAO no lo lee.
--   La columna DISPONIBLE sigue existiendo en la BD y se sigue
--   calculando en INSERTAR, pero no se expone al DAO.
-- =============================================================

CREATE PROCEDURE INSERTAR_DETALLE_PEDIDO(
    OUT _detalle_pedido_id INT,
    IN  _pedido_id         INT,
    IN  _producto_id       INT,
    IN  _cantidad          INT
)
BEGIN
    DECLARE v_precio     DECIMAL(10,2);
    DECLARE v_stock      INT;
    DECLARE v_disponible TINYINT;

    SELECT PRECIO_UNITARIO, STOCK INTO v_precio, v_stock
    FROM productos WHERE PRODUCTO_ID = _producto_id;

    -- DISPONIBLE se calcula y guarda en BD aunque el DTO no lo exponga
    SET v_disponible = IF(v_stock >= _cantidad, 1, 0);

    INSERT INTO detalles_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD,
                                PRECIO_UNITARIO, SUBTOTAL, DISPONIBLE)
    VALUES(_pedido_id, _producto_id, _cantidad,
           v_precio, v_precio * _cantidad, v_disponible);
    SET _detalle_pedido_id = LAST_INSERT_ID();

    -- Recalcular monto total del pedido
    UPDATE pedidos SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_pedido WHERE PEDIDO_ID = _pedido_id
    ) WHERE PEDIDO_ID = _pedido_id;
END$$

CREATE PROCEDURE MODIFICAR_DETALLE_PEDIDO(
    IN _detalle_pedido_id INT,
    IN _cantidad          INT
)
BEGIN
    DECLARE v_precio    DECIMAL(10,2);
    DECLARE v_pedido_id INT;

    SELECT PRECIO_UNITARIO, PEDIDO_ID INTO v_precio, v_pedido_id
    FROM detalles_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    UPDATE detalles_pedido
    SET CANTIDAD = _cantidad,
        SUBTOTAL = v_precio * _cantidad
    WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    UPDATE pedidos SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_pedido WHERE PEDIDO_ID = v_pedido_id
    ) WHERE PEDIDO_ID = v_pedido_id;
END$$

CREATE PROCEDURE ELIMINAR_DETALLE_PEDIDO(
    IN _detalle_pedido_id INT
)
BEGIN
    DECLARE v_pedido_id INT;

    SELECT PEDIDO_ID INTO v_pedido_id
    FROM detalles_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    DELETE FROM detalles_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    UPDATE pedidos SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalles_pedido WHERE PEDIDO_ID = v_pedido_id
    ) WHERE PEDIDO_ID = v_pedido_id;
END$$

CREATE PROCEDURE LISTAR_DETALLES_PEDIDO()
BEGIN
    SELECT dp.DETALLE_PEDIDO_ID,
           dp.PEDIDO_ID,
           dp.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dp.CANTIDAD,
           dp.PRECIO_UNITARIO,
           dp.SUBTOTAL
    FROM detalles_pedido dp
    INNER JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID;
END$$

CREATE PROCEDURE BUSCAR_DETALLE_PEDIDO_POR_ID(
    IN _detalle_pedido_id INT
)
BEGIN
    -- El DAO lee: DETALLE_PEDIDO_ID, PEDIDO_ID, PRODUCTO_ID,
    --   PRODUCTO_NOMBRE, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL
    -- Se eliminó DISPONIBLE: no existe en DetallePedidoDto
    SELECT dp.DETALLE_PEDIDO_ID,
           dp.PEDIDO_ID,
           dp.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dp.CANTIDAD,
           dp.PRECIO_UNITARIO,
           dp.SUBTOTAL
    FROM detalles_pedido dp
    INNER JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dp.DETALLE_PEDIDO_ID = _detalle_pedido_id;
END$$

CREATE PROCEDURE LISTAR_DETALLES_POR_PEDIDO(
    IN _pedido_id INT
)
BEGIN
    -- Se eliminó DISPONIBLE: no existe en DetallePedidoDto
    SELECT dp.DETALLE_PEDIDO_ID,
           dp.PEDIDO_ID,
           dp.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dp.CANTIDAD,
           dp.PRECIO_UNITARIO,
           dp.SUBTOTAL
    FROM detalles_pedido dp
    INNER JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dp.PEDIDO_ID = _pedido_id;
END$$

-- =============================================================
-- MÓDULO 9: PROVEEDORES  (sin cambios)
-- =============================================================

CREATE PROCEDURE INSERTAR_PROVEEDOR(
    OUT _proveedor_id INT,
    IN  _razon_social VARCHAR(150),
    IN  _ruc          VARCHAR(11),
    IN  _telefono     VARCHAR(15),
    IN  _email        VARCHAR(100),
    IN  _direccion    VARCHAR(255),
    IN  _contacto     VARCHAR(100)
)
BEGIN
    INSERT INTO proveedores(RAZON_SOCIAL, RUC, TELEFONO, EMAIL, DIRECCION, CONTACTO)
    VALUES(_razon_social, _ruc, _telefono, _email, _direccion, _contacto);
    SET _proveedor_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_PROVEEDOR(
    IN _proveedor_id  INT,
    IN _razon_social  VARCHAR(150),
    IN _ruc           VARCHAR(11),
    IN _telefono      VARCHAR(15),
    IN _email         VARCHAR(100),
    IN _direccion     VARCHAR(255),
    IN _contacto      VARCHAR(100)
)
BEGIN
    UPDATE proveedores SET
        RAZON_SOCIAL = _razon_social,
        RUC          = _ruc,
        TELEFONO     = _telefono,
        EMAIL        = _email,
        DIRECCION    = _direccion,
        CONTACTO     = _contacto
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
-- MÓDULO 10: ÓRDENES DE REABASTECIMIENTO  (sin cambios)
-- =============================================================

CREATE PROCEDURE INSERTAR_ORDEN_REABASTECIMIENTO(
    OUT _orden_id INT,
    IN  _proveedor_id             INT,
    IN  _trabajador_id            INT,
    IN  _fecha_entrega_estimada   DATE,
    IN  _observaciones            VARCHAR(500)
)
BEGIN
    INSERT INTO ordenes_reabastecimiento(PROVEEDOR_ID, TRABAJADOR_ID,
        FECHA_ENTREGA_ESTIMADA, OBSERVACIONES)
    VALUES(_proveedor_id, _trabajador_id, _fecha_entrega_estimada, _observaciones);
    SET _orden_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE INSERTAR_DETALLE_ORDEN_REABASTECIMIENTO(
    OUT _detalle_orden_id    INT,
    IN  _orden_id            INT,
    IN  _producto_id         INT,
    IN  _cantidad_solicitada INT,
    IN  _precio_compra       DECIMAL(10,2)
)
BEGIN
    INSERT INTO detalles_orden_reabastecimiento(ORDEN_ID, PRODUCTO_ID,
        CANTIDAD_SOLICITADA, PRECIO_COMPRA)
    VALUES(_orden_id, _producto_id, _cantidad_solicitada, _precio_compra);
    SET _detalle_orden_id = LAST_INSERT_ID();
END$$

CREATE PROCEDURE MODIFICAR_ESTADO_ORDEN(
    IN _orden_id INT,
    IN _estado   VARCHAR(20)
)
BEGIN
    UPDATE ordenes_reabastecimiento SET ESTADO_ORDEN = _estado
    WHERE ORDEN_ID = _orden_id;
END$$

CREATE PROCEDURE RECIBIR_ORDEN_REABASTECIMIENTO(
    IN _orden_id INT
)
BEGIN
    UPDATE detalles_orden_reabastecimiento
    SET CANTIDAD_RECIBIDA = CANTIDAD_SOLICITADA
    WHERE ORDEN_ID = _orden_id;

    UPDATE productos p
    INNER JOIN detalles_orden_reabastecimiento d ON p.PRODUCTO_ID = d.PRODUCTO_ID
    SET p.STOCK = p.STOCK + d.CANTIDAD_SOLICITADA
    WHERE d.ORDEN_ID = _orden_id;

    UPDATE ordenes_reabastecimiento SET ESTADO_ORDEN = 'RECIBIDA'
    WHERE ORDEN_ID = _orden_id;
END$$

CREATE PROCEDURE LISTAR_ORDENES_REABASTECIMIENTO()
BEGIN
    SELECT o.ORDEN_ID, o.PROVEEDOR_ID, pr.RAZON_SOCIAL,
           o.TRABAJADOR_ID,
           CONCAT(u.NOMBRES, ' ', u.APELLIDOS) AS TRABAJADOR,
           o.FECHA_CREACION, o.FECHA_ENTREGA_ESTIMADA,
           o.ESTADO_ORDEN, o.OBSERVACIONES
    FROM ordenes_reabastecimiento o
    INNER JOIN proveedores  pr ON o.PROVEEDOR_ID  = pr.PROVEEDOR_ID
    INNER JOIN trabajadores t  ON o.TRABAJADOR_ID = t.TRABAJADOR_ID
    INNER JOIN usuarios     u  ON t.USUARIO_ID    = u.USUARIO_ID
    ORDER BY o.FECHA_CREACION DESC;
END$$

-- =============================================================
-- MÓDULO 11: JUNCTIONS  (sin cambios)
-- =============================================================

CREATE PROCEDURE VINCULAR_PRODUCTO_PROVEEDOR(
    IN _producto_id   INT,
    IN _proveedor_id  INT,
    IN _precio_compra DECIMAL(10,2),
    IN _tiempo_entrega INT
)
BEGIN
    INSERT INTO productos_proveedores(PRODUCTO_ID, PROVEEDOR_ID,
        PRECIO_COMPRA, TIEMPO_ENTREGA)
    VALUES(_producto_id, _proveedor_id, _precio_compra, _tiempo_entrega)
    ON DUPLICATE KEY UPDATE
        PRECIO_COMPRA  = _precio_compra,
        TIEMPO_ENTREGA = _tiempo_entrega;
END$$

CREATE PROCEDURE VINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id  INT
)
BEGIN
    INSERT IGNORE INTO promociones_productos(PROMOCION_ID, PRODUCTO_ID)
    VALUES(_promocion_id, _producto_id);
END$$

CREATE PROCEDURE DESVINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id  INT
)
BEGIN
    DELETE FROM promociones_productos
    WHERE PROMOCION_ID = _promocion_id AND PRODUCTO_ID = _producto_id;
END$$

-- =============================================================
-- MÓDULO 12: PROMOCIONES  (sin cambios)
-- =============================================================

CREATE PROCEDURE INSERTAR_PROMOCION(
    OUT _promocion_id INT,
    IN  _nombre           VARCHAR(100),
    IN  _descripcion      VARCHAR(500),
    IN  _tipo_descuento   VARCHAR(20),
    IN  _valor_descuento  DECIMAL(10,2),
    IN  _fecha_inicio     DATE,
    IN  _fecha_fin        DATE,
    IN  _condiciones      VARCHAR(500)
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
-- MÓDULO 13: DEVOLUCIONES  (sin cambios)
-- =============================================================

CREATE PROCEDURE INSERTAR_DEVOLUCION(
    OUT _devolucion_id INT,
    IN  _producto_id      INT,
    IN  _trabajador_id    INT,
    IN  _tipo_devolucion  VARCHAR(20),
    IN  _cantidad         INT,
    IN  _motivo           VARCHAR(500)
)
BEGIN
    INSERT INTO devoluciones(PRODUCTO_ID, TRABAJADOR_ID, TIPO_DEVOLUCION,
                             CANTIDAD, MOTIVO)
    VALUES(_producto_id, _trabajador_id, _tipo_devolucion, _cantidad, _motivo);
    SET _devolucion_id = LAST_INSERT_ID();

    IF _tipo_devolucion = 'CLIENTE' THEN
        UPDATE productos SET STOCK = STOCK + _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    END IF;
END$$

-- =============================================================
-- MÓDULO 14: REPORTES  (sin cambios)
-- =============================================================

CREATE PROCEDURE REPORTE_VENTAS_POR_PERIODO(
    IN _fecha_inicio DATE,
    IN _fecha_fin    DATE
)
BEGIN
    SELECT v.VENTA_ID, v.FECHA_HORA,
           CONCAT(uc.NOMBRES, ' ', uc.APELLIDOS) AS CLIENTE,
           mp.NOMBRE AS METODO_PAGO,
           v.CANAL_VENTA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.ESTADO_VENTA
    FROM ventas v
    LEFT  JOIN clientes     c  ON v.CLIENTE_ID    = c.CLIENTE_ID
    LEFT  JOIN usuarios     uc ON c.USUARIO_ID    = uc.USUARIO_ID
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    WHERE DATE(v.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
      AND v.ESTADO_VENTA != 'ANULADA'
    ORDER BY v.FECHA_HORA;
END$$

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

CREATE PROCEDURE REPORTE_PRODUCTOS_MAS_VENDIDOS(
    IN _fecha_inicio DATE,
    IN _fecha_fin    DATE,
    IN _limite       INT
)
BEGIN
    SELECT p.PRODUCTO_ID, p.NOMBRE, c.NOMBRE AS CATEGORIA,
           SUM(dv.CANTIDAD)              AS TOTAL_VENDIDO,
           SUM(dv.SUBTOTAL)              AS INGRESO_TOTAL,
           COUNT(DISTINCT dv.VENTA_ID)   AS NUM_VENTAS
    FROM detalles_venta dv
    INNER JOIN productos  p ON dv.PRODUCTO_ID  = p.PRODUCTO_ID
    INNER JOIN categorias c ON p.CATEGORIA_ID  = c.CATEGORIA_ID
    INNER JOIN ventas     v ON dv.VENTA_ID      = v.VENTA_ID
    WHERE DATE(v.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
      AND v.ESTADO_VENTA != 'ANULADA'
    GROUP BY p.PRODUCTO_ID, p.NOMBRE, c.NOMBRE
    ORDER BY TOTAL_VENDIDO DESC
    LIMIT _limite;
END$$

CREATE PROCEDURE REPORTE_PERDIDAS_POR_PERIODO(
    IN _fecha_inicio DATE,
    IN _fecha_fin    DATE
)
BEGIN
    SELECT d.DEVOLUCION_ID, p.NOMBRE AS PRODUCTO, d.TIPO_DEVOLUCION,
           d.CANTIDAD, d.MOTIVO, d.FECHA_HORA,
           CONCAT(u.NOMBRES, ' ', u.APELLIDOS) AS REGISTRADO_POR
    FROM devoluciones d
    INNER JOIN productos    p ON d.PRODUCTO_ID   = p.PRODUCTO_ID
    INNER JOIN trabajadores t ON d.TRABAJADOR_ID = t.TRABAJADOR_ID
    INNER JOIN usuarios     u ON t.USUARIO_ID    = u.USUARIO_ID
    WHERE d.TIPO_DEVOLUCION IN ('MERMA','VENCIMIENTO','DEFECTO')
      AND DATE(d.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
    ORDER BY d.FECHA_HORA DESC;
END$$

DELIMITER ;
