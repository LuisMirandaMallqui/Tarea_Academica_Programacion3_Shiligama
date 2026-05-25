-- ==========================================================
-- MÓDULO USUARIOS — HERENCIA: tabla padre (usuario) + hijos
-- Patrón: Table-per-subclass con PK compartida
-- Los hijos NO tienen su propio ID, usan id_usuario del padre
-- ==========================================================

-- ==========================================================
-- TABLAS
-- ==========================================================

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario      INT AUTO_INCREMENT PRIMARY KEY,
    nombres         VARCHAR(100)  NOT NULL,
    apellidos       VARCHAR(100)  NOT NULL,
    dni             VARCHAR(8)    NOT NULL UNIQUE,
    telefono        VARCHAR(15),
    correo          VARCHAR(150)  NOT NULL UNIQUE,
    contrasena      VARCHAR(255)  NOT NULL,
    activo          TINYINT(1)    NOT NULL DEFAULT 1,
    fecha_creacion  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Hijo: CLIENTE  (PK = id_usuario = FK a usuario)
CREATE TABLE IF NOT EXISTS cliente (
    id_usuario          INT PRIMARY KEY,
    direccion_entrega   VARCHAR(255),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- Hijo: ADMINISTRADOR  (PK = id_usuario = FK a usuario)
CREATE TABLE IF NOT EXISTS administrador (
    id_usuario  INT PRIMARY KEY,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- Hijo: TRABAJADOR  (PK = id_usuario = FK a usuario)
CREATE TABLE IF NOT EXISTS trabajador (
    id_usuario      INT PRIMARY KEY,
    fecha_ingreso   DATE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);


-- ==========================================================
-- PROCEDIMIENTOS ALMACENADOS — CLIENTE
-- ==========================================================
DELIMITER $$

-- INSERT: primero inserta en usuario (padre), luego en cliente (hijo)
-- Retorna el id_usuario generado como OUT
CREATE PROCEDURE INSERTAR_CLIENTE(
    OUT _id_usuario INT,
    IN _nombres VARCHAR(100),
    IN _apellidos VARCHAR(100),
    IN _dni VARCHAR(8),
    IN _telefono VARCHAR(15),
    IN _correo VARCHAR(150),
    IN _contrasena VARCHAR(255),
    IN _direccion_entrega VARCHAR(255)
)
BEGIN
    INSERT INTO usuario(nombres, apellidos, dni, telefono, correo, contrasena)
    VALUES (_nombres, _apellidos, _dni, _telefono, _correo, _contrasena);

    SET _id_usuario = LAST_INSERT_ID();

    INSERT INTO cliente(id_usuario, direccion_entrega)
    VALUES (_id_usuario, _direccion_entrega);
END$$

-- UPDATE: modifica datos del padre + datos del hijo
CREATE PROCEDURE MODIFICAR_CLIENTE(
    IN _id_usuario INT,
    IN _nombres VARCHAR(100),
    IN _apellidos VARCHAR(100),
    IN _dni VARCHAR(8),
    IN _telefono VARCHAR(15),
    IN _correo VARCHAR(150),
    IN _direccion_entrega VARCHAR(255)
)
BEGIN
    UPDATE usuario
    SET nombres = _nombres, apellidos = _apellidos, dni = _dni,
        telefono = _telefono, correo = _correo
    WHERE id_usuario = _id_usuario AND activo = 1;

    UPDATE cliente
    SET direccion_entrega = _direccion_entrega
    WHERE id_usuario = _id_usuario;
END$$

-- DELETE lógico: solo desactiva en la tabla padre
CREATE PROCEDURE ELIMINAR_CLIENTE(
    IN _id_usuario INT
)
BEGIN
    UPDATE usuario SET activo = 0 WHERE id_usuario = _id_usuario;
END$$

-- SELECT por ID: JOIN padre + hijo
CREATE PROCEDURE BUSCAR_CLIENTE_X_ID(
    IN _id_usuario INT
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, c.direccion_entrega AS DIRECCION_ENTREGA
    FROM usuario u
    INNER JOIN cliente c ON u.id_usuario = c.id_usuario
    WHERE u.id_usuario = _id_usuario AND u.activo = 1;
END$$

-- SELECT todos los clientes activos
CREATE PROCEDURE LISTAR_CLIENTES()
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, c.direccion_entrega AS DIRECCION_ENTREGA
    FROM usuario u
    INNER JOIN cliente c ON u.id_usuario = c.id_usuario
    WHERE u.activo = 1;
END$$

-- Buscar cliente por correo
CREATE PROCEDURE BUSCAR_CLIENTE_X_CORREO(
    IN _correo VARCHAR(150)
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, c.direccion_entrega AS DIRECCION_ENTREGA
    FROM usuario u
    INNER JOIN cliente c ON u.id_usuario = c.id_usuario
    WHERE u.correo = _correo AND u.activo = 1;
END$$

-- Buscar cliente por DNI
CREATE PROCEDURE BUSCAR_CLIENTE_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, c.direccion_entrega AS DIRECCION_ENTREGA
    FROM usuario u
    INNER JOIN cliente c ON u.id_usuario = c.id_usuario
    WHERE u.dni = _dni AND u.activo = 1;
END$$

-- ==========================================================
-- PROCEDIMIENTOS ALMACENADOS — ADMINISTRADOR
-- ==========================================================

CREATE PROCEDURE INSERTAR_ADMINISTRADOR(
    OUT _id_usuario INT,
    IN _nombres VARCHAR(100),
    IN _apellidos VARCHAR(100),
    IN _dni VARCHAR(8),
    IN _telefono VARCHAR(15),
    IN _correo VARCHAR(150),
    IN _contrasena VARCHAR(255)
)
BEGIN
    INSERT INTO usuario(nombres, apellidos, dni, telefono, correo, contrasena)
    VALUES (_nombres, _apellidos, _dni, _telefono, _correo, _contrasena);

    SET _id_usuario = LAST_INSERT_ID();

    INSERT INTO administrador(id_usuario)
    VALUES (_id_usuario);
END$$

CREATE PROCEDURE MODIFICAR_ADMINISTRADOR(
    IN _id_usuario INT,
    IN _nombres VARCHAR(100),
    IN _apellidos VARCHAR(100),
    IN _dni VARCHAR(8),
    IN _telefono VARCHAR(15),
    IN _correo VARCHAR(150)
)
BEGIN
    UPDATE usuario
    SET nombres = _nombres, apellidos = _apellidos, dni = _dni,
        telefono = _telefono, correo = _correo
    WHERE id_usuario = _id_usuario AND activo = 1;
END$$

CREATE PROCEDURE ELIMINAR_ADMINISTRADOR(
    IN _id_usuario INT
)
BEGIN
    UPDATE usuario SET activo = 0 WHERE id_usuario = _id_usuario;
END$$

CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_ID(
    IN _id_usuario INT
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA
    FROM usuario u
    INNER JOIN administrador a ON u.id_usuario = a.id_usuario
    WHERE u.id_usuario = _id_usuario AND u.activo = 1;
END$$

CREATE PROCEDURE LISTAR_ADMINISTRADORES()
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA
    FROM usuario u
    INNER JOIN administrador a ON u.id_usuario = a.id_usuario
    WHERE u.activo = 1;
END$$

CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_CORREO(
    IN _correo VARCHAR(150)
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA
    FROM usuario u
    INNER JOIN administrador a ON u.id_usuario = a.id_usuario
    WHERE u.correo = _correo AND u.activo = 1;
END$$

CREATE PROCEDURE BUSCAR_ADMINISTRADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA
    FROM usuario u
    INNER JOIN administrador a ON u.id_usuario = a.id_usuario
    WHERE u.dni = _dni AND u.activo = 1;
END$$

-- ==========================================================
-- PROCEDIMIENTOS ALMACENADOS — TRABAJADOR
-- ==========================================================

CREATE PROCEDURE INSERTAR_TRABAJADOR(
    OUT _id_usuario INT,
    IN _nombres VARCHAR(100),
    IN _apellidos VARCHAR(100),
    IN _dni VARCHAR(8),
    IN _telefono VARCHAR(15),
    IN _correo VARCHAR(150),
    IN _contrasena VARCHAR(255),
    IN _fecha_ingreso DATE
)
BEGIN
    INSERT INTO usuario(nombres, apellidos, dni, telefono, correo, contrasena)
    VALUES (_nombres, _apellidos, _dni, _telefono, _correo, _contrasena);

    SET _id_usuario = LAST_INSERT_ID();

    INSERT INTO trabajador(id_usuario, fecha_ingreso)
    VALUES (_id_usuario, _fecha_ingreso);
END$$

CREATE PROCEDURE MODIFICAR_TRABAJADOR(
    IN _id_usuario INT,
    IN _nombres VARCHAR(100),
    IN _apellidos VARCHAR(100),
    IN _dni VARCHAR(8),
    IN _telefono VARCHAR(15),
    IN _correo VARCHAR(150),
    IN _fecha_ingreso DATE
)
BEGIN
    UPDATE usuario
    SET nombres = _nombres, apellidos = _apellidos, dni = _dni,
        telefono = _telefono, correo = _correo
    WHERE id_usuario = _id_usuario AND activo = 1;

    UPDATE trabajador
    SET fecha_ingreso = _fecha_ingreso
    WHERE id_usuario = _id_usuario;
END$$

CREATE PROCEDURE ELIMINAR_TRABAJADOR(
    IN _id_usuario INT
)
BEGIN
    UPDATE usuario SET activo = 0 WHERE id_usuario = _id_usuario;
END$$

CREATE PROCEDURE BUSCAR_TRABAJADOR_X_ID(
    IN _id_usuario INT
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, t.fecha_ingreso AS FECHA_INGRESO
    FROM usuario u
    INNER JOIN trabajador t ON u.id_usuario = t.id_usuario
    WHERE u.id_usuario = _id_usuario AND u.activo = 1;
END$$

CREATE PROCEDURE LISTAR_TRABAJADORES()
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, t.fecha_ingreso AS FECHA_INGRESO
    FROM usuario u
    INNER JOIN trabajador t ON u.id_usuario = t.id_usuario
    WHERE u.activo = 1;
END$$

CREATE PROCEDURE BUSCAR_TRABAJADOR_X_CORREO(
    IN _correo VARCHAR(150)
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, t.fecha_ingreso AS FECHA_INGRESO
    FROM usuario u
    INNER JOIN trabajador t ON u.id_usuario = t.id_usuario
    WHERE u.correo = _correo AND u.activo = 1;
END$$

CREATE PROCEDURE BUSCAR_TRABAJADOR_X_DNI(
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT u.id_usuario AS ID_USUARIO, u.nombres AS NOMBRES, u.apellidos AS APELLIDOS,
           u.dni AS DNI, u.telefono AS TELEFONO, u.correo AS CORREO,
           u.contrasena AS CONTRASENA, t.fecha_ingreso AS FECHA_INGRESO
    FROM usuario u
    INNER JOIN trabajador t ON u.id_usuario = t.id_usuario
    WHERE u.dni = _dni AND u.activo = 1;
END$$

-- ==========================================================
-- PROCEDIMIENTO COMPARTIDO — EXISTE USUARIO EN BD
-- Busca en la tabla padre si ya existe un usuario con ese correo o DNI
-- ==========================================================

CREATE PROCEDURE EXISTE_USUARIO_EN_BD(
    IN _correo VARCHAR(150),
    IN _dni VARCHAR(8)
)
BEGIN
    SELECT COUNT(*) FROM usuario
    WHERE (correo = _correo OR dni = _dni) AND activo = 1;
END$$

DELIMITER ;
