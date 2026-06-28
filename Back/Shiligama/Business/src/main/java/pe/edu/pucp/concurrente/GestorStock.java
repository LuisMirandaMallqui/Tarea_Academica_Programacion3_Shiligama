package pe.edu.pucp.concurrente;

import java.util.HashMap;
import java.util.Map;

/**
 * Recurso compartido entre hilos concurrentes que gestionan el stock de productos.
 *
 * EQUIVALENTE a CuentaBancaria del ejemplo HILOS:
 *   CuentaBancaria.saldo          →  stockPorProducto (mapa idProducto → unidades)
 *   CuentaBancaria.retirar()      →  reservarStock()   (wait si no hay suficiente)
 *   CuentaBancaria.depositar()    →  reponerStock()    (notifyAll tras agregar)
 *
 * PATRON SINGLETON: una sola instancia por JVM gestiona todo el stock en memoria,
 * igual que EnvioCorreo. El getInstance() es synchronized para que la creacion
 * de la instancia tambien sea mutuamente exclusiva.
 *
 * AREA CRITICA: los metodos reservarStock y reponerStock son synchronized.
 * Solo un hilo a la vez puede ejecutar cualquiera de ellos sobre este objeto
 * (el monitor/candado es la instancia misma de GestorStock).
 *
 * PROBLEMA QUE RESUELVE:
 *   Sin synchronized, dos pedidos simultaneos podrian leer el mismo stock
 *   disponible y ambos creer que pueden reservar, generando sobreventas.
 *   Con synchronized + wait/notifyAll el acceso al stock es mutuamente
 *   exclusivo y los pedidos que no tienen stock quedan en espera hasta
 *   que llegue una reposicion.
 */
public class GestorStock {

    private static GestorStock instancia;

    // Recurso compartido: stock actual por idProducto
    // Equivalente al campo "saldo" de CuentaBancaria
    private final Map<Integer, Integer> stockPorProducto = new HashMap<>();

    private GestorStock() { }

    public static synchronized GestorStock getInstance() {
        if (instancia == null) {
            instancia = new GestorStock();
        }
        return instancia;
    }

    /** Carga el stock inicial de un producto (llamar al arrancar o desde un DAO). */
    public synchronized void inicializarProducto(int idProducto, int stockInicial) {
        stockPorProducto.put(idProducto, stockInicial);
        System.out.println("[GestorStock] Producto " + idProducto
                + " inicializado con " + stockInicial + " unidades.");
    }

    /**
     * Inicializa el stock de un producto solo si aun no esta registrado.
     * Llamado desde PedidoBoImpl la primera vez que se confirma un pedido
     * con ese producto, cargando el valor real desde la BD.
     */
    public synchronized void inicializarSiNecesario(int idProducto, int stockActual) {
        if (!stockPorProducto.containsKey(idProducto)) {
            stockPorProducto.put(idProducto, stockActual);
            System.out.println("[GestorStock] Producto " + idProducto
                    + " cargado desde BD con " + stockActual + " unidades.");
        }
    }

    /** Consulta el stock actual (lectura segura). */
    public synchronized int getStock(int idProducto) {
        return stockPorProducto.getOrDefault(idProducto, 0);
    }

    /**
     * Reserva {@code cantidad} unidades del producto para un pedido.
     *
     * Si el stock es insuficiente, el hilo llamador entra en espera (wait)
     * y libera el monitor hasta que otro hilo llame a reponerStock y haga
     * notifyAll(). Cuando despierta, vuelve a evaluar la condicion (while,
     * no if, para evitar spurious wakeups).
     *
     * @param solicitante  nombre del pedido/hilo (para logging)
     * @param idProducto   producto a reservar
     * @param cantidad     unidades requeridas
     */
    public synchronized void reservarStock(String solicitante, int idProducto, int cantidad) {
        while (stockPorProducto.getOrDefault(idProducto, 0) < cantidad) {
            System.out.println("[" + solicitante + "] quiere reservar " + cantidad
                    + " und. del producto " + idProducto
                    + " pero hay solo " + stockPorProducto.getOrDefault(idProducto, 0)
                    + " disponibles. Esperando reposicion...");
            try {
                wait(); // libera el monitor; el hilo queda suspendido
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.err.println("[" + solicitante + "] fue interrumpido esperando stock.");
                return;
            }
        }

        int stockAntes = stockPorProducto.get(idProducto);
        stockPorProducto.put(idProducto, stockAntes - cantidad);
        System.out.println("[" + solicitante + "] reservo " + cantidad
                + " und. del producto " + idProducto
                + " | stock: " + stockAntes + " -> " + stockPorProducto.get(idProducto));
    }

    /**
     * Repone {@code cantidad} unidades del producto (ingreso de lote).
     *
     * Incrementa el stock y llama a notifyAll() para despertar todos los
     * hilos que estaban esperando en reservarStock(). Cada uno revaluara
     * la condicion y procedera si hay stock suficiente.
     *
     * @param responsable  nombre del trabajador/hilo que repone (para logging)
     * @param idProducto   producto a reponer
     * @param cantidad     unidades agregadas
     */
    public synchronized void reponerStock(String responsable, int idProducto, int cantidad) {
        int stockAntes = stockPorProducto.getOrDefault(idProducto, 0);
        stockPorProducto.put(idProducto, stockAntes + cantidad);
        System.out.println("[" + responsable + "] repuso " + cantidad
                + " und. del producto " + idProducto
                + " | stock: " + stockAntes + " -> " + stockPorProducto.get(idProducto));
        notifyAll(); // despierta hilos en espera para que reevaluen la condicion
    }
}
