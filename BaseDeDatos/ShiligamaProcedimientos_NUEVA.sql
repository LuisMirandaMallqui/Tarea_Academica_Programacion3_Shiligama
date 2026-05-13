-- =====================================================================
-- SHILIGAMA - Procedimientos Almacenados (ACTUALIZADO A SINGULAR)
-- =====================================================================
USE `shiligama`;

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

-- ---- BUSCAR_USUARIO_X_ID -----
DROP PROCEDURE IF EXISTS BUSCAR_USUARIO_X_ID$$
CREATE PROCEDURE BUSCAR_USUARIO_X_ID(
    IN _id INT
)
BEGIN
    SELECT 1 AS EXISTE
    FROM usuario
    WHERE USUARIO_ID = _id
    LIMIT 1;
END$$

-- ----- BUSCAR_USUARIO_X_DNI -----
DROP PROCEDURE IF EXISTS BUSCAR_USUARIO_X_DNI$$
CREATE PROCEDURE BUSCAR_USUARIO_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT 1 AS EXISTE
    FROM usuario
    WHERE DNI = _dni
    LIMIT 1;
END$$

-- =====================================================================
-- CLIENTES
-- =====================================================================
DROP PROCEDURE IF EXISTS INSERTAR_CLIENTE$$
CREATE PROCEDURE INSERTAR_CLIENTE(
    OUT _cliente_id       INT,
    IN  _nombres          VARCHAR(100),
    IN  _apellidos        VARCHAR(100),
    IN  _dni              VARCHAR(8),
    IN  _telefono         VARCHAR(15),
    IN  _correo           VARCHAR(100),
    IN  _contrasena       VARCHAR(255),
    IN  _direccion_entrega VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;
    INSERT INTO usuario(NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA, ACTIVO)
    VALUES(_nombres, _apellidos, _dni, _telefono, _correo, _contrasena, 1);
    SET v_usuario_id = LAST_INSERT_ID();
    INSERT INTO cliente(USUARIO_ID, DIRECCION_ENTREGA)
    VALUES(v_usuario_id, _direccion_entrega);
    SET _cliente_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_CLIENTE$$
CREATE PROCEDURE MODIFICAR_CLIENTE(
    IN _cliente_id         INT,
    IN _nombres            VARCHAR(100),
    IN _apellidos          VARCHAR(100),
    IN _dni                VARCHAR(8),
    IN _telefono           VARCHAR(15),
    IN _correo             VARCHAR(100),
    IN _direccion_entrega  VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;
    SELECT USUARIO_ID INTO v_usuario_id FROM cliente WHERE CLIENTE_ID = _cliente_id;
    UPDATE usuario SET
        NOMBRES = _nombres,
        APELLIDOS = _apellidos,
        TELEFONO = _telefono,
        CORREO = _correo
    WHERE USUARIO_ID = v_usuario_id;
    UPDATE cliente SET
        DIRECCION_ENTREGA = _direccion_entrega
    WHERE CLIENTE_ID = _cliente_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CLIENTE$$
CREATE PROCEDURE ELIMINAR_CLIENTE(
    IN _cliente_id INT
)
BEGIN
    DECLARE v_usuario_id INT;
    SELECT USUARIO_ID INTO v_usuario_id FROM cliente WHERE CLIENTE_ID = _cliente_id;
    UPDATE cliente SET ACTIVO = 0 WHERE CLIENTE_ID = _cliente_id;
    UPDATE usuario SET ACTIVO = 0 WHERE USUARIO_ID = v_usuario_id;
END$$

-- DAO: ClienteDaoImpl.buscarPorID → "BUSCAR_CLIENTE_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_ID$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_ID(
    IN _cliente_id INT
)
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE c.CLIENTE_ID = _cliente_id AND u.ACTIVO = 1;
END$$

-- DAO: ClienteDaoImpl.listarTodos → "LISTAR_CLIENTES"
DROP PROCEDURE IF EXISTS LISTAR_CLIENTES$$
CREATE PROCEDURE LISTAR_CLIENTES()
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.ACTIVO = 1;
END$$

-- DAO: ClienteDaoImpl.buscarPorCorreo → "BUSCAR_CLIENTE_X_CORREO"
DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_CORREO$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND u.ACTIVO = 1;
END$$

-- DAO: ClienteDaoImpl.obtenerPorDNI → "BUSCAR_CLIENTE_X_DNI"
DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_DNI$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM cliente c
    INNER JOIN usuario u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.DNI = _dni AND u.ACTIVO = 1;
END$$

-- =====================================================================
-- TRABAJADORES
-- =====================================================================
DROP PROCEDURE IF EXISTS INSERTAR_TRABAJADOR$$
CREATE PROCEDURE INSERTAR_TRABAJADOR(
    OUT _trabajador_id INT,
    IN  _nombres       VARCHAR(100),
    IN  _apellidos     VARCHAR(100),
    IN  _dni           VARCHAR(8),
    IN  _telefono      VARCHAR(15),
    IN  _correo        VARCHAR(100),
    IN  _contrasena    VARCHAR(255),
    IN  _cargo         VARCHAR(100),
    IN  _fecha_ingreso DATE
)
BEGIN
    DECLARE v_usuario_id INT;
    INSERT INTO usuario(NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA, ACTIVO)
    VALUES(_nombres, _apellidos, _dni, _telefono, _correo, _contrasena, 1);
    SET v_usuario_id = LAST_INSERT_ID();
    INSERT INTO trabajador(USUARIO_ID, CARGO, FECHA_INGRESO, ACTIVO)
    VALUES(v_usuario_id, _cargo, _fecha_ingreso, 1);
    SET _trabajador_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_TRABAJADOR$$
CREATE PROCEDURE MODIFICAR_TRABAJADOR(
    IN _trabajador_id INT,
    IN _nombres       VARCHAR(100),
    IN _apellidos     VARCHAR(100),
    IN _dni           VARCHAR(8),
    IN _telefono      VARCHAR(15),
    IN _correo        VARCHAR(100),
    IN _cargo         VARCHAR(100),
    IN _fecha_ingreso DATE
)
BEGIN
    DECLARE v_usuario_id INT;
    SELECT USUARIO_ID INTO v_usuario_id FROM trabajador WHERE TRABAJADOR_ID = _trabajador_id;
    UPDATE usuario SET
        NOMBRES = _nombres,
        APELLIDOS = _apellidos,
        TELEFONO = _telefono,
        CORREO = _correo
    WHERE USUARIO_ID = v_usuario_id;
    UPDATE trabajador SET
        CARGO = _cargo,
        FECHA_INGRESO = _fecha_ingreso
    WHERE TRABAJADOR_ID = _trabajador_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_TRABAJADOR$$
CREATE PROCEDURE ELIMINAR_TRABAJADOR(
    IN _trabajador_id INT
)
BEGIN
    DECLARE v_usuario_id INT;
    SELECT USUARIO_ID INTO v_usuario_id FROM trabajador WHERE TRABAJADOR_ID = _trabajador_id;
    UPDATE usuario     SET ACTIVO = 0 WHERE USUARIO_ID    = v_usuario_id;
    UPDATE trabajador SET ACTIVO = 0 WHERE TRABAJADOR_ID = _trabajador_id;
END$$

-- DAO: TrabajadorDaoImpl.buscarPorID → "BUSCAR_TRABAJADOR_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_ID$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_ID(
    IN _trabajador_id INT
)
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE t.TRABAJADOR_ID = _trabajador_id AND t.ACTIVO = 1;
END$$

