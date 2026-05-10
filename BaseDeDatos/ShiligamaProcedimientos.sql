-- =====================================================================
-- SHILIGAMA - Procedimientos Almacenados
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
    INSERT INTO usuarios(NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA, ACTIVO)
    VALUES(_nombres, _apellidos, _dni, _telefono, _correo, _contrasena, 1);
    SET _usuario_id = LAST_INSERT_ID();
END$$

-- ----- EXISTE_USUARIO_EN_BD -----
-- ClienteDaoImpl, TrabajadorDaoImpl, AdministradorDaoImpl
-- Params: IN _correo (pos 1), IN _dni (pos 2)
-- DAO solo verifica rs.next() — basta con devolver 1 fila si existe
DROP PROCEDURE IF EXISTS EXISTE_USUARIO_EN_BD$$
CREATE PROCEDURE EXISTE_USUARIO_EN_BD(
    IN _correo VARCHAR(100),
    IN _dni    VARCHAR(8)
)
BEGIN
    SELECT 1 AS EXISTE
    FROM usuarios
    WHERE (CORREO = _correo OR DNI = _dni) AND ACTIVO = 1
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

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    CALL INSERTAR_USUARIO(v_usuario_id, _nombres, _apellidos, _dni,
                          _telefono, _correo, _contrasena);

    INSERT INTO clientes(USUARIO_ID, DIRECCION_ENTREGA)
    VALUES(v_usuario_id, _direccion_entrega);

    SET _cliente_id = LAST_INSERT_ID();

    COMMIT;
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

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT USUARIO_ID INTO v_usuario_id
    FROM clientes WHERE CLIENTE_ID = _cliente_id;

    UPDATE usuarios SET
        NOMBRES   = _nombres,
        APELLIDOS = _apellidos,
        DNI       = _dni,
        TELEFONO  = _telefono,
        CORREO    = _correo
    WHERE USUARIO_ID = v_usuario_id;

    UPDATE clientes SET
        DIRECCION_ENTREGA = _direccion_entrega
    WHERE CLIENTE_ID = _cliente_id;

    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_CLIENTE$$
CREATE PROCEDURE ELIMINAR_CLIENTE(
    IN _cliente_id INT
)
BEGIN
    DECLARE v_usuario_id INT;

    SELECT USUARIO_ID INTO v_usuario_id
    FROM clientes WHERE CLIENTE_ID = _cliente_id;

    UPDATE usuarios SET ACTIVO = 0 WHERE USUARIO_ID = v_usuario_id;
END$$

-- DAO: ClienteDaoImpl.buscarPorID → "BUSCAR_CLIENTE_X_ID"
-- Columnas que lee el DAO: CLIENTE_ID, DIRECCION_ENTREGA, USUARIO_ID,
--   NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA
DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_ID$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_ID(
    IN _cliente_id INT
)
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM clientes c
    INNER JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE c.CLIENTE_ID = _cliente_id AND u.ACTIVO = 1;
END$$

-- DAO: ClienteDaoImpl.listarTodos → "LISTAR_CLIENTES"
DROP PROCEDURE IF EXISTS LISTAR_CLIENTES$$
CREATE PROCEDURE LISTAR_CLIENTES()
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM clientes c
    INNER JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.ACTIVO = 1;
END$$

-- DAO: ClienteDaoImpl.buscarPorCorreo → "BUSCAR_CLIENTE_X_CORREO"
DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_CORREO$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM clientes c
    INNER JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND u.ACTIVO = 1;
END$$

