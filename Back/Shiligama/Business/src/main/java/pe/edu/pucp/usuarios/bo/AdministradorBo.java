package pe.edu.pucp.usuarios.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.usuario.AdministradorDto;

public interface AdministradorBo extends BaseBo<AdministradorDto> {
    AdministradorDto buscarPorCorreo(String correo);
    AdministradorDto obtenerPorDNI(String dni);
}
