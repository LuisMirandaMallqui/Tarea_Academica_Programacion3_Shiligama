package pe.edu.pucp.usuario.impl;

import java.util.List;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.persistance.dao.usuario.impl.TrabajadorDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.usuario.bo.TrabajadorBo;

public class TrabajadorBoImpl implements TrabajadorBo {
    private final UsuarioDao<Trabajador> daoTrabajador;

    public TrabajadorBoImpl() {
        daoTrabajador = new TrabajadorDaoImpl();
    }

    @Override
    public int insertar(Trabajador trabajador) throws Exception {
        validar(trabajador, false);
        return daoTrabajador.insertar(trabajador);
    }

    @Override
    public int modificar(Trabajador trabajador) throws Exception {
        validar(trabajador, true);
        return daoTrabajador.modificar(trabajador);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del trabajador debe ser mayor que cero.");
        }
        return daoTrabajador.eliminar(id);
    }

    @Override
    public Trabajador buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del trabajador debe ser mayor que cero.");
        }
        return daoTrabajador.buscarPorId(id);
    }

    @Override
    public List<Trabajador> listarTodos() throws Exception {
        return daoTrabajador.listarTodos();
    }

    @Override
    public Trabajador buscarPorCorreo(String correo) throws Exception {
        validarTextoObligatorio(correo, "El correo es obligatorio.");
        return daoTrabajador.buscarPorCorreo(correo);
    }

    @Override
    public Trabajador obtenerPorDNI(String dni) throws Exception {
        validarDNI(dni);
        return daoTrabajador.obtenerPorDNI(dni);
    }

    private void validar(Trabajador trabajador, boolean esModificacion) throws Exception {
        if (trabajador == null) {
            throw new Exception("El trabajador no puede ser nulo.");
        }
        if (esModificacion && trabajador.getIdUsuario() <= 0) {
            throw new Exception("El ID del trabajador es obligatorio para la modificacion.");
        }
        validarDNI(trabajador.getDni());
        validarTextoObligatorio(trabajador.getNombres(), "El nombre del trabajador es obligatorio.");
        validarTextoObligatorio(trabajador.getApellidos(), "Los apellidos del trabajador son obligatorios.");
        validarTextoObligatorio(trabajador.getCorreo(), "El correo del trabajador es obligatorio.");
        validarTextoObligatorio(trabajador.getContrasena(), "La contrasena es obligatoria.");
        if (!esModificacion && daoTrabajador.existeUsuarioEnBD(trabajador)) {
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
