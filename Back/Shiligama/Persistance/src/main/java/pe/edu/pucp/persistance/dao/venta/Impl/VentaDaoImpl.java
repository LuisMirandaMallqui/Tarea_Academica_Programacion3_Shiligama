package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.model.venta.Boleta;
import pe.edu.pucp.model.venta.DetalleVenta;
import pe.edu.pucp.model.venta.MetodoPago;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.persistance.dao.usuario.impl.ClienteDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDao;
import pe.edu.pucp.model.venta.TopProductoDto;
import pe.edu.pucp.model.venta.VentaReporteDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VentaDaoImpl implements VentaDao {

    @Override
    public int insertar(Venta venta) {
        int resultado = 0;
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.iniciarTransaccion();

            // Insertar cabecera de venta
            Map<Integer, Object> paramsEntrada = new HashMap<>();
            Map<Integer, Object> paramsSalida = new HashMap<>();

            paramsSalida.put(1, Types.INTEGER);
            if (venta.getCliente() != null && venta.getCliente().getIdUsuario() > 0) {
                paramsEntrada.put(2, venta.getCliente().getIdUsuario());
            } else {
                paramsEntrada.put(2, null);
            }
            paramsEntrada.put(3, venta.getTrabajador().getIdUsuario());
            paramsEntrada.put(4, venta.getMetodoPago().getIdMetodoPago());
            paramsEntrada.put(5, venta.getCanalVenta().name());
            paramsEntrada.put(6, venta.getObservaciones());

            dbManager.ejecutarProcedimientoTransaccion(
                    "INSERTAR_VENTA", paramsEntrada, paramsSalida);
            venta.setIdVenta((int) paramsSalida.get(1));

            // Insertar detalles
            if (venta.getDetalles() != null) {
                for (DetalleVenta detalle : venta.getDetalles()) {
                    Map<Integer, Object> paramsDetEntrada = new HashMap<>();
                    Map<Integer, Object> paramsDetSalida = new HashMap<>();

                    paramsDetSalida.put(1, Types.INTEGER);
                    paramsDetEntrada.put(2, venta.getIdVenta());
                    paramsDetEntrada.put(3, detalle.getProducto().getIdProducto());
                    paramsDetEntrada.put(4, detalle.getCantidad());

                    dbManager.ejecutarProcedimientoTransaccion(
                            "INSERTAR_DETALLE_VENTA", paramsDetEntrada, paramsDetSalida);
                    detalle.setIdDetalleVenta((int) paramsDetSalida.get(1));
                }
            }

            dbManager.confirmarTransaccion();
            resultado = venta.getIdVenta();
        } catch (SQLException ex) {
            System.out.println("Error al insertar venta: " + ex.getMessage());
            dbManager.cancelarTransaccion();
        }
        return resultado;
    }

    @Override
    public int modificar(Venta venta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, venta.getIdVenta());
        return DBManager.getInstance().ejecutarProcedimiento(
                "COMPLETAR_VENTA", parametrosEntrada, null);
    }

    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ANULAR_VENTA", parametrosEntrada, null);
    }

    @Override
    public Venta buscarPorId(int id) {
        Venta venta = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_VENTA_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    venta = mapearVenta(rs);
                    venta.setDetalles(listarDetallesPorVenta(venta.getIdVenta()));
                    BoletaDaoImpl boletaDao = new BoletaDaoImpl();
                    venta.setBoleta(boletaDao.buscarPorVentaId(venta.getIdVenta()));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar venta: " + ex.getMessage());
        }
        return venta;
    }

    @Override
    public List<Venta> listarTodos() {
        List<Venta> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_VENTAS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                BoletaDaoImpl boletaDao = new BoletaDaoImpl();
                while (rs.next()) {
                    Venta v = mapearVenta(rs);
                    v.setBoleta(boletaDao.buscarPorVentaId(v.getIdVenta()));
                    lista.add(v);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar ventas: " + ex.getMessage());
        }
        return lista;
    }

    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta v = new Venta();
        completarCamposVenta(rs, v);
        return v;
    }

    private void completarCamposVenta(ResultSet rs, Venta v) throws SQLException {
        v.setIdVenta(rs.getInt("VENTA_ID"));
        v.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
        v.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
        v.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
        v.setCanalVenta(CanalVenta.valueOf(rs.getString("CANAL_VENTA")));
        v.setEstado(rs.getString("ESTADO_VENTA"));
        v.setObservaciones(rs.getString("OBSERVACIONES"));

        int clienteId = rs.getInt("CLIENTE_ID");
        if (clienteId > 0) {
            ClienteDaoImpl clienteDao = new ClienteDaoImpl();
            Cliente cliente = clienteDao.buscarPorId(clienteId);
            if (cliente != null) {
                v.setCliente(cliente);
            } else {
                Cliente clienteMinimo = new Cliente();
                clienteMinimo.setIdUsuario(clienteId);
                v.setCliente(clienteMinimo);
            }
        }

        Trabajador trabajador = new Trabajador();
        trabajador.setIdUsuario(rs.getInt("TRABAJADOR_ID"));
        v.setTrabajador(trabajador);

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
        metodoPago.setNombre(rs.getString("METODO_PAGO_NOMBRE"));
        v.setMetodoPago(metodoPago);
    }

    @Override
    public List<Venta> listarPorFechas(java.time.LocalDateTime fechaInicio, java.time.LocalDateTime fechaFin) {
        List<Venta> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, Timestamp.valueOf(fechaInicio));
        parametrosEntrada.put(2, Timestamp.valueOf(fechaFin));

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_VENTAS_X_FECHAS", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                BoletaDaoImpl boletaDao = new BoletaDaoImpl();
                while (rs.next()) {
                    Venta v = mapearVenta(rs);
                    v.setBoleta(boletaDao.buscarPorVentaId(v.getIdVenta()));
                    lista.add(v);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en listarPorFechas (ventas): " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<Venta> listarPorTrabajador(int idTrabajador) {
        List<Venta> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idTrabajador);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_VENTAS_X_TRABAJADOR", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                BoletaDaoImpl boletaDao = new BoletaDaoImpl();
                while (rs.next()) {
                    Venta v = mapearVenta(rs);
                    v.setBoleta(boletaDao.buscarPorVentaId(v.getIdVenta()));
                    lista.add(v);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en listarPorTrabajador (ventas): " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<VentaReporteDto> reporteVentasPorPeriodo(String fechaInicio, String fechaFin) {
        List<VentaReporteDto> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, fechaInicio);
        parametrosEntrada.put(2, fechaFin);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("REPORTE_VENTAS_POR_PERIODO", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    VentaReporteDto dto = new VentaReporteDto();
                    dto.setIdVenta(rs.getInt("VENTA_ID"));
                    dto.setFechaHora(rs.getTimestamp("FECHA_HORA").toLocalDateTime());
                    dto.setCliente(rs.getString("CLIENTE"));
                    dto.setMetodoPago(rs.getString("METODO_PAGO"));
                    dto.setCanalVenta(rs.getString("CANAL_VENTA"));
                    dto.setMontoTotal(rs.getDouble("MONTO_TOTAL"));
                    dto.setMontoDescuento(rs.getDouble("MONTO_DESCUENTO"));
                    dto.setEstadoVenta(rs.getString("ESTADO_VENTA"));
                    lista.add(dto);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en reporte ventas por periodo: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public List<TopProductoDto> topProductosVendidos() {
        List<TopProductoDto> lista = new ArrayList<>();
        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("TOP_PRODUCTOS_VENDIDOS", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    TopProductoDto dto = new TopProductoDto();
                    dto.setIdProducto(rs.getInt("PRODUCTO_ID"));
                    dto.setNombre(rs.getString("NOMBRE"));
                    dto.setImagenUrl(rs.getString("IMAGEN_URL"));
                    dto.setTotalUnidades(rs.getInt("TOTAL_UNIDADES"));
                    dto.setTotalIngresos(rs.getDouble("TOTAL_INGRESOS"));
                    lista.add(dto);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error en top productos vendidos: " + ex.getMessage());
        }
        return lista;
    }

    private List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
        List<DetalleVenta> lista = new ArrayList<>();
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idVenta);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_DETALLES_POR_VENTA", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setIdDetalleVenta(rs.getInt("DETALLE_VENTA_ID"));
                    detalle.setIdPadreVenta(rs.getInt("VENTA_ID"));
                    detalle.setCantidad(rs.getInt("CANTIDAD"));
                    detalle.setPrecioUnitario(rs.getDouble("PRECIO_UNITARIO"));
                    detalle.setSubtotal(rs.getDouble("SUBTOTAL"));

                    Producto producto = new Producto();
                    producto.setIdProducto(rs.getInt("PRODUCTO_ID"));
                    producto.setNombre(rs.getString("PRODUCTO_NOMBRE"));
                    detalle.setProducto(producto);
                    detalle.setDescripcion(rs.getString("PRODUCTO_NOMBRE"));
                    lista.add(detalle);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar detalles por venta: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public int confirmarVenta(int idVenta) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idVenta);
        return DBManager.getInstance().ejecutarProcedimiento(
                "sp_ConfirmarVenta", parametrosEntrada, null);
    }

    @Override
    public int actualizarEstadoVenta(int idVenta, String estado) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, idVenta);
        parametrosEntrada.put(2, estado);
        return DBManager.getInstance().ejecutarProcedimiento(
                "sp_ActualizarEstadoVentaBoleta", parametrosEntrada, null);
    }
}
