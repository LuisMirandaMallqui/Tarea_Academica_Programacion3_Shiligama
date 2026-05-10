package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.producto.Producto;

public class DetallePedido {
    private int idDetallePedido;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private int idPadrePedido;
    private Producto producto;

    public DetallePedido() {
    }

    public DetallePedido(int idDetallePedido, int cantidad, double precioUnitario,
                         double subtotal, int idPadrePedido,
                         Producto producto) {
        this.idDetallePedido = idDetallePedido;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.idPadrePedido = idPadrePedido;
        this.producto = producto;
    }

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }


    public int getIdPadrePedido() {
        return idPadrePedido;
    }

    public void setIdPadrePedido(int idPadrePedido) {
        this.idPadrePedido = idPadrePedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // Métodos de negocio
    public double calcularSubtotal() { return cantidad * precioUnitario; }
}
