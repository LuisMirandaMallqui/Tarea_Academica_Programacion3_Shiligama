package pe.edu.pucp.model.producto;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

@XmlType(name = "Categoria")
@XmlAccessorType(XmlAccessType.FIELD)
public class Categoria {
    @XmlElement(name = "idCategoria")
    @JsonbProperty("idCategoria")
    private int idCategoria;
    @XmlElement(name = "nombre")
    @JsonbProperty("nombre")
    private String nombre;
    @XmlElement(name = "descripcion")
    @JsonbProperty("descripcion")
    private String descripcion;
    @XmlElement(name = "categoriaPadre")
    @JsonbProperty("categoriaPadre")
    private Categoria categoriaPadre;
    @XmlElement(name = "icono")
    @JsonbProperty("icono")
    private String icono;
    @XmlElement(name = "estado")
    @JsonbProperty("estado")
    private boolean estado;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre, String descripcion, Categoria categoriaPadre, String icono, boolean estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaPadre = categoriaPadre;
        this.icono = icono;
        this.estado = estado;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
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

    public Categoria getCategoriaPadre() {
        return categoriaPadre;
    }

    public void setCategoriaPadre(Categoria categoriaPadre) {
        this.categoriaPadre = categoriaPadre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
