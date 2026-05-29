package pe.edu.pucp.venta.impl;

import pe.edu.pucp.model.venta.Boleta;
import pe.edu.pucp.persistance.dao.venta.Impl.BoletaDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.BoletaDao;
import pe.edu.pucp.venta.bo.BoletaBo;

import java.util.List;

public class BoletaBoImpl implements BoletaBo {
    private final BoletaDao boletaDao;

    public BoletaBoImpl() {
        this.boletaDao = new BoletaDaoImpl();
    }

    @Override
    public String emitir(int idVenta, String ruc, String contactoCliente, String mensajeBoleta) throws Exception {
        if (idVenta <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        // RUC del local: opcional según el caso. Validamos longitud si viene.
        if (ruc != null && !ruc.isBlank() && !ruc.trim().matches("\\d{11}")) {
            throw new Exception("El RUC debe tener 11 dígitos.");
        }
        return boletaDao.emitir(idVenta,
                ruc == null ? null : ruc.trim(),
                contactoCliente,
                mensajeBoleta);
    }

    @Override
    public Boleta buscarPorIdVenta(int idVenta) throws Exception {
        if (idVenta <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        return boletaDao.buscarPorIdVenta(idVenta);
    }

    @Override
    public Boleta buscarPorNumero(String numeroBoleta) throws Exception {
        if (numeroBoleta == null || numeroBoleta.isBlank()) {
            throw new Exception("El número de boleta es obligatorio.");
        }
        return boletaDao.buscarPorNumero(numeroBoleta.trim());
    }

    @Override
    public List<Boleta> listarTodas() throws Exception {
        return boletaDao.listarTodas();
    }

    @Override
    public int anular(int idVenta) throws Exception {
        if (idVenta <= 0) {
            throw new Exception("El ID de la venta debe ser mayor que cero.");
        }
        return boletaDao.anular(idVenta);
    }
}
