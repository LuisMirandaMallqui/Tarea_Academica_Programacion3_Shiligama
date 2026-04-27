package pe.edu.pucp.bo;

import java.util.List;

public interface BaseBo<T> {
    int insertar(T t);
    int modificar(T t);
    int eliminar(int id);
    T buscarPorID(int id);
    List<T> listarTodos();
}
