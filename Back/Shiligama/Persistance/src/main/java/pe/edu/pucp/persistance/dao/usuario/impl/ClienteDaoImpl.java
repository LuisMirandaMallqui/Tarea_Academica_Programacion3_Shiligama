package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.persistance.dao.usuario.dao.ClienteDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteDaoImpl implements ClienteDao {

    // -------------------------------------------------------------------------
    // INSERT con transaccion:
    //   1) INSERTAR_USUARIO(OUT _usuario_id, IN _nombres, _apellidos, _dni,
    //      _telefono, _correo, _contrasena)
    //   2) INSERTAR_CLIENTE(IN _id_usuario, IN _direccion_entrega)
    // -------------------------------------------------------------------------
    @Override
    public int insertar(Cliente cliente) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // 1) Insertar en tabla padre: usuario
            Map<Integer, Object> paramsUsr = new HashMap<>();
            Map<Integer, Object> paramsUsrOut = new HashMap<>();
            paramsUsrOut.put(1, Types.INTEGER);            // OUT _usuario_id
            paramsUsr.put(2, cliente.getNombres());
            paramsUsr.put(3, cliente.getApellidos());
            paramsUsr.put(4, cliente.getDni());
            paramsUsr.put(5, cliente.getTelefono());
            paramsUsr.put(6, cliente.getCorreo());
            paramsUsr.put(7, cliente.getContrasena());

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_USUARIO", paramsUsr, paramsUsrOut);
            int idGenerado = (int) paramsUsrOut.get(1);
            cliente.setIdUsuario(idGenerado);

            // 2) Insertar en tabla hija: cliente
            Map<Integer, Object> paramsCli = new HashMap<>();
            paramsCli.put(1, idGenerado);
            paramsCli.put(2, cliente.getDireccionEntrega());

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_CLIENTE", paramsCli, null);

            dbManager.confirmarTransaccion();
            resultado = idGenerado;
        } catch (SQLException ex) {
            System.out.println("Error al insertar cliente: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // UPDATE con transaccion:
    //   1) MODIFICAR_USUARIO(IN _usuario_id, _nombres, _apellidos, _dni,
    //      _telefono, _correo)
    //   2) MODIFICAR_CLIENTE(IN _usuario_id, IN _direccion_entrega)
    // -------------------------------------------------------------------------
    @Override
    public int modificar(Cliente cliente) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // 1) Modificar tabla padre: usuario
            Map<Integer, Object> paramsUsr = new HashMap<>();
            paramsUsr.put(1, cliente.getIdUsuario());
            paramsUsr.put(2, cliente.getNombres());
            paramsUsr.put(3, cliente.getApellidos());
            paramsUsr.put(4, cliente.getDni());
            paramsUsr.put(5, cliente.getTelefono());
            paramsUsr.put(6, cliente.getCorreo());

            dbManager.ejecutarProcedimientoTransaccion(
                    "MODIFICAR_USUARIO", paramsUsr, null);

            // 2) Modificar tabla hija: cliente
            Map<Integer, Object> paramsCli = new HashMap<>();
            paramsCli.put(1, cliente.getIdUsuario());
            paramsCli.put(2, cliente.getDireccionEntrega());

            resultado = dbManager.ejecutarProcedimientoTransaccion(
                    "MODIFICAR_CLIENTE", paramsCli, null);

            dbManager.confirmarTransaccion();
        } catch (SQLException ex) {
            System.out.println("Error al modificar cliente: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    // SP: ELIMINAR_CLIENTE(IN _usuario_id)  — desactiva en tabla usuario
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_CLIENTE", parametrosEntrada, null);
    }

    // SP: BUSCAR_CLIENTE_X_ID(IN _usuario_id)
    @Override
    public Cliente buscarPorID(int id) {
        Cliente cliente = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_CLIENTE_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar cliente: " + ex.getMessage());
        }
        return cliente;
    }

    // SP: LISTAR_CLIENTES()
    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_CLIENTES", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearCliente(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar clientes: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        Cliente cliente = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, correo);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_CLIENTE_X_CORREO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en buscarPorCorreo (cliente): " + ex.getMessage());
        }
        return cliente;
    }

    @Override
    public Cliente obtenerPorDNI(String dni) {
        Cliente cliente = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, dni);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_CLIENTE_X_DNI", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en obtenerPorDNI (cliente): " + ex.getMessage());
        }
        return cliente;
    }

    @Override
    public Boolean existeUsuarioEnBD(Cliente cliente) {
        Boolean existe = false;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, cliente.getCorreo());
        parametrosEntrada.put(2, cliente.getDni());

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("EXISTE_USUARIO_EN_BD", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en existeUsuarioEnBD (cliente): " + ex.getMessage());
        }
        return existe;
    }

    // Mapeo — usa USUARIO_ID (PK compartida padre-hijo)
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdUsuario(rs.getInt("USUARIO_ID"));
        c.setNombres(rs.getString("NOMBRES"));
        c.setApellidos(rs.getString("APELLIDOS"));
        c.setDni(rs.getString("DNI"));
        c.setTelefono(rs.getString("TELEFONO"));
        c.setCorreo(rs.getString("CORREO"));
        c.setContrasena(rs.getString("CONTRASENA"));
        c.setDireccionEntrega(rs.getString("DIRECCION_ENTREGA"));
        return c;
    }
}
