package pe.edu.pucp.model.enums;

/**
 * Tipo de entidad a la que apunta una Notificacion via REFERENCIA_ID.
 * Permite que el front (Cliente, Trabajador, Admin) navegue exactamente
 * al item del que habla la notificacion (ej: el pedido especifico,
 * el producto especifico, etc.) en vez de a un listado generico.
 */
public enum ReferenciaNotificacion {
    PEDIDO,
    PRODUCTO,
    DEVOLUCION,
    PROMOCION
}
