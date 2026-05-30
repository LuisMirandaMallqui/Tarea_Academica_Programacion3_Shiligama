-- 3. Selecciona el schema para empezar a trabajar en él
USE `shiligama`;

-- =====================================================================
-- SHILIGAMA - Procedimientos Almacenados
-- =====================================================================git@github.com:fpaz19/1INF30-2026-1.git

DELIMITER $$

-- =============================================================
-- MÓDULO 1: USUARIOS
-- =============================================================

-- ----- INSERTAR_USUARIO -----
DROP PROCEDURE IF EXISTS INSERTAR_USUARIO$$
CREATE PROCEDURE INSERTAR_USUARIO(
    OUT _usuario_id  INT,
    IN  _nombres     VARCHAR(100),
    IN  _apellidos   VARCHAR(100),
    IN  _dni         VARCHAR(8),
    IN  _telefono    VARCHAR(15),
    IN  _correo      VARCHAR(100),
    IN  _contrasena  VARCHAR(255)
)
BEGIN
    INSERT INTO usuario(NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA, ACTIVO)
    VALUES(_nombres, _apellidos, _dni, _telefono, _correo, _contrasena, 1);
    SET _usuario_id = LAST_INSERT_ID();
END$$

-- ----- EXISTE_USUARIO_EN_BD -----
DROP PROCEDURE IF EXISTS EXISTE_USUARIO_EN_BD$$
CREATE PROCEDURE EXISTE_USUARIO_EN_BD(
    IN _correo VARCHAR(100),
    IN _dni    VARCHAR(8)
)
BEGIN
    SELECT 1 AS EXISTE
    FROM usuario
    WHERE (CORREO = _correo OR DNI = _dni) AND ACTIVO = 1
    LIMIT 1;
END$$

-- ----- MODIFICAR_USUARIO -----
DROP PROCEDURE IF EXISTS MODIFICAR_USUARIO$$
CREATE PROCEDURE MODIFICAR_USUARIO(
    IN _usuario_id  INT,
    IN _nombres     VARCHAR(100),
    IN _apellidos   VARCHAR(100),
    IN _dni         VARCHAR(8),
    IN _telefono    VARCHAR(15),
    IN _correo      VARCHAR(100)
)
BEGIN
    UPDATE usuario SET
        NOMBRES   = _nombres,
        APELLIDOS = _apellidos,
        DNI       = _dni,
        TELEFONO  = _telefono,
        CORREO    = _correo
    WHERE USUARIO_ID = _usuario_id;
END$$

-- ----- ELIMINAR_USUARIO -----
DROP PROCEDURE IF EXISTS ELIMINAR_USUARIO$$
CREATE PROCEDURE ELIMINAR_USUARIO(
    IN _usuario_id INT
)
BEGIN
    UPDATE usuario SET ACTIVO = 0 WHERE USUARIO_ID = _usuario_id;
END$$

-- =====================================================================
-- CLIENTES
-- =====================================================================
DROP PROCEDURE IF EXISTS INSERTAR_CLIENTE$$
CREATE PROCEDURE INSERTAR_CLIENTE(
    IN  _id_usuario       INT,
    IN  _direccion_entrega VARCHAR(255)
)
BEGIN
    INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA)
    VALUES(_id_usuario, _direccion_entrega);
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CLIENTE$$
CREATE PROCEDURE MODIFICAR_CLIENTE(
    IN _usuario_id         INT,
    IN _direccion_entrega  VARCHAR(255)
)
BEGIN
    UPDATE cliente SET
        DIRECCION_ENTREGA = _direccion_entrega
    WHERE USUARIO_ID = _usuario_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CLIENTE$$
CREATE PROCEDURE ELIMINAR_CLIENTE(
    IN _usuario_id INT
)
BEGIN
    UPDATE usuario SET ACTIVO = 0 WHERE USUARIO_ID = _usuario_id;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_ID$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_ID(
    IN _usuario_id INT
)
BEGIN
    SELECT c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE c.USUARIO_ID = _usuario_id AND u.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CLIENTES$$
CREATE PROCEDURE LISTAR_CLIENTES()
BEGIN
    SELECT c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_CORREO$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND u.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_DNI$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.DNI = _dni AND u.ACTIVO = 1;
END$$

-- =====================================================================
-- TRABAJADORES
-- =====================================================================
DROP PROCEDURE IF EXISTS INSERTAR_TRABAJADOR$$
CREATE PROCEDURE INSERTAR_TRABAJADOR(
    IN  _usuario_id    INT,
    IN  _cargo         VARCHAR(100),
    IN  _fecha_ingreso DATE
)
BEGIN
    INSERT INTO trabajador(USUARIO_ID, CARGO, FECHA_INGRESO, ACTIVO)
    VALUES(_usuario_id, _cargo, _fecha_ingreso, 1);
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_TRABAJADOR$$
CREATE PROCEDURE MODIFICAR_TRABAJADOR(
    IN _usuario_id    INT,
    IN _cargo         VARCHAR(100),
    IN _fecha_ingreso DATE
)
BEGIN
    UPDATE trabajador SET
        CARGO         = _cargo,
        FECHA_INGRESO = _fecha_ingreso
    WHERE USUARIO_ID = _usuario_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_TRABAJADOR$$
CREATE PROCEDURE ELIMINAR_TRABAJADOR(
    IN _usuario_id INT
)
BEGIN
    UPDATE usuario     SET ACTIVO = 0 WHERE USUARIO_ID    = _usuario_id;
    UPDATE trabajador SET ACTIVO = 0 WHERE USUARIO_ID = _usuario_id;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_ID$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_ID(
    IN _usuario_id INT
)
BEGIN
    SELECT t.CARGO, t.FECHA_INGRESO, u.USUARIO_ID, u.NOMBRES,
    u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE t.USUARIO_ID = _usuario_id AND t.ACTIVO = 1;
END$$

-- DAO: TrabajadorDaoImpl.listarTodos → "LISTAR_TRABAJADORES"
DROP PROCEDURE IF EXISTS LISTAR_TRABAJADORES$$
CREATE PROCEDURE LISTAR_TRABAJADORES()
BEGIN
    SELECT t.CARGO, t.FECHA_INGRESO, u.USUARIO_ID, u.NOMBRES,
    u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE t.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_CORREO$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT t.CARGO, t.FECHA_INGRESO, u.USUARIO_ID, u.NOMBRES,
    u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND t.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_DNI$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT t.CARGO, t.FECHA_INGRESO, u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE u.DNI = _dni AND t.ACTIVO = 1;
END$$

-- =====================================================================
-- ADMINISTRADORES
-- =====================================================================
DROP PROCEDURE IF EXISTS INSERTAR_ADMINISTRADOR$$
CREATE PROCEDURE INSERTAR_ADMINISTRADOR(
    IN  _usuario_id INT
)
BEGIN
    INSERT INTO administrador(USUARIO_ID, ACTIVO)
    VALUES(_usuario_id, 1);
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_ADMINISTRADOR$$
CREATE PROCEDURE ELIMINAR_ADMINISTRADOR(
    IN _usuario_id INT
)
BEGIN
    UPDATE usuario        SET ACTIVO = 0 WHERE USUARIO_ID       = _usuario_id;
    UPDATE administrador SET ACTIVO = 0 WHERE USUARIO_ID = _usuario_id;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_ID$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_ID(
    IN _usuario_id INT
)
BEGIN
    SELECT u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administrador a
    INNER JOIN usuario u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE a.USUARIO_ID = _usuario_id AND a.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_ADMINISTRADORES$$
CREATE PROCEDURE LISTAR_ADMINISTRADORES()
BEGIN
    SELECT u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administrador a
    INNER JOIN usuario u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE a.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_CORREO$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administrador a
    INNER JOIN usuario u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND a.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_DNI$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administrador a
    INNER JOIN usuario u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE u.DNI = _dni AND a.ACTIVO = 1;
END$$


-- =============================================================
-- MÓDULO 2: CATEGORÍAS Y PRODUCTOS
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_CATEGORIA$$
CREATE PROCEDURE INSERTAR_CATEGORIA(
    OUT _categoria_id INT,
    IN  _nombre             VARCHAR(100),
    IN  _descripcion        VARCHAR(255),
    IN  _categoria_padre_id INT
)
BEGIN
    INSERT INTO categoria(NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID)
    VALUES(_nombre, _descripcion, _categoria_padre_id);
    SET _categoria_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CATEGORIA$$
