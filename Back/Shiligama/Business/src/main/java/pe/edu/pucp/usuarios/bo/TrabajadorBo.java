package pe.edu.pucp.usuarios.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.usuario.TrabajadorDto;

public interface TrabajadorBo extends BaseBo<TrabajadorDto> {
    TrabajadorDto buscarPorCorreo(String correo);
    TrabajadorDto obtenerPorDNI(String dni);
}