-- DAO: ClienteDaoImpl.obtenerPorDNI → "BUSCAR_CLIENTE_X_DNI"
DROP PROCEDURE IF EXISTS BUSCAR_CLIENTE_X_DNI$$
CREATE PROCEDURE BUSCAR_CLIENTE_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM clientes c
    INNER JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID
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

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    CALL INSERTAR_USUARIO(v_usuario_id, _nombres, _apellidos, _dni,
                          _telefono, _correo, _contrasena);

    INSERT INTO trabajadores(USUARIO_ID, CARGO, FECHA_INGRESO, ACTIVO)
    VALUES(v_usuario_id, _cargo, _fecha_ingreso, 1);

    SET _trabajador_id = LAST_INSERT_ID();

    COMMIT;
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_TRABAJADOR$$
CREATE PROCEDURE MODIFICAR_TRABAJADOR(
    IN _trabajador_id INT,
    IN _nombres       VARCHAR(100),
    IN _apellidos     VARCHAR(100),
    IN _dni           VARCHAR(8),
    IN _telefono      VARCHAR(15),
    IN _correo        VARCHAR(100),
    IN _fecha_ingreso DATE
)
BEGIN
    DECLARE v_usuario_id INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT USUARIO_ID INTO v_usuario_id
    FROM trabajadores WHERE TRABAJADOR_ID = _trabajador_id;

    UPDATE usuarios SET
        NOMBRES   = _nombres,
        APELLIDOS = _apellidos,
        DNI       = _dni,
        TELEFONO  = _telefono,
        CORREO    = _correo
    WHERE USUARIO_ID = v_usuario_id;

    UPDATE trabajadores SET
        FECHA_INGRESO = _fecha_ingreso
    WHERE TRABAJADOR_ID = _trabajador_id;

    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_TRABAJADOR$$
CREATE PROCEDURE ELIMINAR_TRABAJADOR(
    IN _trabajador_id INT
)
BEGIN
    DECLARE v_usuario_id INT;

    SELECT USUARIO_ID INTO v_usuario_id
    FROM trabajadores WHERE TRABAJADOR_ID = _trabajador_id;

    UPDATE usuarios     SET ACTIVO = 0 WHERE USUARIO_ID    = v_usuario_id;
    UPDATE trabajadores SET ACTIVO = 0 WHERE TRABAJADOR_ID = _trabajador_id;
END$$

-- DAO: TrabajadorDaoImpl.buscarPorID → "BUSCAR_TRABAJADOR_X_ID"
-- Columnas: TRABAJADOR_ID, FECHA_INGRESO, USUARIO_ID,
--   NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO, CONTRASENA
DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_ID$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_ID(
    IN _trabajador_id INT
)
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajadores t
    INNER JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE t.TRABAJADOR_ID = _trabajador_id AND t.ACTIVO = 1;
END$$

-- DAO: TrabajadorDaoImpl.listarTodos → "LISTAR_TRABAJADORES"
DROP PROCEDURE IF EXISTS LISTAR_TRABAJADORES$$
CREATE PROCEDURE LISTAR_TRABAJADORES()
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajadores t
    INNER JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE t.ACTIVO = 1;
END$$

-- DAO: TrabajadorDaoImpl.buscarPorCorreo → "BUSCAR_TRABAJADOR_X_CORREO"
DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_CORREO$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajadores t
    INNER JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND t.ACTIVO = 1;
END$$

-- DAO: TrabajadorDaoImpl.obtenerPorDNI → "BUSCAR_TRABAJADOR_X_DNI"
DROP PROCEDURE IF EXISTS BUSCAR_TRABAJADOR_X_DNI$$
CREATE PROCEDURE BUSCAR_TRABAJADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT t.TRABAJADOR_ID, t.FECHA_INGRESO,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM trabajadores t
    INNER JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID
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

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    CALL INSERTAR_USUARIO(v_usuario_id, _nombres, _apellidos, _dni,
                          _telefono, _correo, _contrasena);

    INSERT INTO administradores(USUARIO_ID, ACTIVO)
    VALUES(v_usuario_id, 1);

    SET _administrador_id = LAST_INSERT_ID();

    COMMIT;
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

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    SELECT USUARIO_ID INTO v_usuario_id
    FROM administradores WHERE ADMINISTRADOR_ID = _administrador_id;

    UPDATE usuarios SET
        NOMBRES   = _nombres,
        APELLIDOS = _apellidos,
        DNI       = _dni,
        TELEFONO  = _telefono,
        CORREO    = _correo
    WHERE USUARIO_ID = v_usuario_id;

    COMMIT;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_ADMINISTRADOR$$
CREATE PROCEDURE ELIMINAR_ADMINISTRADOR(
    IN _administrador_id INT
)
BEGIN
    DECLARE v_usuario_id INT;

    SELECT USUARIO_ID INTO v_usuario_id
    FROM administradores WHERE ADMINISTRADOR_ID = _administrador_id;

    UPDATE usuarios        SET ACTIVO = 0 WHERE USUARIO_ID       = v_usuario_id;
    UPDATE administradores SET ACTIVO = 0 WHERE ADMINISTRADOR_ID = _administrador_id;
