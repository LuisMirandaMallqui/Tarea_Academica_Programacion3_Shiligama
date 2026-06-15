package pe.edu.pucp.persistance.dao.producto.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.producto.Categoria;
import pe.edu.pucp.persistance.dao.producto.dao.CategoriaDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaDaoImpl implements CategoriaDao {

    // SP: INSERTAR_CATEGORIA(OUT _categoria_id, IN _nombre, IN _descripcion, IN _categoria_padre_id)
    @Override
    public int insertar(Categoria categoria) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, categoria.getNombre());
        parametrosEntrada.put(3, categoria.getDescripcion());
        parametrosEntrada.put(4, categoria.getCategoriaPadre() != null
                ? categoria.getCategoriaPadre().getIdCategoria() : null);

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_CATEGORIA", parametrosEntrada, parametrosSalida);
        categoria.setIdCategoria((int) parametrosSalida.get(1));
        return categoria.getIdCategoria();
    }

    // SP: MODIFICAR_CATEGORIA(IN _categoria_id, IN _nombre, IN _descripcion, IN _categoria_padre_id)
    @Override
    public int modificar(Categoria categoria) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();

        parametrosEntrada.put(1, categoria.getIdCategoria());
        parametrosEntrada.put(2, categoria.getNombre());
        parametrosEntrada.put(3, categoria.getDescripcion());
        parametrosEntrada.put(4, categoria.getCategoriaPadre() != null
                ? categoria.getCategoriaPadre().getIdCategoria() : null);

        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_CATEGORIA", parametrosEntrada, null);
    }

    // SP: ELIMINAR_CATEGORIA(IN _categoria_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_CATEGORIA", parametrosEntrada, null);
    }

    // SP: BUSCAR_CATEGORIA_X_ID(IN _categoria_id)
    @Override
    public Categoria buscarPorId(int id) {
        Categoria categoria = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_CATEGORIA_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    categoria = mapearCategoria(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar categoria: " + ex.getMessage());
        }
        return categoria;
    }

    // SP: LISTAR_CATEGORIAS()
    @Override
    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_CATEGORIAS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearCategoria(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar categorias: " + ex.getMessage());
        }
        return lista;
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setIdCategoria(rs.getInt("CATEGORIA_ID"));
        c.setNombre(rs.getString("NOMBRE"));
        c.setDescripcion(rs.getString("DESCRIPCION"));
        c.setEstado(rs.getBoolean("ACTIVO"));

        int categoriaPadreId = rs.getInt("CATEGORIA_PADRE_ID");
        if (!rs.wasNull()) {
            Categoria padre = new Categoria();
            padre.setIdCategoria(categoriaPadreId);
            c.setCategoriaPadre(padre);
        }
        return c;
    }
}