-- DAO: TrabajadorDaoImpl.listarTodos → "LISTAR_TRABAJADORES"
DROP PROCEDURE IF EXISTS LISTAR_TRABAJADORES$$
CREATE PROCEDURE LISTAR_TRABAJADORES()
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE t.ACTIVO = 1;
END$$

-- DAO: TrabajadorDaoImpl.buscarPorCorreo → "BUSCAR_TRABAJADOR_X_CORREO"
DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_CORREO$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND t.ACTIVO = 1;
END$$

-- DAO: TrabajadorDaoImpl.obtenerPorDNI → "BUSCAR_TRABAJADOR_X_DNI"
DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_DNI$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajador t
    INNER JOIN usuario u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE u.DNI = _dni AND t.ACTIVO = 1;
END$$

-- =====================================================================
-- ADMINISTRADORES
-- =====================================================================
DROP PROCEDURE IF EXISTS INSERTAR_ADMINISTRADOR$$
CREATE PROCEDURE INSERTAR_ADMINISTRADOR(
    OUT _administrador_id INT,
    IN  _nombres          VARCHAR(100),
    IN  _apellidos        VARCHAR(100),
    IN  _dni              VARCHAR(8),
    IN  _telefono         VARCHAR(15),
    IN  _correo           VARCHAR(100),
    IN  _contrasena       VARCHAR(255)
)
BEGIN
    DECLARE v_usuario_id INT;
    INSERT INTO usuario(NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA, ACTIVO)
    VALUES(_nombres, _apellidos, _dni, _telefono, _correo, _contrasena, 1);
    SET v_usuario_id = LAST_INSERT_ID();
    INSERT INTO administrador(USUARIO_ID, ACTIVO)
    VALUES(v_usuario_id, 1);
    SET _administrador_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_ADMINISTRADOR$$