END$$

-- DAO: AdministradorDaoImpl.buscarPorID → "BUSCAR_ADMINISTRADOR_X_ID"
-- Columnas: ADMINISTRADOR_ID, USUARIO_ID, NOMBRES, APELLIDOS, DNI,
--   TELEFONO, CORREO, CONTRASENA
DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_ID$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_ID(
    IN _administrador_id INT
)
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administradores a
    INNER JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE a.ADMINISTRADOR_ID = _administrador_id AND a.ACTIVO = 1;
END$$

-- DAO: AdministradorDaoImpl.listarTodos → "LISTAR_ADMINISTRADORES"
DROP PROCEDURE IF EXISTS LISTAR_ADMINISTRADORES$$
CREATE PROCEDURE LISTAR_ADMINISTRADORES()
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administradores a
    INNER JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE a.ACTIVO = 1;
END$$

-- DAO: AdministradorDaoImpl.buscarPorCorreo → "BUSCAR_ADMINISTRADOR_X_CORREO"
DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_CORREO$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_CORREO(
    IN _correo VARCHAR(100)
)
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administradores a
    INNER JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID
    WHERE u.CORREO = _correo AND a.ACTIVO = 1;
END$$

-- DAO: AdministradorDaoImpl.obtenerPorDNI → "BUSCAR_ADMINISTRADOR_X_DNI"
DROP PROCEDURE IF EXISTS BUSCAR_ADMINISTRADOR_X_DNI$$
CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT a.ADMINISTRADOR_ID,
           u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI,
           u.TELEFONO, u.CORREO, u.CONTRASENA
    FROM administradores a
    INNER JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID
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
    INSERT INTO categorias(NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID)
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
    UPDATE categorias SET
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
    UPDATE categorias SET ACTIVO = 0 WHERE CATEGORIA_ID = _categoria_id;
END$$

DROP PROCEDURE IF EXISTS LISTAR_CATEGORIAS$$
CREATE PROCEDURE LISTAR_CATEGORIAS()
BEGIN
    SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO
    FROM categorias WHERE ACTIVO = 1;
END$$

