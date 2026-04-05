package pe.edu.pucp.model.producto;

public class CategoriaDto {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private CategoriaDto categoriaPadre;
    private boolean estado;

    public CategoriaDto() {
    }

    public CategoriaDto(int idCategoria, String nombre, String descripcion, CategoriaDto categoriaPadre, boolean estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaPadre = categoriaPadre;
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

    public CategoriaDto getCategoriaPadre() {
        return categoriaPadre;
    }

    public void setCategoriaPadre(CategoriaDto categoriaPadre) {
        this.categoriaPadre = categoriaPadre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
