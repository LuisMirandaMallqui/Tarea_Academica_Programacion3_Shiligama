package pe.edu.pucp.usuario.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.usuario.Administrador;

public interface AdministradorBo extends BaseBo<Administrador> {
    Administrador buscarPorCorreo(String correo) throws Exception;
    Administrador obtenerPorDNI(String dni) throws Exception;
}
