package pe.edu.pucp.persistance.dao.usuario.dao;

import pe.edu.pucp.model.usuario.UsuarioDto;
import pe.edu.pucp.persistance.dao.IDAO;

public interface UsuarioDAO extends IDAO<UsuarioDto> {
    UsuarioDto buscarPorCorreo(String correo);
    UsuarioDto obtenerPorDNI(String DNI);
    Boolean existeUsuarioEnBD(UsuarioDto per);
}
