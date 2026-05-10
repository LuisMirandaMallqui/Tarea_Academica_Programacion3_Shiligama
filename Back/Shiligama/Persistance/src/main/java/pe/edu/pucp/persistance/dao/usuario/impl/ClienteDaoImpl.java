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
    // INSERT — SP: INSERTAR_CLIENTE(OUT _id_cliente, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo, IN _contrasena, IN _direccion_entrega)
    // -------------------------------------------------------------------------
    @Override
    public int insertar(Cliente cliente) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, cliente.getNombres());
        parametrosEntrada.put(3, cliente.getApellidos());
        parametrosEntrada.put(4, cliente.getDni());
        parametrosEntrada.put(5, cliente.getTelefono());
        parametrosEntrada.put(6, cliente.getCorreo());
        parametrosEntrada.put(7, cliente.getContrasena());
        parametrosEntrada.put(8, cliente.getDireccionEntrega());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_CLIENTE", parametrosEntrada, parametrosSalida);
        cliente.setIdCliente((int) parametrosSalida.get(1));
        return cliente.getIdCliente();
    }

    // -------------------------------------------------------------------------
    // UPDATE — SP: MODIFICAR_CLIENTE(IN _id_cliente, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo, IN _direccion_entrega)
    // -------------------------------------------------------------------------
    @Override
    public int modificar(Cliente cliente) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, cliente.getIdCliente());
        parametrosEntrada.put(2, cliente.getNombres());
        parametrosEntrada.put(3, cliente.getApellidos());
        parametrosEntrada.put(4, cliente.getDni());
        parametrosEntrada.put(5, cliente.getTelefono());
        parametrosEntrada.put(6, cliente.getCorreo());
        parametrosEntrada.put(7, cliente.getDireccionEntrega());

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_CLIENTE", parametrosEntrada, null);
    }

    // -------------------------------------------------------------------------
    // DELETE (logico) — SP: ELIMINAR_CLIENTE(IN _id_cliente)
    // -------------------------------------------------------------------------
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_CLIENTE", parametrosEntrada, null);
    }

    // -------------------------------------------------------------------------
    // SELECT por ID — SP: BUSCAR_CLIENTE_X_ID(IN _id_cliente)
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // SELECT todos — SP: LISTAR_CLIENTES()
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // Metodos especificos de UsuarioDao
    // -------------------------------------------------------------------------

    // SP: BUSCAR_CLIENTE_X_CORREO(IN _correo)
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

    // SP: BUSCAR_CLIENTE_X_DNI(IN _dni)
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

    // SP: EXISTE_USUARIO_EN_BD(IN _correo, IN _dni) — retorna COUNT
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

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet
    // -------------------------------------------------------------------------
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("CLIENTE_ID"));
        c.setDireccionEntrega(rs.getString("DIRECCION_ENTREGA"));
        c.setIdUsuario(rs.getInt("USUARIO_ID"));
        c.setNombres(rs.getString("NOMBRES"));
        c.setApellidos(rs.getString("APELLIDOS"));
        c.setDni(rs.getString("DNI"));
        c.setTelefono(rs.getString("TELEFONO"));
        c.setCorreo(rs.getString("CORREO"));
        c.setContrasena(rs.getString("CONTRASENA"));
        return c;
    }
}
