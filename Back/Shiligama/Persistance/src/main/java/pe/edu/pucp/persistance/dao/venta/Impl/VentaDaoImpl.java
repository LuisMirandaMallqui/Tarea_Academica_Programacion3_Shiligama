package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.model.venta.DetalleVentaDto;
import pe.edu.pucp.persistance.daoImpl.DaoImplBase;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDao;
import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoVenta;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.model.venta.BoletaDto;
import pe.edu.pucp.model.venta.MetodoPagoDto;
import pe.edu.pucp.model.venta.VentaDto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class VentaDaoImpl extends DaoImplBase implements VentaDao {
    private VentaDto venta;

    public VentaDaoImpl() {
        this.venta = null;
    }

    public VentaDaoImpl(VentaDto venta) {
        this.venta = venta;
    }

    // -------------------------------------------------------------------------
    // Metodos CRUD importantes
    // -------------------------------------------------------------------------

    @Override
    public int insertar(VentaDto venta) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL INSERTAR_VENTA(?, ?, ?, ?, ?, ?)}");
            cs.registerOutParameter("_venta_id", Types.INTEGER);
            cs.setInt("_cliente_id", venta.getCliente().getIdCliente());
            cs.setInt("_trabajador_id", venta.getTrabajador().getIdTrabajador());
            cs.setInt("_metodo_pago_id", venta.getMetodoPago().getIdMetodoPago());
            cs.setString("_canal_venta", venta.getCanalVenta().name());
            cs.setString("_observaciones", venta.getObservaciones());
            cs.execute();
            venta.setIdVenta(cs.getInt(1));
            resultado = 1;

            /*
            CREATE PROCEDURE INSERTAR_DETALLE_VENTA(
                OUT _detalle_venta_id INT,
                IN  _venta_id         INT,
                IN  _producto_id      INT,
                IN  _cantidad         INT
                //FALTA ACTUALIZAR :"v
            )

                -- Primary Key
                `DETALLE_VENTA_ID` INT            NOT NULL AUTO_INCREMENT, OK
                -- Atributos
                `VENTA_ID`         INT            NOT NULL, OK
                `PRODUCTO_ID`      INT            NOT NULL, OK
                `DESCRIPCION` VARCHAR(100) NOT NULL COMMENT 'Descripción de la línea de detalle.',
                `PRECIO_UNITARIO`  DECIMAL(10,2)  NOT NULL,
                `CANTIDAD`         INT            NOT NULL,
                `SUBTOTAL`         DECIMAL(10,2)  NOT NULL,
             */
            if (venta.getDetalles() != null) {
                for (DetalleVentaDto detalleVenta : venta.getDetalles()) {
                    cs = this.conexion.prepareCall("{CALL INSERTAR_DETALLE_VENTA(?, ?, ?, ?,?,?)}");
                    cs.registerOutParameter("_detalle_venta_id", Types.INTEGER);
                    cs.setInt("_venta_id", venta.getIdVenta());
                    cs.setInt("_producto_id", detalleVenta.getProducto().getIdProducto());
                    cs.setString("_descripcion", detalleVenta.getDescripcion());
                    cs.setDouble("_precio_unitario", detalleVenta.getPrecioUnitario());
                    cs.setInt("_cantidad", detalleVenta.getCantidad());
                    cs.setDouble("_subtotal", detalleVenta.getSubtotal());
                    cs.executeUpdate();
                }
            }
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al insertar venta: " + ex.getMessage());
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(VentaDto venta) {
        int resultado = 0;
        try {
            this.iniciarTransaccion();
            CallableStatement cs = this.conexion.prepareCall("{CALL COMPLETAR_VENTA(?)}");
            cs.setInt(1, venta.getIdVenta());
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al completar venta: " + ex.getMessage());
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
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
            CallableStatement cs = this.conexion.prepareCall("{CALL ANULAR_VENTA(?)}");
            cs.setInt(1, id);
            cs.execute();
            resultado = 1;
            this.comitarTransaccion();
        } catch (SQLException ex) {
            System.err.println("Error al anular venta: " + ex.getMessage());
            try {
                this.rollbackTransaccion();
            } catch (SQLException ex1) {
                System.err.println("Error en rollback: " + ex1.getMessage());
            }
        } finally {
            try {
                this.cerrarConexion();
            } catch (SQLException ex) {
                System.err.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Para operaciones SELECT se hace uso de PreparedStatement
    // -------------------------------------------------------------------------

    public VentaDto buscarPorID(int id) {
        this.venta = new VentaDto();
        this.venta.setIdVenta(id);
        this.obtenerPorId();
        return this.venta;
    }

    @Override
    protected String obtenerSQLParaObtenerPorId() {
        return "SELECT v.VENTA_ID, v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO, "
                + "v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES, "
                + "v.CLIENTE_ID, v.TRABAJADOR_ID, "
                + "v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA, "
                + "mp.METODO_PAGO_ID, mp.NOMBRE AS METODO_PAGO_NOMBRE "
                + "FROM ventas v JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID "
                + "WHERE v.VENTA_ID = ?";
    }

    @Override
    protected void incluirParametrosParaObtenerPorId() throws SQLException {
        this.preparedStatement.setInt(1, this.venta.getIdVenta());
    }

    // Si el registro tiene NUMERO_BOLETA, se instancia BoletaDto.
    // Si no, es una venta simple y se instancia VentaDto.
    @Override
    protected void instanciarObjetoDelResultSet() throws SQLException {
        String numeroBoleta = resultSet.getString("NUMERO_BOLETA");
        if (numeroBoleta != null && !numeroBoleta.isEmpty()) {
            this.venta = mapearBoleta(numeroBoleta);
        } else {
            this.venta = mapearVenta();
        }
    }

    @Override
    protected void limpiarObjetoDelResultSet() {
        this.venta = null;
    }

    @Override
    public List<VentaDto> listarTodos() {
        return super.listarTodos();
    }

    @Override
    protected String obtenerSQLParaListarTodos() {
        return "SELECT v.VENTA_ID, v.FECHA_HORA, v.MONTO_TOTAL, v.MONTO_DESCUENTO, "
                + "v.CANAL_VENTA, v.ESTADO_VENTA, v.OBSERVACIONES, "
                + "v.CLIENTE_ID, v.TRABAJADOR_ID, "
                + "v.NUMERO_BOLETA, v.RUC_EMPRESA, v.CONTACTO_CLIENTE, v.MENSAJE_BOLETA, "
                + "mp.METODO_PAGO_ID, mp.NOMBRE AS METODO_PAGO_NOMBRE "
                + "FROM ventas v JOIN metodos_pago mp ON v.METODO_PAGO_ID = mp.METODO_PAGO_ID "
                + "ORDER BY v.FECHA_HORA DESC";
    }

    @Override
    protected void agregarObjetoALaLista(List lista) throws SQLException {
        String numeroBoleta = resultSet.getString("NUMERO_BOLETA");
        if (numeroBoleta != null && !numeroBoleta.isEmpty()) {
            lista.add(mapearBoleta(numeroBoleta));
        } else {
            lista.add(mapearVenta());
        }
    }

    // -------------------------------------------------------------------------
    // Mapeo del ResultSet
    // -------------------------------------------------------------------------

    // Campos comunes a toda venta, reutilizados por ambos métodos de mapeo
    private VentaDto mapearVenta() throws SQLException {
        VentaDto v = new VentaDto();
        completarCamposVenta(v);
        return v;
    }

    // Cuando hay boleta, se mapean primero los campos de venta y luego los específicos de boleta
    private BoletaDto mapearBoleta(String numeroBoleta) throws SQLException {
        BoletaDto boleta = new BoletaDto();
        completarCamposVenta(boleta);
        boleta.setNumeroBoleta(numeroBoleta);
        boleta.setRuc(resultSet.getString("RUC_EMPRESA"));
        boleta.setContactoCliente(resultSet.getString("CONTACTO_CLIENTE"));
        boleta.setMensajeBoleta(resultSet.getString("MENSAJE_BOLETA"));
        return boleta;
    }

    // Extrae los campos comunes de venta del ResultSet hacia cualquier VentaDto (o subclase)
    private void completarCamposVenta(VentaDto v) throws SQLException {
        v.setIdVenta(resultSet.getInt("VENTA_ID"));
        v.setFechaHora(resultSet.getTimestamp("FECHA_HORA").toLocalDateTime());
        v.setMontoTotal(resultSet.getDouble("MONTO_TOTAL"));
        v.setMontoDescuento(resultSet.getDouble("MONTO_DESCUENTO"));
        v.setCanalVenta(CanalVenta.valueOf(resultSet.getString("CANAL_VENTA")));
        v.setEstadoVenta(EstadoVenta.valueOf(resultSet.getString("ESTADO_VENTA")));
        v.setObservaciones(resultSet.getString("OBSERVACIONES"));

        ClienteDto cliente = new ClienteDto();
        cliente.setIdCliente(resultSet.getInt("CLIENTE_ID"));
        v.setCliente(cliente);

        TrabajadorDto trabajador = new TrabajadorDto();
        trabajador.setIdTrabajador(resultSet.getInt("TRABAJADOR_ID"));
        v.setTrabajador(trabajador);

        MetodoPagoDto metodoPago = new MetodoPagoDto();
        metodoPago.setIdMetodoPago(resultSet.getInt("METODO_PAGO_ID"));
        metodoPago.setNombre(resultSet.getString("METODO_PAGO_NOMBRE"));
        v.setMetodoPago(metodoPago);
    }
}