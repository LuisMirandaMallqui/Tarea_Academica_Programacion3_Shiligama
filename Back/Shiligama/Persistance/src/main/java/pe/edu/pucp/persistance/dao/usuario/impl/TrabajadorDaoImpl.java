package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.persistance.dao.usuario.dao.TrabajadorDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrabajadorDaoImpl implements TrabajadorDao {

    // -------------------------------------------------------------------------
    // INSERT con transaccion:
    //   1) INSERTAR_USUARIO(OUT _usuario_id, IN _nombres, _apellidos, _dni,
    //      _telefono, _correo, _contrasena)
    //   2) INSERTAR_TRABAJADOR(IN _usuario_id, IN _cargo, IN _fecha_ingreso)
    // -------------------------------------------------------------------------
    @Override
    public int insertar(Trabajador trabajador) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // 1) Insertar en tabla padre: usuario
            Map<Integer, Object> paramsUsr = new HashMap<>();
            Map<Integer, Object> paramsUsrOut = new HashMap<>();
            paramsUsrOut.put(1, Types.INTEGER);
            paramsUsr.put(2, trabajador.getNombres());
            paramsUsr.put(3, trabajador.getApellidos());
            paramsUsr.put(4, trabajador.getDni());
            paramsUsr.put(5, trabajador.getTelefono());
            paramsUsr.put(6, trabajador.getCorreo());
            paramsUsr.put(7, trabajador.getContrasena());

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_USUARIO", paramsUsr, paramsUsrOut);
            int idGenerado = (int) paramsUsrOut.get(1);
            trabajador.setIdUsuario(idGenerado);

            // 2) Insertar en tabla hija: trabajador
            Map<Integer, Object> paramsTrab = new HashMap<>();
            paramsTrab.put(1, idGenerado);
            paramsTrab.put(2, trabajador.getCargo());
            paramsTrab.put(3, trabajador.getFechaIngreso() != null
                    ? java.sql.Date.valueOf(trabajador.getFechaIngreso()) : null);

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_TRABAJADOR", paramsTrab, null);

            dbManager.confirmarTransaccion();
            resultado = idGenerado;
        } catch (SQLException ex) {
            System.out.println("Error al insertar trabajador: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // UPDATE con transaccion:
    //   1) MODIFICAR_USUARIO(IN _usuario_id, _nombres, _apellidos, _dni,
    //      _telefono, _correo)
    //   2) MODIFICAR_TRABAJADOR(IN _usuario_id, IN _cargo, IN _fecha_ingreso)
    // -------------------------------------------------------------------------
    @Override
    public int modificar(Trabajador trabajador) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // 1) Modificar tabla padre: usuario
            Map<Integer, Object> paramsUsr = new HashMap<>();
            paramsUsr.put(1, trabajador.getIdUsuario());
            paramsUsr.put(2, trabajador.getNombres());
            paramsUsr.put(3, trabajador.getApellidos());
            paramsUsr.put(4, trabajador.getDni());
            paramsUsr.put(5, trabajador.getTelefono());
            paramsUsr.put(6, trabajador.getCorreo());

            dbManager.ejecutarProcedimientoTransaccion(
                    "MODIFICAR_USUARIO", paramsUsr, null);

            // 2) Modificar tabla hija: trabajador
            Map<Integer, Object> paramsTrab = new HashMap<>();
            paramsTrab.put(1, trabajador.getIdUsuario());
            paramsTrab.put(2, trabajador.getCargo());
            paramsTrab.put(3, trabajador.getFechaIngreso() != null
                    ? java.sql.Date.valueOf(trabajador.getFechaIngreso()) : null);

            resultado = dbManager.ejecutarProcedimientoTransaccion(
                    "MODIFICAR_TRABAJADOR", paramsTrab, null);

            dbManager.confirmarTransaccion();
        } catch (SQLException ex) {
            System.out.println("Error al modificar trabajador: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    // SP: ELIMINAR_TRABAJADOR(IN _usuario_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_TRABAJADOR", parametrosEntrada, null);
    }

    // SP: BUSCAR_TRABAJADOR_X_ID(IN _usuario_id)
    @Override
    public Trabajador buscarPorId(int id) {
        Trabajador trabajador = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_TRABAJADOR_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    trabajador = mapearTrabajador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar trabajador: " + ex.getMessage());
        }
        return trabajador;
    }

    // SP: LISTAR_TRABAJADORES()
    @Override
    public List<Trabajador> listarTodos() {
        List<Trabajador> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_TRABAJADORES", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearTrabajador(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar trabajadores: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public Trabajador buscarPorCorreo(String correo) {
        Trabajador trabajador = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, correo);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_TRABAJADOR_X_CORREO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    trabajador = mapearTrabajador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en buscarPorCorreo (trabajador): " + ex.getMessage());
        }
        return trabajador;
    }

    @Override
    public Trabajador obtenerPorDNI(String dni) {
        Trabajador trabajador = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, dni);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_TRABAJADOR_X_DNI", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    trabajador = mapearTrabajador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en obtenerPorDNI (trabajador): " + ex.getMessage());
        }
        return trabajador;
    }

    @Override
    public Boolean existeUsuarioEnBD(Trabajador trabajador) {
        Boolean existe = false;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, trabajador.getCorreo());
        parametrosEntrada.put(2, trabajador.getDni());

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("EXISTE_USUARIO_EN_BD", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en existeUsuarioEnBD (trabajador): " + ex.getMessage());
        }
        return existe;
    }

    // Mapeo — usa USUARIO_ID (PK compartida padre-hijo)
    private Trabajador mapearTrabajador(ResultSet rs) throws SQLException {
        Trabajador t = new Trabajador();
        t.setIdUsuario(rs.getInt("USUARIO_ID"));
        t.setCargo(rs.getString("CARGO"));
        Date fechaIngreso = rs.getDate("FECHA_INGRESO");
        if (fechaIngreso != null) {
            t.setFechaIngreso(fechaIngreso.toLocalDate());
        }
        t.setNombres(rs.getString("NOMBRES"));
        t.setApellidos(rs.getString("APELLIDOS"));
        t.setDni(rs.getString("DNI"));
        t.setTelefono(rs.getString("TELEFONO"));
        t.setCorreo(rs.getString("CORREO"));
        // La contraseña NO se expone en el JSON de respuesta por seguridad
        t.setContrasena(null);
        return t;
    }
}
