using System;

namespace shilligama_blazor.Models;

// DTO que mapea Notificacion del backend Java.
// Usado por NotificacionService y la página Admin/Notificaciones.razor.
public class NotificacionItem
{
    public int     IdNotificacion  { get; set; }
    public string  Titulo         { get; set; } = string.Empty;
    public string  Mensaje        { get; set; } = string.Empty;
    public string? Tipo           { get; set; }  // STOCK_BAJO, NUEVO_PEDIDO, DEVOLUCION_PENDIENTE, etc.
    public bool    Leida          { get; set; }
    public DateTime? FechaCreacion { get; set; }
    public int?    IdDestinatario { get; set; }

    // Mapea el tipo de la BD al tipo visual usado en la UI
    public string TipoVisual => Tipo?.ToUpper() switch
    {
        "STOCK_BAJO"           => "warning",
        "DEVOLUCION_PENDIENTE" => "danger",
        "NUEVO_PEDIDO"         => "info",
        "PEDIDO_LISTO"         => "info",
        "VENTA_REGISTRADA"     => "info",
        "SISTEMA"              => "warning",
        _                      => "info"
    };
}
