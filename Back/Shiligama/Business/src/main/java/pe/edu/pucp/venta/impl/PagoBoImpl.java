package pe.edu.pucp.venta.impl;

import java.util.List;
import pe.edu.pucp.model.enums.EstadoPago;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.venta.Pago;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.persistance.dao.venta.Impl.PagoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.Impl.PedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.PagoDao;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;
import pe.edu.pucp.venta.bo.PagoBo;

public class PagoBoImpl implements PagoBo {

    private final PagoDao pagoDao;
    private final PedidoDao pedidoDao;

    public PagoBoImpl() {
        this.pagoDao = new PagoDaoImpl();
        this.pedidoDao = new PedidoDaoImpl();
    }

    @Override
    public int insertar(Pago pago) throws Exception {
        validar(pago, false);
        return pagoDao.insertar(pago);
    }

    @Override
    public int modificar(Pago pago) throws Exception {
        validar(pago, true);
        return pagoDao.modificar(pago);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pago debe ser mayor que cero.");
        }
        return pagoDao.eliminar(id);
    }

    @Override
    public Pago buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del pago debe ser mayor que cero.");
        }
        return pagoDao.buscarPorId(id);
    }

    @Override
    public List<Pago> listarTodos() throws Exception {
        return pagoDao.listarTodos();
    }

    @Override
    public Pago buscarPorPedido(int idPedido) throws Exception {
        if (idPedido <= 0) {
            throw new Exception("El ID del pedido debe ser mayor que cero.");
        }
        return pagoDao.buscarPorPedido(idPedido);
    }

    @Override
    public Pago buscarPorOrder(String orderId) throws Exception {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new Exception("El order id es obligatorio.");
        }
        return pagoDao.buscarPorOrder(orderId.trim());
    }

    @Override
    public Pago registrarPagoPendiente(int idPedido, int idMetodoPago, double monto,
                                       String moneda, String orderId) throws Exception {
        if (idPedido <= 0) {
            throw new Exception("El ID del pedido es obligatorio.");
        }
        if (idMetodoPago <= 0) {
            throw new Exception("El método de pago es obligatorio.");
        }
        if (monto <= 0) {
            throw new Exception("El monto del pago debe ser mayor que cero.");
        }
        Pago pago = new Pago();
        pago.setIdPedido(idPedido);
        pago.setIdMetodoPago(idMetodoPago);
        pago.setMonto(monto);
        pago.setMoneda(moneda != null && !moneda.isBlank() ? moneda : "PEN");
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setOrderId(orderId);
        pagoDao.insertar(pago);
        return pago;
    }

    @Override
    public void procesarResultadoPasarela(String orderId, boolean autorizado,
                                          String referencia) throws Exception {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new Exception("El order id es obligatorio.");
        }
        Pago pago = pagoDao.buscarPorOrder(orderId.trim());
        if (pago == null) {
            throw new Exception("No existe un pago para el order id: " + orderId);
        }

        EstadoPago nuevoEstado = autorizado ? EstadoPago.AUTORIZADO : EstadoPago.RECHAZADO;
        pagoDao.modificarPorOrder(orderId.trim(), nuevoEstado, referencia);

        if (autorizado) {
            // Pago confirmado: el pedido pasa a ser procesado por la tienda.
            // (El enum EstadoPedido no tiene "PAGADO"; EN_PROCESO representa
            //  un pedido pagado y listo para atención.)
            Pedido pedido = new Pedido();
            pedido.setIdPedido(pago.getIdPedido());
            pedido.setEstadoPedido(EstadoPedido.EN_PROCESO);
            pedidoDao.modificar(pedido);
        }
    }

    private void validar(Pago pago, boolean esModificacion) throws Exception {
        if (pago == null) {
            throw new Exception("El pago no puede ser nulo.");
        }
        if (esModificacion && pago.getIdPago() <= 0) {
            throw new Exception("El ID del pago es obligatorio para la modificación.");
        }
        if (!esModificacion) {
            if (pago.getIdPedido() <= 0) {
                throw new Exception("El pago debe estar asociado a un pedido.");
            }
            if (pago.getIdMetodoPago() <= 0) {
                throw new Exception("El método de pago es obligatorio.");
            }
            if (pago.getMonto() <= 0) {
                throw new Exception("El monto del pago debe ser mayor que cero.");
            }
        }
    }
}
