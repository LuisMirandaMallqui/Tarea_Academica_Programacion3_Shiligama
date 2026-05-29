package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.usuario.Administrador;
import pe.edu.pucp.persistance.dao.usuario.dao.AdministradorDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdministradorDaoImpl implements AdministradorDao {

    // -------------------------------------------------------------------------
    // INSERT con transaccion:
    //   1) INSERTAR_USUARIO(OUT _usuario_id, IN _nombres, _apellidos, _dni,
    //      _telefono, _correo, _contrasena)
    //   2) INSERTAR_ADMINISTRADOR(IN _usuario_id)
    // -------------------------------------------------------------------------
    @Override
    public int insertar(Administrador administrador) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // 1) Insertar en tabla padre: usuario
            Map<Integer, Object> paramsUsr = new HashMap<>();
            Map<Integer, Object> paramsUsrOut = new HashMap<>();
            paramsUsrOut.put(1, Types.INTEGER);
            paramsUsr.put(2, administrador.getNombres());
            paramsUsr.put(3, administrador.getApellidos());
            paramsUsr.put(4, administrador.getDni());
            paramsUsr.put(5, administrador.getTelefono());
            paramsUsr.put(6, administrador.getCorreo());
            paramsUsr.put(7, administrador.getContrasena());

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_USUARIO", paramsUsr, paramsUsrOut);
            int idGenerado = (int) paramsUsrOut.get(1);
            administrador.setIdUsuario(idGenerado);

            // 2) Insertar en tabla hija: administrador
            Map<Integer, Object> paramsAdm = new HashMap<>();
            paramsAdm.put(1, idGenerado);

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_ADMINISTRADOR", paramsAdm, null);

            dbManager.confirmarTransaccion();
            resultado = idGenerado;
        } catch (SQLException ex) {
            System.out.println("Error al insertar administrador: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // UPDATE con transaccion:
    //   1) MODIFICAR_USUARIO(IN _usuario_id, _nombres, _apellidos, _dni,
    //      _telefono, _correo)
    //   Administrador no tiene campos propios que modificar
    // -------------------------------------------------------------------------
    @Override
    public int modificar(Administrador administrador) {
        Map<Integer, Object> paramsUsr = new HashMap<>();
        paramsUsr.put(1, administrador.getIdUsuario());
        paramsUsr.put(2, administrador.getNombres());
        paramsUsr.put(3, administrador.getApellidos());
        paramsUsr.put(4, administrador.getDni());
        paramsUsr.put(5, administrador.getTelefono());
        paramsUsr.put(6, administrador.getCorreo());

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_USUARIO", paramsUsr, null);
    }

    // SP: ELIMINAR_ADMINISTRADOR(IN _usuario_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_ADMINISTRADOR", parametrosEntrada, null);
    }

    // SP: BUSCAR_ADMINISTRADOR_X_ID(IN _usuario_id)
    @Override
    public Administrador buscarPorID(int id) {
        Administrador admin = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_ADMINISTRADOR_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    admin = mapearAdministrador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar administrador: " + ex.getMessage());
        }
        return admin;
    }

    // SP: LISTAR_ADMINISTRADORES()
    @Override
    public List<Administrador> listarTodos() {
        List<Administrador> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_ADMINISTRADORES", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearAdministrador(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar administradores: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Administrador buscarPorCorreo(String correo) {
        Administrador admin = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, correo);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_ADMINISTRADOR_X_CORREO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    admin = mapearAdministrador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en buscarPorCorreo (admin): " + ex.getMessage());
        }
        return admin;
    }

    @Override
    public Administrador obtenerPorDNI(String dni) {
        Administrador admin = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, dni);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_ADMINISTRADOR_X_DNI", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    admin = mapearAdministrador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en obtenerPorDNI (admin): " + ex.getMessage());
        }
        return admin;
    }

    @Override
    public Boolean existeUsuarioEnBD(Administrador administrador) {
        Boolean existe = false;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, administrador.getCorreo());
        parametrosEntrada.put(2, administrador.getDni());

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("EXISTE_USUARIO_EN_BD", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en existeUsuarioEnBD (admin): " + ex.getMessage());
        }
        return existe;
    }

    // Mapeo — usa USUARIO_ID (PK compartida padre-hijo)
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        Administrador a = new Administrador();
        a.setIdUsuario(rs.getInt("USUARIO_ID"));
        a.setNombres(rs.getString("NOMBRES"));
        a.setApellidos(rs.getString("APELLIDOS"));
        a.setDni(rs.getString("DNI"));
        a.setTelefono(rs.getString("TELEFONO"));
        a.setCorreo(rs.getString("CORREO"));
        a.setContrasena(rs.getString("CONTRASENA"));
        return a;
    }
}
