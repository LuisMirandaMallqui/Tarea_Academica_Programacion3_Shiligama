package pe.edu.pucp.persistance.daoImpl;

import pe.edu.pucp.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para todas las implementaciones DAO del sistema.
 * Criterios de uso:
 * - PreparedStatement : para SELECT directos con parámetros (obtenerPorId, listarTodos, agregados).
 * Es más portable y directo cuando no se necesita un procedimiento almacenado.
 * - CallableStatement : para invocar procedimientos almacenados. Se declara de forma LOCAL
 * en cada método DML de la clase hija (insertar, modificar, eliminar),
 * lo que da claridad y control total sobre los parámetros OUT.
 * Las operaciones DML (insertar, modificar, eliminar) NO están en esta clase base.
 * Cada clase hija las implementa directamente con su CallableStatement y su procedimiento específico.
 * Ejemplo de insertar() en la clase hija (con parámetro OUT para recuperar el ID):
 * public Integer insertar() {
 * Integer resultado = 0;
 * try {
 * this.iniciarTransaccion();
 * CallableStatement cs = this.conexion.prepareCall("{call INSERTAR_AREA(?, ?)}");
 * cs.setString(1, this.area.getNombre());
 * cs.registerOutParameter(2, Types.INTEGER);
 * cs.execute();
 * resultado = cs.getInt(2);
 * this.area.setIdArea(resultado);
 * this.comitarTransaccion();
 * } catch (SQLException ex) {
 * System.err.println("Error al insertar - " + ex);
 * try { this.rollbackTransaccion(); } catch (SQLException ex1) { ... }
 * } finally {
 * try { this.cerrarConexion(); } catch (SQLException ex) { ... }
 * }
 * return resultado;
 * }
 */
public abstract class DaoImplBase {

    // --- ATRIBUTOS ---
    protected Connection conexion;
    // PreparedStatement: exclusivo para SELECTs directos (obtenerPorId, listarTodos, agregados)
    protected PreparedStatement preparedStatement;
    // ResultSet compartido por los métodos de consulta de la clase base
    protected ResultSet resultSet;

    // -------------------------------------------------------------------------
    // Ciclo de vida de la conexión
    // -------------------------------------------------------------------------
    protected void abrirConexion() throws SQLException {
        this.conexion = DBManager.getInstance().getConnection();
    }

    protected void cerrarConexion() throws SQLException {
        if (this.resultSet != null) {
            this.resultSet.close();
            this.resultSet = null;
        }
        if (this.preparedStatement != null) {
            this.preparedStatement.close();
            this.preparedStatement = null;
        }
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

    // -------------------------------------------------------------------------
    // Preparación y ejecución de SELECT con PreparedStatement
    // -------------------------------------------------------------------------

    protected void prepararConsulta(String sql) throws SQLException {
        System.out.println("[SQL]: " + sql);
        this.preparedStatement = this.conexion.prepareStatement(sql);
    }

    protected void ejecutarConsulta() throws SQLException {
        this.resultSet = this.preparedStatement.executeQuery();
    }

    // -------------------------------------------------------------------------
    // obtenerPorId — Como ejecutarlo para los hijos?
    // La clase hija debe sobreescribir:
    //   obtenerSQLParaObtenerPorId()         → SQL con placeholders (?)
    //   incluirParametrosParaObtenerPorId()  → setea valores en preparedStatement
    //   instanciarObjetoDelResultSet()       → mapea la fila al objeto de dominio
    //   limpiarObjetoDelResultSet()          → deja el objeto en estado vacío si no hay resultado
    // -------------------------------------------------------------------------

    public void obtenerPorId() {
        try {
            this.abrirConexion();
            this.prepararConsulta(this.obtenerSQLParaObtenerPorId());
            this.incluirParametrosParaObtenerPorId();
            this.ejecutarConsulta();
            if (this.resultSet.next()) {
                this.instanciarObjetoDelResultSet();
            } else {
                this.limpiarObjetoDelResultSet();
            }
        } catch (SQLException ex) {
            System.err.println("Error en obtenerPorId - " + ex);
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión en obtenerPorId - " + ex);
            }
        }
    }

    protected abstract String obtenerSQLParaObtenerPorId();

    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        throw new UnsupportedOperationException("Debe sobreescribir incluirParametrosParaObtenerPorId()");
    }

    protected void instanciarObjetoDelResultSet() throws SQLException {
        throw new UnsupportedOperationException("Debe sobreescribir instanciarObjetoDelResultSet()");
    }

    protected void limpiarObjetoDelResultSet() {
        throw new UnsupportedOperationException("Debe sobreescribir limpiarObjetoDelResultSet()");
    }

    // -------------------------------------------------------------------------
    // listarTodos — Guia para los hijos
    // La clase hija debe sobreescribir:
    //   obtenerSQLParaListarTodos()      → SQL (puede incluir WHERE, ORDER BY, etc.)
    //   agregarObjetoALaLista(lista)     → mapea cada fila y agrega el objeto a la lista
    //
    // Si el listado necesita parámetros (filtros), sobreescribir también:
    //   incluirParametrosParaListarTodos() → setea valores en preparedStatement
    // -------------------------------------------------------------------------

    public List listarTodos() {
        List lista = new ArrayList<>();
        try {
            this.abrirConexion();
            this.prepararConsulta(this.obtenerSQLParaListarTodos());
            this.incluirParametrosParaListarTodos();
            this.ejecutarConsulta();
            while (this.resultSet.next()) {
                this.agregarObjetoALaLista(lista);
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarTodos - " + ex);
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión en listarTodos - " + ex);
            }
        }
        return lista;
    }

    protected abstract String obtenerSQLParaListarTodos();

    // Sobreescribir si listarTodos necesita parámetros de filtro.
    protected void incluirParametrosParaListarTodos() throws SQLException {
    }

    protected void agregarObjetoALaLista(List lista) throws SQLException {
        throw new UnsupportedOperationException("Debe sobreescribir agregarObjetoALaLista()");
    }

    // -------------------------------------------------------------------------
    // Utilidad: ejecutar funciones de agregado (COUNT, SUM, AVG, MIN, MAX)
    // Usa PreparedStatement porque es un SELECT directo, no un procedimiento almacenado.
    // Útil para paginación, totales, etc.
    //
    // @param sql        : Query con placeholders (?), ej: "SELECT COUNT(*) FROM tabla WHERE estado=?"
    // @param parametros : Valores para los placeholders, o null si no hay
    // @return           : El resultado entero del agregado (0 si falla o no hay filas)
    // -------------------------------------------------------------------------

    protected Integer ejecutarAgregado(StringBuilder sql, ArrayList<Object> parametros) {
        Integer total = 0;
        try {
            this.abrirConexion();
            try (PreparedStatement ps = this.conexion.prepareStatement(sql.toString())) {
                if (parametros != null) {
                    for (int i = 0; i < parametros.size(); i++) {
                        ps.setObject(i + 1, parametros.get(i));
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getInt(1); // índice 1: universal para COUNT, SUM, AVG, etc.
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error en ejecutarAgregado: " + ex.getMessage());
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión en ejecutarAgregado - " + ex);
            }
        }
        return total;
    }
}