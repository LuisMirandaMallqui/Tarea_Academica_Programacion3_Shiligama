package pe.edu.pucp.persistance.dao.producto.Impl;

import pe.edu.pucp.persistance.dao.producto.dao.CategoriaDAO;
import pe.edu.pucp.model.producto.CategoriaDto;
import pe.edu.pucp.db.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad Categoria.
 * Utiliza procedimientos almacenados para las operaciones en BD.
 */
public class CategoriaImpl implements CategoriaDAO {
    // ================= RECURSOS JDBC =================
    private Connection con;
    private CallableStatement cs;
    private PreparedStatement pst;
    private Statement st;
    private ResultSet rs;

    /**
     * Inserta una nueva categoría.
     * @param categoria Objeto con los datos
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int insertar(CategoriaDto categoria) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call INSERTAR_CATEGORIA(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER); // ID devuelto
            cs.setString(2, categoria.getNombre());
            cs.setString(3, categoria.getDescripcion());

            // Manejo de categoría padre
            if (categoria.getCategoriaPadre() != null) {
                cs.setInt(4, categoria.getCategoriaPadre().getIdCategoria());
            } else {
                cs.setNull(4, Types.INTEGER);
            }

            cs.executeUpdate();
            categoria.setIdCategoria(cs.getInt(1));
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en insertar Categoria: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Modifica una categoría existente.
     * @param categoria Objeto con los nuevos datos
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int modificar(CategoriaDto categoria) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_CATEGORIA(?,?,?,?)}");
            cs.setInt(1, categoria.getIdCategoria());
            cs.setString(2, categoria.getNombre());
            cs.setString(3, categoria.getDescripcion());

            // Manejo de categoría padre
            if (categoria.getCategoriaPadre() != null) {
                cs.setInt(4, categoria.getCategoriaPadre().getIdCategoria());
            } else {
                cs.setNull(4, Types.INTEGER);
            }

            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en modificar Categoria: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Elimina lógicamente una categoría (activo = 0).
     * @param id ID de la categoría
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call ELIMINAR_CATEGORIA(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en eliminar Categoria: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Busca una categoría mediante su ID.
     * @param id ID de la categoría
     * @return Objeto CategoriaDto, o null si no se encuentra
     */
    @Override
    public CategoriaDto buscarPorID(int id) {
        CategoriaDto c = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call BUSCAR_CATEGORIA_POR_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                c = new CategoriaDto();
                c.setIdCategoria(rs.getInt("CATEGORIA_ID"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setEstado(rs.getBoolean("ACTIVO"));

                // Manejo de categoría padre
                int categoriaPadreId = rs.getInt("CATEGORIA_PADRE_ID");
                if (!rs.wasNull()) {
                    // Si necesitas cargar la categoría padre completa,
                    // puedes hacer una búsqueda recursiva aquí
                    CategoriaDto padre = new CategoriaDto();
                    padre.setIdCategoria(categoriaPadreId);
                    c.setCategoriaPadre(padre);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorID Categoria: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return c;
    }

    /**
     * Lista todas las categorías activas registradas en el sistema.
     * @return Lista de categorías
     */
    @Override
    public List<CategoriaDto> listarTodos() {
        List<CategoriaDto> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_CATEGORIAS()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                CategoriaDto c = new CategoriaDto();
                c.setIdCategoria(rs.getInt("CATEGORIA_ID"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setDescripcion(rs.getString("DESCRIPCION"));
                c.setEstado(rs.getBoolean("ACTIVO"));

                // Manejo de categoría padre
                int categoriaPadreId = rs.getInt("CATEGORIA_PADRE_ID");
                if (!rs.wasNull()) {
                    CategoriaDto padre = new CategoriaDto();
                    padre.setIdCategoria(categoriaPadreId);
                    c.setCategoriaPadre(padre);
                }

                lista.add(c);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarTodos Categorias: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    /**
     * Cierra todos los recursos JDBC abiertos.
     */
    private void cerrarRecursos() {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (pst != null) pst.close();
            if (st != null) st.close();
            if (con != null) con.close();
        } catch (SQLException ex) {
            System.err.println("Error al cerrar recursos: " + ex.getMessage());
        }
    }
}