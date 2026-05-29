package pe.edu.pucp.producto.impl;

import pe.edu.pucp.model.producto.Categoria;
import pe.edu.pucp.persistance.dao.producto.Impl.CategoriaDaoImpl;
import pe.edu.pucp.persistance.dao.producto.dao.CategoriaDao;
import pe.edu.pucp.producto.bo.CategoriaBo;

import java.util.List;

public class CategoriaBoImpl implements CategoriaBo {
    private final CategoriaDao daoCategoria;

    public CategoriaBoImpl() {
        this.daoCategoria = new CategoriaDaoImpl();
    }

    @Override
    public int insertar(Categoria categoria) throws Exception {
        validar(categoria, false);
        return daoCategoria.insertar(categoria);
    }

    @Override
    public int modificar(Categoria categoria) throws Exception {
        validar(categoria, true);
        return daoCategoria.modificar(categoria);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la categoria debe ser mayor que cero.");
        }
        return daoCategoria.eliminar(id);
    }

    @Override
    public Categoria buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID de la categoria debe ser mayor que cero.");
        }
        return daoCategoria.buscarPorID(id);
    }

    @Override
    public List<Categoria> listarTodos() throws Exception {
        return daoCategoria.listarTodos();
    }

    private void validar(Categoria categoria, boolean esModificacion) throws Exception {
        if (categoria == null) {
            throw new Exception("La categoria no puede ser nula.");
        }
        if (esModificacion && categoria.getIdCategoria() <= 0) {
            throw new Exception("El ID de la categoria es obligatorio para la modificacion.");
        }
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la categoria es obligatorio.");
        }
    }
}
