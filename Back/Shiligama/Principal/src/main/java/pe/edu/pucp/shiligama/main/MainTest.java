package pe.edu.pucp.shiligama.main;

import com.mysql.cj.protocol.Message;
import pe.edu.pucp.model.enums.*;
import pe.edu.pucp.model.promocion.Promocion;
import pe.edu.pucp.model.operacion.Devolucion;
import pe.edu.pucp.model.operacion.MovimientoInventario;
import pe.edu.pucp.model.producto.Categoria;
import pe.edu.pucp.model.producto.Producto;
import pe.edu.pucp.model.usuario.Administrador;
import pe.edu.pucp.model.usuario.Cliente;
import pe.edu.pucp.model.usuario.Trabajador;
import pe.edu.pucp.model.venta.*;
import pe.edu.pucp.operacion.bo.DevolucionBO;
import pe.edu.pucp.operacion.bo.MovimientoInventarioBO;
import pe.edu.pucp.operacion.bo.PromocionBO;
import pe.edu.pucp.operacion.impl.DevolucionBoImpl;
import pe.edu.pucp.operacion.impl.MovimientoInventarioBoImpl;
import pe.edu.pucp.operacion.impl.PromocionBoImpl;
import pe.edu.pucp.persistance.dao.operacion.Impl.PromocionDaoImpl;
import pe.edu.pucp.persistance.dao.operacion.Impl.DevolucionDaoImpl;
import pe.edu.pucp.persistance.dao.operacion.Impl.MovimientoInventarioDaoImpl;
import pe.edu.pucp.persistance.dao.producto.Impl.CategoriaDaoImpl;
import pe.edu.pucp.persistance.dao.producto.Impl.ProductoDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.impl.AdministradorDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.impl.ClienteDaoImpl;
import pe.edu.pucp.persistance.dao.usuario.impl.TrabajadorDaoImpl;
import pe.edu.pucp.venta.bo.*;
import pe.edu.pucp.venta.impl.*;
import pe.edu.pucp.producto.bo.CategoriaBo;
import pe.edu.pucp.producto.bo.ProductoBo;
import pe.edu.pucp.producto.impl.CategoriaBoImpl;
import pe.edu.pucp.producto.impl.ProductoBoImpl;
import pe.edu.pucp.usuario.bo.AdministradorBo;
import pe.edu.pucp.usuario.bo.ClienteBo;
import pe.edu.pucp.usuario.impl.AdministradorBoImpl;
import pe.edu.pucp.usuario.impl.ClienteBoImpl;
import pe.edu.pucp.usuario.impl.TrabajadorBoImpl;
import pe.edu.pucp.model.venta.VentaReporteDto;
import pe.edu.pucp.venta.bo.VentaBo;
import pe.edu.pucp.venta.impl.VentaBoImpl;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainTest {
    private static int idVentaCreada = 0;
    private static int idPedidoCreado = 0;

    // PRINCIPAL
    public static void main(String[] args) throws Exception {
        System.out.println("=== SISTEMA SHILIGAMA - PRUEBAS DE USUARIOS ===");

        ejecutarPruebasVentaPedidos();
        ejectutarPruebasOperacionproductoPromocion();
        ejecutarPruebasUsuarios();
    }

    // Ya con BO implementado
    private static void ejecutarPruebasUsuarios() throws Exception {
        imprimir_encabezado("Usuarios");
        pruebaCliente();
        pruebaTrabajador();
        pruebaAdministrador();
        imprimir_cierre();
    }

    // Ya con BO implementado
    private static void ejectutarPruebasOperacionproductoPromocion() throws Exception {
        imprimir_encabezado("Operaciones, Producto y Promocion");
        pruebaPromocion();
        pruebaDevolucion();
        pruebaMovimientoInventario();
        pruebaCategoria();
        pruebaProducto();
        imprimir_cierre();
    }

    private static void ejecutarPruebasVentaPedidos() throws Exception {
        imprimir_encabezado("Ventas y Pedidos");
        pruebaMetodoPago();
        pruebaVenta();
        // pruebaDetalleVenta();
        pruebaPedido();
        pruebaDetallePedido();
        pruebaReporteVentasPorPeriodo();
        imprimir_cierre();
    }

    private static void pruebaPromocion() throws Exception {
        System.out.println("------------ PRUEBA PROMOCIÓN ------------");
        PromocionBO boPromo = new PromocionBoImpl();

        // 1. Insertar
        Promocion p = new Promocion(0, "Promo Verano", "Descuento por verano", TipoDescuento.PORCENTAJE, 15.0,
                LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(10).atStartOfDay(), "Aplica a bebidas", true);
        int resIns = boPromo.insertar(p);
        System.out.println("Insertar promoción: " + (resIns > 0 ? "Exito ID: " + p.getIdPromocion() : "Error"));

        if (resIns > 0) {
            // 2. Modificar
            p.setNombre("Promo Verano Modificada");
            p.setValorDescuento(20.0);
            int resMod = boPromo.modificar(p);
            System.out.println("Modificar promoción: " + (resMod == 1 ? "Exito" : "Error"));

            // 3. Buscar
            Promocion pBuscada = boPromo.buscarPorId(p.getIdPromocion());
            System.out.println("Buscar promoción: " + (pBuscada != null ? pBuscada.getNombre() : "No encontrada"));

            // 4. Asociar producto (idProducto = 1 asumido)
            int resAsoc = boPromo.asociarProducto(p.getIdPromocion(), 1);
            System.out.println("Asociar producto 1: " + (resAsoc == 1 ? "Exito" : "Error"));

            // 5. Listar productos asociados
            List<Integer> prods = boPromo.listarProductosPorPromocion(p.getIdPromocion());
            System.out.println("Productos asociados a la promoción: " + prods);

            // 6. Desasociar producto
            int resDes = boPromo.desasociarProducto(p.getIdPromocion(), 1);
            System.out.println("Desasociar producto 1: " + (resDes == 1 ? "Exito" : "Error"));

            // 7. Listar todos
            List<Promocion> todas = boPromo.listarTodos();
            System.out.println("Listar todas las promociones. Cantidad: " + todas.size());

            // 8. Listar vigentes
            List<Promocion> vigentes = boPromo.listarVigentes();
            System.out.println("Listar promociones vigentes. Cantidad: " + vigentes.size());

            // 9. Eliminar
            int resElim = boPromo.eliminar(p.getIdPromocion());
            System.out.println("Eliminar promoción: " + (resElim == 1 ? "Exito" : "Error"));
        }
    }

    private static void pruebaDevolucion() throws Exception {
        System.out.println("\n------------ PRUEBA DEVOLUCIÓN ------------");
        DevolucionBO boDevolucion = new DevolucionBoImpl();

        // 1. Insertar
        Devolucion d = new Devolucion();
        d.setIdDevolucion(1); // asumiendo que existe venta con id 1
        d.setIdProducto(1);
        d.setIdTrabajador(1);
        d.setEstadoDevolucion("PENDIENTE");
        d.setCantidad(5);
        d.setMotivo("Producto dañado");
        d.setFechaHora(LocalDateTime.now());
        d.setActivo(true);

        int resIns = boDevolucion.insertar(d);
        System.out.println("Insertar devolución: " + (resIns > 0 ? "Exito ID: " + d.getIdDevolucion() : "Error"));

        if (resIns > 0) {
            // 2. Modificar
            d.setEstadoDevolucion("APROBADO");
            int resMod = boDevolucion.modificar(d);
            System.out.println("Modificar devolución: " + (resMod == 1 ? "Exito" : "Error"));

            // 3. Buscar
            Devolucion dBuscada = boDevolucion.buscarPorId(d.getIdDevolucion());
            System.out.println("Buscar devolución: "
                    + (dBuscada != null ? "Encontrada, Estado: " + dBuscada.getEstadoDevolucion() : "No encontrada"));

            // 4. Listar todas
            List<Devolucion> todas = boDevolucion.listarTodos();
            System.out.println("Listar todas las devoluciones. Cantidad: " + todas.size());

            // 5. Listar por fechas
            List<Devolucion> porFechas = boDevolucion.listarPorFechas(LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1));
            System.out.println("Listar devoluciones por fecha. Cantidad: " + porFechas.size());

            // 6. Eliminar
            int resElim = boDevolucion.eliminar(d.getIdDevolucion());
            System.out.println("Eliminar devolución: " + (resElim == 1 ? "Exito" : "Error"));
        }
    }

    private static void pruebaMovimientoInventario() throws Exception {
        System.out.println("\n------------ PRUEBA MOVIMIENTO INVENTARIO ------------");
        MovimientoInventarioBO boMovInvent = new MovimientoInventarioBoImpl();

        // 1. Insertar (Log inmutable)
        MovimientoInventario mov = new MovimientoInventario();
        mov.setIdProducto(1);
        mov.setIdTrabajador(1);
        mov.setTipoMovimiento("ENTRADA");
        mov.setCantidad(50);
        mov.setStockAnterior(10);
        mov.setStockResultante(60);
        mov.setMotivo("Reabastecimiento");
        mov.setFechaHora(LocalDateTime.now());
        // mov.setUsuarioCreacion(1); EN CASO SE AGREGE ID DE USUARIO

        int resIns = boMovInvent.insertar(mov);
        System.out
                .println("Insertar movimiento (Log): " + (resIns > 0 ? "Exito ID: " + mov.getIdMovimiento() : "Error"));

        if (resIns > 0) {
            // 2. Modificar (Debe dar mensaje de error custom y retornar 0)
            try {
                int resMod = boMovInvent.modificar(mov);
                System.out.println("Intentar modificar log inmutable, resultado: " + resMod);
            } catch (Exception ex) {
                System.out.println("Intentar modificar log inmutable, resultado: " + ex.getMessage());
            }

            // 3. Eliminar (Debe dar mensaje de error custom y retornar 0)
            try {
                int resElim = boMovInvent.eliminar(mov.getIdMovimiento());
                System.out.println("Intentar eliminar log inmutable, resultado: " + resElim);
            } catch (Exception ex) {
                System.out.println("Intentar eliminar log inmutable, resultado: " + ex.getMessage());
            }

            // 4. Buscar
            MovimientoInventario mBuscado = boMovInvent.buscarPorId(mov.getIdMovimiento());
            System.out.println("Buscar movimiento: "
                    + (mBuscado != null ? "Encontrado, Tipo: " + mBuscado.getTipoMovimiento() : "No encontrado"));

            // 5. Listar todos
            List<MovimientoInventario> todos = boMovInvent.listarTodos();
            System.out.println("Listar todos los movimientos. Cantidad: " + todos.size());

            // 6. Listar por producto
            List<MovimientoInventario> porProd = boMovInvent.listarPorProducto(1);
            System.out.println("Listar movimientos por producto 1. Cantidad: " + porProd.size());

            // 7. Listar por fechas
            List<MovimientoInventario> porFechas = boMovInvent.listarPorFechas(LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1));
            System.out.println("Listar movimientos por fecha. Cantidad: " + porFechas.size());
        }
    }

    private static void pruebaCategoria() throws Exception {
        System.out.println("\n------------ PRUEBA CATEGORÍA ------------");
        CategoriaBo boCategoria = new CategoriaBoImpl();

        // 1. Insertar categoría padre
        Categoria categoriaPadre = new Categoria();
        categoriaPadre.setNombre("Bebidas");
        categoriaPadre.setDescripcion("Categoría principal de bebidas");
        categoriaPadre.setCategoriaPadre(null); // No tiene padre
        categoriaPadre.setEstado(true);

        int resInsPadre = boCategoria.insertar(categoriaPadre);
        System.out.println("Insertar categoría padre: "
                + (resInsPadre > 0 ? "Éxito ID: " + categoriaPadre.getIdCategoria() : "Error"));

        if (resInsPadre > 0) {
            // 2. Insertar categoría hija
            Categoria categoriaHija = new Categoria();
            categoriaHija.setNombre("Gaseosas");
            categoriaHija.setDescripcion("Bebidas gaseosas");
            categoriaHija.setCategoriaPadre(categoriaPadre); // Tiene como padre a "Bebidas"
            categoriaHija.setEstado(true);

            int resInsHija = boCategoria.insertar(categoriaHija);
            System.out.println("Insertar categoría hija: "
                    + (resInsHija > 0 ? "Éxito ID: " + categoriaHija.getIdCategoria() : "Error"));

            if (resInsHija > 0) {
                // 3. Modificar categoría hija
                categoriaHija.setNombre("Bebidas Gaseosas");
                categoriaHija.setDescripcion("Bebidas carbonatadas y refrescantes");
                int resMod = boCategoria.modificar(categoriaHija);
                System.out.println("Modificar categoría: " + (resMod == 1 ? "Éxito" : "Error"));

                // 4. Buscar por ID
                Categoria cBuscada = boCategoria.buscarPorId(categoriaHija.getIdCategoria());
                System.out.println("Buscar categoría: "
                        + (cBuscada != null ? "Encontrada: " + cBuscada.getNombre() + " - " + cBuscada.getDescripcion()
                                : "No encontrada"));

                // 5. Listar todas
                List<Categoria> todas = boCategoria.listarTodos();
                System.out.println("Listar todas las categorías. Cantidad: " + todas.size());
                for (Categoria cat : todas) {
                    System.out.println("  - ID: " + cat.getIdCategoria() +
                            " | Nombre: " + cat.getNombre() +
                            " | Padre: "
                            + (cat.getCategoriaPadre() != null ? cat.getCategoriaPadre().getIdCategoria() : "Ninguno"));
                }

                // 6. Eliminar categoría hija
                int resElimHija = boCategoria.eliminar(categoriaHija.getIdCategoria());
                System.out.println(
                        "Eliminar categoría hija: " + (resElimHija == 1 ? "Éxito (marcada como inactiva)" : "Error"));

                // 7. Verificar que fue eliminada (búsqueda debe retornar null o inactiva)
                Categoria cEliminada = boCategoria.buscarPorId(categoriaHija.getIdCategoria());
                System.out.println("Verificar eliminación: " +
                        (cEliminada == null || !cEliminada.isEstado() ? "Categoría inactiva/no encontrada correctamente"
                                : "ERROR: Categoría sigue activa"));
            }

            // 8. Eliminar categoría padre
            int resElimPadre = boCategoria.eliminar(categoriaPadre.getIdCategoria());
            System.out.println(
                    "Eliminar categoría padre: " + (resElimPadre == 1 ? "Éxito (marcada como inactiva)" : "Error"));
        }
    }

    private static void pruebaProducto() throws Exception {
        System.out.println("\n------------ PRUEBA PRODUCTO ------------");
        ProductoBo boProducto = new ProductoBoImpl();
        CategoriaBo boCategoria = new CategoriaBoImpl();

        // Primero necesitamos una categoría activa para asociar al producto
        Categoria categoria = new Categoria();
        categoria.setNombre("Snacks");
        categoria.setDescripcion("Productos para picar");
        categoria.setCategoriaPadre(null);
        categoria.setEstado(true);

        int resInsCat = boCategoria.insertar(categoria);
        System.out.println("Insertar categoría para productos: "
                + (resInsCat > 0 ? "Éxito ID: " + categoria.getIdCategoria() : "Error"));

        if (resInsCat > 0) {
            // 1. Insertar producto
            Producto producto = new Producto();
            producto.setNombre("Papas Lays Original");
            producto.setDescripcion("Papas fritas sabor original 150g");
            producto.setPrecioUnitario(5.50);
            producto.setStock(100);
            producto.setStockMinimo(20);
            producto.setUnidadMedida("Unidad");
            producto.setCodigoBarras("7501234567890");
            producto.setImagenUrl("http://ejemplo.com/lays.jpg");
            producto.setEstado(true);
            producto.setCategoria(categoria);

            int resIns = boProducto.insertar(producto);
            System.out
                    .println("Insertar producto: " + (resIns > 0 ? "Éxito ID: " + producto.getIdProducto() : "Error"));

            if (resIns > 0) {
                // 2. Modificar producto
                producto.setNombre("Papas Lays Original 200g");
                producto.setPrecioUnitario(7.00);
                producto.setStockMinimo(15);
                int resMod = boProducto.modificar(producto);
                System.out.println("Modificar producto: " + (resMod == 1 ? "Éxito" : "Error"));

                // 3. Buscar por ID
                Producto pBuscado = boProducto.buscarPorId(producto.getIdProducto());
                System.out.println("Buscar producto: " + (pBuscado != null ? "Encontrado: " + pBuscado.getNombre() +
                        " | Precio: S/ " + pBuscado.getPrecioUnitario() +
                        " | Categoría: " + pBuscado.getCategoria().getNombre() : "No encontrado"));

                // 4. Verificar stock bajo (método de negocio)
                if (pBuscado != null) {
                    System.out.println("¿Tiene stock bajo?: " + pBuscado.tieneStockBajo());
                }

                // 5. Listar todos
                List<Producto> todos = boProducto.listarTodos();
                System.out.println("Listar todos los productos. Cantidad: " + todos.size());
                for (Producto p : todos) {
                    System.out.println("  - ID: " + p.getIdProducto() +
                            " | Nombre: " + p.getNombre() +
                            " | Precio: S/ " + p.getPrecioUnitario() +
                            " | Stock: " + p.getStock() +
                            " | Categoría: " + p.getCategoria().getNombre());
                }

                // 6. Eliminar producto
                int resElim = boProducto.eliminar(producto.getIdProducto());
                System.out.println("Eliminar producto: " + (resElim == 1 ? "Éxito (marcado como inactivo)" : "Error"));

                // 7. Verificar que fue eliminado
                Producto pEliminado = boProducto.buscarPorId(producto.getIdProducto());
                System.out.println("Verificar eliminación: " +
                        (pEliminado == null || !pEliminado.isEstado() ? "Producto inactivo/no encontrado correctamente"
                                : "ERROR: Producto sigue activo"));
            }

            // Limpiar: eliminar la categoría de prueba
            boCategoria.eliminar(categoria.getIdCategoria());
            System.out.println("Categoría de prueba eliminada");
        }
    }

    // =========================================================
    // MÉTODO PAGO
    // =========================================================
    private static void pruebaMetodoPago() throws Exception {
        System.out.println("------------ PRUEBA MÉTODO DE PAGO ------------");
        MetodoPagoBo bo = new MetodoPagoBoImpl();

        MetodoPago mp = new MetodoPago();
        mp.setNombre("Yape");
        int resIns = bo.insertar(mp);
        System.out.println("Insertar método de pago: " + (resIns > 0 ? "Éxito ID: " + mp.getIdMetodoPago() : "Error"));

        if (resIns > 0) {
            MetodoPago mpBuscado = bo.buscarPorId(mp.getIdMetodoPago());
            System.out
                    .println("Buscar método de pago: " + (mpBuscado != null ? mpBuscado.getNombre() : "No encontrado"));

            mp.setNombre("Yape Modificado");
            int resMod = bo.modificar(mp);
            System.out.println("Modificar método de pago: " + (resMod == 1 ? "Éxito" : "Error"));

            List<MetodoPago> lista = bo.listarTodos();
            System.out.println("Listar todos los métodos de pago. Cantidad: " + lista.size());

            int resElim = bo.eliminar(mp.getIdMetodoPago());
            System.out.println("Eliminar método de pago: " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    // =========================================================
    // VENTA
    // =========================================================
    private static void pruebaVenta() throws Exception {
        System.out.println("\n------------ PRUEBA VENTA ------------");
        VentaBo bo = new VentaBoImpl();

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(1);

        Trabajador trabajador = new Trabajador();
        trabajador.setIdUsuario(1);

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(1);

        Venta venta = new Venta();
        venta.setCliente(cliente); // CLIENTE_ID int
        venta.setTrabajador(trabajador); // TRABAJADOR_ID int
        venta.setMetodoPago(metodoPago); // METODO_PAGO_ID int
        venta.setCanalVenta(CanalVenta.PRESENCIAL); // CANAL_VENTA enum('PRESENCIAL','WEB')
        venta.setObservaciones("Venta de prueba"); // OBSERVACIONES varchar(500)

        int resIns = bo.insertar(venta);
        idVentaCreada = venta.getIdVenta(); // guarda el ID
        System.out.println("Insertar venta: " + (resIns > 0 ? "Éxito ID: " + venta.getIdVenta() : "Error"));

        if (resIns > 0) {
            Venta ventaBuscada = bo.buscarPorId(venta.getIdVenta());
            System.out.println("Buscar venta: " + (ventaBuscada != null
                    ? "Encontrada, Canal: " + ventaBuscada.getCanalVenta()
                    : "No encontrada"));

            int resMod = bo.modificar(venta);
            System.out.println("Completar venta (modificar): " + (resMod == 1 ? "Éxito" : "Error"));

            List<Venta> lista = bo.listarTodos();
            System.out.println("Listar todas las ventas. Cantidad: " + lista.size());

            int resElim = bo.eliminar(venta.getIdVenta());
            System.out.println("Anular venta (eliminar): " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    /*
     * // =========================================================
     * // DETALLE VENTA
     * // =========================================================
     * private static void pruebaDetalleVenta() throws Exception {
     * System.out.println("\n------------ PRUEBA DETALLE VENTA ------------");
     * DetalleVentaBo bo = new DetalleVentaBoImpl();
     * 
     * Producto producto = new Producto();
     * producto.setIdProducto(1);
     * 
     * DetalleVenta detalle = new DetalleVenta();
     * detalle.setIdPadreVenta(idVentaCreada); // ID de la venta creada en
     * pruebaVenta()
     * detalle.setProducto(producto);
     * detalle.setCantidad(3);
     * int resIns = bo.insertar(detalle);
     * System.out.println("Insertar detalle venta: " + (resIns > 0 ? "Éxito ID: " +
     * detalle.getIdDetalleVenta() : "Error"));
     * 
     * if (resIns > 0) {
     * DetalleVenta detalleBuscado = bo.buscarPorId(detalle.getIdDetalleVenta());
     * System.out.println("Buscar detalle venta: " + (detalleBuscado != null
     * ? "Encontrado, Cantidad: " + detalleBuscado.getCantidad() :
     * "No encontrado"));
     * 
     * detalle.setCantidad(5);
     * int resMod = bo.modificar(detalle);
     * System.out.println("Modificar detalle venta: " + (resMod == 1 ? "Éxito" :
     * "Error"));
     * 
     * List<DetalleVenta> lista = bo.listarTodos();
     * System.out.println("Listar todos los detalles de venta. Cantidad: " +
     * lista.size());
     * 
     * int resElim = bo.eliminar(detalle.getIdDetalleVenta());
     * System.out.println("Eliminar detalle venta: " + (resElim == 1 ? "Éxito" :
     * "Error"));
     * }
     * }
     */

    // =========================================================
    // PEDIDO
    // =========================================================
    private static void pruebaPedido() throws Exception {
        System.out.println("\n------------ PRUEBA PEDIDO ------------");
        PedidoBo bo = new PedidoBoImpl();

        Cliente cliente = new Cliente();
        cliente.setIdUsuario(1);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDireccionEntrega("Av. Universitaria 1801, San Miguel");
        pedido.setModalidadVenta(ModalidadVenta.DELIVERY);
        pedido.setObservaciones("Pedido de prueba");
        int resIns = bo.insertar(pedido);
        idPedidoCreado = pedido.getIdPedido();
        System.out.println("Insertar pedido: " + (resIns > 0 ? "Éxito ID: " + pedido.getIdPedido() : "Error"));

        if (resIns > 0) {
            Pedido pedidoBuscado = bo.buscarPorId(pedido.getIdPedido());
            System.out.println("Buscar pedido: " + (pedidoBuscado != null
                    ? "Encontrado, Estado: " + pedidoBuscado.getEstadoPedido()
                    : "No encontrado"));

            pedido.setEstadoPedido(EstadoPedido.EN_PROCESO);
            int resMod = bo.modificar(pedido);
            System.out.println("Modificar estado pedido: " + (resMod == 1 ? "Éxito" : "Error"));

            List<Pedido> lista = bo.listarTodos();
            System.out.println("Listar todos los pedidos. Cantidad: " + lista.size());

            int resElim = bo.eliminar(pedido.getIdPedido());
            System.out.println("Eliminar pedido: " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    // =========================================================
    // DETALLE PEDIDO
    // =========================================================
    private static void pruebaDetallePedido() throws Exception {
        System.out.println("\n------------ PRUEBA DETALLE PEDIDO ------------");
        DetallePedidoBo bo = new DetallePedidoBoImpl();

        Producto producto = new Producto();
        producto.setIdProducto(1);

        DetallePedido detalle = new DetallePedido();
        detalle.setIdPadrePedido(idPedidoCreado); // ID de un pedido existente en la BD
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        int resIns = bo.insertar(detalle);
        System.out.println(
                "Insertar detalle pedido: " + (resIns > 0 ? "Éxito ID: " + detalle.getIdDetallePedido() : "Error"));

        if (resIns > 0) {
            DetallePedido detalleBuscado = bo.buscarPorId(detalle.getIdDetallePedido());
            System.out.println("Buscar detalle pedido: " + (detalleBuscado != null
                    ? "Encontrado, Cantidad: " + detalleBuscado.getCantidad()
                    : "No encontrado"));

            detalle.setCantidad(4);
            int resMod = bo.modificar(detalle);
            System.out.println("Modificar detalle pedido: " + (resMod == 1 ? "Éxito" : "Error"));

            List<DetallePedido> lista = bo.listarTodos();
            System.out.println("Listar todos los detalles de pedido. Cantidad: " + lista.size());

            int resElim = bo.eliminar(detalle.getIdDetallePedido());
            System.out.println("Eliminar detalle pedido: " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    // =========================================================
    // CLIENTE
    // =========================================================
    public static void pruebaCliente() throws Exception {
        System.out.println("\n--- [TEST CLIENTE] ---");
        // ClienteDaoImpl cliente = new ClienteDaoImpl();
        ClienteBo clienteBO = new ClienteBoImpl();
        // Crear
        Cliente nuevo = new Cliente();
        nuevo.setNombres("Juan");
        nuevo.setApellidos("Perez");
        nuevo.setDni("77889900");
        nuevo.setTelefono("987654321");
        nuevo.setCorreo("juan.perez@pucp.edu.pe");
        nuevo.setContrasena("pucp123");
        nuevo.setDireccionEntrega("Av. Universitaria 1801, San Miguel");

        int resIns = clienteBO.insertar(nuevo);
        if (resIns > 0) {
            System.out.println("Éxito: Cliente insertado con ID: " + nuevo.getIdUsuario());

            // Listar
            List<Cliente> lista = clienteBO.listarTodos();
            System.out.println("Total clientes en BD: " + (lista != null ? lista.size() : 0));

            // Limpiar (Eliminar)
            int resElim = clienteBO.eliminar(nuevo.getIdUsuario());
            if (resElim > 0) {
                System.out.println("Limpieza: Cliente eliminado correctamente.");
            } else {
                System.out.println("Fallo al limpiar el cliente.");
            }
        } else {
            System.out.println("Error al insertar cliente.");
        }
    }

    // =========================================================
    // TRABAJADOR
    // =========================================================
    public static void pruebaTrabajador() throws Exception {
        System.out.println("\n--- [TEST TRABAJADOR] ---");
        // TrabajadorDaoImpl dao = new TrabajadorDaoImpl();
        TrabajadorBoImpl trabajadorBo = new TrabajadorBoImpl();
        // Crear
        Trabajador trabajador = new Trabajador();
        trabajador.setNombres("Maria");
        trabajador.setApellidos("Lopez");
        trabajador.setDni("11223344");
        trabajador.setTelefono("912345678");
        trabajador.setCorreo("maria.trabajadora@gmail.com");
        trabajador.setContrasena("secret");
        trabajador.setFechaIngreso(LocalDate.now());

        int resIns = trabajadorBo.insertar(trabajador);
        if (resIns > 0) {
            System.out.println("Éxito: Trabajador insertado con ID: " + trabajador.getIdUsuario());
            // Listar
            List<Trabajador> lista = trabajadorBo.listarTodos();
            System.out.println("Total trabajadores en BD: " + (lista != null ? lista.size() : 0));
            // Limpiar (Eliminar)
            int resElim = trabajadorBo.eliminar(trabajador.getIdUsuario());
            if (resElim > 0) {
                System.out.println("Limpieza: Trabajador eliminado correctamente.");
            } else {
                System.out.println("Fallo al limpiar el trabajador.");
            }
        } else {
            System.out.println("Error al insertar trabajador.");
        }
    }

    // =========================================================
    // ADMINISTRADOR
    // =========================================================
    public static void pruebaAdministrador() throws Exception {
        System.out.println("\n--- [TEST ADMINISTRADOR] ---");
        // AdministradorDaoImpl dao = new AdministradorDaoImpl();
        AdministradorBo administradorBo = new AdministradorBoImpl();
        // Crear
        Administrador admin = new Administrador();
        admin.setNombres("Super");
        admin.setApellidos("User");
        admin.setDni("00000001");
        admin.setCorreo("admin@shiligama.com");
        admin.setContrasena("adminroot");

        int resIns = administradorBo.insertar(admin);
        if (resIns > 0) {
            System.out.println("Éxito: Administrador creado con ID: " + admin.getIdUsuario());
            // Listar
            List<Administrador> lista = administradorBo.listarTodos();
            System.out.println("Total administradores en BD: " + (lista != null ? lista.size() : 0));

            // Limpiar (Eliminar)
            int resElim = administradorBo.eliminar(admin.getIdUsuario());
            if (resElim > 0) {
                System.out.println("Limpieza: Administrador eliminado correctamente.");
            } else {
                System.out.println("Fallo al limpiar el administrador.");
            }
        } else {
            System.out.println("Error al crear administrador.");
        }
    }

    // ------------------------REPORTES---------------------------------------
    private static void pruebaReporteVentasPorPeriodo() throws Exception {
        System.out.println("\n------------ PRUEBA REPORTE VENTAS POR PERIODO ------------");
        VentaBo bo = new VentaBoImpl();

        String fechaInicio = "2026-01-01";
        String fechaFin = "2026-12-31";

        List<VentaReporteDto> reporte = bo.reporteVentasPorPeriodo(fechaInicio, fechaFin);
        System.out.println("Reporte de ventas del " + fechaInicio + " al " + fechaFin
                + ". Cantidad: " + reporte.size());

        for (VentaReporteDto dto : reporte) {
            System.out.println("  ID: " + dto.getIdVenta()
                    + " | Fecha: " + dto.getFechaHora()
                    + " | Cliente: " + dto.getCliente()
                    + " | Método pago: " + dto.getMetodoPago()
                    + " | Canal: " + dto.getCanalVenta()
                    + " | Total: " + dto.getMontoTotal()
                    + " | Estado: " + dto.getEstadoVenta());
        }
    }

    private static void imprimir_encabezado(String prueba) {
        System.out.println("==================================================");
        System.out.println(" INICIANDO PRUEBAS MÓDULO/s: " + prueba);
        System.out.println("==================================================\n");
    }

    private static void imprimir_cierre() {
        System.out.println("\n==================================================");
        System.out.println(" FIN DE LAS PRUEBAS ");
        System.out.println("==================================================");
    }

}
