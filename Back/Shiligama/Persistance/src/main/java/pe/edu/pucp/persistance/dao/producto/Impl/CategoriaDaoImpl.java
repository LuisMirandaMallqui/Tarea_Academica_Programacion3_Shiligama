package pe.edu.pucp.persistance.dao.producto.Impl;

import pe.edu.pucp.persistance.dao.producto.dao.CategoriaDao;
import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.model.producto.CategoriaDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class CategoriaDaoImpl extends DaoImplBase implements CategoriaDao {

    private CategoriaDto categoria;

    public CategoriaDaoImpl() {
        this.categoria = null;
    }

    // -------------------------------------------------------------------------
    // DML con CallableStatement + parámetros nombrados
    // -------------------------------------------------------------------------

    @Override
    public int insertar(CategoriaDto categoria) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_CATEGORIA(?,?,?,?)}");
            cs.registerOutParameter("_categoria_id", Types.INTEGER);
            cs.setString("_nombre", categoria.getNombre());
            cs.setString("_descripcion", categoria.getDescripcion());
            if (categoria.getCategoriaPadre() != null) {
                cs.setInt("_categoria_padre_id", categoria.getCategoriaPadre().getIdCategoria());
            } else {
                cs.setNull("_categoria_padre_id", Types.INTEGER);
            }
            cs.executeUpdate();
            categoria.setIdCategoria(cs.getInt("_categoria_id"));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar categoria: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(CategoriaDto categoria) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_CATEGORIA(?,?,?,?)}");
            cs.setInt("_categoria_id", categoria.getIdCategoria());
            cs.setString("_nombre", categoria.getNombre());
            cs.setString("_descripcion", categoria.getDescripcion());
            if (categoria.getCategoriaPadre() != null) {
                cs.setInt("_categoria_padre_id", categoria.getCategoriaPadre().getIdCategoria());
            } else {
                cs.setNull("_categoria_padre_id", Types.INTEGER);
            }
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar categoria: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_CATEGORIA(?)}");
            cs.setInt("_categoria_id", id);
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar categoria: " + ex.getMessage());
            try { this.rollbackTransaccion(); } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // SELECTs via DaoImplBase (PreparedStatement)
    // -------------------------------------------------------------------------

    @Override
    public CategoriaDto buscarPorID(int id) {
        this.categoria = new CategoriaDto();
        this.categoria.setIdCategoria(id);
        this.obtenerPorId();
        return this.categoria;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO "
                + "FROM categorias WHERE CATEGORIA_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.categoria.getIdCategoria());
    }

    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        this.categoria = mapearCategoria();
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.categoria = null;
    }

    @Override
    public List<CategoriaDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT CATEGORIA_ID, NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID, ACTIVO "
                + "FROM categorias WHERE ACTIVO = 1 ORDER BY NOMBRE";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        lista.add(mapearCategoria());
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet
    // -------------------------------------------------------------------------

    private CategoriaDto mapearCategoria() throws SQLException {
        CategoriaDto c = new CategoriaDto();
        c.setIdCategoria(resultSet.getInt("CATEGORIA_ID"));
        c.setNombre(resultSet.getString("NOMBRE"));
        c.setDescripcion(resultSet.getString("DESCRIPCION"));
        c.setEstado(resultSet.getBoolean("ACTIVO"));

        int categoriaPadreId = resultSet.getInt("CATEGORIA_PADRE_ID");
        if (!resultSet.wasNull()) {
            CategoriaDto padre = new CategoriaDto();
            padre.setIdCategoria(categoriaPadreId);
            c.setCategoriaPadre(padre);
        }
        return c;
    }
}
