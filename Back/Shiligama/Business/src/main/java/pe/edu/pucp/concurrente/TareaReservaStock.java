package pe.edu.pucp.concurrente;

/**
 * Tarea concurrente que intenta reservar stock de un producto
 * para un pedido en linea.
 *
 * EQUIVALENTE a Retiro.java del ejemplo HILOS:
 *   Retiro     opera sobre CuentaBancaria  →  TareaReservaStock opera sobre GestorStock
 *   retirar()  puede hacer wait()          →  reservarStock()   puede hacer wait()
 *
 * El nombre del hilo (Thread.currentThread().getName()) identifica al pedido
 * que solicita el stock, igual que en el ejemplo donde cada hilo tenia
 * el nombre del cliente o empleado.
 */
public class TareaReservaStock implements Runnable {

    private final GestorStock gestorStock;
    private final int idProducto;
    private final int cantidad;

    public TareaReservaStock(GestorStock gestorStock, int idProducto, int cantidad) {
        this.gestorStock = gestorStock;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    @Override
    public void run() {
        String solicitante = Thread.currentThread().getName();
        gestorStock.reservarStock(solicitante, idProducto, cantidad);
    }
}
