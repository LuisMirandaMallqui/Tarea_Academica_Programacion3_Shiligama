package pe.edu.pucp.venta.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.venta.DetallePedido;
import java.util.List;

public interface DetallePedidoBo extends BaseBo<DetallePedido> {

    /** Lista los detalles (líneas) de un pedido específico. */
    List<DetallePedido> listarPorPedido(int idPedido) throws Exception;
}
