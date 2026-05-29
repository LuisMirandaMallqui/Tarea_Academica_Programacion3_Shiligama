package pe.edu.pucp.persistance.dao.venta.dao;

import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.venta.Pedido;
import pe.edu.pucp.persistance.dao.IDAO;

import java.util.List;

public interface PedidoDao extends IDAO<Pedido> {
    List<Pedido> listarPorCliente(int idCliente);
    List<Pedido> listarPorEstado(EstadoPedido estado);
}
