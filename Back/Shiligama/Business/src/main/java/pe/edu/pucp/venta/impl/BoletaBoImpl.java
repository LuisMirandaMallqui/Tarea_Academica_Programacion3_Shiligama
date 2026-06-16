package pe.edu.pucp.venta.impl;

import pe.edu.pucp.config.Config;
import pe.edu.pucp.model.venta.Boleta;
import pe.edu.pucp.model.venta.DetalleBoleta;
import pe.edu.pucp.model.venta.Venta;
import pe.edu.pucp.model.venta.DetalleVenta;
import pe.edu.pucp.persistance.dao.venta.dao.BoletaDao;
import pe.edu.pucp.persistance.dao.venta.Impl.BoletaDaoImpl;
import pe.edu.pucp.persistance.dao.venta.dao.VentaDao;
import pe.edu.pucp.persistance.dao.venta.Impl.VentaDaoImpl;
import pe.edu.pucp.model.nubefact.NubefactRequestDTO;
import pe.edu.pucp.model.nubefact.NubefactItemDTO;
import pe.edu.pucp.model.nubefact.NubefactResponseDTO;
import pe.edu.pucp.venta.bo.BoletaBo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BoletaBoImpl implements BoletaBo {

    private final BoletaDao boletaDao;
    private final VentaDao ventaDao;
    private final NubefactService nubefactService;

    public BoletaBoImpl() {
        this.boletaDao = new BoletaDaoImpl();
        this.ventaDao = new VentaDaoImpl();
        this.nubefactService = new NubefactService();
    }

    @Override
    public Boleta buscarPorVentaId(int ventaId) throws Exception {
        return boletaDao.buscarPorVentaId(ventaId);
    }

    @Override
    public Boleta generarBoleta(int ventaId) throws Exception {
        Venta venta = ventaDao.buscarPorId(ventaId);
        if (venta == null) {
            throw new Exception("La venta no existe.");
        }

        if (!"CONFIRMADA".equalsIgnoreCase(venta.getEstado())) {
            throw new Exception("La venta debe estar en estado CONFIRMADA para generar la boleta. Estado actual: " + venta.getEstado());
        }

        if (venta.getBoleta() != null) {
            throw new Exception("La venta ya tiene una boleta generada.");
        }

        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new Exception("La venta no tiene productos para facturar.");
        }

        String serie = Config.get("nubefact.serie", "B001");
        int siguienteNumero = boletaDao.obtenerSiguienteNumero(serie);

        NubefactRequestDTO request = new NubefactRequestDTO();
        request.setSerie(serie);
        request.setNumero(siguienteNumero);
        request.setFechaDeEmision(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        configurarClienteNubefact(request, venta);

        double totalGravada = 0;
        double totalIgv = 0;
        double total = 0;

        List<NubefactItemDTO> items = new ArrayList<>();
        List<DetalleBoleta> detallesBoleta = new ArrayList<>();

        for (DetalleVenta dv : venta.getDetalles()) {
            if (dv.getProducto() == null) {
                throw new Exception("La venta contiene un detalle sin producto asociado.");
            }

            NubefactItemDTO item = new NubefactItemDTO();
            String unidad = normalizarUnidadSunat(dv.getProducto().getUnidadMedida());
            item.setUnidadDeMedida(unidad);

            String codigo = (dv.getProducto().getCodigoBarras() != null && !dv.getProducto().getCodigoBarras().isBlank())
                    ? dv.getProducto().getCodigoBarras()
                    : String.valueOf(dv.getProducto().getIdProducto());
            item.setCodigo(codigo);
            item.setDescripcion(sanitizarTextoNubefact(
                    dv.getDescripcion() != null ? dv.getDescripcion() : dv.getProducto().getNombre()));
            item.setCantidad(dv.getCantidad());
            
            double precioUnitario = dv.getPrecioUnitario();
            double totalItem = dv.getSubtotal();
            double subtotalItem = Math.round((totalItem / 1.18) * 100.0) / 100.0;
            double igvItem = Math.round((totalItem - subtotalItem) * 100.0) / 100.0;
            double valorUnitario = Math.round((precioUnitario / 1.18) * 100.0) / 100.0;

            item.setPrecioUnitario(precioUnitario);
            item.setValorUnitario(valorUnitario);
            item.setSubtotal(subtotalItem);
            item.setIgv(igvItem);
            item.setTotal(totalItem);

            items.add(item);

            totalGravada += subtotalItem;
            totalIgv += igvItem;
            total += totalItem;

            DetalleBoleta detBoleta = new DetalleBoleta();
            detBoleta.setIdProducto((long) dv.getProducto().getIdProducto());
            detBoleta.setUnidadMedida(unidad);
            detBoleta.setDescripcion(item.getDescripcion());
            detBoleta.setCantidad(dv.getCantidad());
            detBoleta.setValorUnitario(valorUnitario);
            detBoleta.setPrecioUnitario(precioUnitario);
            detBoleta.setSubtotal(subtotalItem);
            detBoleta.setIgv(igvItem);
            detBoleta.setTotal(totalItem);
            detallesBoleta.add(detBoleta);
        }

        request.setItems(items);
        request.setTotalGravada(Math.round(totalGravada * 100.0) / 100.0);
        request.setTotalIgv(Math.round(totalIgv * 100.0) / 100.0);
        request.setTotal(Math.round(total * 100.0) / 100.0);

        NubefactResponseDTO response;
        try {
            response = nubefactService.enviarComprobante(request);
        } catch (Exception ex) {
            String detalle = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            throw new Exception("Error al conectar con Nubefact: " + detalle, ex);
        }

        if (response.getErrors() != null && !response.getErrors().isBlank()) {
            throw new Exception("Nubefact rechazó el comprobante: " + response.getErrors());
        }
        if (response.getEnlace() == null || response.getEnlace().isBlank()) {
            throw new Exception("Nubefact no devolvió un comprobante válido.");
        }

        Boleta boleta = new Boleta();
        boleta.setVentaId(ventaId);
        boleta.setSerie(serie);
        boleta.setNumero(siguienteNumero);
        boleta.setFechaEmision(LocalDate.now());
        boleta.setClienteTipoDocumento(request.getClienteTipoDeDocumento());
        boleta.setClienteNumeroDocumento(request.getClienteNumeroDeDocumento());
        boleta.setClienteDenominacion(request.getClienteDenominacion());
        boleta.setClienteDireccion(request.getClienteDireccion());
        boleta.setClienteEmail(request.getClienteEmail());
        boleta.setMoneda(1);
        boleta.setPorcentajeIgv(18.00);
        boleta.setTotalGravada(request.getTotalGravada());
        boleta.setTotalIgv(request.getTotalIgv());
        boleta.setTotal(request.getTotal());

        boleta.setNubefactEnlace(response.getEnlace());
        boleta.setNubefactEnlacePdf(response.getEnlaceDelPdf());
        boleta.setNubefactEnlaceXml(response.getEnlaceDelXml());
        boleta.setNubefactEnlaceCdr(response.getEnlaceDelCdr());
        boleta.setNubefactCadenaQr(response.getCadenaParaCodigoQr());
        boleta.setNubefactCodigoHash(response.getCodigoHash());
        boleta.setAceptadaPorSunat(response.isAceptadaPorSunat());
        boleta.setSunatResponseCode(response.getSunatResponsecode());
        boleta.setSunatDescription(response.getSunatDescription());
        boleta.setUsuarioRegistro(venta.getTrabajador() != null ? String.valueOf(venta.getTrabajador().getIdUsuario()) : "sistema");
        boleta.setDetalles(detallesBoleta);

        int generatedBoletaId = boletaDao.insertar(boleta);
        if (generatedBoletaId <= 0) {
            throw new Exception("No se pudo registrar la boleta en la base de datos.");
        }

        boleta.setFechaRegistro(LocalDateTime.now());
        ventaDao.actualizarEstadoVenta(ventaId, "BOLETA_GENERADA");

        return boleta;
    }

    private void configurarClienteNubefact(NubefactRequestDTO request, Venta venta) {
        String denominacion = sanitizarTextoNubefact(extraerNombreClientePos(venta));
        String dni = venta.getCliente() != null ? venta.getCliente().getDni() : null;

        if (esDniValidoParaBoleta(dni)) {
            request.setClienteTipoDeDocumento("1");
            request.setClienteNumeroDeDocumento(dni);
            request.setClienteDenominacion(denominacion);
            request.setClienteDireccion(
                    venta.getCliente().getDireccionEntrega() != null ? venta.getCliente().getDireccionEntrega() : "");
            request.setClienteEmail(
                    venta.getCliente().getCorreo() != null ? venta.getCliente().getCorreo() : "");
        } else {
            request.setClienteTipoDeDocumento("-");
            request.setClienteNumeroDeDocumento("00000000");
            request.setClienteDenominacion(denominacion.isBlank() ? "PUBLICO GENERAL" : denominacion);
            request.setClienteDireccion("");
            request.setClienteEmail("");
        }
    }

    private boolean esDniValidoParaBoleta(String dni) {
        if (dni == null || dni.isBlank() || "00000000".equals(dni)) {
            return false;
        }
        return dni.length() == 8 && dni.chars().allMatch(Character::isDigit);
    }

    private String extraerNombreClientePos(Venta venta) {
        if (venta.getObservaciones() != null && venta.getObservaciones().startsWith("Cliente: ")) {
            String nombre = venta.getObservaciones().substring("Cliente: ".length()).trim();
            if (!nombre.isBlank()) {
                return nombre.toUpperCase();
            }
        }
        if (venta.getCliente() != null) {
            String nombres = venta.getCliente().getNombres() != null ? venta.getCliente().getNombres() : "";
            String apellidos = venta.getCliente().getApellidos() != null ? venta.getCliente().getApellidos() : "";
            String completo = (nombres + " " + apellidos).trim();
            if (!completo.isBlank()) {
                return completo.toUpperCase();
            }
        }
        return "PUBLICO GENERAL";
    }

    /**
     * Nubefact/SUNAT exige códigos de catálogo (NIU, KGM, LTR...).
     * Los productos del minimarket guardan descripciones comerciales (Bolsa, Botella...).
     */
    private String normalizarUnidadSunat(String unidadMedida) {
        if (unidadMedida == null || unidadMedida.isBlank()) {
            return "NIU";
        }
        String codigo = unidadMedida.trim().toUpperCase();
        if (codigo.matches("[A-Z]{2,3}")) {
            return codigo;
        }
        return "NIU";
    }

    /** Nubefact rechaza comillas dobles dentro de textos. */
    private String sanitizarTextoNubefact(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("\"", "").trim();
    }
}
