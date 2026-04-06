package pe.edu.pucp.model.proveedor;
import pe.edu.pucp.model.producto.ProductoDto;

public class ProductoProveedorDto {
    private ProductoDto producto;
    private ProveedorDto proveedorDto;
    private double precioCompra;
    private int tiempoEntrega;

    // Constructor vacío
    public ProductoProveedorDto() {
    }

    // Constructor completo
    public ProductoProveedorDto(ProductoDto producto, ProveedorDto proveedorDto,
                                double precioCompra, int tiempoEntrega) {
        this.producto = producto;
        this.proveedorDto = proveedorDto;
        this.precioCompra = precioCompra;
        this.tiempoEntrega = tiempoEntrega;
    }

    // Getters y Setters
    public ProductoDto getProducto() { return producto; }
    public void setProducto(ProductoDto producto) { this.producto = producto; }

    public ProveedorDto getProveedor() { return proveedorDto; }
    public void setProveedor(ProveedorDto proveedorDto) { this.proveedorDto = proveedorDto; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public int getTiempoEntrega() { return tiempoEntrega; }
    public void setTiempoEntrega(int tiempoEntrega) { this.tiempoEntrega = tiempoEntrega; }
}