CREATE PROCEDURE MODIFICAR_CATEGORIA(
    IN _categoria_id        INT,
    IN _nombre              VARCHAR(100),
    IN _descripcion         VARCHAR(255),
    IN _categoria_padre_id  INT
)
BEGIN
    UPDATE categoria SET
        NOMBRE             = _nombre,
        DESCRIPCION        = _descripcion,
        CATEGORIA_PADRE_ID = _categoria_padre_id
    WHERE CATEGORIA_ID = _categoria_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CATEGORIA$$
CREATE PROCEDURE ELIMINAR_CATEGORIA(
    IN _categoria_id INT
)
BEGIN
    UPDATE categoria SET ACTIVO = 0 WHERE CATEGORIA_ID = _categoria_id;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CATEGORIAS$$
CREATE PROCEDURE LISTAR_CATEGORIAS()
BEGIN
    SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO
    FROM categoria WHERE ACTIVO = 1;
END$$

-- DAO: CategoriaDaoImpl.buscarPorID → "BUSCAR_CATEGORIA_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_CATEGORIA_X_ID$$
CREATE PROCEDURE BUSCAR_CATEGORIA_X_ID(
    IN _categoria_id INT
)
BEGIN
    SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO
    FROM categoria
    WHERE CATEGORIA_ID = _categoria_id;
END$$

-- =====================================================================
-- PRODUCTOS
-- =====================================================================

DROP PROCEDURE IF EXISTS INSERTAR_PRODUCTO$$
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
    INSERT INTO producto(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO,
                          STOCK, STOCK_MINIMO, UNIDAD_MEDIDA, CODIGO_BARRAS, IMAGEN_URL)
    VALUES(_categoria_id, _nombre, _descripcion, _precio_unitario,
           _stock, _stock_minimo, _unidad_medida, _codigo_barras, _imagen_url);
    SET _producto_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_PRODUCTO$$
CREATE PROCEDURE MODIFICAR_PRODUCTO(
    IN _producto_id     INT,
    IN _categoria_id    INT,
    IN _nombre          VARCHAR(150),
    IN _descripcion     VARCHAR(500),
    IN _precio_unitario DECIMAL(10,2),
    IN _stock_minimo    INT,
    IN _unidad_medida   VARCHAR(30),
    IN _codigo_barras   VARCHAR(50),
    IN _imagen_url      VARCHAR(500)
)
BEGIN
    UPDATE producto SET
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

DROP PROCEDURE IF EXISTS ELIMINAR_PRODUCTO$$
CREATE PROCEDURE ELIMINAR_PRODUCTO(
    IN _producto_id INT
)
BEGIN
    UPDATE producto SET ACTIVO = 0 WHERE PRODUCTO_ID = _producto_id;
END$$

DROP PROCEDURE IF EXISTS LISTAR_PRODUCTOS$$
CREATE PROCEDURE LISTAR_PRODUCTOS()
BEGIN
    SELECT p.PRODUCTO_ID, p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE,
           p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
           p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS,
           p.IMAGEN_URL, p.ACTIVO, p.FECHA_CREACION
    FROM producto p
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.ACTIVO = 1;
END$$

-- DAO: ProductoDaoImpl.buscarPorID → "BUSCAR_PRODUCTO_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_PRODUCTO_X_ID$$
CREATE PROCEDURE BUSCAR_PRODUCTO_X_ID(
    IN _producto_id INT
)
BEGIN
    SELECT p.PRODUCTO_ID, p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE,
           p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
           p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS,
           p.IMAGEN_URL, p.ACTIVO, p.FECHA_CREACION
    FROM producto p
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.PRODUCTO_ID = _producto_id;
END$$


-- =============================================================
-- MÓDULO 3: INVENTARIO
-- =============================================================

DROP PROCEDURE IF EXISTS REGISTRAR_MOVIMIENTO_INVENTARIO$$
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
    FROM producto WHERE PRODUCTO_ID = _producto_id;

    IF _tipo_movimiento = 'ENTRADA' OR _tipo_movimiento = 'DEVOLUCION' THEN
        UPDATE producto SET STOCK = STOCK + _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    ELSEIF _tipo_movimiento = 'SALIDA' THEN
        UPDATE producto SET STOCK = STOCK - _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    ELSEIF _tipo_movimiento = 'AJUSTE' THEN
        UPDATE producto SET STOCK = _cantidad
        WHERE PRODUCTO_ID = _producto_id;
    END IF;

    INSERT INTO movimiento_inventario(PRODUCTO_ID, TRABAJADOR_ID,
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

-- DAO: MovimientoInventarioDaoImpl.buscarPorID → "BUSCAR_MOVIMIENTO_POR_ID"
DROP PROCEDURE IF EXISTS BUSCAR_MOVIMIENTO_POR_ID$$
CREATE PROCEDURE BUSCAR_MOVIMIENTO_POR_ID(
    IN _movimiento_id INT
)
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR,
           STOCK_RESULTANTE, MOTIVO, FECHA_HORA
    FROM movimiento_inventario
    WHERE MOVIMIENTO_ID = _movimiento_id;
END$$

-- DAO: MovimientoInventarioDaoImpl.listarTodos → "LISTAR_MOVIMIENTOS_TODOS"
DROP PROCEDURE IF EXISTS LISTAR_MOVIMIENTOS_TODOS$$
CREATE PROCEDURE LISTAR_MOVIMIENTOS_TODOS()
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR,
           STOCK_RESULTANTE, MOTIVO, FECHA_HORA
    FROM movimiento_inventario
    ORDER BY FECHA_HORA DESC;
END$$

-- DAO: MovimientoInventarioDaoImpl.listarPorProducto → "LISTAR_MOVIMIENTOS_POR_PRODUCTO"
DROP PROCEDURE IF EXISTS LISTAR_MOVIMIENTOS_POR_PRODUCTO$$
CREATE PROCEDURE LISTAR_MOVIMIENTOS_POR_PRODUCTO(
    IN _producto_id INT
)
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR,
           STOCK_RESULTANTE, MOTIVO, FECHA_HORA
    FROM movimiento_inventario
    WHERE PRODUCTO_ID = _producto_id
    ORDER BY FECHA_HORA DESC;
END$$

-- DAO: MovimientoInventarioDaoImpl.listarPorFechas → "LISTAR_MOVIMIENTOS_POR_FECHAS"
DROP PROCEDURE IF EXISTS LISTAR_MOVIMIENTOS_POR_FECHAS$$
CREATE PROCEDURE LISTAR_MOVIMIENTOS_POR_FECHAS(
    IN _fecha_inicio DATETIME,
    IN _fecha_fin    DATETIME
)
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR,
           STOCK_RESULTANTE, MOTIVO, FECHA_HORA
    FROM movimiento_inventario
    WHERE FECHA_HORA BETWEEN _fecha_inicio AND _fecha_fin
    ORDER BY FECHA_HORA DESC;
END$$


-- =============================================================
-- MÓDULO 4: MÉTODOS DE PAGO
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_METODO_PAGO$$
CREATE PROCEDURE INSERTAR_METODO_PAGO(
    OUT _metodo_pago_id INT,
    IN  _nombre         VARCHAR(50)
)
BEGIN
    INSERT INTO metodo_pago(NOMBRE, ACTIVO)
    VALUES(_nombre, 1);
    SET _metodo_pago_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_METODO_PAGO$$
CREATE PROCEDURE MODIFICAR_METODO_PAGO(
    IN _metodo_pago_id INT,
    IN _nombre         VARCHAR(50)
)
BEGIN
    UPDATE metodo_pago
    SET NOMBRE = _nombre
    WHERE METODO_PAGO_ID = _metodo_pago_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_METODO_PAGO$$
CREATE PROCEDURE ELIMINAR_METODO_PAGO(
    IN _metodo_pago_id INT
)
BEGIN
    UPDATE metodo_pago SET ACTIVO = 0
    WHERE METODO_PAGO_ID = _metodo_pago_id;
END$$

-- DAO: MetodoPagoDaoImpl.buscarPorID → "BUSCAR_METODO_PAGO_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_METODO_PAGO_X_ID$$
CREATE PROCEDURE BUSCAR_METODO_PAGO_X_ID(
    IN _metodo_pago_id INT
)
BEGIN
    SELECT METODO_PAGO_ID, NOMBRE, ACTIVO
    FROM metodo_pago
    WHERE METODO_PAGO_ID = _metodo_pago_id AND ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_METODOS_PAGO$$
CREATE PROCEDURE LISTAR_METODOS_PAGO()
BEGIN
    SELECT METODO_PAGO_ID, NOMBRE, ACTIVO
    FROM metodo_pago
    WHERE ACTIVO = 1;
END$$


-- =============================================================
-- MÓDULO 5: VENTAS
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_VENTA$$
CREATE PROCEDURE INSERTAR_VENTA(
    OUT _venta_id       INT,
    IN  _cliente_id     INT,
    IN  _trabajador_id  INT,
    IN  _metodo_pago_id INT,
    IN  _canal_venta    VARCHAR(20),
    IN  _observaciones  VARCHAR(500)
)
BEGIN
    INSERT INTO venta(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID,
                       CANAL_VENTA, OBSERVACIONES)
    VALUES(_cliente_id, _trabajador_id, _metodo_pago_id,
           _canal_venta, _observaciones);
    SET _venta_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS COMPLETAR_VENTA$$
CREATE PROCEDURE COMPLETAR_VENTA(
    IN _venta_id INT
)
BEGIN
    UPDATE venta SET ESTADO_VENTA = 'COMPLETADA'
    WHERE VENTA_ID = _venta_id;
END$$

DROP PROCEDURE IF EXISTS ANULAR_VENTA$$
CREATE PROCEDURE ANULAR_VENTA(
    IN _venta_id INT
)
BEGIN
    -- Devuelve stock de cada producto del detalle
    UPDATE producto p
    INNER JOIN detalle_venta dv ON p.PRODUCTO_ID = dv.PRODUCTO_ID
    SET p.STOCK = p.STOCK + dv.CANTIDAD
    WHERE dv.VENTA_ID = _venta_id;

    UPDATE venta SET ESTADO_VENTA = 'ANULADA'
    WHERE VENTA_ID = _venta_id;
END$$

-- DAO: VentaDaoImpl.buscarPorID → "BUSCAR_VENTA_X_ID"
-- Incluye NUMERO_BOLETA, RUC_EMPRESA, CONTACTO_CLIENTE, MENSAJE_BOLETA
-- que el DAO lee para distinguir VentaDto vs BoletaDto
DROP PROCEDURE IF EXISTS BUSCAR_VENTA_X_ID$$
CREATE PROCEDURE BUSCAR_VENTA_X_ID(
    IN _venta_id INT
)
BEGIN
    SELECT v.VENTA_ID,
           v.CLIENTE_ID,
           v.TRABAJADOR_ID,
           v.METODO_PAGO_ID,
           mp.NOMBRE        AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA,
           v.MONTO_TOTAL,
           v.MONTO_DESCUENTO,
           v.CANAL_VENTA,
           v.ESTADO_VENTA,
           v.OBSERVACIONES,
           v.NUMERO_BOLETA,
           v.RUC_EMPRESA,
           v.CONTACTO_CLIENTE,
           v.MENSAJE_BOLETA
    FROM venta v
    INNER JOIN metodo_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    WHERE v.VENTA_ID = _venta_id AND v.ACTIVO = 1;
END$$

-- DAO: VentaDaoImpl.listarTodos → "LISTAR_VENTAS"
DROP PROCEDURE IF EXISTS LISTAR_VENTAS$$
CREATE PROCEDURE LISTAR_VENTAS()
BEGIN
    SELECT v.VENTA_ID,
           v.CLIENTE_ID,
           v.TRABAJADOR_ID,
           v.METODO_PAGO_ID,
           mp.NOMBRE        AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA,
           v.MONTO_TOTAL,
           v.MONTO_DESCUENTO,
           v.CANAL_VENTA,
           v.ESTADO_VENTA,
           v.OBSERVACIONES,
           v.NUMERO_BOLETA,
           v.RUC_EMPRESA,
           v.CONTACTO_CLIENTE,
           v.MENSAJE_BOLETA
    FROM venta v
    INNER JOIN metodo_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    WHERE v.ACTIVO = 1
    ORDER BY v.FECHA_HORA DESC;
END$$


-- =============================================================
-- MÓDULO 6: DETALLES DE VENTA
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_DETALLE_VENTA$$
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
    FROM producto WHERE PRODUCTO_ID = _producto_id;

    SET v_subtotal = v_precio * _cantidad;

    INSERT INTO detalle_venta(VENTA_ID, PRODUCTO_ID, CANTIDAD,
                               PRECIO_UNITARIO, SUBTOTAL)
    VALUES(_venta_id, _producto_id, _cantidad, v_precio, v_subtotal);
    SET _detalle_venta_id = LAST_INSERT_ID();

    -- Descontar stock
    UPDATE producto SET STOCK = STOCK - _cantidad
    WHERE PRODUCTO_ID = _producto_id;

    -- Recalcular monto total de la venta
    UPDATE venta SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalle_venta WHERE VENTA_ID = _venta_id
    ) WHERE VENTA_ID = _venta_id;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_DETALLE_VENTA$$
