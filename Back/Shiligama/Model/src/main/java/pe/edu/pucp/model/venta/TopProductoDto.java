package pe.edu.pucp.model.venta;

public class TopProductoDto {
    private int    idProducto;
    private String nombre;
    private String imagenUrl;
    private int    totalUnidades;
    private double totalIngresos;

    public TopProductoDto() {}

    public int    getIdProducto()    { return idProducto; }
    public void   setIdProducto(int v)    { this.idProducto = v; }

    public String getNombre()        { return nombre; }
    public void   setNombre(String v)     { this.nombre = v; }

    public String getImagenUrl()     { return imagenUrl; }
    public void   setImagenUrl(String v)  { this.imagenUrl = v; }

    public int    getTotalUnidades() { return totalUnidades; }
    public void   setTotalUnidades(int v) { this.totalUnidades = v; }

    public double getTotalIngresos() { return totalIngresos; }
    public void   setTotalIngresos(double v) { this.totalIngresos = v; }
}
