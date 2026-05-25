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

    // SP: INSERTAR_ADMINISTRADOR(OUT _id_usuario, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo, IN _contrasena)
    @Override
    public int insertar(Administrador administrador) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);        // OUT _id_usuario
        parametrosEntrada.put(2, administrador.getNombres());
        parametrosEntrada.put(3, administrador.getApellidos());
        parametrosEntrada.put(4, administrador.getDni());
        parametrosEntrada.put(5, administrador.getTelefono());
        parametrosEntrada.put(6, administrador.getCorreo());
        parametrosEntrada.put(7, administrador.getContrasena());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_ADMINISTRADOR", parametrosEntrada, parametrosSalida);
        administrador.setIdUsuario((int) parametrosSalida.get(1));
        return administrador.getIdUsuario();
    }

    // SP: MODIFICAR_ADMINISTRADOR(IN _id_usuario, IN _nombres, IN _apellidos,
    //   IN _dni, IN _telefono, IN _correo)
    @Override
    public int modificar(Administrador administrador) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, administrador.getIdUsuario());
        parametrosEntrada.put(2, administrador.getNombres());
        parametrosEntrada.put(3, administrador.getApellidos());
        parametrosEntrada.put(4, administrador.getDni());
        parametrosEntrada.put(5, administrador.getTelefono());
        parametrosEntrada.put(6, administrador.getCorreo());

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_ADMINISTRADOR", parametrosEntrada, null);
    }

    // SP: ELIMINAR_ADMINISTRADOR(IN _id_usuario)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_ADMINISTRADOR", parametrosEntrada, null);
    }

    // SP: BUSCAR_ADMINISTRADOR_X_ID(IN _id_usuario)
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

    // Mapeo — ahora usa ID_USUARIO (PK compartida)
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        Administrador a = new Administrador();
        a.setIdUsuario(rs.getInt("ID_USUARIO"));
        a.setNombres(rs.getString("NOMBRES"));
        a.setApellidos(rs.getString("APELLIDOS"));
        a.setDni(rs.getString("DNI"));
        a.setTelefono(rs.getString("TELEFONO"));
        a.setCorreo(rs.getString("CORREO"));
        a.setContrasena(rs.getString("CONTRASENA"));
        return a;
    }
}
