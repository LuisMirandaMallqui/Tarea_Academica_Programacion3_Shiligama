package pe.edu.pucp.concurrente;

/**
 * Tarea concurrente que repone stock de un producto
 * (equivalente a registrar un lote nuevo en almacen).
 *
 * EQUIVALENTE a Deposito.java del ejemplo HILOS:
 *   Deposito    opera sobre CuentaBancaria  →  TareaReposicionStock opera sobre GestorStock
 *   depositar() hace notifyAll()            →  reponerStock()       hace notifyAll()
 *
 * Al llamar reponerStock(), el GestorStock hace notifyAll() y despierta
 * todos los hilos de TareaReservaStock que estaban en wait() esperando
 * que hubiera suficiente stock disponible.
 */
public class TareaReposicionStock implements Runnable {

    private final GestorStock gestorStock;
    private final int idProducto;
    private final int cantidad;

    public TareaReposicionStock(GestorStock gestorStock, int idProducto, int cantidad) {
        this.gestorStock = gestorStock;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    @Override
    public void run() {
        String responsable = Thread.currentThread().getName();
        gestorStock.reponerStock(responsable, idProducto, cantidad);
    }
}
