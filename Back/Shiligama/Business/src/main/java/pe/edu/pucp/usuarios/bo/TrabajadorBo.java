package pe.edu.pucp.usuarios.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.usuario.TrabajadorDto;

public interface TrabajadorBo extends BaseBo<TrabajadorDto> {
    TrabajadorDto buscarPorCorreo(String correo) throws Exception;
    TrabajadorDto obtenerPorDNI(String dni) throws Exception;
}
