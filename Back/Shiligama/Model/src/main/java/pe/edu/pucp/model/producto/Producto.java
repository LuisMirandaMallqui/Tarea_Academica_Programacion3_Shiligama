package pe.edu.pucp.model.producto;

import java.time.LocalDateTime;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Producto")
@XmlAccessorType(XmlAccessType.FIELD)
public class Producto {
    @XmlElement(name = "idProducto")
    @JsonbProperty("idProducto")
    private int idProducto;
    @XmlElement(name = "nombre")
    @JsonbProperty("nombre")
    private String nombre;
    @XmlElement(name = "descripcion")
    @JsonbProperty("descripcion")
    private String descripcion;
    @XmlElement(name = "precioUnitario")
    @JsonbProperty("precioUnitario")
    private double precioUnitario;
    @XmlElement(name = "stock")
    @JsonbProperty("stock")
    private int stock;
    @XmlElement(name = "stockMinimo")
    @JsonbProperty("stockMinimo")
    private int stockMinimo;
    @XmlElement(name = "unidadMedida")
    @JsonbProperty("unidadMedida")
    private String unidadMedida;
    @XmlElement(name = "codigoBarras")
    @JsonbProperty("codigoBarras")
    private String codigoBarras;
    @XmlElement(name = "imagenUrl")
    @JsonbProperty("imagenUrl")
    private String imagenUrl;
    @XmlElement(name = "estado")
    @JsonbProperty("estado")
    private boolean estado;
    @XmlElement(name = "fechaRegistro")
    @JsonbDateFormat(value = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonbProperty("fechaRegistro")
    private LocalDateTime fechaRegistro;
    @XmlElement(name = "categoria")
    @JsonbProperty("categoria")
    private Categoria categoria;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, String descripcion,
                    double precioUnitario, int stock, int stockMinimo,
                    String unidadMedida, String codigoBarras, String imagenUrl,
                    boolean estado, LocalDateTime fechaRegistro, Categoria categoria) {
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Métodos de negocio
    public boolean tieneStockBajo(){
        return stock <= stockMinimo;
    }
}
