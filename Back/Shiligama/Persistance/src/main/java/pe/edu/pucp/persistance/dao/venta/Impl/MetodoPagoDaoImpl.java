package pe.edu.pucp.persistance.dao.venta.Impl;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.venta.MetodoPagoDto;
import pe.edu.pucp.persistance.dao.venta.dao.MetodoPagoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetodoPagoDaoImpl implements MetodoPagoDao {

    // SP: INSERTAR_METODO_PAGO(OUT _metodo_pago_id, IN _nombre)
    @Override
    public int insertar(MetodoPagoDto metodoPago) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        Map<Integer, Object> parametrosSalida = new HashMap<>();

        parametrosSalida.put(1, Types.INTEGER);
        parametrosEntrada.put(2, metodoPago.getNombre());

        DBManager.getInstance().ejecutarProcedimiento(
                "INSERTAR_METODO_PAGO", parametrosEntrada, parametrosSalida);
        metodoPago.setIdMetodoPago((int) parametrosSalida.get(1));
        return metodoPago.getIdMetodoPago();
    }

    // SP: MODIFICAR_METODO_PAGO(IN _metodo_pago_id, IN _nombre)
    @Override
    public int modificar(MetodoPagoDto metodoPago) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, metodoPago.getIdMetodoPago());
        parametrosEntrada.put(2, metodoPago.getNombre());
        return DBManager.getInstance().ejecutarProcedimiento(
                "MODIFICAR_METODO_PAGO", parametrosEntrada, null);
    }

    // SP: ELIMINAR_METODO_PAGO(IN _metodo_pago_id)
    @Override
    public int eliminar(int id) {
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);
        return DBManager.getInstance().ejecutarProcedimiento(
                "ELIMINAR_METODO_PAGO", parametrosEntrada, null);
    }

    // SP: BUSCAR_METODO_PAGO_X_ID(IN _metodo_pago_id)
    @Override
    public MetodoPagoDto buscarPorID(int id) {
        MetodoPagoDto metodoPago = null;
        Map<Integer, Object> parametrosEntrada = new HashMap<>();
        parametrosEntrada.put(1, id);

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("BUSCAR_METODO_PAGO_X_ID", parametrosEntrada)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                if (rs.next()) {
                    metodoPago = mapearMetodoPago(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar metodo de pago: " + ex.getMessage());
        }
        return metodoPago;
    }

    // SP: LISTAR_METODOS_PAGO()
    @Override
    public List<MetodoPagoDto> listarTodos() {
        List<MetodoPagoDto> lista = new ArrayList<>();

        try (DBManager.ResultadoConsulta resultado = DBManager.getInstance()
                .ejecutarProcedimientoLectura("LISTAR_METODOS_PAGO", null)) {
            if (resultado != null) {
                ResultSet rs = resultado.getRs();
                while (rs.next()) {
                    lista.add(mapearMetodoPago(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar metodos de pago: " + ex.getMessage());
        }
        return lista;
    }

    private MetodoPagoDto mapearMetodoPago(ResultSet rs) throws SQLException {
        MetodoPagoDto mp = new MetodoPagoDto();
        mp.setIdMetodoPago(rs.getInt("METODO_PAGO_ID"));
        mp.setNombre(rs.getString("NOMBRE"));
        mp.setEstado(rs.getBoolean("ACTIVO"));
        return mp;
    }
}
