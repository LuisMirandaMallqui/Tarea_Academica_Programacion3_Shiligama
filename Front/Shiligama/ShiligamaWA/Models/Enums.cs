namespace ShiligamaWA.Models;

public enum RolUsuario
{
    Cliente,
    Trabajador,
    Administrador
}

public enum EstadoPedido
{
    Pendiente,
    Confirmado,
    EnPreparacion,
    EnRuta,
    Entregado,
    Cancelado
}

public enum EstadoVenta
{
    Borrador,
    Confirmada,
    Anulada
}

public enum EstadoDevolucion
{
    Solicitada,
    EnRevision,
    Aprobada,
    Rechazada,
    Completada
}

public enum MetodoPago
{
    Efectivo,
    Yape,
    Plin,
    Tarjeta,
    Transferencia
}

public enum TipoMovimientoInventario
{
    Ingreso,
    Salida,
    Ajuste,
    Devolucion
}

public enum TipoNotificacion
{
    Pedido,
    StockBajo,
    Devolucion,
    Sistema,
    Promocion
}

public enum CanalVenta
{
    Online,
    Tienda
}
