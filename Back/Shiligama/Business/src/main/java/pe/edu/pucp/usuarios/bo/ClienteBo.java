package pe.edu.pucp.usuarios.bo;

import pe.edu.pucp.bo.BaseBo;
import pe.edu.pucp.model.usuario.ClienteDto;

public interface ClienteBo extends BaseBo<ClienteDto> {
    ClienteDto buscarPorCorreo(String correo);
    ClienteDto obtenerPorDNI(String dni);
}
