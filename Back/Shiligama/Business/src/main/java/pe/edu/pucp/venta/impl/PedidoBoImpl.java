package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.concurrente.GestorPostConfirmacion;
import pe.edu.pucp.concurrente.GestorStock;
import pe.edu.pucp.concurrente.TareaNotificacionCambioEstadoPedido;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ReferenciaNotificacion;
import pe.edu.pucp.model.enums.TipoNotificacion;
import pe.edu.pucp.model.operacion.Lote;
import pe.edu.pucp.model.venta.DetallePedido;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.notificacion.impl.NotificacionHelper;
import pe.edu.pucp.operacion.impl.LoteBoImpl;
import pe.edu.pucp.persistance.dao.venta.Impl.PedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;
import pe.edu.pucp.venta.bo.PedidoBo;

public class PedidoBoImpl implements PedidoBo {
    private final PedidoDao daoPedido;

    public PedidoBoImpl() {
        daoPedido = new PedidoDaoImpl();
    }

    @Override
    public int insertar(Pedido pedido) throws Exception {
        validar(pedido, false);
        int idPedido = daoPedido.insertar(pedido);

        // Notificacion broadcast a trabajadores: nuevo pedido recibido.
        // No bloquea la respuesta del checkout al cliente.
        NotificacionHelper.notificarBroadcast(
                "Nuevo pedido #" + idPedido,
                "Se recibió un nuevo pedido por S/ " + String.format("%.2f", pedido.getMontoTotal()) + ".",
                TipoNotificacion.NUEVO_PEDIDO,
                ReferenciaNotificacion.PEDIDO, idPedido);

        return idPedido;
    }

    @Override
    public int modificar(Pedido pedido) throws Exception {
        validar(pedido, true);



        // Permite reabrir/corregir pedidos en estado terminal (ATENDIDO, RECHAZADO,
        // CANCELADO) hacia cualquier otro estado (excepto ATENDIDO, bloqueado arriba),
        // para que el panel admin pueda corregir un cambio de estado erróneo.
        int filas = daoPedido.modificar(pedido);

        // Notificar al cliente el cambio de estado (EN_PROCESO, RECHAZADO, CANCELADO).
        // Se busca el pedido completo porque el objeto recibido del front solo trae
        // idPedido/estadoPedido/observaciones (ver PedidoDaoImpl.modificar).
        try {
            Pedido completo = daoPedido.buscarPorId(pedido.getIdPedido());
            if (completo != null && completo.getCliente() != null) {
                Runnable tarea = new TareaNotificacionCambioEstadoPedido(
                        completo.getCliente().getIdUsuario(),
                        completo.getIdPedido(),
                        pedido.getEstadoPedido(),
                        completo.getModalidadVenta());
                new Thread(tarea, "Hilo-Notificacion-CambioEstado-Pedido" + pedido.getIdPedido()).start();
            }
        } catch (Exception ex) {
            System.err.println("[PedidoBoImpl] Error al notificar cambio de estado: " + ex.getMessage());
        }

        return filas;
    }

    @Override
    public int confirmarPedido(int idPedido, int idTrabajador, int idMetodoPago) throws Exception {
        if (idPedido <= 0) throw new Exception("El ID del pedido debe ser mayor que cero.");

        // Verificar estado actual antes de delegar al SP (error amigable en caso terminal)
        Pedido actual = daoPedido.buscarPorId(idPedido);
        if (actual == null) throw new Exception("Pedido con ID " + idPedido + " no encontrado.");

        EstadoPedido estado = actual.getEstadoPedido();
        if (estado == EstadoPedido.LISTO ||
                estado == EstadoPedido.EN_CAMINO ||
                estado == EstadoPedido.ENTREGADO ||
                estado == EstadoPedido.RECOGIDO ||
                estado == EstadoPedido.RECHAZADO ||
                estado == EstadoPedido.CANCELADO) {

            throw new Exception(
                    "El pedido ya está en estado " + estado + " y no puede confirmarse nuevamente.");
        }

        // Gestion concurrente de stock: reservar antes de ejecutar el SP.
        // Cada request HTTP de confirmacion es un hilo distinto (Tomcat).
        // GestorStock.reservarStock() es synchronized -> area critica:
        // solo un hilo a la vez puede decrementar; los demas esperan (wait)
        // si no hay stock, hasta que llegue una reposicion (notifyAll).


        // El SP CONFIRMAR_PEDIDO_A_VENTA se encarga de: crear venta, copiar detalles,
        // decrementar stock y marcar el pedido como ATENDIDO de forma atómica.
        int idVenta = daoPedido.confirmarPedidoAVenta(idPedido, idTrabajador, idMetodoPago);
        if (idVenta <= 0) throw new Exception("Error al crear la venta para el pedido " + idPedido + ".");

        // Lanzar tareas post-confirmacion de forma concurrente (no bloquean la respuesta al cliente).
        // Hilo-Notificacion: registra notificacion VENTA_REGISTRADA en BD.
        // Hilo-Correo: envia email de confirmacion al cliente.
        GestorPostConfirmacion.lanzarTareasPostConfirmacion(actual, idVenta);

        // Notificacion adicional de PEDIDO_ATENDIDO (distinta de VENTA_REGISTRADA):
        // confirma al cliente que su pedido especifico fue atendido, con referencia
        // directa al pedido para que "ver" navegue al item exacto en Mis Pedidos.

        // El SP CONFIRMAR_PEDIDO_A_VENTA se encarga de crear venta, copiar detalles,
        // decrementar stock y marcar el pedido como LISTO de forma atómica.
        Runnable tareaEstado = new TareaNotificacionCambioEstadoPedido(
                actual.getCliente().getIdUsuario(), idPedido,
                EstadoPedido.LISTO, actual.getModalidadVenta());
        new Thread(tareaEstado, "Hilo-Notificacion-Listo-Pedido" + idPedido).start();

        return idVenta;
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoPedido.eliminar(id);
    }

    @Override
    public Pedido buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return daoPedido.buscarPorId(id);
    }

    @Override
    public List<Pedido> listarTodos() throws Exception {
        return daoPedido.listarTodos();
    }

    @Override
    public List<Pedido> listarPorCliente(int idCliente) throws Exception {
        if (idCliente <= 0) {
            throw new Exception("El ID del cliente debe ser mayor que cero.");
        }
        return daoPedido.listarPorCliente(idCliente);
    }

    @Override
    public List<Pedido> listarPorEstado(EstadoPedido estado) throws Exception {
        if (estado == null) {
            throw new Exception("El estado del pedido es obligatorio.");
        }
        return daoPedido.listarPorEstado(estado);
    }

    private void validar(Pedido pedido, boolean esModificacion) throws Exception {
        if (pedido == null) {
            throw new Exception("El pedido no puede ser nulo.");
        }
        if (esModificacion && pedido.getIdPedido() <= 0) {
            throw new Exception("El ID del pedido es obligatorio para la modificacion.");
        }
        if (!esModificacion && pedido.getCliente() == null) {
            throw new Exception("El pedido debe tener un cliente asignado.");
        }
        if (!esModificacion && pedido.getMontoTotal() <= 0) {
            throw new Exception("El monto total del pedido debe ser mayor que cero.");
        }
    }

    @Override
    public String reservarStockParaPago(int idPedido) throws Exception {
        if (idPedido <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }

        return daoPedido.reservarStockParaPago(idPedido);
    }

    @Override
    public int restaurarStockReservado(int idPedido) throws Exception {
        if (idPedido <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }

        return daoPedido.restaurarStockReservado(idPedido);
    }
}