CREATE PROCEDURE MODIFICAR_DETALLE_VENTA(
    IN _detalle_venta_id INT,
    IN _cantidad         INT
)
BEGIN
    DECLARE v_precio   DECIMAL(10,2);
    DECLARE v_venta_id INT;

    SELECT PRECIO_UNITARIO, VENTA_ID INTO v_precio, v_venta_id
    FROM detalle_venta WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    -- Ajustar stock: devolver cantidad anterior y descontar la nueva
    UPDATE producto p
    INNER JOIN detalle_venta dv ON p.PRODUCTO_ID = dv.PRODUCTO_ID
    SET p.STOCK = p.STOCK + dv.CANTIDAD - _cantidad
    WHERE dv.DETALLE_VENTA_ID = _detalle_venta_id;

    UPDATE detalle_venta
    SET CANTIDAD = _cantidad,
        SUBTOTAL = v_precio * _cantidad
    WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    UPDATE venta SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalle_venta WHERE VENTA_ID = v_venta_id
    ) WHERE VENTA_ID = v_venta_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_DETALLE_VENTA$$
CREATE PROCEDURE ELIMINAR_DETALLE_VENTA(
    IN _detalle_venta_id INT
)
BEGIN
    DECLARE v_venta_id INT;
    DECLARE v_producto_id INT;
    DECLARE v_cantidad INT;

    SELECT VENTA_ID, PRODUCTO_ID, CANTIDAD INTO v_venta_id, v_producto_id, v_cantidad
    FROM detalle_venta WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    -- Devolver stock
    UPDATE producto SET STOCK = STOCK + v_cantidad
    WHERE PRODUCTO_ID = v_producto_id;

    DELETE FROM detalle_venta WHERE DETALLE_VENTA_ID = _detalle_venta_id;

    -- Recalcular monto total
    UPDATE venta SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalle_venta WHERE VENTA_ID = v_venta_id
    ) WHERE VENTA_ID = v_venta_id;
END$$

DROP PROCEDURE IF EXISTS LISTAR_DETALLES_VENTA$$
CREATE PROCEDURE LISTAR_DETALLES_VENTA()
BEGIN
    SELECT dv.DETALLE_VENTA_ID,
           dv.VENTA_ID,
           dv.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dv.CANTIDAD,
           dv.PRECIO_UNITARIO,
           dv.SUBTOTAL
    FROM detalle_venta dv
    INNER JOIN producto p ON dv.PRODUCTO_ID = p.PRODUCTO_ID;
END$$

-- DAO: DetalleVentaDaoImpl.buscarPorID → "BUSCAR_DETALLE_VENTA_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_DETALLE_VENTA_X_ID$$
CREATE PROCEDURE BUSCAR_DETALLE_VENTA_X_ID(
    IN _detalle_venta_id INT
)
BEGIN
    SELECT dv.DETALLE_VENTA_ID,
           dv.VENTA_ID,
           dv.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dv.CANTIDAD,
           dv.PRECIO_UNITARIO,
           dv.SUBTOTAL
    FROM detalle_venta dv
    INNER JOIN producto p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dv.DETALLE_VENTA_ID = _detalle_venta_id;
END$$

-- SP extra útil (no llamado directamente por un DAO standalone)
DROP PROCEDURE IF EXISTS LISTAR_DETALLES_POR_VENTA$$
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
    FROM detalle_venta dv
    INNER JOIN producto p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dv.VENTA_ID = _venta_id;
END$$


