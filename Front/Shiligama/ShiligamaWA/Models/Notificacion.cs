namespace ShiligamaWA.Models;

public class Notificacion
{
    public int Id { get; set; }
    public TipoNotificacion Tipo { get; set; }
    public string Titulo { get; set; } = string.Empty;
    public string Mensaje { get; set; } = string.Empty;
    public DateTime Fecha { get; set; } = DateTime.Now;
    public bool Leida { get; set; }
    public string? EnlaceAccion { get; set; }
    public string Icono => Tipo switch
    {
        TipoNotificacion.Pedido => "fa-solid fa-clipboard-list",
        TipoNotificacion.StockBajo => "fa-solid fa-triangle-exclamation",
        TipoNotificacion.Devolucion => "fa-solid fa-rotate-left",
        TipoNotificacion.Promocion => "fa-solid fa-tag",
        _ => "fa-solid fa-bell"
    };
    public string ColorBadge => Tipo switch
    {
        TipoNotificacion.StockBajo => "warning",
        TipoNotificacion.Devolucion => "info",
        TipoNotificacion.Pedido => "primary",
        TipoNotificacion.Promocion => "success",
        _ => "secondary"
    };
}
