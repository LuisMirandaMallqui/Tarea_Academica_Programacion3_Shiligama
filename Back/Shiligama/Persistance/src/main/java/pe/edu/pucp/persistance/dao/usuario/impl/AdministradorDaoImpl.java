package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.model.usuario.AdministradorDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class AdministradorDaoImpl extends DaoImplBase implements UsuarioDao<AdministradorDto> {

    private AdministradorDto administrador;

    public AdministradorDaoImpl() {
        this.administrador = null;
    }

    public AdministradorDaoImpl(AdministradorDto administrador) {
        this.administrador = administrador;
    }

    // -------------------------------------------------------------------------
    // Metodos CRUD importantes
    // -------------------------------------------------------------------------

    @Override
    public int insertar(AdministradorDto administrador) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_ADMINISTRADOR(?, ?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, administrador.getNombres());
            cs.setString(3, administrador.getApellidos());
            cs.setString(4, administrador.getDni());
            cs.setString(5, administrador.getTelefono());
            cs.setString(6, administrador.getEmail());
            cs.setString(7, administrador.getContrasena());
            cs.execute();
            administrador.setIdAdministrador(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar administrador: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(AdministradorDto administrador) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_ADMINISTRADOR(?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, administrador.getIdAdministrador());
            cs.setString(2, administrador.getNombres());
            cs.setString(3, administrador.getApellidos());
            cs.setString(4, administrador.getDni());
            cs.setString(5, administrador.getTelefono());
            cs.setString(6, administrador.getEmail());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar administrador: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_ADMINISTRADOR(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar administrador: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // SELECT — PreparedStatement a través de los template methods de la base
    // -------------------------------------------------------------------------

    public AdministradorDto buscarPorID(int id) {
        this.administrador = new AdministradorDto();
        this.administrador.setIdAdministrador(id);
        this.obtenerPorId();
        return this.administrador;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT a.ADMINISTRADOR_ID, "
                + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                + "FROM administradores a JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID "
                + "WHERE a.ADMINISTRADOR_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.administrador.getIdAdministrador());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.administrador = mapearAdministrador();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.administrador = null;
    }

    @Override
    public List<AdministradorDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT a.ADMINISTRADOR_ID, "
                + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                + "FROM administradores a JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID "
                + "WHERE a.ACTIVO = 1 "
                + "ORDER BY u.APELLIDOS, u.NOMBRES";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearAdministrador());
    }

    // -------------------------------------------------------------------------
    // Métodos específicos de UsuarioDao
    // -------------------------------------------------------------------------

    @Override
    public AdministradorDto buscarPorCorreo(String correo) {
        this.administrador = null;
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT a.ADMINISTRADOR_ID, "
                            + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                            + "FROM administradores a JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID "
                            + "WHERE u.CORREO = ?"
            );
            this.preparedStatement.setString(1, correo);
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                this.administrador = mapearAdministrador();
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorCorreo (administrador): " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return this.administrador;
    }

    @Override
    public AdministradorDto obtenerPorDNI(String dni) {
        this.administrador = null;
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT a.ADMINISTRADOR_ID, "
                            + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                            + "FROM administradores a JOIN usuarios u ON a.USUARIO_ID = u.USUARIO_ID "
                            + "WHERE u.DNI = ?"
            );
            this.preparedStatement.setString(1, dni);
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                this.administrador = mapearAdministrador();
            }
        } catch (SQLException ex) {
            System.err.println("Error en obtenerPorDNI (administrador): " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return this.administrador;
    }

    @Override
    public Boolean existeUsuarioEnBD(AdministradorDto administrador) {
        Boolean existe = false;
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT COUNT(*) FROM usuarios WHERE CORREO = ? OR DNI = ?"
            );
            this.preparedStatement.setString(1, administrador.getEmail());
            this.preparedStatement.setString(2, administrador.getDni());
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                existe = this.resultSet.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error en existeUsuarioEnBD (administrador): " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return existe;
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet — centralizado para todos los métodos SELECT
    // -------------------------------------------------------------------------

    private AdministradorDto mapearAdministrador() throws SQLException {
        AdministradorDto a = new AdministradorDto();
        a.setIdAdministrador(resultSet.getInt("ADMINISTRADOR_ID"));
        a.setIdUsuario(resultSet.getInt("USUARIO_ID"));
        a.setNombres(resultSet.getString("NOMBRES"));
        a.setApellidos(resultSet.getString("APELLIDOS"));
        a.setDni(resultSet.getString("DNI"));
        a.setTelefono(resultSet.getString("TELEFONO"));
        a.setEmail(resultSet.getString("CORREO"));
        a.setContrasena(resultSet.getString("CONTRASENA"));
        return a;
    }
}