package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.producto.ProductoDto;

public class DetalleVentaDto {
    private int idDetalleVenta;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private VentaDto venta;
    private ProductoDto producto;

    public DetalleVentaDto() {
    }

    public DetalleVentaDto(int idDetalleVenta, int cantidad, double precioUnitario,
                           double subtotal, VentaDto venta, ProductoDto producto) {
        this.idDetalleVenta = idDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;

        this.venta = venta;
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

    // EN ESTOS DE ACA FALTARIA ENCAPSULAR PARA QUE NO SE COPIEN REFERENCIAS A CLASES CREO
    // Revisando parece que en esta capa no habria problema, en la capa de dominio si
    public VentaDto getVenta() {
        return venta;
    }

    public void setVenta(VentaDto venta) {
        this.venta = venta;
    }

    public ProductoDto getProducto() {
        return producto;
    }

    public void setProducto(ProductoDto producto) {
        this.producto = producto;
    }
    //

    // Métodos de negocio
    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }
}
