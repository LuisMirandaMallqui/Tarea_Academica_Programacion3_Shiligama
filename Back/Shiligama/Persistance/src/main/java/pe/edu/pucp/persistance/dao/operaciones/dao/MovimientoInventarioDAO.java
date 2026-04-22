package pe.edu.pucp.persistance.dao.operaciones.dao;

import pe.edu.pucp.persistance.dao.IDAO;
import pe.edu.pucp.model.operaciones.MovimientoInventario;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz DAO específica para la entidad MovimientoInventario.
 */
public interface MovimientoInventarioDao extends IDAO<MovimientoInventario> {
    // Aquí se pueden agregar firmas de métodos específicos
    List<MovimientoInventario> listarPorProducto(int idProducto);
    List<MovimientoInventario> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
