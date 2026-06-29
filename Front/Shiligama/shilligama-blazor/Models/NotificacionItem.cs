using System;

namespace shilligama_blazor.Models;

// DTO que mapea Notificacion del backend Java.
// Usado por NotificacionService y las páginas de Notificaciones (Admin/Trabajador/Cliente).
public class NotificacionItem
{
    public int IdNotificacion { get; set; }
    public string Titulo { get; set; } = string.Empty;
    public string Mensaje { get; set; } = string.Empty;
    public string? Tipo { get; set; }  // STOCK_BAJO, NUEVO_PEDIDO, PEDIDO_ATENDIDO, etc.
    public bool Leida { get; set; }
    public DateTime? FechaCreacion { get; set; }
    public int? IdDestinatario { get; set; }

    // Entidad a la que apunta esta notificación (para el botón "Ver").
    // REFERENCIA_TIPO: PEDIDO, PRODUCTO, DEVOLUCION, PROMOCION (null si no aplica).
    public string? ReferenciaTipo { get; set; }
    public int? ReferenciaId { get; set; }

    // Mapea el tipo de la BD al tipo visual usado en la UI
    public string TipoVisual => Tipo?.ToUpper() switch
    {
        "STOCK_BAJO" => "warning",
        "DEVOLUCION_PENDIENTE" => "danger",
        "DEVOLUCION_RESUELTA" => "info",
        "NUEVO_PEDIDO" => "info",
        "PEDIDO_LISTO" => "info",
        "PEDIDO_ATENDIDO" => "success",
        "VENTA_REGISTRADA" => "info",
        "PROMOCION_POR_VENCER" => "warning",
        "SISTEMA" => "warning",
        _ => "info"
    };

    // Icono Font Awesome según el tipo (usado por la tarjeta de notificación).
    public string Icono => Tipo?.ToUpper() switch
    {
        "STOCK_BAJO" => "fa-triangle-exclamation",
        "DEVOLUCION_PENDIENTE" => "fa-rotate-left",
        "DEVOLUCION_RESUELTA" => "fa-check-double",
        "NUEVO_PEDIDO" => "fa-cart-shopping",
        "PEDIDO_LISTO" => "fa-box-open",
        "PEDIDO_ATENDIDO" => "fa-circle-check",
        "VENTA_REGISTRADA" => "fa-receipt",
        "PROMOCION_POR_VENCER" => "fa-tag",
        _ => "fa-bell"
    };

    // Ruta de destino para el botón "Ver", segun el rol que esta viendo la
    // notificación — null si no hay referencia o si esa entidad no tiene
    // pantalla propia para ese rol (ej: trabajador no tiene /trabajador/pedidos,
    // usa /trabajador para el listado de pedidos activos; no tiene /promociones).
    //
    // rol esperado: "admin" | "trabajador" | "cliente"
    public string? RutaVerPara(string rol)
    {
        if (!ReferenciaId.HasValue || string.IsNullOrEmpty(ReferenciaTipo)) return null;

        return rol.ToLowerInvariant() switch
        {
            "admin" => ReferenciaTipo.ToUpper() switch
            {
                "PEDIDO" => $"/admin/pedidos?resaltar={ReferenciaId}",
                "PRODUCTO" => $"/admin/productos?resaltar={ReferenciaId}",
                "DEVOLUCION" => $"/admin/devoluciones?resaltar={ReferenciaId}",
                "PROMOCION" => $"/admin/promociones?resaltar={ReferenciaId}",
                _ => null
            },
            "trabajador" => ReferenciaTipo.ToUpper() switch
            {
                // No existe /trabajador/pedidos: el listado de pedidos activos
                // vive embebido en el Dashboard (/trabajador).
                "PEDIDO" => "/trabajador",
                "PRODUCTO" => $"/trabajador/productos?resaltar={ReferenciaId}",
                "DEVOLUCION" => $"/trabajador/devoluciones?resaltar={ReferenciaId}",
                // Trabajador no administra promociones, no hay pantalla a la que ir.
                "PROMOCION" => null,
                _ => null
            },
            "cliente" => ReferenciaTipo.ToUpper() switch
            {
                "PEDIDO" => $"/mis-pedidos?resaltar={ReferenciaId}",
                _ => null
            },
            _ => null
        };
    }
}
