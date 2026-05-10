package pe.edu.pucp.usuario.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.usuario.Cliente;

public interface ClienteBo extends BaseBo<Cliente> {
    Cliente buscarPorCorreo(String correo) throws Exception;
    Cliente obtenerPorDNI(String dni) throws Exception;
}
