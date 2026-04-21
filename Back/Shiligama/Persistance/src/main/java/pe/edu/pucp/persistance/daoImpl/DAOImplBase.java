package pe.edu.pucp.persistance.daoImpl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.persistance.daoImpl.util.Columna;
import pe.edu.pucp.persistance.daoImpl.util.Tipo_Operacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract  class DAOImplBase {
    protected String nombre_tabla;
    protected ArrayList<Columna> listaColumnas;
    protected Boolean retornarLlavePrimaria;
    protected Connection conexion;
    protected PreparedStatement prepare_statement;
    protected ResultSet resultSet;

    public DAOImplBase(String nombre_tabla) { // EXPLICAR
        this.nombre_tabla = nombre_tabla;
        this.retornarLlavePrimaria = false;
        this.incluirListaDeColumnas();
    }

    private void incluirListaDeColumnas() { // EXPLICAR
        this.listaColumnas = new ArrayList<>();
        this.configurarListaDeColumnas();
    }

    protected abstract void configurarListaDeColumnas();

    protected void abrirConexion() throws SQLException {
        this.conexion = DBManager.getInstance().getConnection();
    }

    protected void cerrarConexion() throws SQLException {
        //Se cierra rs, ps, y la conexion
        if (this.resultSet != null) this.resultSet.close();
        if (this.prepare_statement != null) this.prepare_statement.close();
        if (this.conexion != null && !this.conexion.isClosed()) {
            this.conexion.close();
        }
    }

    protected void iniciarTransaccion() throws SQLException {
        this.abrirConexion();
        this.conexion.setAutoCommit(false);
    }

    protected void comitarTransaccion() throws SQLException {
        if (this.conexion != null) {
            this.conexion.commit();
        }
    }

    protected void rollbackTransaccion() throws SQLException {
        if (this.conexion != null) {
            this.conexion.rollback();
        }
    }

    protected void colocarSQLEnStatement(String sql) throws SQLException {
        // Log para debug en la consola
        System.out.println("[SQL]: " + sql);
        if (this.retornarLlavePrimaria) {
            // Usar prepareStatement con la opción de recuperar llaves generadas
            this.prepare_statement = this.conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } else {
            this.prepare_statement = this.conexion.prepareStatement(sql);
        }
    }

    protected Integer ejecutarDMLEnBD() throws SQLException {
        return this.prepare_statement.executeUpdate();
    }

    protected void ejecutarSelectEnDB() throws SQLException {
        this.resultSet = this.prepare_statement.executeQuery();
    }

    protected Integer insertar() {
        return this.ejecuta_DML(Tipo_Operacion.INSERTAR);
    }

    protected Integer modificar() {
        return this.ejecuta_DML(Tipo_Operacion.MODIFICAR);
    }

    protected Integer eliminar() {
        return this.ejecuta_DML(Tipo_Operacion.ELIMINAR);
    }

    private Integer ejecuta_DML(Tipo_Operacion tipo_operacion) {
        Integer resultado = 0;
        try {
            this.iniciarTransaccion();
            String sql = switch (tipo_operacion) {
                case Tipo_Operacion.INSERTAR -> this.generarSQLParaInsercion();
                case Tipo_Operacion.MODIFICAR -> this.generarSQLParaModificacion();
                case Tipo_Operacion.ELIMINAR -> this.generarSQLParaEliminacion();
                default -> null;
            };
            this.colocarSQLEnStatement(sql);

            switch (tipo_operacion) {
                case Tipo_Operacion.INSERTAR -> this.incluirValorDeParametrosParaInsercion();
                case Tipo_Operacion.MODIFICAR -> this.incluirValorDeParametrosParaModificacion();
                case Tipo_Operacion.ELIMINAR -> this.incluirValorDeParametrosParaEliminacion();
            }
            resultado = this.ejecutarDMLEnBD();
            if (this.retornarLlavePrimaria && tipo_operacion == Tipo_Operacion.INSERTAR) {
                resultado = this.retornarUltimoAutoGenerado();
            }

            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error DML (" + tipo_operacion + "): " + ex);
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error al hacer rollback - " + ex1);
            }
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión - " + ex);
            }
        }
        return resultado;
    }

    protected String generarSQLParaInsercion() {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(this.nombre_tabla).append("(");
        StringBuilder sql_columnas = new StringBuilder();
        StringBuilder sql_parametros = new StringBuilder();

        for (Columna columna : this.listaColumnas) {
            if (!columna.getEsAutoGenerado()) {
                if (sql_columnas.length() > 0) {
                    sql_columnas.append(", ");
                    sql_parametros.append(", ");
                }
                sql_columnas.append(columna.getNombre());
                sql_parametros.append("?");
            }
        }
        return sql.append(sql_columnas).append(") VALUES (").append(sql_parametros).append(")").toString();
    }

    protected String generarSQLParaModificacion() {
        StringBuilder sql = new StringBuilder("UPDATE ").append(this.nombre_tabla).append(" SET ");
        StringBuilder sql_columnas = new StringBuilder();
        StringBuilder sql_predicado = new StringBuilder();

        for (Columna columna : this.listaColumnas) {
            if (columna.getEsLlavePrimaria()) {
                if (sql_predicado.length() > 0) sql_predicado.append(" AND ");
                sql_predicado.append(columna.getNombre()).append("=?");
            } else {
                if (sql_columnas.length() > 0) sql_columnas.append(", ");
                sql_columnas.append(columna.getNombre()).append("=?");
            }
        }
        return sql.append(sql_columnas).append(" WHERE ").append(sql_predicado).toString();
    }

    protected String generarSQLParaEliminacion() {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(this.nombre_tabla).append(" WHERE ");
        StringBuilder sql_predicado = new StringBuilder();
        for (Columna columna : this.listaColumnas) {
            if (columna.getEsLlavePrimaria()) {
                if (sql_predicado.length() > 0) sql_predicado.append(" AND ");
                sql_predicado.append(columna.getNombre()).append("=?");
            }
        }
        return sql.append(sql_predicado).toString();
    }

    protected String generarSQLParaObtenerPorId() {
        StringBuilder sql = new StringBuilder("SELECT ");
        StringBuilder sql_columnas = new StringBuilder();
        StringBuilder sql_predicado = new StringBuilder();

        for (Columna columna : this.listaColumnas) {
            if (columna.getEsLlavePrimaria()) {
                if (sql_predicado.length() > 0) sql_predicado.append(" AND ");
                sql_predicado.append(columna.getNombre()).append("=?");
            }
            if (sql_columnas.length() > 0) sql_columnas.append(", ");
            sql_columnas.append(columna.getNombre());
        }
        return sql.append(sql_columnas).append(" FROM ").append(this.nombre_tabla)
                .append(" WHERE ").append(sql_predicado).toString();
    }

    protected String generarSQLParaListarTodos() {
        StringBuilder sql = new StringBuilder("SELECT ");
        StringBuilder sql_columnas = new StringBuilder();
        for (Columna columna : this.listaColumnas) {
            if (sql_columnas.length() > 0) sql_columnas.append(", ");
            sql_columnas.append(columna.getNombre());
        }
        return sql.append(sql_columnas).append(" FROM ").append(this.nombre_tabla).toString();
    }

    // Métodos abstractos que deben ser implementados por las clases hijas
    protected void incluirValorDeParametrosParaInsercion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    protected void incluirValorDeParametrosParaModificacion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    protected void incluirValorDeParametrosParaEliminacion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer retornarUltimoAutoGenerado() {
        Integer resultado = null;
        try {
            String sql = DBManager.getInstance().retornarSQLParaUltimoAutoGenerado();

            this.prepare_statement = this.conexion.prepareStatement(sql);
            this.resultSet = this.prepare_statement.executeQuery();

            if (this.resultSet.next()) {
                resultado = this.resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            System.err.println("Error al intentar retornarUltimoAutoGenerado - " + ex);
        }
        return resultado;
    }

    public void obtenerPorId() {
        try {
            this.abrirConexion();
            String sql = this.generarSQLParaObtenerPorId();
            this.colocarSQLEnStatement(sql);
            this.incluirValorDeParametrosParaObtenerPorId();
            this.ejecutarSelectEnDB();
            if (this.resultSet.next()) {
                this.instanciarObjetoDelResultSet();
            } else {
                this.limpiarObjetoDelResultSet();
            }
        } catch (SQLException ex) {
            System.err.println("Error al intentar obtenerPorId - " + ex);
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) { }
        }
    }

    protected void incluirValorDeParametrosParaObtenerPorId() throws SQLException {
        throw new UnsupportedOperationException("El método no ha sido sobreescrito.");
    }
    protected void instanciarObjetoDelResultSet() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    protected void limpiarObjetoDelResultSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List listarTodos() {
        return this.listarTodos(null, null, null);
    }

    public List listarTodos(String sql, Consumer incluirValorDeParametros, Object parametros) {
        List lista = new ArrayList<>();
        try {
            this.abrirConexion();
            if (sql == null) {
                sql = this.generarSQLParaListarTodos();
            }
            this.colocarSQLEnStatement(sql);
            if (incluirValorDeParametros != null) {
                incluirValorDeParametros.accept(this.prepare_statement);
            }
            this.ejecutarSelectEnDB();
            while (this.resultSet.next()) {
                agregarObjetoALaLista(lista);
            }
        } catch (SQLException ex) {
            System.err.println("Error al intentar listarTodos - " + ex);
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) { }
        }
        return lista;
    }

    protected void agregarObjetoALaLista(List lista) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ejecutarProcedimientoAlmacenado(String sql, Boolean conTransaccion) {
        this.ejecutarProcedimientoAlmacenado(sql, null, null, conTransaccion);
    }

    public void ejecutarProcedimientoAlmacenado(String sql, Consumer incluirValorDeParametros, Object parametros, Boolean conTransaccion) {
        try {
            if (conTransaccion) {
                this.iniciarTransaccion();
            } else {
                this.abrirConexion();
            }
            this.colocarSQLEnStatement(sql);
            if (incluirValorDeParametros != null) {
                incluirValorDeParametros.accept(parametros);
            }
            this.ejecutarDMLEnBD();
            if (conTransaccion) {
                this.comitarTransaccion();
            }
        } catch (SQLException ex) {
            System.err.println("Error SP: " + ex);
            try { if (conTransaccion) this.rollbackTransaccion(); } catch (SQLException ex1) {}
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {}
        }
    }

    protected int getMinId(String tabla, String col) throws SQLException {
        String sql = "SELECT MIN(" + col + ") AS id FROM " + tabla;
        try (java.sql.PreparedStatement ps = this.conexion.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int id = rs.getInt("id");
                if (!rs.wasNull()) return id;
            }
        }
        return 1;
    }

    //Sirve para ejecutar cualquier función de agregado (COUNT, SUM, AVG) que devuelva un único número entero.
    //Importante para paginación por ejemplo
    protected Integer ejecutarConteo(StringBuilder sql, ArrayList<Object> parametros) {
        Integer total = 0;
        try {
            this.abrirConexion();

            // Usamos try-with-resources para asegurar el cierre de ps y rs
            try (java.sql.PreparedStatement ps = this.conexion.prepareStatement(sql.toString())) { // para
                int i = 1;
                if (parametros != null) {
                    for (Object param : parametros) {
                        ps.setObject(i++, param);
                    }
                }
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Se hace uso del índice 1 pues es universal (AVG, COUNT, etc)
                        total = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al contar: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try { this.cerrarConexion(); } catch (SQLException e) {}
        }
        return total;
    }

//    protected int safeFkId(Integer provided, String tabla, String col) throws SQLException {
//        if (provided != null && provided > 0) return provided;
//        return getMinId(tabla, col);
//    }
}