CREATE PROCEDURE MODIFICAR_ADMINISTRADOR(
    IN _administrador_id INT,
    IN _nombres          VARCHAR(100),
    IN _apellidos        VARCHAR(100),
    IN _dni              VARCHAR(8),
    IN _telefono         VARCHAR(15),
    IN _correo           VARCHAR(100)
)
BEGIN
    DECLARE v_usuario_id INT;
    SELECT USUARIO_ID INTO v_usuario_id FROM administrador WHERE ADMINISTRADOR_ID = _administrador_id;
    UPDATE usuario SET
        NOMBRES = _nombres,
        APELLIDOS = _apellidos,
        TELEFONO = _telefono,
        CORREO = _correo
    WHERE USUARIO_ID = v_usuario_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_ADMINISTRADOR$$
CREATE PROCEDURE ELIMINAR_ADMINISTRADOR(
    IN _administrador_id INT
)
BEGIN
    DECLARE v_usuario_id INT;
    SELECT USUARIO_ID INTO v_usuario_id FROM administrador WHERE ADMINISTRADOR_ID = _administrador_id;
    UPDATE administrador SET ACTIVO = 0 WHERE ADMINISTRADOR_ID = _administrador_id;
    UPDATE usuario SET ACTIVO = 0 WHERE USUARIO_ID = v_usuario_id;
END$$

-- DAO: AdministradorDaoImpl.buscarPorID → "BUSCAR_ADMINISTRADOR_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_ID$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_ID(
    IN _administrador_id INT
)
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administrador a
    INNER JOIN usuario u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE a.ADMINISTRADOR_ID = _administrador_id AND a.ACTIVO = 1;
END$$

-- DAO: AdministradorDaoImpl.listarTodos → "LISTAR_ADMINISTRADORES"
DROP PROCEDURE IF EXISTS LISTAR_ADMINISTRADORES$$
CREATE PROCEDURE LISTAR_ADMINISTRADORES()
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administrador a
    INNER JOIN usuario u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE a.ACTIVO = 1;
END$$

-- DAO: AdministradorDaoImpl.buscarPorCorreo → "BUSCAR_ADMINISTRADOR_X_CORREO"
DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_CORREO$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administrador a
    INNER JOIN usuario u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND a.ACTIVO = 1;
END$$

-- DAO: AdministradorDaoImpl.obtenerPorDNI → "BUSCAR_ADMINISTRADOR_X_DNI"
DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_DNI$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA
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
    IN  _nombre VARCHAR(100),
    IN  _descripcion VARCHAR(255),
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
        NOMBRE = _nombre,
        DESCRIPCION = _descripcion,
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
    IN  _categoria_id INT,
    IN  _nombre VARCHAR(150),
    IN  _descripcion VARCHAR(500),
    IN  _precio_unitario DECIMAL(10,2),
    IN  _stock INT,
    IN  _stock_minimo INT,
    IN  _unidad_medida VARCHAR(30),
    IN  _codigo_barras VARCHAR(50),
    IN  _imagen_url       VARCHAR(500)
)
BEGIN
    INSERT INTO producto(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO,
                          STOCK, STOCK_MINIMO, UNIDAD_MEDIDA, CODIGO_BARRAS, IMAGEN_URL)
    VALUES(_categoria_id, _nombre, _descripcion, _precio_unitario, _stock, _stock_minimo, _unidad_medida, _codigo_barras, _imagen_url);
    SET _producto_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_PRODUCTO$$
