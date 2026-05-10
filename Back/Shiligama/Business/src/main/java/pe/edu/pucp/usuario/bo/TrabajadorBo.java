package pe.edu.pucp.usuario.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.usuario.Trabajador;

public interface TrabajadorBo extends BaseBo<Trabajador> {
    Trabajador buscarPorCorreo(String correo) throws Exception;
    Trabajador obtenerPorDNI(String dni) throws Exception;
}
