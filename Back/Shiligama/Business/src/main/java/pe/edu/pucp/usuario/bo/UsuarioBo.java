package pe.edu.pucp.usuario.bo;

import pe.edu.pucp.bo.BaseBo;

public interface UsuarioBo<Usuario>  extends BaseBo<Usuario> {
    Usuario buscarPorCorreo(String correo) throws Exception;
    Usuario obtenerPorDNI(String dni) throws Exception;
}
