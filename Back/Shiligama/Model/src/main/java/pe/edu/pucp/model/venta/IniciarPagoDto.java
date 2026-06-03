package pe.edu.pucp.model.venta;

/**
 * Cuerpo de la petición para iniciar un pago con la pasarela.
 * El monto y la moneda son opcionales: si no llegan, el backend los
 * deduce del propio pedido (más seguro que confiar en el cliente).
 */
public class IniciarPagoDto {
    private int idPedido;
    private String email;     // correo del comprador (Izipay lo requiere)
    private double monto;     // opcional; 0 => se toma del pedido
    private String moneda;    // opcional; null => "PEN"

    public IniciarPagoDto() {
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
}
