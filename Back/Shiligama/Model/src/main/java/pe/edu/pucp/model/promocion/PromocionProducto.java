package pe.edu.pucp.model.promocion;

import pe.edu.pucp.model.producto.Producto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "PromocionProducto")
@XmlAccessorType(XmlAccessType.FIELD)
public class PromocionProducto {
    @XmlElement(name = "promocion")
    @JsonbProperty("promocion")
    private Promocion promocion;
    @XmlElement(name = "producto")
    @JsonbProperty("producto")
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