package pe.edu.pucp.model.promocion;

import pe.edu.pucp.model.producto.Producto;

public class PromocionProducto {
    private Promocion promocion;
    private Producto producto;
    //private double precioDescuento;

    // Constructor vacío
    public PromocionProducto() {
    }

    // Constructor completo
  //  public PromocionProductoDto(PromocionDto promocionDto, ProductoDto producto, double precioDescuento) {
    public PromocionProducto(Promocion promocion, Producto producto) {
        this.promocion = promocion;
        this.producto = producto;
        //this.precioDescuento = precioDescuento;
    }

    // Getters y Setters
    public Promocion getPromocion() { return promocion; }
    public void setPromocion(Promocion promocion) { this.promocion = promocion; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    //public double getPrecioDescuento() { return precioDescuento; }
    //public void setPrecioDescuento(double precioDescuento) { this.precioDescuento = precioDescuento; }
    @Override
    public String toString() {
        return "PromocionProducto{" +
                "idPromocion=" + promocion.getIdPromocion() +
                ", idProducto=" + producto.getIdProducto() +
                '}';
    }
}