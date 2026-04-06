package pe.edu.pucp.model.proveedor;

import pe.edu.pucp.model.producto.ProductoDto;

public class DetalleOrdenReabastecimientoDto {
    private int idDetalleOrden;
    private int cantidadSolicitada;
    private int cantidadRecibida;
    private double precioCompra;
    private OrdenReabastecimientoDto orden;
    private ProductoDto producto;

    // Constructor vacío
    public DetalleOrdenReabastecimientoDto() {
    }

    // Constructor completo
    public DetalleOrdenReabastecimientoDto(int idDetalleOrden,
                                           int cantidadSolicitada,
                                           int cantidadRecibida,
                                           double precioCompra,
                                           OrdenReabastecimientoDto orden,
                                           ProductoDto producto) {
        this.idDetalleOrden = idDetalleOrden;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadRecibida = cantidadRecibida;
        this.precioCompra = precioCompra;
        this.orden = orden;
        this.producto = producto;
    }

    // Getters y Setters
    public int getIdDetalleOrden() { return idDetalleOrden; }
    public void setIdDetalleOrden(int idDetalleOrden) { this.idDetalleOrden = idDetalleOrden; }

    public int getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(int cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }

    public int getCantidadRecibida() { return cantidadRecibida; }
    public void setCantidadRecibida(int cantidadRecibida) { this.cantidadRecibida = cantidadRecibida; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public OrdenReabastecimientoDto getOrden() { return orden; }
    public void setOrden(OrdenReabastecimientoDto orden) { this.orden = orden; }

    public ProductoDto getProducto() { return producto; }
    public void setProducto(ProductoDto producto) { this.producto = producto; }
}