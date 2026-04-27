package pe.edu.pucp.persistance.dao.operaciones.Impl;

import pe.edu.pucp.model.enums.TipoDescuento;
import pe.edu.pucp.model.promocion.PromocionDto;
import pe.edu.pucp.persistance.dao.operaciones.dao.PromocionDao;
import pe.edu.pucp.persistance.daoImpl.DaoImplBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocionDaoImpl extends DaoImplBase implements PromocionDao {

    // -------------------------------------------------------------------------
    // DML con CallableStatement + parámetros nombrados
    // -------------------------------------------------------------------------

    @Override
    public int insertar(PromocionDto promocion) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_PROMOCION(?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("_promocion_id", Types.INTEGER);
            cs.setString("_nombre", promocion.getNombre());
            cs.setString("_descripcion", promocion.getDescripcion());
            cs.setString("_tipo_descuento", promocion.getTipoDescuento().toString());
            cs.setDouble("_valor_descuento", promocion.getValorDescuento());
            cs.setDate("_fecha_inicio", Date.valueOf(promocion.getFechaInicio()));
            cs.setDate("_fecha_fin", Date.valueOf(promocion.getFechaFin()));
            cs.setString("_condiciones", promocion.getCondiciones());
            cs.executeUpdate();
            promocion.setIdPromocion(cs.getInt("_promocion_id"));
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar promocion: " + ex.getMessage());
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
    public int modificar(PromocionDto promocion) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL MODIFICAR_PROMOCION(?,?,?,?,?,?,?,?)}");
            cs.setInt("_promocion_id", promocion.getIdPromocion());
            cs.setString("_nombre", promocion.getNombre());
            cs.setString("_descripcion", promocion.getDescripcion());
            cs.setString("_tipo_descuento", promocion.getTipoDescuento().toString());
            cs.setDouble("_valor_descuento", promocion.getValorDescuento());
            cs.setDate("_fecha_inicio", Date.valueOf(promocion.getFechaInicio()));
            cs.setDate("_fecha_fin", Date.valueOf(promocion.getFechaFin()));
            cs.setString("_condiciones", promocion.getCondiciones());
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al modificar promocion: " + ex.getMessage());
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ELIMINAR_PROMOCION(?)}");
            cs.setInt("_promocion_id", id);
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar promocion: " + ex.getMessage());
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
    // SELECTs — usan SPs con CallableStatement directamente
    // -------------------------------------------------------------------------

    @Override
    public PromocionDto buscarPorID(int id) {
        PromocionDto p = null;
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL BUSCAR_PROMOCION_POR_ID(?)}");
            cs.setInt("_promocion_id", id);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                p = mapearPromocion(rs);
            }
        } catch (SQLException ex) {
            System.err.println("Error en buscarPorID promocion: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return p;
    }

    @Override
    public List<PromocionDto> listarTodos() {
        List<PromocionDto> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL LISTAR_PROMOCIONES_TODAS()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearPromocion(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarTodos promociones: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public List<PromocionDto> listarVigentes() {
        List<PromocionDto> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            CallableStatement cs = this.conexion.prepareCall("{CALL LISTAR_PROMOCIONES_VIGENTES()}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                lista.add(mapearPromocion(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarVigentes promociones: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public int asociarProducto(int idPromocion, int idProducto) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL VINCULAR_PRODUCTO_PROMOCION(?,?)}");
            cs.setInt("_promocion_id", idPromocion);
            cs.setInt("_producto_id", idProducto);
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error en asociarProducto: " + ex.getMessage());
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
    public int desasociarProducto(int idPromocion, int idProducto) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL DESVINCULAR_PRODUCTO_PROMOCION(?,?)}");
            cs.setInt("_promocion_id", idPromocion);
            cs.setInt("_producto_id", idProducto);
            cs.executeUpdate();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error en desasociarProducto: " + ex.getMessage());
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
    public List<Integer> listarProductosPorPromocion(int idPromocion) {
        List<Integer> lista = new ArrayList<>();
        try {
            this.abrirConexion();
            this.preparedStatement = this.conexion.prepareStatement(
                    "SELECT PRODUCTO_ID FROM promociones_productos WHERE PROMOCION_ID = ?");
            this.preparedStatement.setInt(1, idPromocion);
            this.resultSet = this.preparedStatement.executeQuery();
            while (this.resultSet.next()) {
                lista.add(this.resultSet.getInt("PRODUCTO_ID"));
            }
        } catch (SQLException ex) {
            System.err.println("Error en listarProductosPorPromocion: " + ex.getMessage());
        } finally {
            try { this.cerrarConexion(); } catch (SQLException ex) {
                System.err.println("Error al cerrar conexion: " + ex.getMessage());
            }
        }
        return lista;
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet
    // -------------------------------------------------------------------------

    private PromocionDto mapearPromocion(ResultSet rs) throws SQLException {
        PromocionDto p = new PromocionDto();
        p.setIdPromocion(rs.getInt("id_promocion"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setTipoDescuento(TipoDescuento.valueOf(rs.getString("tipo_descuento")));
        p.setValorDescuento(rs.getDouble("valor_descuento"));
        p.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        p.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
        p.setCondiciones(rs.getString("condiciones"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }

    // -------------------------------------------------------------------------
    // Métodos abstractos de DaoImplBase (no usados, SELECTs via SPs)
    // -------------------------------------------------------------------------
    @Override
    protected String obtenerSQLParaObtenerPorId() {
        throw new UnsupportedOperationException("Este DAO usa SPs para SELECTs.");
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        throw new UnsupportedOperationException("Este DAO usa SPs para SELECTs.");
    }
}
