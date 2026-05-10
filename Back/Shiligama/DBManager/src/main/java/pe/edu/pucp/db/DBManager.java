package pe.edu.pucp.db;

import java.sql.*;
import java.util.Map;
import java.util.ResourceBundle;

public class DBManager {

    private static DBManager dbManager;

    private final String hostname;
    private final String esquema;
    private final String puerto;
    private final String usuario;
    private final String password;
    private final String url;

    private Connection conTransaccion;

    private DBManager() {
        ResourceBundle db = ResourceBundle.getBundle("datos");
        this.hostname = db.getString("db.host");
        this.esquema = db.getString("db.schema");
        this.puerto = db.getString("db.port");
        this.usuario = db.getString("db.user");
        this.password = db.getString("db.password");
        this.url = "jdbc:mysql://" + hostname + ":" + puerto + "/" + esquema;
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("No se encontro el driver de conexion.", ex);
        }
        return DriverManager.getConnection(url, usuario, password);
    }

    // -------------------------------------------------------------------------
    // Ejecutar procedimiento almacenado DML (INSERT, UPDATE, DELETE)
    // parametrosEntrada: Map<posicion, valor>
    // parametrosSalida:  Map<posicion, sqlType (Types.INTEGER, etc.)>
    //   -> despues de ejecutar, el map se actualiza con los valores de salida
    // -------------------------------------------------------------------------

    public int ejecutarProcedimiento(
            String nombreProcedimiento,
            Map<Integer, Object> parametrosEntrada,
            Map<Integer, Object> parametrosSalida) {

        int resultado = 0;
        try (
            Connection con = getConnection();
            CallableStatement cst = formarLlamadaProcedimiento(
                    con, nombreProcedimiento, parametrosEntrada, parametrosSalida)
        ) {
            if (parametrosEntrada != null) {
                registrarParametrosEntrada(cst, parametrosEntrada);
            }
            if (parametrosSalida != null) {
                registrarParametrosSalida(cst, parametrosSalida);
            }
            resultado = cst.executeUpdate();
            if (parametrosSalida != null) {
                obtenerValoresSalida(cst, parametrosSalida);
            }
        } catch (SQLException ex) {
            System.out.println("Error ejecutando procedimiento almacenado: " + ex.getMessage());
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Ejecutar procedimiento almacenado de lectura (SELECT)
    // Retorna ResultadoConsulta (AutoCloseable) con Connection + CS + RS
    // Usar con try-with-resources
    // -------------------------------------------------------------------------

    public ResultadoConsulta ejecutarProcedimientoLectura(
            String nombreProcedimiento,
            Map<Integer, Object> parametrosEntrada) {

        try {
            Connection con = getConnection();
            CallableStatement cs = formarLlamadaProcedimiento(
                    con, nombreProcedimiento, parametrosEntrada, null);
            if (parametrosEntrada != null) {
                registrarParametrosEntrada(cs, parametrosEntrada);
            }
            ResultSet rs = cs.executeQuery();
            return new ResultadoConsulta(con, cs, rs);
        } catch (SQLException ex) {
            System.out.println("Error ejecutando procedimiento de lectura: " + ex.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Transacciones
    // -------------------------------------------------------------------------

    public void iniciarTransaccion() throws SQLException {
        conTransaccion = getConnection();
        conTransaccion.setAutoCommit(false);
    }

    public void confirmarTransaccion() throws SQLException {
        if (conTransaccion != null) {
            conTransaccion.commit();
            conTransaccion.close();
            conTransaccion = null;
        }
    }

    public void cancelarTransaccion() {
        try {
            if (conTransaccion != null) {
                conTransaccion.rollback();
                conTransaccion.close();
                conTransaccion = null;
            }
        } catch (SQLException ex) {
            System.out.println("Error al cancelar transaccion: " + ex.getMessage());
        }
    }

    public int ejecutarProcedimientoTransaccion(
            String nombreProcedimiento,
            Map<Integer, Object> parametrosEntrada,
            Map<Integer, Object> parametrosSalida) {

        int resultado = 0;
        try (
            CallableStatement cst = formarLlamadaProcedimiento(
                    conTransaccion, nombreProcedimiento, parametrosEntrada, parametrosSalida)
        ) {
            if (parametrosEntrada != null) {
                registrarParametrosEntrada(cst, parametrosEntrada);
            }
            if (parametrosSalida != null) {
                registrarParametrosSalida(cst, parametrosSalida);
            }
            resultado = cst.executeUpdate();
            if (parametrosSalida != null) {
                obtenerValoresSalida(cst, parametrosSalida);
            }
        } catch (SQLException ex) {
            System.out.println("Error ejecutando procedimiento en transaccion: " + ex.getMessage());
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Metodos privados de ayuda
    // -------------------------------------------------------------------------

    public CallableStatement formarLlamadaProcedimiento(
            Connection con,
            String nombreProcedimiento,
            Map<Integer, Object> parametrosEntrada,
            Map<Integer, Object> parametrosSalida) throws SQLException {

        StringBuilder call = new StringBuilder("{call " + nombreProcedimiento + "(");
        int cantEntrada = parametrosEntrada != null ? parametrosEntrada.size() : 0;
        int cantSalida = parametrosSalida != null ? parametrosSalida.size() : 0;
        int numParams = cantEntrada + cantSalida;
        for (int i = 0; i < numParams; i++) {
            call.append("?");
            if (i < numParams - 1) call.append(",");
        }
        call.append(")}");
        return con.prepareCall(call.toString());
    }

    private void registrarParametrosEntrada(
            CallableStatement cs,
            Map<Integer, Object> parametros) throws SQLException {

        for (Map.Entry<Integer, Object> entry : parametros.entrySet()) {
            Integer key = entry.getKey();
            Object value = entry.getValue();
            switch (value) {
                case Integer entero -> cs.setInt(key, entero);
                case String cadena -> cs.setString(key, cadena);
                case Double decimal -> cs.setDouble(key, decimal);
                case Boolean booleano -> cs.setBoolean(key, booleano);
                case java.util.Date fecha -> cs.setDate(key, new java.sql.Date(fecha.getTime()));
                case Character caracter -> cs.setString(key, String.valueOf(caracter));
                case byte[] archivo -> cs.setBytes(key, archivo);
                case null -> cs.setNull(key, Types.NULL);
                default -> cs.setObject(key, value);
            }
        }
    }

    private void registrarParametrosSalida(
            CallableStatement cst,
            Map<Integer, Object> params) throws SQLException {

        for (Map.Entry<Integer, Object> entry : params.entrySet()) {
            Integer posicion = entry.getKey();
            int sqlType = (int) entry.getValue();
            cst.registerOutParameter(posicion, sqlType);
        }
    }

    private void obtenerValoresSalida(
            CallableStatement cst,
            Map<Integer, Object> parametrosSalida) throws SQLException {

        for (Map.Entry<Integer, Object> entry : parametrosSalida.entrySet()) {
            Integer posicion = entry.getKey();
            int sqlType = (int) entry.getValue();
            Object value;
            switch (sqlType) {
                case Types.INTEGER -> value = cst.getInt(posicion);
                case Types.VARCHAR -> value = cst.getString(posicion);
                case Types.DOUBLE -> value = cst.getDouble(posicion);
                case Types.BOOLEAN -> value = cst.getBoolean(posicion);
                case Types.DATE -> value = cst.getDate(posicion);
                case Types.BLOB -> value = cst.getBytes(posicion);
                default -> value = cst.getObject(posicion);
            }
            parametrosSalida.put(posicion, value);
        }
    }

    // -------------------------------------------------------------------------
    // Wrapper para lecturas — AutoCloseable para usar con try-with-resources
    // -------------------------------------------------------------------------

    public static class ResultadoConsulta implements AutoCloseable {
        private final Connection con;
        private final CallableStatement cs;
        private final ResultSet rs;

        public ResultadoConsulta(Connection con, CallableStatement cs, ResultSet rs) {
            this.con = con;
            this.cs = cs;
            this.rs = rs;
        }

        public ResultSet getRs() {
            return rs;
        }

        @Override
        public void close() {
            try { if (rs != null) rs.close(); }
            catch (SQLException ex) { System.out.println("Error cerrando ResultSet: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); }
            catch (SQLException ex) { System.out.println("Error cerrando CallableStatement: " + ex.getMessage()); }
            try { if (con != null) con.close(); }
            catch (SQLException ex) { System.out.println("Error cerrando Connection: " + ex.getMessage()); }
        }
    }
}
