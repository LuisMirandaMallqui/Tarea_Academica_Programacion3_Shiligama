package pe.edu.pucp.venta.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.venta.Pedido;

import java.util.List;

public interface PedidoBo extends BaseBo<Pedido> {
    List<Pedido> listarPorCliente(int idCliente) throws Exception;
    List<Pedido> listarPorEstado(EstadoPedido estado) throws Exception;
}
