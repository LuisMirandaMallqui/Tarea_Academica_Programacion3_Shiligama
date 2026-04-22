package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.model.usuario.TrabajadorDto;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class TrabajadorDaoImpl extends DaoImplBase implements UsuarioDao<TrabajadorDto> {

    private TrabajadorDto trabajador;

    public TrabajadorDaoImpl() {
        this.trabajador = null;
    }
    public TrabajadorDaoImpl(TrabajadorDto trabajador) {
        this.trabajador = trabajador;
    }

    // -------------------------------------------------------------------------
    // DML — el SP maneja el INSERT en usuarios + trabajadores en una sola operación
    // -------------------------------------------------------------------------

    @Override
    public int insertar(TrabajadorDto trabajador) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_TRABAJADOR(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, trabajador.getNombres());
            cs.setString(3, trabajador.getApellidos());
            cs.setString(4, trabajador.getDni());
            cs.setString(5, trabajador.getTelefono());
            cs.setString(6, trabajador.getEmail());
            cs.setString(7, trabajador.getContrasena());
            cs.setString(8, null); // CARGO — no está en TrabajadorDto aún, se pasa null por ahora
            cs.setDate(9, Date.valueOf(trabajador.getFechaIngreso()));
            cs.execute();
            trabajador.setIdTrabajador(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar trabajador: " + ex.getMessage());
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
    public int modificar(TrabajadorDto trabajador) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_TRABAJADOR(?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, trabajador.getIdTrabajador());
            cs.setString(2, trabajador.getNombres());
            cs.setString(3, trabajador.getApellidos());
            cs.setString(4, trabajador.getDni());
            cs.setString(5, trabajador.getTelefono());
            cs.setString(6, trabajador.getEmail());
            cs.setDate(7, Date.valueOf(trabajador.getFechaIngreso()));
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar trabajador: " + ex.getMessage());
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_TRABAJADOR(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar trabajador: " + ex.getMessage());
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
    // Select a través de PrepareStatement
    // -------------------------------------------------------------------------

    public TrabajadorDto buscarPorID(int id) {
        this.trabajador = new TrabajadorDto();
        this.trabajador.setIdTrabajador(id);
        this.obtenerPorId();
        return this.trabajador;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT t.TRABAJADOR_ID, t.CARGO, t.FECHA_INGRESO, "
                + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                + "FROM trabajadores t JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID "
                + "WHERE t.TRABAJADOR_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.trabajador.getIdTrabajador());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.trabajador = mapearTrabajador();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.trabajador = null;
    }

    @Override
    public List<TrabajadorDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT t.TRABAJADOR_ID, t.CARGO, t.FECHA_INGRESO, "
                + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                + "FROM trabajadores t JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID "
                + "WHERE t.ACTIVO = 1 "
                + "ORDER BY u.APELLIDOS, u.NOMBRES";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearTrabajador());
    }

    // -------------------------------------------------------------------------
    // Métodos específicos de UsuarioDao
    // -------------------------------------------------------------------------

    @Override
    public TrabajadorDto buscarPorCorreo(String correo) {
        this.trabajador = null;
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT t.TRABAJADOR_ID, t.CARGO, t.FECHA_INGRESO, "
                            + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                            + "FROM trabajadores t JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID "
                            + "WHERE u.CORREO = ?"
            );
            this.preparedStatement.setString(1, correo);
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                this.trabajador = mapearTrabajador();
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorCorreo (trabajador): " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return this.trabajador;
    }

    @Override
    public TrabajadorDto obtenerPorDNI(String dni) {
        this.trabajador = null;
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT t.TRABAJADOR_ID, t.CARGO, t.FECHA_INGRESO, "
                            + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                            + "FROM trabajadores t JOIN usuarios u ON t.USUARIO_ID = u.USUARIO_ID "
                            + "WHERE u.DNI = ?"
            );
            this.preparedStatement.setString(1, dni);
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                this.trabajador = mapearTrabajador();
            }
        } catch (SQLException ex) {
            System.err.println("Error en obtenerPorDNI (trabajador): " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return this.trabajador;
    }

    @Override
    public Boolean existeUsuarioEnBD(TrabajadorDto trabajador) {
        Boolean existe = false;
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT COUNT(*) FROM usuarios WHERE CORREO = ? OR DNI = ?"
            );
            this.preparedStatement.setString(1, trabajador.getEmail());
            this.preparedStatement.setString(2, trabajador.getDni());
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                existe = this.resultSet.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error en existeUsuarioEnBD (trabajador): " + ex.getMessage());
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

    private TrabajadorDto mapearTrabajador() throws SQLException {
        TrabajadorDto t = new TrabajadorDto();
        t.setIdTrabajador(resultSet.getInt("TRABAJADOR_ID"));
        Date fechaIngreso = resultSet.getDate("FECHA_INGRESO");
        if (fechaIngreso != null) {
            t.setFechaIngreso(fechaIngreso.toLocalDate());
        }
        t.setIdUsuario(resultSet.getInt("USUARIO_ID"));
        t.setNombres(resultSet.getString("NOMBRES"));
        t.setApellidos(resultSet.getString("APELLIDOS"));
        t.setDni(resultSet.getString("DNI"));
        t.setTelefono(resultSet.getString("TELEFONO"));
        t.setEmail(resultSet.getString("CORREO"));
        t.setContrasena(resultSet.getString("CONTRASENA"));
        return t;
    }
}