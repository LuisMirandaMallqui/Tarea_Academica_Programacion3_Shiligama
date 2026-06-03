package pe.edu.pucp.model.enums;

/**
 * Estado de una transacción de pago gestionada por la pasarela.
 * Mapea 1:1 con el ENUM de la columna ESTADO de la tabla `pago`.
 */
public enum EstadoPago {
    PENDIENTE,
    AUTORIZADO,
    RECHAZADO,
    CANCELADO
}
