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
    public int insertar(ClienteDto cliente) throws Exception {
        validar(cliente, false);
        return daoCliente.insertar(cliente);
    }

    @Override
    public int modificar(ClienteDto cliente) throws Exception {
        validar(cliente, true);
        return daoCliente.modificar(cliente);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del cliente debe ser mayor que cero.");
        }
        return daoCliente.eliminar(id);
    }

    @Override
    public ClienteDto buscarPorID(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El ID del cliente debe ser mayor que cero.");
        }
        return daoCliente.buscarPorID(id);
    }

    @Override
    public List<ClienteDto> listarTodos() throws Exception {
        return daoCliente.listarTodos();
    }

    @Override
    public ClienteDto buscarPorCorreo(String correo) throws Exception {
        validarTextoObligatorio(correo, "El correo es obligatorio.");
        return daoCliente.buscarPorCorreo(correo);
    }

    @Override
    public ClienteDto obtenerPorDNI(String dni) throws Exception {
        validarDNI(dni);
        return daoCliente.obtenerPorDNI(dni);
    }

    private void validar(ClienteDto cliente, boolean esModificacion) throws Exception {
        if (cliente == null) {
            throw new Exception("El cliente no puede ser nulo.");
        }
        if (esModificacion && cliente.getIdCliente() <= 0) {
            throw new Exception("El ID del cliente es obligatorio para la modificacion.");
        }
        validarDNI(cliente.getDni());
        validarTextoObligatorio(cliente.getNombres(), "El nombre del cliente es obligatorio.");
        validarTextoObligatorio(cliente.getApellidos(), "Los apellidos del cliente son obligatorios.");
        validarTextoObligatorio(cliente.getCorreo(), "El correo del cliente es obligatorio.");
        validarTextoObligatorio(cliente.getContrasena(), "La contrasena es obligatoria.");
        if (!esModificacion && daoCliente.existeUsuarioEnBD(cliente)) {
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
