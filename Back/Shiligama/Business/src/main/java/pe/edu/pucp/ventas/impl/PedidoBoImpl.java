package pe.edu.pucp.ventas.impl;

import java.util.List;
import pe.edu.pucp.model.venta.PedidoDto;
import pe.edu.pucp.persistance.dao.venta.Impl.PedidoDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.PedidoDao;
import pe.edu.pucp.ventas.bo.PedidoBo;

//OrdenVenta seria en nuestro caso Pedido
public class PedidoBoImpl implements PedidoBo {
    private final PedidoDao daoPedido;

    public PedidoBoImpl(){
        daoPedido = new PedidoDaoImpl();
    }

    @Override
    public int insertar(PedidoDto pedido){
        if(pedido.getCliente() == null){
            throw new RuntimeException("Error: el pedido debe tener un cliente asignado.");
        }
        return daoPedido.insertar(pedido);
    }

    @Override
    public int modificar(PedidoDto pedido){
        if(pedido.getIdPedido() <= 0){
            throw new RuntimeException("Error: el pedido no tiene un ID valido.");
        }
        return daoPedido.modificar(pedido);
    }

    @Override
    public int eliminar(int id){
        if(id <= 0){
            throw new RuntimeException("Error: el ID del pedido no es valido.");
        }
        return daoPedido.eliminar(id);
    }

    @Override
    public PedidoDto buscarPorID(int id){
        if(id <= 0){
            throw new RuntimeException("Error: el ID del pedido no es valido.");
        }
        return daoPedido.buscarPorID(id);
    }

    @Override
    public List<PedidoDto> listarTodos(){
        return daoPedido.listarTodos();
    }
}

