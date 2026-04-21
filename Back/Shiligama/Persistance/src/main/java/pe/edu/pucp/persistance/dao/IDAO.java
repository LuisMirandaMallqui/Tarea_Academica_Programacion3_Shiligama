package pe.edu.pucp.persistance.dao;
import java.util.List;

public interface IDAO<T> {
    int insertar(T obj);
    int modificar(T obj);
    int eliminar(int id);
    T buscarPorID(int id);
    List<T> listarTodos();
}