-- =============================================================
-- MÓDULO 7: PEDIDOS
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_PEDIDO$$
CREATE PROCEDURE INSERTAR_PEDIDO(
    OUT _pedido_id         INT,
    IN  _cliente_id        INT,
    IN  _direccion_entrega VARCHAR(255),
    IN  _modalidad_entrega VARCHAR(20),
    IN  _observaciones     VARCHAR(500)
)
BEGIN
    DECLARE v_prioridad INT;

    SELECT COALESCE(MAX(PRIORIDAD), 0) + 1 INTO v_prioridad
    FROM pedido
    WHERE ESTADO_PEDIDO IN ('RECIBIDO', 'EN_PROCESO');

    INSERT INTO pedido(CLIENTE_ID, DIRECCION_ENTREGA, MODALIDAD_ENTREGA,
                        PRIORIDAD, OBSERVACIONES)
    VALUES(_cliente_id, _direccion_entrega, _modalidad_entrega,
           v_prioridad, _observaciones);
    SET _pedido_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_ESTADO_PEDIDO$$
CREATE PROCEDURE MODIFICAR_ESTADO_PEDIDO(
    IN _pedido_id     INT,
    IN _estado_pedido VARCHAR(20)
)
BEGIN
    UPDATE pedido SET ESTADO_PEDIDO = _estado_pedido
    WHERE PEDIDO_ID = _pedido_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_PEDIDO$$
CREATE PROCEDURE ELIMINAR_PEDIDO(
    IN _pedido_id INT
)
BEGIN
    UPDATE pedido SET ACTIVO = 0
    WHERE PEDIDO_ID = _pedido_id;
END$$

-- DAO: PedidoDaoImpl.buscarPorID → "BUSCAR_PEDIDO_X_ID"
-- Nota: DDL de pedido NO tiene columna MONTO_DESCUENTO, se eliminó del SELECT
DROP PROCEDURE IF EXISTS BUSCAR_PEDIDO_X_ID$$
CREATE PROCEDURE BUSCAR_PEDIDO_X_ID(
    IN _pedido_id INT
)
BEGIN
    SELECT p.PEDIDO_ID,
           p.CLIENTE_ID,
           p.FECHA_HORA,
           p.MONTO_TOTAL,
           p.ESTADO_PEDIDO,
           p.DIRECCION_ENTREGA,
           p.MODALIDAD_ENTREGA,
           p.OBSERVACIONES
    FROM pedido p
    WHERE p.PEDIDO_ID = _pedido_id AND p.ACTIVO = 1;
END$$

-- DAO: PedidoDaoImpl.listarTodos → "LISTAR_PEDIDOS"
DROP PROCEDURE IF EXISTS LISTAR_PEDIDOS$$
CREATE PROCEDURE LISTAR_PEDIDOS()
BEGIN
    SELECT p.PEDIDO_ID,
           p.CLIENTE_ID,
           p.FECHA_HORA,
           p.MONTO_TOTAL,
           p.ESTADO_PEDIDO,
           p.DIRECCION_ENTREGA,
           p.MODALIDAD_ENTREGA,
           p.OBSERVACIONES
    FROM pedido p
    WHERE p.ACTIVO = 1
    ORDER BY p.FECHA_HORA ASC;
END$$


-- =============================================================
-- MÓDULO 8: DETALLES DE PEDIDO
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_DETALLE_PEDIDO$$
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
    FROM producto WHERE PRODUCTO_ID = _producto_id;

    SET v_disponible = IF(v_stock >= _cantidad, 1, 0);

    INSERT INTO detalle_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD,
                                PRECIO_UNITARIO, SUBTOTAL, DISPONIBLE)
    VALUES(_pedido_id, _producto_id, _cantidad,
           v_precio, v_precio * _cantidad, v_disponible);
    SET _detalle_pedido_id = LAST_INSERT_ID();

    -- Recalcular monto total del pedido
    UPDATE pedido SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalle_pedido WHERE PEDIDO_ID = _pedido_id
    ) WHERE PEDIDO_ID = _pedido_id;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_DETALLE_PEDIDO$$
CREATE PROCEDURE MODIFICAR_DETALLE_PEDIDO(
    IN _detalle_pedido_id INT,
    IN _cantidad          INT
)
BEGIN
    DECLARE v_precio    DECIMAL(10,2);
    DECLARE v_pedido_id INT;

    SELECT PRECIO_UNITARIO, PEDIDO_ID INTO v_precio, v_pedido_id
    FROM detalle_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    UPDATE detalle_pedido
    SET CANTIDAD = _cantidad,
        SUBTOTAL = v_precio * _cantidad
    WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    UPDATE pedido SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalle_pedido WHERE PEDIDO_ID = v_pedido_id
    ) WHERE PEDIDO_ID = v_pedido_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_DETALLE_PEDIDO$$
CREATE PROCEDURE ELIMINAR_DETALLE_PEDIDO(
    IN _detalle_pedido_id INT
)
BEGIN
    DECLARE v_pedido_id INT;

    SELECT PEDIDO_ID INTO v_pedido_id
    FROM detalle_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    DELETE FROM detalle_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;

    UPDATE pedido SET MONTO_TOTAL = (
        SELECT COALESCE(SUM(SUBTOTAL), 0)
        FROM detalle_pedido WHERE PEDIDO_ID = v_pedido_id
    ) WHERE PEDIDO_ID = v_pedido_id;
END$$

DROP PROCEDURE IF EXISTS LISTAR_DETALLES_PEDIDO$$
CREATE PROCEDURE LISTAR_DETALLES_PEDIDO()
BEGIN
    SELECT dp.DETALLE_PEDIDO_ID,
           dp.PEDIDO_ID,
           dp.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dp.CANTIDAD,
           dp.PRECIO_UNITARIO,
           dp.SUBTOTAL
    FROM detalle_pedido dp
    INNER JOIN producto p ON dp.PRODUCTO_ID = p.PRODUCTO_ID;
END$$

-- DAO: DetallePedidoDaoImpl.buscarPorID → "BUSCAR_DETALLE_PEDIDO_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_DETALLE_PEDIDO_X_ID$$
CREATE PROCEDURE BUSCAR_DETALLE_PEDIDO_X_ID(
    IN _detalle_pedido_id INT
)
BEGIN
    SELECT dp.DETALLE_PEDIDO_ID,
           dp.PEDIDO_ID,
           dp.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dp.CANTIDAD,
           dp.PRECIO_UNITARIO,
           dp.SUBTOTAL
    FROM detalle_pedido dp
    INNER JOIN producto p ON dp.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dp.DETALLE_PEDIDO_ID = _detalle_pedido_id;
END$$

-- SP extra útil
DROP PROCEDURE IF EXISTS LISTAR_DETALLES_POR_PEDIDO$$
CREATE PROCEDURE LISTAR_DETALLES_POR_PEDIDO(
    IN _pedido_id INT
)
BEGIN
    SELECT dp.DETALLE_PEDIDO_ID,
           dp.PEDIDO_ID,
           dp.PRODUCTO_ID,
           p.NOMBRE  AS PRODUCTO_NOMBRE,
           dp.CANTIDAD,
           dp.PRECIO_UNITARIO,
           dp.SUBTOTAL
    FROM detalle_pedido dp
    INNER JOIN producto p ON dp.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dp.PEDIDO_ID = _pedido_id;
END$$


-- =============================================================
-- MÓDULO 9: PROMOCIONES
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_PROMOCION$$
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
    INSERT INTO promocion(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO,
                            FECHA_INICIO, FECHA_FIN, CONDICIONES)
    VALUES(_nombre, _descripcion, _tipo_descuento, _valor_descuento,
           _fecha_inicio, _fecha_fin, _condiciones);
    SET _promocion_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_PROMOCION$$
CREATE PROCEDURE MODIFICAR_PROMOCION(
    IN _promocion_id      INT,
    IN _nombre            VARCHAR(100),
    IN _descripcion       VARCHAR(500),
    IN _tipo_descuento    VARCHAR(20),
    IN _valor_descuento   DECIMAL(10,2),
    IN _fecha_inicio      DATE,
    IN _fecha_fin         DATE,
    IN _condiciones       VARCHAR(500)
)
BEGIN
    UPDATE promocion SET
        NOMBRE          = _nombre,
        DESCRIPCION     = _descripcion,
        TIPO_DESCUENTO  = _tipo_descuento,
        VALOR_DESCUENTO = _valor_descuento,
        FECHA_INICIO    = _fecha_inicio,
        FECHA_FIN       = _fecha_fin,
        CONDICIONES     = _condiciones
    WHERE PROMOCION_ID = _promocion_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_PROMOCION$$
CREATE PROCEDURE ELIMINAR_PROMOCION(
    IN _promocion_id INT
)
BEGIN
    UPDATE promocion SET ACTIVO = 0
    WHERE PROMOCION_ID = _promocion_id;