CREATE PROCEDURE MODIFICAR_PRODUCTO(
    IN _producto_id     INT,
    IN _categoria_id    INT,
    IN _nombre          VARCHAR(150),
    IN _descripcion     VARCHAR(500),
    IN _precio_unitario DECIMAL(10,2),
    IN _stock           INT,
    IN _stock_minimo    INT,
    IN _unidad_medida   VARCHAR(30),
    IN _codigo_barras   VARCHAR(50),
    IN _imagen_url      VARCHAR(500)
)
BEGIN
    UPDATE producto SET
        CATEGORIA_ID = _categoria_id,
        NOMBRE = _nombre,
        DESCRIPCION = _descripcion,
        PRECIO_UNITARIO = _precio_unitario,
        STOCK = _stock,
        STOCK_MINIMO = _stock_minimo,
        UNIDAD_MEDIDA = _unidad_medida,
        CODIGO_BARRAS = _codigo_barras,
        IMAGEN_URL = _imagen_url
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
           p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK, p.STOCK_MINIMO,
           p.UNIDAD_MEDIDA, p.CODIGO_BARRAS, p.IMAGEN_URL
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
           p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK, p.STOCK_MINIMO,
           p.UNIDAD_MEDIDA, p.CODIGO_BARRAS, p.IMAGEN_URL
    FROM producto p
    INNER JOIN categoria c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    WHERE p.PRODUCTO_ID = _producto_id;
END$$


-- =============================================================
-- MÓDULO 3: INVENTARIO Y MOVIMIENTOS
-- =============================================================

DROP PROCEDURE IF EXISTS REGISTRAR_MOVIMIENTO_INVENTARIO$$
CREATE PROCEDURE REGISTRAR_MOVIMIENTO_INVENTARIO(
    OUT _movimiento_id INT,
    IN  _producto_id INT,
    IN  _trabajador_id INT,
    IN  _tipo_movimiento VARCHAR(20),
    IN  _cantidad INT,
    IN  _motivo           VARCHAR(255)
)
BEGIN
    DECLARE v_stock_actual INT;
    DECLARE v_stock_resultante INT;
    
    SELECT STOCK INTO v_stock_actual FROM producto WHERE PRODUCTO_ID = _producto_id;
    
    IF _tipo_movimiento = 'ENTRADA' OR _tipo_movimiento = 'DEVOLUCION' THEN
        SET v_stock_resultante = v_stock_actual + _cantidad;
        UPDATE producto SET STOCK = v_stock_resultante WHERE PRODUCTO_ID = _producto_id;
    ELSEIF _tipo_movimiento = 'SALIDA' THEN
        SET v_stock_resultante = v_stock_actual - _cantidad;
        UPDATE producto SET STOCK = v_stock_resultante WHERE PRODUCTO_ID = _producto_id;
    ELSEIF _tipo_movimiento = 'AJUSTE' THEN
        SET v_stock_resultante = _cantidad;
        UPDATE producto SET STOCK = v_stock_resultante WHERE PRODUCTO_ID = _producto_id;
    END IF;
    
    INSERT INTO movimiento_inventario(PRODUCTO_ID, TRABAJADOR_ID, TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO)
    VALUES(_producto_id, _trabajador_id, _tipo_movimiento, _cantidad, v_stock_actual, v_stock_resultante, _motivo);
    SET _movimiento_id = LAST_INSERT_ID();
END$$

-- DAO: MovimientoInventarioDaoImpl.buscarPorID → "BUSCAR_MOVIMIENTO_POR_ID"
DROP PROCEDURE IF EXISTS BUSCAR_MOVIMIENTO_POR_ID$$
CREATE PROCEDURE BUSCAR_MOVIMIENTO_POR_ID(
    IN _movimiento_id INT
)
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO, FECHA_HORA
    FROM movimiento_inventario
    WHERE MOVIMIENTO_ID = _movimiento_id;
END$$

