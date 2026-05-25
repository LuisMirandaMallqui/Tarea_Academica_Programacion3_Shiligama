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

    // SP: INSERTAR_TRABAJADOR(OUT _id_usuario, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo, IN _contrasena, IN _fecha_ingreso)
    @Override
    public int insertar(Trabajador trabajador) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);        // OUT _id_usuario
        parametrosEntrada.put(2, trabajador.getNombres());
        parametrosEntrada.put(3, trabajador.getApellidos());
        parametrosEntrada.put(4, trabajador.getDni());
        parametrosEntrada.put(5, trabajador.getTelefono());
        parametrosEntrada.put(6, trabajador.getCorreo());
        parametrosEntrada.put(7, trabajador.getContrasena());
        parametrosEntrada.put(8, trabajador.getFechaIngreso() != null
                ? java.sql.Date.valueOf(trabajador.getFechaIngreso()) : null);

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_TRABAJADOR", parametrosEntrada, parametrosSalida);
        trabajador.setIdUsuario((int) parametrosSalida.get(1));
        return trabajador.getIdUsuario();
    }

    // SP: MODIFICAR_TRABAJADOR(IN _id_usuario, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo, IN _fecha_ingreso)
    @Override
    public int modificar(Trabajador trabajador) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, trabajador.getIdUsuario());
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

    // SP: ELIMINAR_TRABAJADOR(IN _id_usuario)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_TRABAJADOR", parametrosEntrada, null);
    }

    // SP: BUSCAR_TRABAJADOR_X_ID(IN _id_usuario)
    @Override
    public Trabajador buscarPorID(int id) {
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

    // Mapeo — ahora usa ID_USUARIO (PK compartida)
    private Trabajador mapearTrabajador(ResultSet rs) throws SQLException {
        Trabajador t = new Trabajador();
        t.setIdUsuario(rs.getInt("ID_USUARIO"));
        Date fechaIngreso = rs.getDate("FECHA_INGRESO");
        if (fechaIngreso != null) {
            t.setFechaIngreso(fechaIngreso.toLocalDate());
        }
        t.setNombres(rs.getString("NOMBRES"));
        t.setApellidos(rs.getString("APELLIDOS"));
        t.setDni(rs.getString("DNI"));
        t.setTelefono(rs.getString("TELEFONO"));
        t.setCorreo(rs.getString("CORREO"));
        t.setContrasena(rs.getString("CONTRASENA"));
        return t;
    }
}
