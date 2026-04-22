package pe.edu.pucp.ventas.impl;

import pe.edu.pucp.model.venta.PedidoDto;
import pe.edu.pucp.persistance.dao.venta.Impl.PedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;

//OrdenVenta seria en nuestro caso Pedido
public class PedidoBoImpl {
    private final PedidoDao daoPedido;

    public PedidoBoImpl(){
        daoPedido = new PedidoDaoImpl();
    }

    @Override
    public int insertar(PedidoDto pedido){
        // pasa por esta capa de validación, y luego recien inserta la orden de venta
        if(pedido.getCliente()==null){
            throw new RuntimeException("Error: no se ha asignado ...");
        }
        return daoPedido.insertar(pedido);
    }

}

