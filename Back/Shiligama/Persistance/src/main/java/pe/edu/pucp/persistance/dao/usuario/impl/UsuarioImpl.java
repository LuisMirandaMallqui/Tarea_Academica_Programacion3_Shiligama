package pe.edu.pucp.persistance.dao.usuario.impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.producto.CategoriaDto;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.usuario.UsuarioDto;
import pe.edu.pucp.persistance.dao.usuario.dao.UsuarioDAO;
import pe.edu.pucp.persistance.daoImpl.DAOImplBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioImpl extends DAOImplBase implements UsuarioDAO {
    private UsuarioDto usuario;
    public UsuarioImpl(){
        super("personas");
        this.usuario = null;
        this.retornarLlavePrimaria = true;
    }

    @Override
    protected void configurarListaDeColumnas() {
        this.listaColumnas.add(new Columna("PERSONA_ID", true, true));
        this.listaColumnas.add(new Columna("ESTADO_PERSONA_ID_ESTADOPERSONA", false, false));
        this.listaColumnas.add(new Columna("NOMBRES", false, false));
        this.listaColumnas.add(new Columna("PRIMER_APELLIDO", false, false));
        this.listaColumnas.add(new Columna("SEGUNDO_APELLIDO", false, false));
        this.listaColumnas.add(new Columna("CODIGO", false, false));
        this.listaColumnas.add(new Columna("CORREO", false, false));
        this.listaColumnas.add(new Columna("CONTRASENA", false, false));
        this.listaColumnas.add(new Columna("ULTIMA_ACTIVIDAD", false, false));
        this.listaColumnas.add(new Columna("USUARIO_CREACION", false, false));
        this.listaColumnas.add(new Columna("USUARIO_MODIFICACION", false, false));
    }











    // ================= RECURSOS JDBC =================
    private Connection con;
    private CallableStatement cs;
    private PreparedStatement pst;
    private Statement st;
    private ResultSet rs;

    /**
     * Inserta un nuevo producto.
     * @param producto Objeto con los datos
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int insertar(ProductoDto producto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call INSERTAR_PRODUCTO(?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER); // ID devuelto
            cs.setInt(2, producto.getCategoria().getIdCategoria());
            cs.setString(3, producto.getNombre());
            cs.setString(4, producto.getDescripcion());
            cs.setDouble(5, producto.getPrecioUnitario());
            cs.setInt(6, producto.getStock());
            cs.setInt(7, producto.getStockMinimo());
            cs.setString(8, producto.getUnidadMedida());
            cs.setString(9, producto.getCodigoBarras());
            cs.setString(10, producto.getImagenUrl());
            cs.executeUpdate();
            producto.setIdProducto(cs.getInt(1));
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en insertar Producto: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Modifica un producto existente.
     * @param producto Objeto con los nuevos datos
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int modificar(ProductoDto producto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_PRODUCTO(?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, producto.getIdProducto());
            cs.setInt(2, producto.getCategoria().getIdCategoria());
            cs.setString(3, producto.getNombre());
            cs.setString(4, producto.getDescripcion());
            cs.setDouble(5, producto.getPrecioUnitario());
            cs.setInt(6, producto.getStockMinimo());
            cs.setString(7, producto.getUnidadMedida());
            cs.setString(8, producto.getCodigoBarras());
            cs.setString(9, producto.getImagenUrl());
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en modificar Producto: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Elimina lógicamente un producto (activo = 0).
     * @param id ID del producto
     * @return 1 si éxito, 0 si error
     */
    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call ELIMINAR_PRODUCTO(?)}");
            cs.setInt(1, id);
            cs.executeUpdate();
            resultado = 1;
        } catch (Exception ex) {
            System.err.println("Error en eliminar Producto: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return resultado;
    }

    /**
     * Busca un producto mediante su ID.
     * @param id ID del producto
     * @return Objeto ProductoDto, o null si no se encuentra
     */
    @Override
    public ProductoDto buscarPorID(int id) {
        ProductoDto p = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call BUSCAR_PRODUCTO_POR_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                p = new ProductoDto();
                p.setIdProducto(rs.getInt("PRODUCTO_ID"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                p.setStock(rs.getInt("STOCK"));
                p.setStockMinimo(rs.getInt("STOCK_MINIMO"));
                p.setUnidadMedida(rs.getString("UNIDAD_MEDIDA"));
                p.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                p.setImagenUrl(rs.getString("IMAGEN_URL"));
                p.setEstado(rs.getBoolean("ACTIVO"));

                // Crear objeto categoría con los datos del JOIN
                CategoriaDto categoria = new CategoriaDto();
                categoria.setIdCategoria(rs.getInt("CATEGORIA_ID"));
                categoria.setNombre(rs.getString("CATEGORIA_NOMBRE"));
                p.setCategoria(categoria);

                // Manejo de fecha si existe en el resultado
                Timestamp fechaReg = rs.getTimestamp("FECHA_REGISTRO");
                if (fechaReg != null) {
                    p.setFechaRegistro(fechaReg.toLocalDateTime());
                }
            }
        } catch (Exception ex) {
            System.err.println("Error en buscarPorID Producto: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            cerrarRecursos();
        }
        return p;
    }

    /**
     * Lista todos los productos activos registrados en el sistema.
     * @return Lista de productos
     */
    @Override
    public List<ProductoDto> listarTodos() {
        List<ProductoDto> lista = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_PRODUCTOS()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                ProductoDto p = new ProductoDto();
                p.setIdProducto(rs.getInt("PRODUCTO_ID"));
                p.setNombre(rs.getString("NOMBRE"));
                p.setDescripcion(rs.getString("DESCRIPCION"));
                p.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                p.setStock(rs.getInt("STOCK"));
                p.setStockMinimo(rs.getInt("STOCK_MINIMO"));
                p.setUnidadMedida(rs.getString("UNIDAD_MEDIDA"));
                p.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                p.setImagenUrl(rs.getString("IMAGEN_URL"));
                p.setEstado(rs.getBoolean("ACTIVO"));

                // Crear objeto categoría con los datos del JOIN
                CategoriaDto categoria = new CategoriaDto();
                categoria.setIdCategoria(rs.getInt("CATEGORIA_ID"));
                categoria.setNombre(rs.getString("CATEGORIA_NOMBRE"));
                p.setCategoria(categoria);

                // Manejo de fecha si existe en el resultado
                Timestamp fechaReg = rs.getTimestamp("FECHA_REGISTRO");
                if (fechaReg != null) {
                    p.setFechaRegistro(fechaReg.toLocalDateTime());
                }

                lista.add(p);
            }
        } catch (Exception ex) {
            System.err.println("Error en listarTodos Productos: " + ex.getMessage());
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
