package pe.edu.pucp.model.producto;

import java.time.LocalDateTime;

public class ProductoDto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int stock;
    private int stockMinimo;
    private String unidadMedida;
    private String codigoBarras;
    private String imagenUrl;
    private boolean estado;
    private LocalDateTime fechaRegistro;
    private CategoriaDto categoria;

    public ProductoDto() {
    }

    public ProductoDto(int idProducto, String nombre, String descripcion,
                       double precioUnitario, int stock, int stockMinimo,
                       String unidadMedida, String codigoBarras, String imagenUrl,
                       boolean estado, LocalDateTime fechaRegistro, CategoriaDto categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.unidadMedida = unidadMedida;
        this.codigoBarras = codigoBarras;
        this.imagenUrl = imagenUrl;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.categoria = categoria;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public CategoriaDto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDto categoria) {
        this.categoria = categoria;
    }

    // Métodos de negocio
    public boolean tieneStockBajo(){
        return stock <= stockMinimo;
    }
}
