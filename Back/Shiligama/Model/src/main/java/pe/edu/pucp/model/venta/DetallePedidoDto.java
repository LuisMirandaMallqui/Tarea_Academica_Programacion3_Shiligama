package pe.edu.pucp.model.venta;

import pe.edu.pucp.model.producto.ProductoDto;

public class DetallePedidoDto {
    private int idDetallePedido;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private int idPadrePedido;
    private ProductoDto producto;

    public DetallePedidoDto() {
    }

    public DetallePedidoDto(int idDetallePedido, int cantidad, double precioUnitario,
                            double subtotal, int idPadrePedido,
                            ProductoDto producto) {
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

    public void setIdPadrePedido(PedidoDto pedido) {
        this.idPadrePedido = idPadrePedido;
    }

    public ProductoDto getProducto() {
        return producto;
    }

    public void setProducto(ProductoDto producto) {
        this.producto = producto;
    }

    // Métodos de negocio
    public double calcularSubtotal() { return cantidad * precioUnitario; }
}
