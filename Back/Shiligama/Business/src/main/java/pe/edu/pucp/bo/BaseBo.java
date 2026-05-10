package pe.edu.pucp.bo;

import java.util.List;

public interface BaseBo<T> {
    int insertar(T t) throws Exception;
    int modificar(T t) throws Exception;
    int eliminar(int id) throws Exception;
    T buscarPorID(int id) throws Exception;
    List<T> listarTodos() throws Exception;
}
