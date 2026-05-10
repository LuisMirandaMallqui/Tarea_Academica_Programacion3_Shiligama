package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.persistance.dao.usuario.dao.TrabajadorDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrabajadorDaoImpl implements TrabajadorDao {

    // SP: INSERTAR_TRABAJADOR(OUT _id_trabajador, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo, IN _contrasena, IN _cargo, IN _fecha_ingreso)
    @Override
    public int insertar(TrabajadorDto trabajador) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, trabajador.getNombres());
        parametrosEntrada.put(3, trabajador.getApellidos());
        parametrosEntrada.put(4, trabajador.getDni());
        parametrosEntrada.put(5, trabajador.getTelefono());
        parametrosEntrada.put(6, trabajador.getCorreo());
        parametrosEntrada.put(7, trabajador.getContrasena());
        parametrosEntrada.put(8, null); // CARGO — agregar al model si es necesario
        parametrosEntrada.put(9, trabajador.getFechaIngreso() != null
                ? java.sql.Date.valueOf(trabajador.getFechaIngreso()) : null);

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_TRABAJADOR", parametrosEntrada, parametrosSalida);
        trabajador.setIdTrabajador((int) parametrosSalida.get(1));
        return trabajador.getIdTrabajador();
    }

    // SP: MODIFICAR_TRABAJADOR(IN _id_trabajador, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo, IN _fecha_ingreso)
    @Override
    public int modificar(TrabajadorDto trabajador) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, trabajador.getIdTrabajador());
        parametrosEntrada.put(2, trabajador.getNombres());
        parametrosEntrada.put(3, trabajador.getApellidos());
        parametrosEntrada.put(4, trabajador.getDni());
        parametrosEntrada.put(5, trabajador.getTelefono());
        parametrosEntrada.put(6, trabajador.getCorreo());
        parametrosEntrada.put(7, trabajador.getFechaIngreso() != null
                ? java.sql.Date.valueOf(trabajador.getFechaIngreso()) : null);

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_TRABAJADOR", parametrosEntrada, null);
    }

    // SP: ELIMINAR_TRABAJADOR(IN _id_trabajador)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_TRABAJADOR", parametrosEntrada, null);
    }

    // SP: BUSCAR_TRABAJADOR_X_ID(IN _id_trabajador)
    @Override
    public TrabajadorDto buscarPorID(int id) {
        TrabajadorDto trabajador = null;
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
    public List<TrabajadorDto> listarTodos() {
        List<TrabajadorDto> lista = new ArrayList<>();

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

    // SP: BUSCAR_TRABAJADOR_X_CORREO(IN _correo)
    @Override
    public TrabajadorDto buscarPorCorreo(String correo) {
        TrabajadorDto trabajador = null;
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

    // SP: BUSCAR_TRABAJADOR_X_DNI(IN _dni)
    @Override
    public TrabajadorDto obtenerPorDNI(String dni) {
        TrabajadorDto trabajador = null;
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

    // SP: EXISTE_USUARIO_EN_BD(IN _correo, IN _dni)
    @Override
    public Boolean existeUsuarioEnBD(TrabajadorDto trabajador) {
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

    private TrabajadorDto mapearTrabajador(ResultSet rs) throws SQLException {
        TrabajadorDto t = new TrabajadorDto();
        t.setIdTrabajador(rs.getInt("TRABAJADOR_ID"));
        Date fechaIngreso = rs.getDate("FECHA_INGRESO");
        if (fechaIngreso != null) {
            t.setFechaIngreso(fechaIngreso.toLocalDate());
        }
        t.setIdUsuario(rs.getInt("USUARIO_ID"));
        t.setNombres(rs.getString("NOMBRES"));
        t.setApellidos(rs.getString("APELLIDOS"));
        t.setDni(rs.getString("DNI"));
        t.setTelefono(rs.getString("TELEFONO"));
        t.setCorreo(rs.getString("CORREO"));
        t.setContrasena(rs.getString("CONTRASENA"));
        return t;
    }
}