-- DAO: MovimientoInventarioDaoImpl.listarTodos → "LISTAR_MOVIMIENTOS_TODOS"
DROP PROCEDURE IF EXISTS LISTAR_MOVIMIENTOS_TODOS$$
CREATE PROCEDURE LISTAR_MOVIMIENTOS_TODOS()
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO, FECHA_HORA
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
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO, FECHA_HORA
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
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR, STOCK_RESULTANTE, MOTIVO, FECHA_HORA
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
    UPDATE metodo_pago SET
        NOMBRE = _nombre
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
-- MÓDULO 5: PEDIDOS Y DETALLES DE PEDIDO
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_PEDIDO$$
CREATE PROCEDURE INSERTAR_PEDIDO(
    OUT _pedido_id         INT,
    IN  _cliente_id        INT,
    IN  _direccion_entrega VARCHAR(255),
    IN  _modalidad_entrega VARCHAR(50),
    IN  _observaciones     VARCHAR(500)
)
BEGIN
    INSERT INTO pedido(CLIENTE_ID, DIRECCION_ENTREGA, MODALIDAD_ENTREGA, OBSERVACIONES)
    VALUES(_cliente_id, _direccion_entrega, _modalidad_entrega, _observaciones);
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
DROP PROCEDURE IF EXISTS BUSCAR_PEDIDO_X_ID$$
CREATE PROCEDURE BUSCAR_PEDIDO_X_ID(
    IN _pedido_id INT
)
BEGIN
    SELECT p.PEDIDO_ID, p.CLIENTE_ID, p.FECHA_HORA,
           p.MONTO_TOTAL, p.ESTADO_PEDIDO, p.PRIORIDAD, p.DIRECCION_ENTREGA,
           p.MODALIDAD_ENTREGA, p.OBSERVACIONES
    FROM pedido p
    WHERE p.PEDIDO_ID = _pedido_id AND p.ACTIVO = 1;
END$$

-- DAO: PedidoDaoImpl.listarTodos → "LISTAR_PEDIDOS"
DROP PROCEDURE IF EXISTS LISTAR_PEDIDOS$$
CREATE PROCEDURE LISTAR_PEDIDOS()
BEGIN
    SELECT p.PEDIDO_ID, p.CLIENTE_ID, p.FECHA_HORA,
           p.MONTO_TOTAL, p.ESTADO_PEDIDO, p.PRIORIDAD, p.DIRECCION_ENTREGA,
           p.MODALIDAD_ENTREGA, p.OBSERVACIONES
    FROM pedido p
    WHERE p.ACTIVO = 1
    ORDER BY p.FECHA_HORA ASC;
END$$


DROP PROCEDURE IF EXISTS INSERTAR_DETALLE_PEDIDO$$
CREATE PROCEDURE INSERTAR_DETALLE_PEDIDO(
    OUT _detalle_pedido_id INT,
    IN  _pedido_id         INT,
    IN  _producto_id       INT,
    IN  _cantidad          INT
)
BEGIN
    DECLARE v_precio     DECIMAL(10,2);
    DECLARE v_subtotal   DECIMAL(10,2);
    DECLARE v_monto_pedido DECIMAL(10,2);
    
    SELECT PRECIO_UNITARIO INTO v_precio FROM producto WHERE PRODUCTO_ID = _producto_id;
    SET v_subtotal = v_precio * _cantidad;
    
    INSERT INTO detalle_pedido(PEDIDO_ID, PRODUCTO_ID, CANTIDAD, PRECIO_UNITARIO, SUBTOTAL)
    VALUES(_pedido_id, _producto_id, _cantidad, v_precio, v_subtotal);
    SET _detalle_pedido_id = LAST_INSERT_ID();
    
    SELECT SUM(SUBTOTAL) INTO v_monto_pedido FROM detalle_pedido WHERE PEDIDO_ID = _pedido_id;
    UPDATE pedido SET MONTO_TOTAL = v_monto_pedido WHERE PEDIDO_ID = _pedido_id;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_DETALLE_PEDIDO$$
CREATE PROCEDURE MODIFICAR_DETALLE_PEDIDO(
    IN _detalle_pedido_id INT,
    IN _cantidad          INT
)
BEGIN
    DECLARE v_precio     DECIMAL(10,2);
    DECLARE v_subtotal   DECIMAL(10,2);
    DECLARE v_pedido_id   INT;
    DECLARE v_monto_pedido DECIMAL(10,2);
    
    SELECT PEDIDO_ID, PRECIO_UNITARIO INTO v_pedido_id, v_precio FROM detalle_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;
    SET v_subtotal = v_precio * _cantidad;
    
    UPDATE detalle_pedido SET
        CANTIDAD = _cantidad,
        SUBTOTAL = v_subtotal
    WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;
    
    SELECT SUM(SUBTOTAL) INTO v_monto_pedido FROM detalle_pedido WHERE PEDIDO_ID = v_pedido_id;
    UPDATE pedido SET MONTO_TOTAL = v_monto_pedido WHERE PEDIDO_ID = v_pedido_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_DETALLE_PEDIDO$$
