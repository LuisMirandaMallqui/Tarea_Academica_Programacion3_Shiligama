package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.producto.Producto;

public class DetalleVenta {
    private int idDetalleVenta;
    private int idPadreVenta; //En lugar de guardar el objeto completo guardamos el ID
    private Producto producto;
    private String descripcion;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleVenta() {
    }

    public DetalleVenta(int idDetalleVenta, int cantidad, double precioUnitario,
                        double subtotal, int idPadreVenta, Producto producto, String descripcion) {
        this.idDetalleVenta = idDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.descripcion = descripcion;
        this.idPadreVenta = idPadreVenta;
        this.producto = producto;
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    // EN ESTOS DE ACA FALTARIA ENCAPSULAR PARA QUE NO SE COPIEN REFERENCIAS A CLASES CREO
    // Revisando parece que en esta capa no habria problema, en la capa de dominio si
    public int getIdPadreVenta() {
        return idPadreVenta;
    }

    public void setIdPadreVenta(int idPadreVenta) {
        this.idPadreVenta = idPadreVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    //

    // Métodos de negocio
    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }
}