-- DAO: CategoriaDaoImpl.buscarPorID → "BUSCAR_CATEGORIA_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_CATEGORIA_X_ID$$
CREATE PROCEDURE BUSCAR_CATEGORIA_X_ID(
    IN _categoria_id INT
)
BEGIN
    SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO
    FROM categorias
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
    INSERT INTO productos(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO,
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

DROP PROCEDURE IF EXISTS ELIMINAR_PRODUCTO$$
CREATE PROCEDURE ELIMINAR_PRODUCTO(
    IN _producto_id INT
)
BEGIN
    UPDATE productos SET ACTIVO = 0 WHERE PRODUCTO_ID = _producto_id;
END$$

DROP PROCEDURE IF EXISTS LISTAR_PRODUCTOS$$
CREATE PROCEDURE LISTAR_PRODUCTOS()
BEGIN
    SELECT p.PRODUCTO_ID, p.CATEGORIA_ID, c.NOMBRE AS CATEGORIA_NOMBRE,
           p.NOMBRE, p.DESCRIPCION, p.PRECIO_UNITARIO, p.STOCK,
           p.STOCK_MINIMO, p.UNIDAD_MEDIDA, p.CODIGO_BARRAS,
           p.IMAGEN_URL, p.ACTIVO, p.FECHA_CREACION
    FROM productos p
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
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
    FROM productos p
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
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

-- DAO: MovimientoInventarioDaoImpl.buscarPorID → "BUSCAR_MOVIMIENTO_POR_ID"
DROP PROCEDURE IF EXISTS BUSCAR_MOVIMIENTO_POR_ID$$
CREATE PROCEDURE BUSCAR_MOVIMIENTO_POR_ID(
    IN _movimiento_id INT
)
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR,
           STOCK_RESULTANTE, MOTIVO, FECHA_HORA
    FROM movimientos_inventario
    WHERE MOVIMIENTO_ID = _movimiento_id;
END$$

-- DAO: MovimientoInventarioDaoImpl.listarTodos → "LISTAR_MOVIMIENTOS_TODOS"
DROP PROCEDURE IF EXISTS LISTAR_MOVIMIENTOS_TODOS$$
CREATE PROCEDURE LISTAR_MOVIMIENTOS_TODOS()
BEGIN
    SELECT MOVIMIENTO_ID, PRODUCTO_ID, TRABAJADOR_ID,
           TIPO_MOVIMIENTO, CANTIDAD, STOCK_ANTERIOR,
           STOCK_RESULTANTE, MOTIVO, FECHA_HORA
    FROM movimientos_inventario
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
    FROM movimientos_inventario
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
    FROM movimientos_inventario
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
    INSERT INTO metodos_pago(NOMBRE, ACTIVO)
    VALUES(_nombre, 1);
    SET _metodo_pago_id = LAST_INSERT_ID();
END$$

DROP PROCEDURE IF EXISTS MODIFICAR_METODO_PAGO$$
CREATE PROCEDURE MODIFICAR_METODO_PAGO(
    IN _metodo_pago_id INT,
    IN _nombre         VARCHAR(50)
)
BEGIN
    UPDATE metodos_pago
    SET NOMBRE = _nombre
    WHERE METODO_PAGO_ID = _metodo_pago_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_METODO_PAGO$$
CREATE PROCEDURE ELIMINAR_METODO_PAGO(
    IN _metodo_pago_id INT
)
BEGIN
    UPDATE metodos_pago SET ACTIVO = 0
    WHERE METODO_PAGO_ID = _metodo_pago_id;
END$$

-- DAO: MetodoPagoDaoImpl.buscarPorID → "BUSCAR_METODO_PAGO_X_ID"
DROP PROCEDURE IF EXISTS BUSCAR_METODO_PAGO_X_ID$$
CREATE PROCEDURE BUSCAR_METODO_PAGO_X_ID(
    IN _metodo_pago_id INT
)
BEGIN
    SELECT METODO_PAGO_ID, NOMBRE, ACTIVO
    FROM metodos_pago
    WHERE METODO_PAGO_ID = _metodo_pago_id AND ACTIVO = 1;
END$$

DROP PROCEDURE IF EXISTS LISTAR_METODOS_PAGO$$
CREATE PROCEDURE LISTAR_METODOS_PAGO()
BEGIN
    SELECT METODO_PAGO_ID, NOMBRE, ACTIVO
    FROM metodos_pago
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
    INSERT INTO ventas(CLIENTE_ID, TRABAJADOR_ID, METODO_PAGO_ID,
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
    UPDATE ventas SET ESTADO_VENTA = 'COMPLETADA'
    WHERE VENTA_ID = _venta_id;
END$$

DROP PROCEDURE IF EXISTS ANULAR_VENTA$$
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
    FROM ventas v
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
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
    FROM ventas v
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
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

DROP PROCEDURE IF EXISTS MODIFICAR_DETALLE_VENTA$$
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

DROP PROCEDURE IF EXISTS ELIMINAR_DETALLE_VENTA$$
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
    FROM detalles_venta dv
    INNER JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID;
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
    FROM detalles_venta dv
    INNER JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
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
    FROM detalles_venta dv
    INNER JOIN productos p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
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
    FROM pedidos
    WHERE ESTADO_PEDIDO IN ('RECIBIDO', 'EN_PROCESO');

    INSERT INTO pedidos(CLIENTE_ID, DIRECCION_ENTREGA, MODALIDAD_ENTREGA,
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
    UPDATE pedidos SET ESTADO_PEDIDO = _estado_pedido
    WHERE PEDIDO_ID = _pedido_id;
END$$

DROP PROCEDURE IF EXISTS ELIMINAR_PEDIDO$$
CREATE PROCEDURE ELIMINAR_PEDIDO(
    IN _pedido_id INT
)
BEGIN
    UPDATE pedidos SET ACTIVO = 0
    WHERE PEDIDO_ID = _pedido_id;
END$$

-- DAO: PedidoDaoImpl.buscarPorID → "BUSCAR_PEDIDO_X_ID"
-- Nota: DDL de pedidos NO tiene columna MONTO_DESCUENTO, se eliminó del SELECT
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
    FROM pedidos p
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
    FROM pedidos p
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
    FROM productos WHERE PRODUCTO_ID = _producto_id;

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

DROP PROCEDURE IF EXISTS MODIFICAR_DETALLE_PEDIDO$$
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

DROP PROCEDURE IF EXISTS ELIMINAR_DETALLE_PEDIDO$$
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
    FROM detalles_pedido dp
    INNER JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID;
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
    FROM detalles_pedido dp
    INNER JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID
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
    FROM detalles_pedido dp
    INNER JOIN productos p ON dp.PRODUCTO_ID = p.PRODUCTO_ID
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
    INSERT INTO promociones(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO,
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
    UPDATE promociones SET
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
    UPDATE promociones SET ACTIVO = 0
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
    FROM promociones
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
    FROM promociones
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
    FROM promociones
    WHERE ACTIVO = 1 AND CURDATE() BETWEEN FECHA_INICIO AND FECHA_FIN;
END$$

-- DAO: PromocionDaoImpl.asociarProducto → "VINCULAR_PRODUCTO_PROMOCION"
DROP PROCEDURE IF EXISTS VINCULAR_PRODUCTO_PROMOCION$$
CREATE PROCEDURE VINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id  INT
)
BEGIN
    INSERT IGNORE INTO promociones_productos(PROMOCION_ID, PRODUCTO_ID)
    VALUES(_promocion_id, _producto_id);
END$$

-- DAO: PromocionDaoImpl.desasociarProducto → "DESVINCULAR_PRODUCTO_PROMOCION"
DROP PROCEDURE IF EXISTS DESVINCULAR_PRODUCTO_PROMOCION$$
CREATE PROCEDURE DESVINCULAR_PRODUCTO_PROMOCION(
    IN _promocion_id INT,
    IN _producto_id  INT
)
BEGIN
    DELETE FROM promociones_productos
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
    FROM promociones_productos pp
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
    INSERT INTO devoluciones(PRODUCTO_ID, TRABAJADOR_ID, ESTADO_DEVOLUCION,
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
    UPDATE devoluciones SET
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
    UPDATE devoluciones SET ACTIVO = 0
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
    FROM devoluciones
    WHERE DEVOLUCION_ID = _devolucion_id AND ACTIVO = 1;
END$$

-- DAO: DevolucionDaoImpl.listarTodos → "LISTAR_DEVOLUCIONES_TODAS"
DROP PROCEDURE IF EXISTS LISTAR_DEVOLUCIONES_TODAS$$
CREATE PROCEDURE LISTAR_DEVOLUCIONES_TODAS()
BEGIN
    SELECT DEVOLUCION_ID, PRODUCTO_ID, TRABAJADOR_ID,
           ESTADO_DEVOLUCION, CANTIDAD, MOTIVO, FECHA_HORA, ACTIVO
    FROM devoluciones
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
    FROM devoluciones
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
    FROM ventas v
    LEFT  JOIN clientes     c  ON v.CLIENTE_ID     = c.CLIENTE_ID
    LEFT  JOIN usuarios     uc ON c.USUARIO_ID     = uc.USUARIO_ID
    INNER JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID
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
    FROM productos p
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
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
    FROM detalles_venta dv
    INNER JOIN productos  p ON dv.PRODUCTO_ID = p.PRODUCTO_ID
    INNER JOIN categorias c ON p.CATEGORIA_ID = c.CATEGORIA_ID
    INNER JOIN ventas     v ON dv.VENTA_ID    = v.VENTA_ID
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
    FROM devoluciones d
    INNER JOIN productos    p ON d.PRODUCTO_ID   = p.PRODUCTO_ID
    INNER JOIN trabajadores t ON d.TRABAJADOR_ID = t.TRABAJADOR_ID
    INNER JOIN usuarios     u ON t.USUARIO_ID    = u.USUARIO_ID
    WHERE d.ESTADO_DEVOLUCION = 'APROBADO'
      AND DATE(d.FECHA_HORA) BETWEEN _fecha_inicio AND _fecha_fin
    ORDER BY d.FECHA_HORA DESC;
END$$

DELIMITER ;
