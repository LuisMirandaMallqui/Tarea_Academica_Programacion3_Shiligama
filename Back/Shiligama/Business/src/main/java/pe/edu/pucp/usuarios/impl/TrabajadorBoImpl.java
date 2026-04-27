package pe.edu.pucp.usuarios.impl;

import java.util.List;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.persistance.dao.usuario.impl.TrabajadorDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.usuarios.bo.TrabajadorBo;

public class TrabajadorBoImpl implements TrabajadorBo {
    private final UsuarioDao<TrabajadorDto> daoTrabajador;

    public TrabajadorBoImpl() {
        daoTrabajador = new TrabajadorDaoImpl();
    }

    @Override
    public int insertar(TrabajadorDto trabajador) {
        if (trabajador.getNombres() == null || trabajador.getNombres().isBlank()) {
            throw new RuntimeException("Error: el nombre del trabajador es obligatorio.");
        }
        if (trabajador.getApellidos() == null || trabajador.getApellidos().isBlank()) {
            throw new RuntimeException("Error: los apellidos del trabajador son obligatorios.");
        }
        if (trabajador.getDni() == null || trabajador.getDni().length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        if (trabajador.getCorreo() == null || trabajador.getCorreo().isBlank()) {
            throw new RuntimeException("Error: el correo del trabajador es obligatorio.");
        }
        if (trabajador.getContrasena() == null || trabajador.getContrasena().isBlank()) {
            throw new RuntimeException("Error: la contrasena es obligatoria.");
        }
        if (daoTrabajador.existeUsuarioEnBD(trabajador)) {
            throw new RuntimeException("Error: ya existe un usuario con ese DNI o correo.");
        }
        return daoTrabajador.insertar(trabajador);
    }

    @Override
    public int modificar(TrabajadorDto trabajador) {
        if (trabajador.getIdTrabajador() <= 0) {
            throw new RuntimeException("Error: el ID del trabajador no es valido.");
        }
        if (trabajador.getNombres() == null || trabajador.getNombres().isBlank()) {
            throw new RuntimeException("Error: el nombre del trabajador es obligatorio.");
        }
        if (trabajador.getDni() == null || trabajador.getDni().length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        return daoTrabajador.modificar(trabajador);
    }

    @Override
    public int eliminar(int id) {
        if (id <= 0) {
            throw new RuntimeException("Error: el ID del trabajador no es valido.");
        }
        return daoTrabajador.eliminar(id);
    }

    @Override
    public TrabajadorDto buscarPorID(int id) {
        if (id <= 0) {
            throw new RuntimeException("Error: el ID del trabajador no es valido.");
        }
        return daoTrabajador.buscarPorID(id);
    }

    @Override
    public List<TrabajadorDto> listarTodos() {
        return daoTrabajador.listarTodos();
    }

    @Override
    public TrabajadorDto buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new RuntimeException("Error: el correo no puede estar vacio.");
        }
        return daoTrabajador.buscarPorCorreo(correo);
    }

    @Override
    public TrabajadorDto obtenerPorDNI(String dni) {
        if (dni == null || dni.length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        return daoTrabajador.obtenerPorDNI(dni);
    }
}