END$$

-- DAO: PromocionDaoImpl.buscarPorID → "BUSCAR_PROMOCION_POR_ID"
-- NOTA: el DAO lee con aliases lowercase (id_promocion, nombre, etc.)
DROP PROCEDURE IF EXISTS BUSCAR_PROMOCION_POR_ID$$
CREATE PROCEDURE BUSCAR_PROMOCION_POR_ID(
    IN _promocion_id INT
)
BEGIN
    SELECT
        PROMOCION_ID    AS id_promocion,
        NOMBRE          AS nombre,
        DESCRIPCION     AS descripcion,
        TIPO_DESCUENTO  AS tipo_descuento,
        VALOR_DESCUENTO AS valor_descuento,
        FECHA_INICIO    AS fecha_inicio,
        FECHA_FIN       AS fecha_fin,
        CONDICIONES     AS condiciones,
        ACTIVO          AS activo
    FROM promocion
    WHERE PROMOCION_ID = _promocion_id AND ACTIVO = 1;
END$$

-- DAO: PromocionDaoImpl.listarTodos → "LISTAR_PROMOCIONES_TODAS"
DROP PROCEDURE IF EXISTS LISTAR_PROMOCIONES_TODAS$$
CREATE PROCEDURE LISTAR_PROMOCIONES_TODAS()
BEGIN
    SELECT
        PROMOCION_ID    AS id_promocion,
        NOMBRE          AS nombre,
        DESCRIPCION     AS descripcion,
        TIPO_DESCUENTO  AS tipo_descuento,
        VALOR_DESCUENTO AS valor_descuento,
        FECHA_INICIO    AS fecha_inicio,
        FECHA_FIN       AS fecha_fin,
        CONDICIONES     AS condiciones,
        ACTIVO          AS activo
    FROM promocion
    WHERE ACTIVO = 1
    ORDER BY FECHA_INICIO DESC;
END$$

-- DAO: PromocionDaoImpl.listarVigentes → "LISTAR_PROMOCIONES_VIGENTES"
DROP PROCEDURE IF EXISTS LISTAR_PROMOCIONES_VIGENTES$$
CREATE PROCEDURE LISTAR_PROMOCIONES_VIGENTES()
BEGIN
    SELECT
        PROMOCION_ID    AS id_promocion,
        NOMBRE          AS nombre,
        DESCRIPCION     AS descripcion,
        TIPO_DESCUENTO  AS tipo_descuento,
        VALOR_DESCUENTO AS valor_descuento,
        FECHA_INICIO    AS fecha_inicio,
        FECHA_FIN       AS fecha_fin,
        CONDICIONES     AS condiciones,
        ACTIVO          AS activo
    FROM promocion
    WHERE ACTIVO = 1 AND CURDATE() BETWEEN FECHA_INICIO AND FECHA_FIN;
END$$

-- DAO: PromocionDaoImpl.asociarProducto → "VINCULAR_PRODUCTO_PROMOCION"
DROP PROCEDURE IF EXISTS VINCULAR_PRODUCTO_PROMOCION$$
CREATE PROCEDURE VINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id  INT
)
BEGIN
    INSERT IGNORE INTO promocion_producto(PROMOCION_ID, PRODUCTO_ID)
    VALUES(_promocion_id, _producto_id);
END$$

-- DAO: PromocionDaoImpl.desasociarProducto → "DESVINCULAR_PRODUCTO_PROMOCION"
DROP PROCEDURE IF EXISTS DESVINCULAR_PRODUCTO_PROMOCION$$
CREATE PROCEDURE DESVINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id  INT
)
BEGIN
    DELETE FROM promocion_producto
    WHERE PROMOCION_ID = _promocion_id AND PRODUCTO_ID = _producto_id;
END$$

-- DAO: PromocionDaoImpl.listarProductosPorPromocion → "LISTAR_PRODUCTOS_POR_PROMOCION"
-- FALTABA — el DAO lo llama pero no existía en SQL
DROP PROCEDURE IF EXISTS LISTAR_PRODUCTOS_POR_PROMOCION$$
CREATE PROCEDURE LISTAR_PRODUCTOS_POR_PROMOCION(
    IN _promocion_id INT
)
BEGIN
    SELECT pp.PRODUCTO_ID
    FROM promocion_producto pp
    WHERE pp.PROMOCION_ID = _promocion_id;
END$$


-- =============================================================
-- MÓDULO 10: DEVOLUCIONES
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_DEVOLUCION$$
CREATE PROCEDURE INSERTAR_DEVOLUCION(
    OUT _devolucion_id    INT,
    IN  _producto_id      INT,
    IN  _trabajador_id    INT,
    IN  _estado_devolucion VARCHAR(20),
    IN  _cantidad         INT,
    IN  _motivo           VARCHAR(500),
    IN  _fecha_hora       DATETIME
)
BEGIN
    INSERT INTO devolucion(PRODUCTO_ID, TRABAJADOR_ID, ESTADO_DEVOLUCION,
                             CANTIDAD, MOTIVO, FECHA_HORA, ACTIVO)
    VALUES(_producto_id, _trabajador_id, _estado_devolucion,
           _cantidad, _motivo, _fecha_hora, 1);
    SET _devolucion_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_DEVOLUCION$$
CREATE PROCEDURE MODIFICAR_DEVOLUCION(
    IN _devolucion_id      INT,
    IN _producto_id        INT,
    IN _trabajador_id      INT,
    IN _estado_devolucion  VARCHAR(20),
    IN _cantidad           INT,
    IN _motivo             VARCHAR(500),
    IN _fecha_hora         DATETIME
)
BEGIN
    UPDATE devolucion SET
        PRODUCTO_ID       = _producto_id,
        TRABAJADOR_ID     = _trabajador_id,
        ESTADO_DEVOLUCION = _estado_devolucion,
        CANTIDAD          = _cantidad,
        MOTIVO            = _motivo,
        FECHA_HORA        = _fecha_hora
    WHERE DEVOLUCION_ID = _devolucion_id;
END$$

-- CORREGIDO: usaba columnas lowercase (id_devolucion, activo) — ahora UPPERCASE
DROP PROCEDURE IF EXISTS ELIMINAR_DEVOLUCION$$
CREATE PROCEDURE ELIMINAR_DEVOLUCION(
    IN _devolucion_id INT
)
BEGIN
    UPDATE devolucion SET ACTIVO = 0
    WHERE DEVOLUCION_ID = _devolucion_id;
END$$

-- DAO: DevolucionDaoImpl.buscarPorID → "BUSCAR_DEVOLUCION_POR_ID"
DROP PROCEDURE IF EXISTS BUSCAR_DEVOLUCION_POR_ID$$
CREATE PROCEDURE BUSCAR_DEVOLUCION_POR_ID(
    IN _devolucion_id INT
)
BEGIN
    SELECT DEVOLUCION_ID, PRODUCTO_ID, TRABAJADOR_ID,
           ESTADO_DEVOLUCION, CANTIDAD, MOTIVO, FECHA_HORA, ACTIVO
    FROM devolucion
    WHERE DEVOLUCION_ID = _devolucion_id AND ACTIVO = 1;
END$$

-- DAO: DevolucionDaoImpl.listarTodos → "LISTAR_DEVOLUCIONES_TODAS"
DROP PROCEDURE IF EXISTS LISTAR_DEVOLUCIONES_TODAS$$
CREATE PROCEDURE LISTAR_DEVOLUCIONES_TODAS()
BEGIN
    SELECT DEVOLUCION_ID, PRODUCTO_ID, TRABAJADOR_ID,
           ESTADO_DEVOLUCION, CANTIDAD, MOTIVO, FECHA_HORA, ACTIVO
    FROM devolucion
    WHERE ACTIVO = 1
    ORDER BY FECHA_HORA DESC;
END$$

-- DAO: DevolucionDaoImpl.listarPorFechas → "LISTAR_DEVOLUCIONES_POR_FECHAS"
DROP PROCEDURE IF EXISTS LISTAR_DEVOLUCIONES_POR_FECHAS$$
CREATE PROCEDURE LISTAR_DEVOLUCIONES_POR_FECHAS(
    IN _fecha_inicio DATETIME,
    IN _fecha_fin    DATETIME
)
BEGIN
    SELECT DEVOLUCION_ID, PRODUCTO_ID, TRABAJADOR_ID,
           ESTADO_DEVOLUCION, CANTIDAD, MOTIVO, FECHA_HORA, ACTIVO
    FROM devolucion
    WHERE ACTIVO = 1 AND FECHA_HORA BETWEEN _fecha_inicio AND _fecha_fin
    ORDER BY FECHA_HORA DESC;
