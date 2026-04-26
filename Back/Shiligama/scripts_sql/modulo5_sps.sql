-- PROCEDIMIENTOS ALMACENADOS FALTANTES - MÓDULO 5 (Operaciones y Fidelización)

-- ==========================================================
-- PROMOCION
-- ==========================================================
DELIMITER $$

CREATE PROCEDURE MODIFICAR_PROMOCION(
    IN p_id_promocion INT,
    IN p_nombre VARCHAR(100),
    IN p_descripcion VARCHAR(255),
    IN p_tipo_descuento VARCHAR(50),
    IN p_valor_descuento DECIMAL(10,2),
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE,
    IN p_condiciones VARCHAR(500)
)
BEGIN
    UPDATE promocionDto
    SET nombre = p_nombre,
        descripcion = p_descripcion,
        tipo_descuento = p_tipo_descuento,
        valor_descuento = p_valor_descuento,
        fecha_inicio = p_fecha_inicio,
        fecha_fin = p_fecha_fin,
        condiciones = p_condiciones,
        fecha_modificacion = NOW()
    WHERE id_promocion = p_id_promocion AND activo = 1;
END$$

CREATE PROCEDURE ELIMINAR_PROMOCION(
    IN p_id_promocion INT
)
BEGIN
    UPDATE promocionDto
    SET activo = 0,
        fecha_modificacion = NOW()
    WHERE id_promocion = p_id_promocion;
END$$

CREATE PROCEDURE LISTAR_PROMOCIONES_TODAS()
BEGIN
    SELECT *
    FROM promocionDto
    WHERE activo = 1;
END$$

CREATE PROCEDURE BUSCAR_PROMOCION_POR_ID(
    IN p_id_promocion INT
)
BEGIN
    SELECT *
    FROM promocionDto
    WHERE id_promocion = p_id_promocion AND activo = 1;
END$$

-- ==========================================================
-- DEVOLUCION
-- ==========================================================

CREATE PROCEDURE MODIFICAR_DEVOLUCION(
    IN p_id_devolucion INT,
    IN p_id_venta INT,
    IN p_motivo VARCHAR(255),
    IN p_fecha_solicitud TIMESTAMP,
    IN p_estado VARCHAR(50)
)
BEGIN
    UPDATE devolucion
    SET id_venta = p_id_venta,
        motivo = p_motivo,
        fecha_solicitud = p_fecha_solicitud,
        estado = p_estado,
        fecha_modificacion = NOW()
    WHERE id_devolucion = p_id_devolucion AND activo = 1;
END$$

CREATE PROCEDURE ELIMINAR_DEVOLUCION(
    IN p_id_devolucion INT
)
BEGIN
    UPDATE devolucion
    SET activo = 0,
        fecha_modificacion = NOW()
    WHERE id_devolucion = p_id_devolucion;
END$$

CREATE PROCEDURE LISTAR_DEVOLUCIONES_POR_FECHAS(
    IN p_fecha_inicio TIMESTAMP,
    IN p_fecha_fin TIMESTAMP
)
BEGIN
    SELECT *
    FROM devolucion
    WHERE activo = 1 
      AND fecha_solicitud BETWEEN p_fecha_inicio AND p_fecha_fin;
END$$

CREATE PROCEDURE LISTAR_DEVOLUCIONES_TODAS()
BEGIN
    SELECT *
    FROM devolucion
    WHERE activo = 1;
END$$

CREATE PROCEDURE BUSCAR_DEVOLUCION_POR_ID(
    IN p_id_devolucion INT
)
BEGIN
    SELECT *
    FROM devolucion
    WHERE id_devolucion = p_id_devolucion AND activo = 1;
END$$

-- ==========================================================
-- MOVIMIENTO INVENTARIO
-- ==========================================================

-- NO SE PERMITE MODIFICAR NI ELIMINAR UN LOG INMUTABLE

CREATE PROCEDURE LISTAR_MOVIMIENTOS_TODOS()
BEGIN
    SELECT *
    FROM movimiento_inventario;
END$$

CREATE PROCEDURE BUSCAR_MOVIMIENTO_POR_ID(
    IN p_id_movimiento INT
)
BEGIN
    SELECT *
    FROM movimiento_inventario
    WHERE id_movimiento = p_id_movimiento;
END$$

CREATE PROCEDURE LISTAR_MOVIMIENTOS_POR_FECHAS(
    IN p_fecha_inicio TIMESTAMP,
    IN p_fecha_fin TIMESTAMP
)
BEGIN
    SELECT *
    FROM movimiento_inventario
    WHERE fecha_hora BETWEEN p_fecha_inicio AND p_fecha_fin;
END$$

DELIMITER ;
