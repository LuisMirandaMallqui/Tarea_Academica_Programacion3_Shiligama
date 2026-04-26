package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDao;
import pe.edu.pucp.model.usuario.ClienteDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class ClienteDaoImpl extends DaoImplBase implements UsuarioDao<ClienteDto> {

    private ClienteDto cliente;

    public ClienteDaoImpl() {
        this.cliente = null;
    }
    public ClienteDaoImpl(ClienteDto cliente) {
        this.cliente = cliente;
    }

    // -------------------------------------------------------------------------
    // Metodos CRUD importantes,  INSERT en usuarios + clientes en una sola operación
    // -------------------------------------------------------------------------

    @Override
    public int insertar(ClienteDto cliente) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_CLIENTE(?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, cliente.getNombres());
            cs.setString(3, cliente.getApellidos());
            cs.setString(4, cliente.getDni());
            cs.setString(5, cliente.getTelefono());
            cs.setString(6, cliente.getEmail());
            cs.setString(7, cliente.getContrasena());
            cs.setString(8, cliente.getDireccionEntrega());
            cs.execute();
            cliente.setIdCliente(cs.getInt(1));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar cliente: " + ex.getMessage());
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
    public int modificar(ClienteDto cliente) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_CLIENTE(?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, cliente.getIdCliente());
            cs.setString(2, cliente.getNombres());
            cs.setString(3, cliente.getApellidos());
            cs.setString(4, cliente.getDni());
            cs.setString(5, cliente.getTelefono());
            cs.setString(6, cliente.getEmail());
            cs.setString(7, cliente.getDireccionEntrega());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar cliente: " + ex.getMessage());
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_CLIENTE(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar cliente: " + ex.getMessage());
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

    public ClienteDto buscarPorID(int id) {
        this.cliente = new ClienteDto();
        this.cliente.setIdCliente(id);
        this.obtenerPorId();
        return this.cliente;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA, "
                + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                + "FROM clientes c JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID "
                + "WHERE c.CLIENTE_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.cliente.getIdCliente());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.cliente = mapearCliente();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.cliente = null;
    }

    @Override
    public List<ClienteDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA, "
                + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                + "FROM clientes c JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID "
                + "WHERE u.ACTIVO = 1 "
                + "ORDER BY u.APELLIDOS, u.NOMBRES";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearCliente());
    }

    // -------------------------------------------------------------------------
    // Métodos específicos de UsuarioDao
    // -------------------------------------------------------------------------

    @Override
    public ClienteDto buscarPorCorreo(String correo) {
        this.cliente = new ClienteDto();
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA, "
                            + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                            + "FROM clientes c JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID "
                            + "WHERE u.CORREO = ?"
            );
            this.preparedStatement.setString(1, correo);
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                this.cliente = mapearCliente();
            } else {
                this.cliente = null;
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorCorreo (cliente): " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return this.cliente;
    }

    @Override
    public ClienteDto obtenerPorDNI(String dni) {
        this.cliente = new ClienteDto();
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT c.CLIENTE_ID, c.DIRECCION_ENTREGA, "
                            + "u.USUARIO_ID, u.NOMBRES, u.APELLIDOS, u.DNI, u.TELEFONO, u.CORREO, u.CONTRASENA "
                            + "FROM clientes c JOIN usuarios u ON c.USUARIO_ID = u.USUARIO_ID "
                            + "WHERE u.DNI = ?"
            );
            this.preparedStatement.setString(1, dni);
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                this.cliente = mapearCliente();
            } else {
                this.cliente = null;
            }
        } catch (SQLException ex) {
            System.err.println("Error en obtenerPorDNI (cliente): " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return this.cliente;
    }

    @Override
    public Boolean existeUsuarioEnBD(ClienteDto cliente) {
        Boolean existe = false;
        try {
            this.abrirConexion();
            this.prepararConsulta(
                    "SELECT COUNT(*) FROM usuarios WHERE CORREO = ? OR DNI = ?"
            );
            this.preparedStatement.setString(1, cliente.getEmail());
            this.preparedStatement.setString(2, cliente.getDni());
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                existe = this.resultSet.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error en existeUsuarioEnBD (cliente): " + ex.getMessage());
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

    private ClienteDto mapearCliente() throws SQLException {
        ClienteDto c = new ClienteDto();
        c.setIdCliente(resultSet.getInt("CLIENTE_ID"));
        c.setDireccionEntrega(resultSet.getString("DIRECCION_ENTREGA"));
        c.setIdUsuario(resultSet.getInt("USUARIO_ID"));
        c.setNombres(resultSet.getString("NOMBRES"));
        c.setApellidos(resultSet.getString("APELLIDOS"));
        c.setDni(resultSet.getString("DNI"));
        c.setTelefono(resultSet.getString("TELEFONO"));
        c.setEmail(resultSet.getString("CORREO"));
        c.setContrasena(resultSet.getString("CONTRASENA"));
        return c;
    }
}