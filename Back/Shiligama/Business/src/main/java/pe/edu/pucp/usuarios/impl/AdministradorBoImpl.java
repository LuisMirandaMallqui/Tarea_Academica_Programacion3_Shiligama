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
    public int insertar(AdministradorDto admin) throws Exception {
        validar(admin, false);
        return daoAdministrador.insertar(admin);
    }

    @Override
    public int modificar(AdministradorDto admin) throws Exception {
        validar(admin, true);
        return daoAdministrador.modificar(admin);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del administrador debe ser mayor que cero.");
        }
        return daoAdministrador.eliminar(id);
    }

    @Override
    public AdministradorDto buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del administrador debe ser mayor que cero.");
        }
        return daoAdministrador.buscarPorID(id);
    }

    @Override
    public List<AdministradorDto> listarTodos() throws Exception {
        return daoAdministrador.listarTodos();
    }

    @Override
    public AdministradorDto buscarPorCorreo(String correo) throws Exception {
        validarTextoObligatorio(correo, "El correo es obligatorio.");
        return daoAdministrador.buscarPorCorreo(correo);
    }

    @Override
    public AdministradorDto obtenerPorDNI(String dni) throws Exception {
        validarDNI(dni);
        return daoAdministrador.obtenerPorDNI(dni);
    }

    private void validar(AdministradorDto admin, boolean esModificacion) throws Exception {
        if (admin == null) {
            throw new Exception("El administrador no puede ser nulo.");
        }
        if (esModificacion && admin.getIdAdministrador() <= 0) {
            throw new Exception("El ID del administrador es obligatorio para la modificacion.");
        }
        validarDNI(admin.getDni());
        validarTextoObligatorio(admin.getNombres(), "El nombre del administrador es obligatorio.");
        validarTextoObligatorio(admin.getApellidos(), "Los apellidos del administrador son obligatorios.");
        validarTextoObligatorio(admin.getCorreo(), "El correo del administrador es obligatorio.");
        validarTextoObligatorio(admin.getContrasena(), "La contrasena es obligatoria.");
        if (!esModificacion && daoAdministrador.existeUsuarioEnBD(admin)) {
            throw new Exception("Ya existe un usuario con ese DNI o correo.");
        }
    }

    private void validarDNI(String dni) throws Exception {
        if (dni == null || dni.trim().isEmpty()) {
            throw new Exception("El DNI es obligatorio.");
        }
        if (!dni.trim().matches("\\d{8}")) {
            throw new Exception("El DNI debe tener exactamente 8 digitos numericos.");
        }
    }

    private void validarTextoObligatorio(String texto, String mensaje) throws Exception {
        if (texto == null || texto.trim().isEmpty()) {
            throw new Exception(mensaje);
        }
    }
}
