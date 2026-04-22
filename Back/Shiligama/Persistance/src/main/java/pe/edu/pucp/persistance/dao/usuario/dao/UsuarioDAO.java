package pe.edu.pucp.persistance.dao.usuario.dao;

import pe.edu.pucp.model.usuario.UsuarioDto;
import pe.edu.pucp.persistance.dao.IDAO;

// Interfaz genérica para todos los DAOs de usuario.
// El parámetro T permite que cada DAO hijo trabaje con su tipo concreto
// sin casteos: ClienteDAOImpl implementa UsuarioDao<ClienteDto>,
// TrabajadorDAOImpl implementa UsuarioDao<TrabajadorDto>, etc.
public interface UsuarioDao<T extends UsuarioDto> extends IDAO<T> {
    T buscarPorCorreo(String correo);
    T obtenerPorDNI(String dni);
    Boolean existeUsuarioEnBD(T usuario);
}