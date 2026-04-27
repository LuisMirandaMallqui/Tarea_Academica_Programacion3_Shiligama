package pe.edu.pucp.usuarios.impl;

import java.util.List;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.persistance.dao.usuario.impl.ClienteDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.usuarios.bo.ClienteBo;

public class ClienteBoImpl implements ClienteBo {
    private final UsuarioDao<ClienteDto> daoCliente;

    public ClienteBoImpl() {
        daoCliente = new ClienteDaoImpl();
    }

    @Override
    public int insertar(ClienteDto cliente) {
        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            throw new RuntimeException("Error: el nombre del cliente es obligatorio.");
        }
        if (cliente.getApellidos() == null || cliente.getApellidos().isBlank()) {
            throw new RuntimeException("Error: los apellidos del cliente son obligatorios.");
        }
        if (cliente.getDni() == null || cliente.getDni().length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        if (cliente.getCorreo() == null || cliente.getCorreo().isBlank()) {
            throw new RuntimeException("Error: el correo del cliente es obligatorio.");
        }
        if (cliente.getContrasena() == null || cliente.getContrasena().isBlank()) {
            throw new RuntimeException("Error: la contrasena es obligatoria.");
        }
        if (daoCliente.existeUsuarioEnBD(cliente)) {
            throw new RuntimeException("Error: ya existe un usuario con ese DNI o correo.");
        }
        return daoCliente.insertar(cliente);
    }

    @Override
    public int modificar(ClienteDto cliente) {
        if (cliente.getIdCliente() <= 0) {
            throw new RuntimeException("Error: el ID del cliente no es valido.");
        }
        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            throw new RuntimeException("Error: el nombre del cliente es obligatorio.");
        }
        if (cliente.getDni() == null || cliente.getDni().length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        return daoCliente.modificar(cliente);
    }

    @Override
    public int eliminar(int id) {
        if (id <= 0) {
            throw new RuntimeException("Error: el ID del cliente no es valido.");
        }
        return daoCliente.eliminar(id);
    }

    @Override
    public ClienteDto buscarPorID(int id) {
        if (id <= 0) {
            throw new RuntimeException("Error: el ID del cliente no es valido.");
        }
        return daoCliente.buscarPorID(id);
    }

    @Override
    public List<ClienteDto> listarTodos() {
        return daoCliente.listarTodos();
    }

    @Override
    public ClienteDto buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new RuntimeException("Error: el correo no puede estar vacio.");
        }
        return daoCliente.buscarPorCorreo(correo);
    }

    @Override
    public ClienteDto obtenerPorDNI(String dni) {
        if (dni == null || dni.length() != 8) {
            throw new RuntimeException("Error: el DNI debe tener exactamente 8 digitos.");
        }
        return daoCliente.obtenerPorDNI(dni);
    }
}
