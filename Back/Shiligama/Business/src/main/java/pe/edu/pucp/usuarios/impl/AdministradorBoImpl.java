package pe.edu.pucp.usuarios.impl;

import java.util.List;
import pe.edu.pucp.model.usuario.AdministradorDto;
import pe.edu.pucp.persistance.dao.usuario.impl.AdministradorDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.usuarios.bo.AdministradorBo;

public class AdministradorBoImpl implements AdministradorBo {
    private final UsuarioDao<AdministradorDto> daoAdministrador;

    public AdministradorBoImpl() {
        daoAdministrador = new AdministradorDaoImpl();
    }

    @Override
    public int insertar(AdministradorDto admin) {
        if (admin.getNombres() == null || admin.getNombres().isBlank()) {
            throw new RuntimeException("Error: el nombre del administrador es obligatorio.");
        }
        if (admin.getApellidos() == null || admin.getApellidos().isBlank()) {
            throw new RuntimeException("Error: los apellidos del administrador son obligatorios.");
        }
        if (admin.getDni() == null || admin.getDni().length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        if (admin.getCorreo() == null || admin.getCorreo().isBlank()) {
            throw new RuntimeException("Error: el correo del administrador es obligatorio.");
        }
        if (admin.getContrasena() == null || admin.getContrasena().isBlank()) {
            throw new RuntimeException("Error: la contrasena es obligatoria.");
        }
        if (daoAdministrador.existeUsuarioEnBD(admin)) {
            throw new RuntimeException("Error: ya existe un usuario con ese DNI o correo.");
        }
        return daoAdministrador.insertar(admin);
    }

    @Override
    public int modificar(AdministradorDto admin) {
        if (admin.getIdAdministrador() <= 0) {
            throw new RuntimeException("Error: el ID del administrador no es valido.");
        }
        if (admin.getNombres() == null || admin.getNombres().isBlank()) {
            throw new RuntimeException("Error: el nombre del administrador es obligatorio.");
        }
        if (admin.getDni() == null || admin.getDni().length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        return daoAdministrador.modificar(admin);
    }

    @Override
    public int eliminar(int id) {
        if (id <= 0) {
            throw new RuntimeException("Error: el ID del administrador no es valido.");
        }
        return daoAdministrador.eliminar(id);
    }

    @Override
    public AdministradorDto buscarPorID(int id) {
        if (id <= 0) {
            throw new RuntimeException("Error: el ID del administrador no es valido.");
        }
        return daoAdministrador.buscarPorID(id);
    }

    @Override
    public List<AdministradorDto> listarTodos() {
        return daoAdministrador.listarTodos();
    }

    @Override
    public AdministradorDto buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new RuntimeException("Error: el correo no puede estar vacio.");
        }
        return daoAdministrador.buscarPorCorreo(correo);
    }

    @Override
    public AdministradorDto obtenerPorDNI(String dni) {
        if (dni == null || dni.length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        return daoAdministrador.obtenerPorDNI(dni);
    }
}
