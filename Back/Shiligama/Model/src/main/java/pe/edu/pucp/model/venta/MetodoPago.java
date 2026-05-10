package pe.edu.pucp.model.venta;

public class MetodoPago {
    private int idMetodoPago;
    private String nombre;
    private boolean estado;

    public MetodoPago() {
    }

    public MetodoPago(int idMetodoPago, String nombre, boolean estado) {
        this.idMetodoPago = idMetodoPago;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


}
