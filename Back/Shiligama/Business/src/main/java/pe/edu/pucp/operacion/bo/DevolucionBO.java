package pe.edu.pucp.operacion.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.operacion.Devolucion;

import java.time.LocalDate;
import java.util.List;

public interface DevolucionBO extends BaseBo<Devolucion> {
    List<Devolucion> listarPorFechas(LocalDate fechaInicio, LocalDate fechaFin) throws Exception;
}
