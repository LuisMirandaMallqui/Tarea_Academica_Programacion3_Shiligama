package pe.edu.pucp.persistance.dao.operaciones.Impl;

import pe.edu.pucp.persistance.dao.operaciones.dao.PromocionDAO;
import pe.edu.pucp.model.operaciones.Promocion;
import pe.edu.pucp.db.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para la entidad Promocion.
 * Utiliza procedimientos almacenados para todas las operaciones en base de datos.
 */
public class PromocionImpl implements PromocionDAO {
    // ================= RECURSOS JDBC =================
    private Connection con;
    private CallableStatement cs;
    private PreparedStatement pst;
    private Statement st;
    private ResultSet rs;

    /**
     * Inserta una nueva promoción usando el SP INSERTAR_PROMOCION
     * @param promocion Objeto con los datos de la promoción
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int insertar(Promocion promocion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call INSERTAR_PROMOCION(?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);  // ID generado
            cs.setString(2, promocion.getNombre());
            cs.setString(3, promocion.getDescripcion());
            cs.setString(4, promocion.getTipoDescuento());
            cs.setDouble(5, promocion.getValorDescuento());
            cs.setDate(6, Date.valueOf(promocion.getFechaInicio()));
            cs.setDate(7, Date.valueOf(promocion.getFechaFin()));
            cs.setString(8, promocion.getCondiciones());
            cs.executeUpdate();
            promocion.setIdPromocion(cs.getInt(1));  // Asignar ID generado
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en insertar Promocion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Modifica una promoción en base de datos.
     * @param promocion Objeto con los nuevos datos
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int modificar(Promocion promocion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_PROMOCION(?,?,?,?,?,?,?,?)}");
            cs.setInt(1, promocion.getIdPromocion());
            cs.setString(2, promocion.getNombre());
            cs.setString(3, promocion.getDescripcion());
            cs.setString(4, promocion.getTipoDescuento());
            cs.setDouble(5, promocion.getValorDescuento());
            cs.setDate(6, Date.valueOf(promocion.getFechaInicio()));
            cs.setDate(7, Date.valueOf(promocion.getFechaFin()));
            cs.setString(8, promocion.getCondiciones());
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en modificar Promocion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Elimina lógicamente una promoción (activo = 0).
     * @param id ID de la promoción
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call ELIMINAR_PROMOCION(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en eliminar Promocion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Busca una promoción mediante su ID.
     * @param id ID de la promoción
     * @return Objeto Promocion, o null si no se encuentra
     */
    @Override
    public Promocion buscarPorID(int id) {
        Promocion p = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call BUSCAR_PROMOCION_POR_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                p = new Promocion();
                p.setIdPromocion(rs.getInt("id_promocion"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setTipoDescuento(rs.getString("tipo_descuento"));
                p.setValorDescuento(rs.getDouble("valor_descuento"));
                p.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                p.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                p.setCondiciones(rs.getString("condiciones"));
                p.setActivo(rs.getBoolean("activo"));
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorID Promocion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return p;
    }

    /**
     * Lista todas las promociones registradas en el sistema.
     * @return Lista de promociones
     */
    @Override
    public List<Promocion> listarTodos() {
        List<Promocion> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_PROMOCIONES_TODAS()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                Promocion p = new Promocion();
                p.setIdPromocion(rs.getInt("id_promocion"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setTipoDescuento(rs.getString("tipo_descuento"));
                p.setValorDescuento(rs.getDouble("valor_descuento"));
                p.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                p.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                p.setCondiciones(rs.getString("condiciones"));
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarTodos Promociones: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    /**
     * Lista las promociones vigentes.
     */
    @Override
    public List<Promocion> listarVigentes() {
        List<Promocion> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_PROMOCIONES_VIGENTES()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                Promocion p = new Promocion();
                p.setIdPromocion(rs.getInt("id_promocion"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setTipoDescuento(rs.getString("tipo_descuento"));
                p.setValorDescuento(rs.getDouble("valor_descuento"));
                p.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                p.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                p.setCondiciones(rs.getString("condiciones"));
                p.setActivo(rs.getBoolean("activo"));
                lista.add(p);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarVigentes Promociones: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    /**
     * Asocia un producto a una promoción.
     */
    @Override
    public int asociarProducto(int idPromocion, int idProducto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call VINCULAR_PRODUCTO_PROMOCION(?,?)}");
            cs.setInt(1, idPromocion);
            cs.setInt(2, idProducto);
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en asociarProducto: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Desasocia un producto de una promoción.
     */
    @Override
    public int desasociarProducto(int idPromocion, int idProducto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call DESVINCULAR_PRODUCTO_PROMOCION(?,?)}");
            cs.setInt(1, idPromocion);
            cs.setInt(2, idProducto);
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en desasociarProducto: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Lista los IDs de productos asociados a una promoción.
     */
    @Override
    public List<Integer> listarProductosPorPromocion(int idPromocion) {
        List<Integer> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "SELECT id_producto FROM promocion_producto WHERE id_promocion = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, idPromocion);
            rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt("id_producto"));
            }
        } catch (Exception ex) {
            System.err.println("Error en listarProductosPorPromocion: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    /**
     * Cierra todos los recursos JDBC utilizados.
     * IMPORTANTE: Siempre llamar en el bloque finally.
     */
    private void cerrarRecursos() {
        try { if (cs != null) cs.close(); } catch (Exception ex) {}
        try { if (pst != null) pst.close(); } catch (Exception ex) {}
        try { if (st != null) st.close(); } catch (Exception ex) {}
        try { if (rs != null) rs.close(); } catch (Exception ex) {}
        try { if (con != null) con.close(); } catch (Exception ex) {}
    }
}
