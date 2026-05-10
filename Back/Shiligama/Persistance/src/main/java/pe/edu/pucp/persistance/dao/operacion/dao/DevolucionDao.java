package pe.edu.pucp.persistance.dao.operacion.dao;

import pe.edu.pucp.persistance.dao.IDAO;
import pe.edu.pucp.model.operacion.Devolucion;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz DAO específica para la entidad Devolucion.
 */
public interface DevolucionDao extends IDAO<Devolucion> {
    // Aquí se pueden agregar firmas de métodos específicos de Devolucion
    List<Devolucion> listarPorFechas(LocalDate fechaInicio, LocalDate fechaFin);
}
