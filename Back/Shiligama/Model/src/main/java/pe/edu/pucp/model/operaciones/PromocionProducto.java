package pe.edu.pucp.model.operaciones;

/**
 * Clase que representa la vinculación entre una Promoción y un Producto.
 * Tabla: promociones_productos — solo contiene PROMOCION_ID y PRODUCTO_ID.
 */
public class PromocionProducto {
    // ================= ATRIBUTOS =================
    private int idPromocion;
    private int idProducto;

    // ================= CONSTRUCTORES =================
    public PromocionProducto() {}

    public PromocionProducto(int idPromocion, int idProducto) {
        this.idPromocion = idPromocion;
        this.idProducto  = idProducto;
    }

    // ================= GETTERS Y SETTERS =================
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    @Override
    public String toString() {
        return "PromocionProducto{" +
                "idPromocion=" + idPromocion +
                ", idProducto=" + idProducto +
                '}';
    }
}