END$$


-- =============================================================
-- MÓDULO 11: REPORTES
-- =============================================================

DROP PROCEDURE IF EXISTS REPORTE_VENTAS_POR_PERIODO$$
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
    FROM venta v
    LEFT  JOIN cliente     c  ON v.CLIENTE_ID     = c.USUARIO_ID
    LEFT  JOIN usuario     uc ON c.USUARIO_ID     = uc.USUARIO_ID
    INNER JOIN metodo_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
    WHERE DATE(v.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
      AND v.ESTADO_VENTA != 'ANULADA'
    ORDER BY v.FECHA_HORA;
END$$

DROP PROCEDURE IF EXISTS REPORTE_PRODUCTOS_BAJO_STOCK$$
CREATE PROCEDURE REPORTE_PRODUCTOS_BAJO_STOCK()
BEGIN
    SELECT p.PRODUCTO_ID, p.NOMBRE, c.NOMBRE AS CATEGORIA,
           p.STOCK, p.STOCK_MINIMO, p.UNIDAD_MEDIDA,
           (p.STOCK_MINIMO - p.STOCK) AS UNIDADES_FALTANTES
    FROM producto p
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.ACTIVO = 1 AND p.STOCK < p.STOCK_MINIMO
    ORDER BY (p.STOCK_MINIMO - p.STOCK) DESC;
END$$

DROP PROCEDURE IF EXISTS REPORTE_PRODUCTOS_MAS_VENDIDOS$$
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
    FROM detalle_venta dv
    INNER JOIN producto  p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    INNER JOIN venta     v ON dv.VENTA_ID    = v.VENTA_ID
    WHERE DATE(v.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
      AND v.ESTADO_VENTA != 'ANULADA'
    GROUP BY p.PRODUCTO_ID, p.NOMBRE, c.NOMBRE
    ORDER BY TOTAL_VENDIDO DESC
    LIMIT _limite;
END$$

DROP PROCEDURE IF EXISTS REPORTE_PERDIDAS_POR_PERIODO$$
CREATE PROCEDURE REPORTE_PERDIDAS_POR_PERIODO(
    IN _fecha_inicio DATE,
    IN _fecha_fin    DATE
)
BEGIN
    SELECT d.DEVOLUCION_ID, p.NOMBRE AS PRODUCTO, d.ESTADO_DEVOLUCION,
           d.CANTIDAD, d.MOTIVO, d.FECHA_HORA,
           CONCAT(u.NOMBRES, ' ', u.APELLIDOS) AS REGISTRADO_POR
    FROM devolucion d
    INNER JOIN producto    p ON d.PRODUCTO_ID   = p.PRODUCTO_ID
    INNER JOIN trabajador t ON d.TRABAJADOR_ID = t.USUARIO_ID
    INNER JOIN usuario     u ON t.USUARIO_ID    = u.USUARIO_ID
    WHERE d.ESTADO_DEVOLUCION = 'APROBADO'
      AND DATE(d.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
    ORDER BY d.FECHA_HORA DESC;
END$$

-- =====================================================================
-- AUTENTICACION UNIFICADA (LOGIN ÚNICO PARA LOS 3 ROLES)
-- Devuelve los datos del usuario + los campos propios del subtipo
-- (cliente/trabajador/administrador) + el ROL en una sola consulta.
-- El DAO usa ROL para instanciar el subtipo concreto polimórficamente.
-- DAO: AuthDaoImpl.autenticar → "AUTENTICAR_USUARIO"
-- =====================================================================
DROP PROCEDURE IF EXISTS AUTENTICAR_USUARIO$$
CREATE PROCEDURE AUTENTICAR_USUARIO(
    IN _correo     VARCHAR(100),
    IN _contrasena VARCHAR(255)
)
BEGIN
    SELECT
        u.USUARIO_ID,
        u.NOMBRES,
        u.APELLIDOS,
        u.DNI,
        u.TELEFONO,
        u.CORREO,
        c.DIRECCION_ENTREGA,
        t.CARGO,
        t.FECHA_INGRESO,
        CASE
            WHEN a.USUARIO_ID IS NOT NULL AND a.ACTIVO = 1 THEN 'ADMINISTRADOR'
            WHEN t.USUARIO_ID IS NOT NULL AND t.ACTIVO = 1 THEN 'TRABAJADOR'
            WHEN c.USUARIO_ID IS NOT NULL                  THEN 'CLIENTE'
            ELSE NULL
        END AS ROL
    FROM usuario u
    LEFT JOIN cliente       c ON u.USUARIO_ID = c.USUARIO_ID
    LEFT JOIN trabajador    t ON u.USUARIO_ID = t.USUARIO_ID
    LEFT JOIN administrador a ON u.USUARIO_ID = a.USUARIO_ID
    WHERE u.CORREO     = _correo
      AND u.CONTRASENA = _contrasena
      AND u.ACTIVO     = 1
    LIMIT 1;
END$$

-- =====================================================================
-- PRODUCTOS: BÚSQUEDA PAGINADA CON FILTROS
-- Parámetros NULL = "sin filtro"; _solo_promo=1 restringe a promociones vigentes.
-- DAO: ProductoDaoImpl.buscarPaginado / contarFiltrados
-- =====================================================================
DROP PROCEDURE IF EXISTS BUSCAR_PRODUCTOS_PAGINADO$$
CREATE PROCEDURE BUSCAR_PRODUCTOS_PAGINADO(
    IN _categoria_id INT,
    IN _q            VARCHAR(100),
    IN _precio_min   DECIMAL(10,2),
    IN _precio_max   DECIMAL(10,2),
    IN _solo_promo   TINYINT,
    IN _pagina       INT,
    IN _tamano       INT
)
BEGIN
    DECLARE _offset INT;
    SET _offset = (_pagina - 1) * _tamano;
    SELECT
        p.PRODUCTO_ID, p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
        p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS, p.IMAGEN_URL,
        p.ACTIVO, p.FECHA_CREACION,
        p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE
    FROM producto p
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.ACTIVO = 1
      AND (_categoria_id IS NULL OR p.CATEGORIA_ID = _categoria_id)
      AND (_q IS NULL OR _q = ''
           OR p.NOMBRE        LIKE CONCAT('%', _q, '%')
           OR p.CODIGO_BARRAS LIKE CONCAT('%', _q, '%'))
      AND (_precio_min IS NULL OR p.PRECIO_UNITARIO >= _precio_min)
      AND (_precio_max IS NULL OR p.PRECIO_UNITARIO <= _precio_max)
      AND (_solo_promo IS NULL OR _solo_promo = 0 OR EXISTS (
            SELECT 1 FROM promocion_producto pp
            INNER JOIN promocion pr ON pp.PROMOCION_ID = pr.PROMOCION_ID
            WHERE pp.PRODUCTO_ID = p.PRODUCTO_ID
              AND pr.ACTIVO = 1
              AND CURDATE() BETWEEN pr.FECHA_INICIO AND pr.FECHA_FIN))
    ORDER BY p.PRODUCTO_ID DESC
    LIMIT _tamano OFFSET _offset;
END$$

DROP PROCEDURE IF EXISTS CONTAR_PRODUCTOS_FILTRADOS$$
CREATE PROCEDURE CONTAR_PRODUCTOS_FILTRADOS(
    IN _categoria_id INT,
    IN _q            VARCHAR(100),
    IN _precio_min   DECIMAL(10,2),
    IN _precio_max   DECIMAL(10,2),
    IN _solo_promo   TINYINT
)
BEGIN
    SELECT COUNT(*) AS TOTAL
    FROM producto p
    WHERE p.ACTIVO = 1
      AND (_categoria_id IS NULL OR p.CATEGORIA_ID = _categoria_id)
      AND (_q IS NULL OR _q = ''
           OR p.NOMBRE        LIKE CONCAT('%', _q, '%')
           OR p.CODIGO_BARRAS LIKE CONCAT('%', _q, '%'))
      AND (_precio_min IS NULL OR p.PRECIO_UNITARIO >= _precio_min)
      AND (_precio_max IS NULL OR p.PRECIO_UNITARIO <= _precio_max)
      AND (_solo_promo IS NULL OR _solo_promo = 0 OR EXISTS (
            SELECT 1 FROM promocion_producto pp
            INNER JOIN promocion pr ON pp.PROMOCION_ID = pr.PROMOCION_ID
            WHERE pp.PRODUCTO_ID = p.PRODUCTO_ID
              AND pr.ACTIVO = 1
              AND CURDATE() BETWEEN pr.FECHA_INICIO AND pr.FECHA_FIN));
END$$

-- DAO: ProductoDaoImpl.buscarPorCodigoBarras (uso clave en POS)
DROP PROCEDURE IF EXISTS BUSCAR_PRODUCTO_X_CODIGO_BARRAS$$
CREATE PROCEDURE BUSCAR_PRODUCTO_X_CODIGO_BARRAS(
    IN _codigo VARCHAR(50)
)
BEGIN
    SELECT
        p.PRODUCTO_ID, p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
        p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS, p.IMAGEN_URL,
        p.ACTIVO, p.FECHA_CREACION,
        p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE
    FROM producto p
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.CODIGO_BARRAS = _codigo AND p.ACTIVO = 1
    LIMIT 1;
END$$

-- DAO: ProductoDaoImpl.listarBajoStock (panel inventario admin)
DROP PROCEDURE IF EXISTS LISTAR_PRODUCTOS_BAJO_STOCK$$
CREATE PROCEDURE LISTAR_PRODUCTOS_BAJO_STOCK()
BEGIN
    SELECT
        p.PRODUCTO_ID, p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
        p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS, p.IMAGEN_URL,
        p.ACTIVO, p.FECHA_CREACION,
        p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE
    FROM producto p
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.ACTIVO = 1 AND p.STOCK <= p.STOCK_MINIMO
    ORDER BY (p.STOCK_MINIMO - p.STOCK) DESC;
END$$

-- =====================================================================
-- PEDIDOS: listados filtrados
-- =====================================================================
-- DAO: PedidoDaoImpl.listarPorCliente
DROP PROCEDURE IF EXISTS LISTAR_PEDIDOS_X_CLIENTE$$
CREATE PROCEDURE LISTAR_PEDIDOS_X_CLIENTE(
    IN _cliente_id INT
)
BEGIN
    SELECT p.PEDIDO_ID, p.CLIENTE_ID, p.VENTA_ID, p.FECHA_HORA, p.MONTO_TOTAL,
           p.ESTADO_PEDIDO, p.DIRECCION_ENTREGA, p.MODALIDAD_ENTREGA, p.OBSERVACIONES
    FROM pedido p
    WHERE p.CLIENTE_ID = _cliente_id AND p.ACTIVO = 1
    ORDER BY p.FECHA_HORA DESC;
END$$

-- DAO: PedidoDaoImpl.listarPorEstado
DROP PROCEDURE IF EXISTS LISTAR_PEDIDOS_X_ESTADO$$
CREATE PROCEDURE LISTAR_PEDIDOS_X_ESTADO(
    IN _estado VARCHAR(20)
)
BEGIN
    SELECT p.PEDIDO_ID, p.CLIENTE_ID, p.VENTA_ID, p.FECHA_HORA, p.MONTO_TOTAL,
           p.ESTADO_PEDIDO, p.DIRECCION_ENTREGA, p.MODALIDAD_ENTREGA, p.OBSERVACIONES
    FROM pedido p
    WHERE p.ESTADO_PEDIDO = _estado AND p.ACTIVO = 1
    ORDER BY p.FECHA_HORA DESC;
END$$

-- =====================================================================
-- VENTAS: listados filtrados
-- =====================================================================
-- DAO: VentaDaoImpl.listarPorFechas
DROP PROCEDURE IF EXISTS LISTAR_VENTAS_X_FECHAS$$
CREATE PROCEDURE LISTAR_VENTAS_X_FECHAS(
    IN _fecha_inicio DATETIME,
    IN _fecha_fin    DATETIME
)
BEGIN
    SELECT v.VENTA_ID, v.CLIENTE_ID, v.TRABAJADOR_ID, v.METODO_PAGO_ID,
           m.NOMBRE AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES,
           v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA
    FROM venta v
    INNER JOIN metodo_pago m ON v.METODO_PAGO_ID = m.METODO_PAGO_ID
    WHERE v.FECHA_HORA BETWEEN _fecha_inicio AND _fecha_fin
      AND v.ACTIVO = 1
    ORDER BY v.FECHA_HORA DESC;
END$$

-- DAO: VentaDaoImpl.listarPorTrabajador
DROP PROCEDURE IF EXISTS LISTAR_VENTAS_X_TRABAJADOR$$
CREATE PROCEDURE LISTAR_VENTAS_X_TRABAJADOR(
    IN _trabajador_id INT
)
BEGIN
    SELECT v.VENTA_ID, v.CLIENTE_ID, v.TRABAJADOR_ID, v.METODO_PAGO_ID,
           m.NOMBRE AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES,
           v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA
    FROM venta v
    INNER JOIN metodo_pago m ON v.METODO_PAGO_ID = m.METODO_PAGO_ID
    WHERE v.TRABAJADOR_ID = _trabajador_id AND v.ACTIVO = 1
    ORDER BY v.FECHA_HORA DESC;
END$$

-- =====================================================================
-- KPIs DASHBOARD ADMIN
-- Devuelve una sola row con todos los KPIs del panel admin
-- DAO: DashboardDaoImpl.obtenerKpisAdmin
-- =====================================================================
DROP PROCEDURE IF EXISTS KPI_ADMIN_DASHBOARD$$
CREATE PROCEDURE KPI_ADMIN_DASHBOARD()
BEGIN
    SELECT
        (SELECT COALESCE(SUM(v.MONTO_TOTAL), 0)
           FROM venta v
           WHERE DATE(v.FECHA_HORA) = CURDATE()
             AND v.ESTADO_VENTA != 'ANULADA'
             AND v.ACTIVO = 1)                          AS VENTAS_HOY,

        (SELECT COUNT(*)
           FROM pedido p
           WHERE p.ESTADO_PEDIDO IN ('RECIBIDO','EN_PROCESO')
             AND p.ACTIVO = 1)                          AS PEDIDOS_PENDIENTES,

        (SELECT COUNT(*)
           FROM producto pr
           WHERE pr.STOCK <= pr.STOCK_MINIMO
             AND pr.ACTIVO = 1)                         AS PRODUCTOS_BAJO_STOCK,

        (SELECT COALESCE(SUM(v.MONTO_TOTAL), 0)
           FROM venta v
           WHERE YEAR(v.FECHA_HORA)  = YEAR(CURDATE())
             AND MONTH(v.FECHA_HORA) = MONTH(CURDATE())
             AND v.ESTADO_VENTA != 'ANULADA'
             AND v.ACTIVO = 1)                          AS INGRESOS_MES;
END$$

-- =====================================================================
-- KPIs DASHBOARD TRABAJADOR
-- DAO: DashboardDaoImpl.obtenerKpisTrabajador
-- =====================================================================
DROP PROCEDURE IF EXISTS KPI_TRABAJADOR_DASHBOARD$$
CREATE PROCEDURE KPI_TRABAJADOR_DASHBOARD(
    IN _trabajador_id INT
)
BEGIN
    SELECT
        (SELECT COUNT(*)
           FROM pedido p
           WHERE p.ESTADO_PEDIDO IN ('RECIBIDO','EN_PROCESO')
             AND DATE(p.FECHA_HORA) = CURDATE()
             AND p.ACTIVO = 1)                          AS PEDIDOS_PENDIENTES_HOY,

        (SELECT COUNT(*)
           FROM venta v
           WHERE v.TRABAJADOR_ID = _trabajador_id
             AND DATE(v.FECHA_HORA) = CURDATE()
             AND v.ESTADO_VENTA != 'ANULADA'
             AND v.ACTIVO = 1)                          AS VENTAS_HOY,

        (SELECT COALESCE(SUM(v.MONTO_TOTAL), 0)
           FROM venta v
           WHERE v.TRABAJADOR_ID = _trabajador_id
             AND DATE(v.FECHA_HORA) = CURDATE()
             AND v.ESTADO_VENTA != 'ANULADA'
             AND v.ACTIVO = 1)                          AS MONTO_RECAUDADO_HOY,

        (SELECT COUNT(*)
           FROM devolucion d
           WHERE d.TRABAJADOR_ID = _trabajador_id
             AND d.ESTADO_DEVOLUCION = 'APROBADO'
             AND DATE(d.FECHA_HORA) = CURDATE())        AS DEVOLUCIONES_ATENDIDAS_HOY;
END$$

-- =====================================================================
-- NOTIFICACIONES
-- =====================================================================
DROP PROCEDURE IF EXISTS INSERTAR_NOTIFICACION$$
CREATE PROCEDURE INSERTAR_NOTIFICACION(
    OUT _notif_id        INT,
    IN  _titulo          VARCHAR(150),
    IN  _mensaje         VARCHAR(500),
    IN  _tipo            VARCHAR(30),
    IN  _id_destinatario INT
)
BEGIN
    INSERT INTO notificacion(TITULO, MENSAJE, TIPO, LEIDA, ID_DESTINATARIO, ACTIVO)
    VALUES(_titulo, _mensaje, _tipo, 0, _id_destinatario, 1);
    SET _notif_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_NOTIFICACION$$
CREATE PROCEDURE MODIFICAR_NOTIFICACION(
    IN _notif_id  INT,
    IN _titulo    VARCHAR(150),
    IN _mensaje   VARCHAR(500),
    IN _tipo      VARCHAR(30),
    IN _leida     TINYINT
)
BEGIN
    UPDATE notificacion
    SET TITULO  = _titulo,
        MENSAJE = _mensaje,
        TIPO    = _tipo,
        LEIDA   = _leida
    WHERE NOTIFICACION_ID = _notif_id AND ACTIVO = 1;
END$$

-- Soft delete
DROP PROCEDURE IF EXISTS ELIMINAR_NOTIFICACION$$
CREATE PROCEDURE ELIMINAR_NOTIFICACION(IN _notif_id INT)
BEGIN
    UPDATE notificacion SET ACTIVO = 0 WHERE NOTIFICACION_ID = _notif_id;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_NOTIFICACION_X_ID$$
CREATE PROCEDURE BUSCAR_NOTIFICACION_X_ID(IN _notif_id INT)
BEGIN
    SELECT NOTIFICACION_ID, TITULO, MENSAJE, TIPO, LEIDA, FECHA_CREACION, ID_DESTINATARIO
    FROM notificacion
    WHERE NOTIFICACION_ID = _notif_id AND ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_NOTIFICACIONES$$
CREATE PROCEDURE LISTAR_NOTIFICACIONES()
BEGIN
    SELECT NOTIFICACION_ID, TITULO, MENSAJE, TIPO, LEIDA, FECHA_CREACION, ID_DESTINATARIO
    FROM notificacion
    WHERE ACTIVO = 1
    ORDER BY FECHA_CREACION DESC;
END$$

-- Devuelve notificaciones del usuario + las broadcast (ID_DESTINATARIO IS NULL)
DROP PROCEDURE IF EXISTS LISTAR_NOTIFICACIONES_X_USUARIO$$
CREATE PROCEDURE LISTAR_NOTIFICACIONES_X_USUARIO(IN _usuario_id INT)
BEGIN
    SELECT NOTIFICACION_ID, TITULO, MENSAJE, TIPO, LEIDA, FECHA_CREACION, ID_DESTINATARIO
    FROM notificacion
    WHERE ACTIVO = 1
      AND (ID_DESTINATARIO = _usuario_id OR ID_DESTINATARIO IS NULL)
    ORDER BY FECHA_CREACION DESC;
END$$

DROP PROCEDURE IF EXISTS MARCAR_NOTIFICACION_LEIDA$$
CREATE PROCEDURE MARCAR_NOTIFICACION_LEIDA(IN _notif_id INT)
BEGIN
    UPDATE notificacion SET LEIDA = 1 WHERE NOTIFICACION_ID = _notif_id;
END$$

DROP PROCEDURE IF EXISTS CONTAR_NOTIFICACIONES_NO_LEIDAS$$
CREATE PROCEDURE CONTAR_NOTIFICACIONES_NO_LEIDAS(IN _usuario_id INT)
BEGIN
    SELECT COUNT(*) AS TOTAL
    FROM notificacion
    WHERE ACTIVO = 1
      AND LEIDA  = 0
      AND (ID_DESTINATARIO = _usuario_id OR ID_DESTINATARIO IS NULL);
END$$

-- =====================================================================
-- BOLETAS (fase de una Venta — actualiza campos NUMERO_BOLETA, RUC, etc.)
-- =====================================================================
-- Emite una boleta sobre una venta existente.
-- Genera un correlativo tipo B-0000123 basado en el conteo actual.
DROP PROCEDURE IF EXISTS EMITIR_BOLETA$$
CREATE PROCEDURE EMITIR_BOLETA(
    IN  _venta_id INT,
    IN  _ruc      VARCHAR(20),
    IN  _contacto VARCHAR(150),
    IN  _mensaje  VARCHAR(500),
    OUT _numero   VARCHAR(20)
)
BEGIN
    DECLARE _siguiente INT;
    SELECT COUNT(*) + 1 INTO _siguiente
    FROM venta
    WHERE NUMERO_BOLETA IS NOT NULL;

    SET _numero = CONCAT('B-', LPAD(_siguiente, 7, '0'));

    UPDATE venta
    SET NUMERO_BOLETA    = _numero,
        RUC_EMPRESA      = _ruc,
        CONTACTO_CLIENTE = _contacto,
        MENSAJE_BOLETA   = _mensaje
    WHERE VENTA_ID = _venta_id;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_BOLETA_X_VENTA$$
CREATE PROCEDURE BUSCAR_BOLETA_X_VENTA(IN _venta_id INT)
BEGIN
    SELECT v.VENTA_ID, v.CLIENTE_ID, v.TRABAJADOR_ID, v.METODO_PAGO_ID,
           m.NOMBRE AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES,
           v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA
    FROM venta v
    INNER JOIN metodo_pago m ON v.METODO_PAGO_ID = m.METODO_PAGO_ID
    WHERE v.VENTA_ID = _venta_id
      AND v.NUMERO_BOLETA IS NOT NULL
      AND v.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS BUSCAR_BOLETA_X_NUMERO$$
CREATE PROCEDURE BUSCAR_BOLETA_X_NUMERO(IN _numero VARCHAR(20))
BEGIN
    SELECT v.VENTA_ID, v.CLIENTE_ID, v.TRABAJADOR_ID, v.METODO_PAGO_ID,
           m.NOMBRE AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES,
           v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA
    FROM venta v
    INNER JOIN metodo_pago m ON v.METODO_PAGO_ID = m.METODO_PAGO_ID
    WHERE v.NUMERO_BOLETA = _numero
      AND v.ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_BOLETAS$$
CREATE PROCEDURE LISTAR_BOLETAS()
BEGIN
    SELECT v.VENTA_ID, v.CLIENTE_ID, v.TRABAJADOR_ID, v.METODO_PAGO_ID,
           m.NOMBRE AS METODO_PAGO_NOMBRE,
           v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES,
           v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA
    FROM venta v
    INNER JOIN metodo_pago m ON v.METODO_PAGO_ID = m.METODO_PAGO_ID
    WHERE v.NUMERO_BOLETA IS NOT NULL
      AND v.ACTIVO = 1
    ORDER BY v.FECHA_HORA DESC;
END$$

-- Anula la boleta limpiando los campos (la venta como tal sigue existiendo).
DROP PROCEDURE IF EXISTS ANULAR_BOLETA$$
CREATE PROCEDURE ANULAR_BOLETA(IN _venta_id INT)
BEGIN
    UPDATE venta
    SET NUMERO_BOLETA    = NULL,
        RUC_EMPRESA      = NULL,
        CONTACTO_CLIENTE = NULL,
        MENSAJE_BOLETA   = NULL
    WHERE VENTA_ID = _venta_id;
END$$

DELIMITER ;
DELIMITER $$

-- =============================================================
-- MÓDULO REPORTES: TOP PRODUCTOS MÁS VENDIDOS
-- =============================================================
DROP PROCEDURE IF EXISTS TOP_PRODUCTOS_VENDIDOS$$
CREATE PROCEDURE TOP_PRODUCTOS_VENDIDOS()
BEGIN
    SELECT  p.PRODUCTO_ID,
            p.NOMBRE,
            p.IMAGEN_URL,
            SUM(dv.CANTIDAD)  AS TOTAL_UNIDADES,
            SUM(dv.SUBTOTAL)  AS TOTAL_INGRESOS
    FROM    detalle_venta dv
    INNER JOIN producto p  ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    INNER JOIN venta v     ON dv.VENTA_ID    = v.VENTA_ID
    WHERE   v.ESTADO_VENTA = 'COMPLETADA'
    GROUP BY dv.PRODUCTO_ID, p.NOMBRE, p.IMAGEN_URL
    ORDER BY TOTAL_UNIDADES DESC
    LIMIT 5;
END$$

DELIMITER ;