CREATE PROCEDURE ELIMINAR_DETALLE_PEDIDO(
    IN _detalle_pedido_id INT
)
BEGIN
    DECLARE v_pedido_id INT;
    DECLARE v_monto_pedido DECIMAL(10,2);
    
    SELECT PEDIDO_ID INTO v_pedido_id FROM detalle_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;
    DELETE FROM detalle_pedido WHERE DETALLE_PEDIDO_ID = _detalle_pedido_id;
    
    SELECT SUM(SUBTOTAL) INTO v_monto_pedido FROM detalle_pedido WHERE PEDIDO_ID = v_pedido_id;
    UPDATE pedido SET MONTO_TOTAL = v_monto_pedido WHERE PEDIDO_ID = v_pedido_id;
END$$

DROP PROCEDURE IF EXISTS LISTAR_DETALLES_PEDIDO$$
CREATE PROCEDURE LISTAR_DETALLES_PEDIDO()
BEGIN
    SELECT dp.DETALLE_PEDIDO_ID, dp.PEDIDO_ID, dp.PRODUCTO_ID,
           p.NOMBRE, dp.CANTIDAD, dp.PRECIO_UNITARIO, dp.SUBTOTAL
    FROM detalle_pedido dp
    INNER JOIN producto p ON dp.PRODUCTO_ID = p.PRODUCTO_ID;
END$$

-- DAO: DetallePedidoDaoImpl.buscarPorID → "BUSCAR_DETALLE_PEDIDO_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_DETALLE_PEDIDO_X_ID$$
CREATE PROCEDURE BUSCAR_DETALLE_PEDIDO_X_ID(
    IN _detalle_pedido_id INT
)
BEGIN
    SELECT dp.DETALLE_PEDIDO_ID, dp.PEDIDO_ID, dp.PRODUCTO_ID,
           p.NOMBRE, dp.CANTIDAD, dp.PRECIO_UNITARIO, dp.SUBTOTAL
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
    SELECT dp.DETALLE_PEDIDO_ID, dp.PEDIDO_ID, dp.PRODUCTO_ID,
           p.NOMBRE, dp.CANTIDAD, dp.PRECIO_UNITARIO, dp.SUBTOTAL
    FROM detalle_pedido dp
    INNER JOIN producto p ON dp.PRODUCTO_ID = p.PRODUCTO_ID
    WHERE dp.PEDIDO_ID = _pedido_id;
END$$


-- =============================================================
-- MÓDULO 6: VENTAS (venta tiene FK a pedido)
-- =============================================================

DROP PROCEDURE IF EXISTS INSERTAR_VENTA$$
CREATE PROCEDURE INSERTAR_VENTA(
    OUT _venta_id       INT,
    IN  _pedido_id       INT,
    IN  _cliente_id      INT,
    IN  _trabajador_id   INT,
    IN  _metodo_pago_id  INT,
    IN  _canal_venta    VARCHAR(20),
    IN  _observaciones  VARCHAR(500)
)
BEGIN
    INSERT INTO venta(PEDIDO_ID, CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID, OBSERVACIONES, CANAL_VENTA)
    VALUES(_pedido_id, _cliente_id, _trabajador_id, _metodo_pago_id, _observaciones, _canal_venta);
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
    UPDATE venta SET ESTADO_VENTA = 'ANULADA'
    WHERE VENTA_ID = _venta_id;
END$$

-- DAO: VentaDaoImpl.buscarPorID → "BUSCAR_VENTA_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_VENTA_X_ID$$
CREATE PROCEDURE BUSCAR_VENTA_X_ID(
    IN _venta_id INT
)
BEGIN
    SELECT v.VENTA_ID, v.PEDIDO_ID, v.CLIENTE_ID, v.TRABAJADOR_ID,
           v.METODO_PAGO_ID, v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES,
           v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA
    FROM venta v
    WHERE v.VENTA_ID = _venta_id AND v.ACTIVO = 1;
END$$

-- DAO: VentaDaoImpl.listarTodos → "LISTAR_VENTAS"
DROP PROCEDURE IF EXISTS LISTAR_VENTAS$$
CREATE PROCEDURE LISTAR_VENTAS()
BEGIN
    SELECT v.VENTA_ID, v.PEDIDO_ID, v.CLIENTE_ID, v.TRABAJADOR_ID,
           v.METODO_PAGO_ID, v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO,
           v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES,
           v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA
    FROM venta v
    WHERE v.ACTIVO = 1
    ORDER BY v.FECHA_HORA DESC;
END$$

DELIMITER ;
