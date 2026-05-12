package pe.edu.pucp.producto.impl;

import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.persistance.dao.producto.Impl.ProductoDaoImpl;
import pe.edu.pucp.persistance.dao.producto.dao.ProductoDao;
import pe.edu.pucp.producto.bo.ProductoBo;

import java.util.List;
public class ProductoBoImpl implements ProductoBo {
    private final ProductoDao daoProducto;

    public ProductoBoImpl() {
        this.daoProducto = new ProductoDaoImpl();
    }

    @Override
    public int insertar(Producto producto) throws Exception {
        validar(producto, false);
        return daoProducto.insertar(producto);
    }

    @Override
    public int modificar(Producto producto) throws Exception {
        validar(producto, true);
        return daoProducto.modificar(producto);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del producto debe ser mayor que cero.");
        }
        return daoProducto.eliminar(id);
    }

    @Override
    public Producto buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del producto debe ser mayor que cero.");
        }
        return daoProducto.buscarPorID(id);
    }

    @Override
    public List<Producto> listarTodos() throws Exception {
        return daoProducto.listarTodos();
    }

    private void validar(Producto producto, boolean esModificacion) throws Exception {
        if (producto == null) {
            throw new Exception("El producto no puede ser nulo.");
        }
        if (esModificacion && producto.getIdProducto() <= 0) {
            throw new Exception("El ID del producto es obligatorio para la modificacion.");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del producto es obligatorio.");
        }
        if (producto.getPrecioUnitario() < 0) {
            throw new Exception("El precio unitario no puede ser negativo.");
        }
        if (producto.getStock() < 0) {
            throw new Exception("El stock no puede ser negativo.");
        }
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() <= 0) {
            throw new Exception("La categoria del producto es obligatoria.");
        }
        if (producto.getCodigoBarras() == null || producto.getCodigoBarras().trim().isEmpty()) {
            throw new Exception("El codigo de barras es obligatorio.");
        }
    }
}
