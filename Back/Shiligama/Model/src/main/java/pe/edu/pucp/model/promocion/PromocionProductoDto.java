package pe.edu.pucp.model.promocion;

import pe.edu.pucp.model.producto.ProductoDto;

public class PromocionProductoDto {
    private PromocionDto promocionDto;
    private ProductoDto producto;
    private double precioDescuento;

    // Constructor vacío
    public PromocionProductoDto() {
    }

    // Constructor completo
    public PromocionProductoDto(PromocionDto promocionDto, ProductoDto producto, double precioDescuento) {
        this.promocionDto = promocionDto;
        this.producto = producto;
        this.precioDescuento = precioDescuento;
    }

    // Getters y Setters
    public PromocionDto getPromocion() { return promocionDto; }
    public void setPromocion(PromocionDto promocionDto) { this.promocionDto = promocionDto; }

    public ProductoDto getProducto() { return producto; }
    public void setProducto(ProductoDto producto) { this.producto = producto; }

    public double getPrecioDescuento() { return precioDescuento; }
    public void setPrecioDescuento(double precioDescuento) { this.precioDescuento = precioDescuento; }
}