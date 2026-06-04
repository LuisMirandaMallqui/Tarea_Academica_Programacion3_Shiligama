using System;
using System.Collections.Generic;
using System.Linq;

namespace shilligama_blazor.Models;

/// <summary>
/// Calcula descuentos impulsados por los datos de <see cref="Promocion"/> del backend.
///
/// La lógica vive aquí — no en CartService — para:
///  - ser usada tanto en la vista del carrito como en el total del pedido
///  - escalar sin tocar el carrito: añadir un nuevo tipo sólo requiere un nuevo case aquí
///
/// Tipos soportados:
///   PORCENTAJE  – descuento porcentual sobre el subtotal de la línea
///   MONTO_FIJO  – descuento fijo en S/. por línea
///   DOS_X_UNO   – paga ceil(qty/2) unidades  (compra 2, paga 1; compra 3, paga 2; etc.)
///   NXM         – forma genérica: paga M de cada N  → codificado como "N:M" en ValorDescuento
///                 Ejemplo: 3x1 = ValorDescuento=3, campo extra QuantidadPaga=1  →  usar "3:1"
///                 Por ahora el back no expone N:M; se reserva para cuando se amplíe el modelo.
/// </summary>
public static class PromocionHelper
{
    /// <summary>
    /// Precio efectivo a pagar por <paramref name="cantidad"/> unidades de un producto,
    /// aplicando la promoción vigente. Devuelve el precio sin descuento si no hay promo.
    /// </summary>
    public static decimal PrecioEfectivo(decimal precioUnitario, int cantidad, Promocion? promo)
    {
        if (promo == null || !promo.EsVigente)
            return precioUnitario * cantidad;

        return promo.TipoDescuento.ToUpper() switch
        {
            "PORCENTAJE" =>
                precioUnitario * cantidad * (1 - (decimal)(promo.ValorDescuento / 100.0)),

            "MONTO_FIJO" =>
                Math.Max(0, precioUnitario * cantidad - (decimal)promo.ValorDescuento),

            "DOS_X_UNO" =>
                // Paga solo la mitad (redondeada arriba): qty=2→1, qty=3→2, qty=4→2 ...
                precioUnitario * (decimal)Math.Ceiling(cantidad / 2.0),

            _ when promo.TipoDescuento.Contains(':') =>
                // Formato NXM genérico, p.ej. "3:1" = compra 3, paga 1
                AplicarNxM(precioUnitario, cantidad, promo.TipoDescuento),

            _ => precioUnitario * cantidad
        };
    }

    /// <summary>
    /// Calcula el total de un carrito aplicando las promos vigentes.
    /// Cada item se compara contra todas las promos; se usa la primera que aplique.
    /// </summary>
    public static decimal TotalConPromos(
        IEnumerable<CartItem> items,
        IEnumerable<Promocion> promosVigentes)
    {
        var promoList = promosVigentes.ToList();
        decimal total = 0;

        foreach (var item in items)
        {
            var promo = promoList.FirstOrDefault(p =>
                p.ProductoIds.Contains(item.Id) && p.EsVigente);
            total += PrecioEfectivo(item.Price, item.Quantity, promo);
        }

        return total;
    }

    /// <summary>
    /// Descuento total de una línea: precio normal − precio con promo.
    /// Útil para mostrar el ahorro en la UI.
    /// </summary>
    public static decimal DescuentoLinea(decimal precioUnitario, int cantidad, Promocion? promo)
    {
        decimal normal = precioUnitario * cantidad;
        return Math.Max(0, normal - PrecioEfectivo(precioUnitario, cantidad, promo));
    }

    // ── Privado ──────────────────────────────────────────────────────────────

    /// <summary>
    /// Aplica formato N:M  (compra N, paga M).
    /// Por cada bloque de N unidades el cliente paga sólo M.
    /// Las unidades sobrantes (resto) se cobran a precio normal.
    /// </summary>
    private static decimal AplicarNxM(decimal precioUnitario, int cantidad, string tipo)
    {
        var partes = tipo.Split(':');
        if (partes.Length != 2
            || !int.TryParse(partes[0], out int n)
            || !int.TryParse(partes[1], out int m)
            || n <= 0 || m < 0 || m >= n)
            return precioUnitario * cantidad;

        int bloques  = cantidad / n;
        int sobrante = cantidad % n;
        return precioUnitario * (bloques * m + sobrante);
    }
}
