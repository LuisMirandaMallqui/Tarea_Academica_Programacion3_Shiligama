package pe.edu.pucp.persistance.dao.venta.dao;

import pe.edu.pucp.model.venta.DetallePedido;
import pe.edu.pucp.persistance.dao.IDAO;
import java.util.List;

public interface DetallePedidoDao extends IDAO<DetallePedido> {

    /** Lista los detalles de un pedido usando SP LISTAR_DETALLES_POR_PEDIDO. */
    List<DetallePedido> listarPorPedido(int idPedido);
}
