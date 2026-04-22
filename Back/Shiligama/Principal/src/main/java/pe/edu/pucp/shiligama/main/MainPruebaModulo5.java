package pe.edu.pucp.shiligama.main;

import pe.edu.pucp.model.enums.CanalVenta;
import pe.edu.pucp.model.enums.EstadoPedido;
import pe.edu.pucp.model.enums.ModalidadVenta;
import pe.edu.pucp.model.operaciones.Promocion;
import pe.edu.pucp.model.operaciones.Devolucion;
import pe.edu.pucp.model.operaciones.MovimientoInventario;
import pe.edu.pucp.model.producto.CategoriaDto;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.model.usuario.ClienteDto;
import pe.edu.pucp.model.usuario.TrabajadorDto;
import pe.edu.pucp.model.venta.*;
import pe.edu.pucp.persistance.dao.operaciones.Impl.PromocionImpl;
import pe.edu.pucp.persistance.dao.operaciones.Impl.DevolucionImpl;
import pe.edu.pucp.persistance.dao.operaciones.Impl.MovimientoInventarioImpl;
import pe.edu.pucp.persistance.dao.producto.Impl.CategoriaImpl;
import pe.edu.pucp.persistance.dao.producto.Impl.ProductoImpl;
import pe.edu.pucp.persistance.dao.venta.Impl.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainPruebaModulo5 {
    private static int idVentaCreada = 0;
    private static int idPedidoCreado = 0;
    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println(" INICIANDO PRUEBAS MÓDULO 4 (Ventas y Pedidos)   ");
        System.out.println("==================================================\n");

        pruebaMetodoPago();
        pruebaVenta();
        pruebaDetalleVenta();
        pruebaPedido();
        pruebaDetallePedido();

        System.out.println("\n==================================================");
        System.out.println(" FIN DE LAS PRUEBAS ");
        System.out.println("==================================================");

        System.out.println("==================================================");
        System.out.println(" INICIANDO PRUEBAS MÓDULO 5 (Operaciones y Fide) ");
        System.out.println("==================================================\n");

        pruebaPromocion();
        pruebaDevolucion();
        pruebaMovimientoInventario();

        pruebaCategoria();
        pruebaProducto();

        System.out.println("\n==================================================");
        System.out.println(" FIN DE LAS PRUEBAS ");
        System.out.println("==================================================");
    }

    private static void pruebaPromocion() {
        System.out.println("------------ PRUEBA PROMOCIÓN ------------");
        PromocionImpl dao = new PromocionImpl();

        // 1. Insertar
        Promocion p = new Promocion(0, "Promo Verano", "Descuento por verano", "Porcentaje", 15.0,
                LocalDate.now(), LocalDate.now().plusDays(10), "Aplica a bebidas", true);
        int resIns = dao.insertar(p);
        System.out.println("Insertar promoción: " + (resIns == 1 ? "Exito ID: " + p.getIdPromocion() : "Error"));

        if (resIns == 1) {
            // 2. Modificar
            p.setNombre("Promo Verano Modificada");
            p.setValorDescuento(20.0);
            int resMod = dao.modificar(p);
            System.out.println("Modificar promoción: " + (resMod == 1 ? "Exito" : "Error"));

            // 3. Buscar
            Promocion pBuscada = dao.buscarPorID(p.getIdPromocion());
            System.out.println("Buscar promoción: " + (pBuscada != null ? pBuscada.getNombre() : "No encontrada"));

            // 4. Asociar producto (idProducto = 1 asumido)
            int resAsoc = dao.asociarProducto(p.getIdPromocion(), 1);
            System.out.println("Asociar producto 1: " + (resAsoc == 1 ? "Exito" : "Error"));

            // 5. Listar productos asociados
            List<Integer> prods = dao.listarProductosPorPromocion(p.getIdPromocion());
            System.out.println("Productos asociados a la promoción: " + prods);

            // 6. Desasociar producto
            int resDes = dao.desasociarProducto(p.getIdPromocion(), 1);
            System.out.println("Desasociar producto 1: " + (resDes == 1 ? "Exito" : "Error"));

            // 7. Listar todos
            List<Promocion> todas = dao.listarTodos();
            System.out.println("Listar todas las promociones. Cantidad: " + todas.size());

            // 8. Listar vigentes
            List<Promocion> vigentes = dao.listarVigentes();
            System.out.println("Listar promociones vigentes. Cantidad: " + vigentes.size());

            // 9. Eliminar
            int resElim = dao.eliminar(p.getIdPromocion());
            System.out.println("Eliminar promoción: " + (resElim == 1 ? "Exito" : "Error"));
        }
    }

    private static void pruebaDevolucion() {
        System.out.println("\n------------ PRUEBA DEVOLUCIÓN ------------");
        DevolucionImpl dao = new DevolucionImpl();

        // 1. Insertar
        Devolucion d = new Devolucion();
        d.setIdDevolucion(1); // asumiendo que existe venta con id 1
        d.setIdProducto(1);
        d.setIdTrabajador(1);
        d.setEstadoDevolucion("Pendiente");
        d.setCantidad(5);
        d.setMotivo("Producto dañado");
        d.setFechaHora(LocalDateTime.now());
        d.setActivo(true);

        int resIns = dao.insertar(d);
        System.out.println("Insertar devolución: " + (resIns == 1 ? "Exito ID: " + d.getIdDevolucion() : "Error"));

        if (resIns == 1) {
            // 2. Modificar
            d.setEstadoDevolucion("Aprobada");
            int resMod = dao.modificar(d);
            System.out.println("Modificar devolución: " + (resMod == 1 ? "Exito" : "Error"));

            // 3. Buscar
            Devolucion dBuscada = dao.buscarPorID(d.getIdDevolucion());
            System.out.println("Buscar devolución: " + (dBuscada != null ? "Encontrada, Estado: " + dBuscada.getEstadoDevolucion() : "No encontrada"));

            // 4. Listar todas
            List<Devolucion> todas = dao.listarTodos();
            System.out.println("Listar todas las devoluciones. Cantidad: " + todas.size());

            // 5. Listar por fechas
            List<Devolucion> porFechas = dao.listarPorFechas(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
            System.out.println("Listar devoluciones por fecha. Cantidad: " + porFechas.size());

            // 6. Eliminar
            int resElim = dao.eliminar(d.getIdDevolucion());
            System.out.println("Eliminar devolución: " + (resElim == 1 ? "Exito" : "Error"));
        }
    }

    private static void pruebaMovimientoInventario() {
        System.out.println("\n------------ PRUEBA MOVIMIENTO INVENTARIO ------------");
        MovimientoInventarioImpl dao = new MovimientoInventarioImpl();

        // 1. Insertar (Log inmutable)
        MovimientoInventario mov = new MovimientoInventario();
        mov.setIdProducto(1);
        mov.setIdTrabajador(1);
        mov.setTipoMovimiento("Entrada");
        mov.setCantidad(50);
        mov.setStockAnterior(10);
        mov.setStockResultante(60);
        mov.setMotivo("Reabastecimiento");
        mov.setFechaHora(LocalDateTime.now());
        //mov.setUsuarioCreacion(1); EN CASO SE AGREGE ID DE USUARIO

        int resIns = dao.insertar(mov);
        System.out.println("Insertar movimiento (Log): " + (resIns == 1 ? "Exito ID: " + mov.getIdMovimiento() : "Error"));

        if (resIns == 1) {
            // 2. Modificar (Debe dar mensaje de error custom y retornar 0)
            int resMod = dao.modificar(mov);
            System.out.println("Intentar modificar log inmutable, resultado: " + resMod);

            // 3. Eliminar (Debe dar mensaje de error custom y retornar 0)
            int resElim = dao.eliminar(mov.getIdMovimiento());
            System.out.println("Intentar eliminar log inmutable, resultado: " + resElim);

            // 4. Buscar
            MovimientoInventario mBuscado = dao.buscarPorID(mov.getIdMovimiento());
            System.out.println("Buscar movimiento: " + (mBuscado != null ? "Encontrado, Tipo: " + mBuscado.getTipoMovimiento() : "No encontrado"));

            // 5. Listar todos
            List<MovimientoInventario> todos = dao.listarTodos();
            System.out.println("Listar todos los movimientos. Cantidad: " + todos.size());

            // 6. Listar por producto
            List<MovimientoInventario> porProd = dao.listarPorProducto(1);
            System.out.println("Listar movimientos por producto 1. Cantidad: " + porProd.size());

            // 7. Listar por fechas
            List<MovimientoInventario> porFechas = dao.listarPorFechas(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
            System.out.println("Listar movimientos por fecha. Cantidad: " + porFechas.size());
        }
    }

    private static void pruebaCategoria() {
        System.out.println("\n------------ PRUEBA CATEGORÍA ------------");
        CategoriaImpl dao = new CategoriaImpl();

        // 1. Insertar categoría padre
        CategoriaDto categoriaPadre = new CategoriaDto();
        categoriaPadre.setNombre("Bebidas");
        categoriaPadre.setDescripcion("Categoría principal de bebidas");
        categoriaPadre.setCategoriaPadre(null); // No tiene padre
        categoriaPadre.setEstado(true);

        int resInsPadre = dao.insertar(categoriaPadre);
        System.out.println("Insertar categoría padre: " + (resInsPadre == 1 ? "Éxito ID: " + categoriaPadre.getIdCategoria() : "Error"));

        if (resInsPadre == 1) {
            // 2. Insertar categoría hija
            CategoriaDto categoriaHija = new CategoriaDto();
            categoriaHija.setNombre("Gaseosas");
            categoriaHija.setDescripcion("Bebidas gaseosas");
            categoriaHija.setCategoriaPadre(categoriaPadre); // Tiene como padre a "Bebidas"
            categoriaHija.setEstado(true);

            int resInsHija = dao.insertar(categoriaHija);
            System.out.println("Insertar categoría hija: " + (resInsHija == 1 ? "Éxito ID: " + categoriaHija.getIdCategoria() : "Error"));

            if (resInsHija == 1) {
                // 3. Modificar categoría hija
                categoriaHija.setNombre("Bebidas Gaseosas");
                categoriaHija.setDescripcion("Bebidas carbonatadas y refrescantes");
                int resMod = dao.modificar(categoriaHija);
                System.out.println("Modificar categoría: " + (resMod == 1 ? "Éxito" : "Error"));

                // 4. Buscar por ID
                CategoriaDto cBuscada = dao.buscarPorID(categoriaHija.getIdCategoria());
                System.out.println("Buscar categoría: " + (cBuscada != null ?
                        "Encontrada: " + cBuscada.getNombre() + " - " + cBuscada.getDescripcion() :
                        "No encontrada"));

                // 5. Listar todas
                List<CategoriaDto> todas = dao.listarTodos();
                System.out.println("Listar todas las categorías. Cantidad: " + todas.size());
                for (CategoriaDto cat : todas) {
                    System.out.println("  - ID: " + cat.getIdCategoria() +
                            " | Nombre: " + cat.getNombre() +
                            " | Padre: " + (cat.getCategoriaPadre() != null ?
                            cat.getCategoriaPadre().getIdCategoria() : "Ninguno"));
                }

                // 6. Eliminar categoría hija
                int resElimHija = dao.eliminar(categoriaHija.getIdCategoria());
                System.out.println("Eliminar categoría hija: " + (resElimHija == 1 ? "Éxito (marcada como inactiva)" : "Error"));

                // 7. Verificar que fue eliminada (búsqueda debe retornar null o inactiva)
                CategoriaDto cEliminada = dao.buscarPorID(categoriaHija.getIdCategoria());
                System.out.println("Verificar eliminación: " +
                        (cEliminada == null || !cEliminada.isEstado() ?
                                "Categoría inactiva/no encontrada correctamente" :
                                "ERROR: Categoría sigue activa"));
            }

            // 8. Eliminar categoría padre
            int resElimPadre = dao.eliminar(categoriaPadre.getIdCategoria());
            System.out.println("Eliminar categoría padre: " + (resElimPadre == 1 ? "Éxito (marcada como inactiva)" : "Error"));
        }
    }

    private static void pruebaProducto() {
        System.out.println("\n------------ PRUEBA PRODUCTO ------------");
        ProductoImpl daoProducto = new ProductoImpl();
        CategoriaImpl daoCategoria = new CategoriaImpl();

        // Primero necesitamos una categoría activa para asociar al producto
        CategoriaDto categoria = new CategoriaDto();
        categoria.setNombre("Snacks");
        categoria.setDescripcion("Productos para picar");
        categoria.setCategoriaPadre(null);
        categoria.setEstado(true);

        int resInsCat = daoCategoria.insertar(categoria);
        System.out.println("Insertar categoría para productos: " + (resInsCat == 1 ? "Éxito ID: " + categoria.getIdCategoria() : "Error"));

        if (resInsCat == 1) {
            // 1. Insertar producto
            ProductoDto producto = new ProductoDto();
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

            int resIns = daoProducto.insertar(producto);
            System.out.println("Insertar producto: " + (resIns == 1 ? "Éxito ID: " + producto.getIdProducto() : "Error"));

            if (resIns == 1) {
                // 2. Modificar producto
                producto.setNombre("Papas Lays Original 200g");
                producto.setPrecioUnitario(7.00);
                producto.setStockMinimo(15);
                int resMod = daoProducto.modificar(producto);
                System.out.println("Modificar producto: " + (resMod == 1 ? "Éxito" : "Error"));

                // 3. Buscar por ID
                ProductoDto pBuscado = daoProducto.buscarPorID(producto.getIdProducto());
                System.out.println("Buscar producto: " + (pBuscado != null ?
                        "Encontrado: " + pBuscado.getNombre() +
                                " | Precio: S/ " + pBuscado.getPrecioUnitario() +
                                " | Categoría: " + pBuscado.getCategoria().getNombre() :
                        "No encontrado"));

                // 4. Verificar stock bajo (método de negocio)
                if (pBuscado != null) {
                    System.out.println("¿Tiene stock bajo?: " + pBuscado.tieneStockBajo());
                }

                // 5. Listar todos
                List<ProductoDto> todos = daoProducto.listarTodos();
                System.out.println("Listar todos los productos. Cantidad: " + todos.size());
                for (ProductoDto p : todos) {
                    System.out.println("  - ID: " + p.getIdProducto() +
                            " | Nombre: " + p.getNombre() +
                            " | Precio: S/ " + p.getPrecioUnitario() +
                            " | Stock: " + p.getStock() +
                            " | Categoría: " + p.getCategoria().getNombre());
                }

                // 6. Eliminar producto
                int resElim = daoProducto.eliminar(producto.getIdProducto());
                System.out.println("Eliminar producto: " + (resElim == 1 ? "Éxito (marcado como inactivo)" : "Error"));

                // 7. Verificar que fue eliminado
                ProductoDto pEliminado = daoProducto.buscarPorID(producto.getIdProducto());
                System.out.println("Verificar eliminación: " +
                        (pEliminado == null || !pEliminado.isEstado() ?
                                "Producto inactivo/no encontrado correctamente" :
                                "ERROR: Producto sigue activo"));
            }

            // Limpiar: eliminar la categoría de prueba
            daoCategoria.eliminar(categoria.getIdCategoria());
            System.out.println("Categoría de prueba eliminada");
        }
    }
    // =========================================================
    //  MÉTODO PAGO
    // =========================================================
    private static void pruebaMetodoPago() {
        System.out.println("------------ PRUEBA MÉTODO DE PAGO ------------");
        MetodoPagoDtoImpl dao = new MetodoPagoDtoImpl();

        MetodoPagoDto mp = new MetodoPagoDto();
        mp.setNombre("Yape");
        int resIns = dao.insertar(mp);
        System.out.println("Insertar método de pago: " + (resIns == 1 ? "Éxito ID: " + mp.getIdMetodoPago() : "Error"));

        if (resIns == 1) {
            MetodoPagoDto mpBuscado = dao.buscarPorID(mp.getIdMetodoPago());
            System.out.println("Buscar método de pago: " + (mpBuscado != null ? mpBuscado.getNombre() : "No encontrado"));

            mp.setNombre("Yape Modificado");
            int resMod = dao.modificar(mp);
            System.out.println("Modificar método de pago: " + (resMod == 1 ? "Éxito" : "Error"));

            List<MetodoPagoDto> lista = dao.listarTodos();
            System.out.println("Listar todos los métodos de pago. Cantidad: " + lista.size());

            int resElim = dao.eliminar(mp.getIdMetodoPago());
            System.out.println("Eliminar método de pago: " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    // =========================================================
    //  VENTA
    // =========================================================
    private static void pruebaVenta() {
        System.out.println("\n------------ PRUEBA VENTA ------------");
        VentaDtoImpl dao = new VentaDtoImpl();

        ClienteDto cliente = new ClienteDto();
        cliente.setIdCliente(1);

        TrabajadorDto trabajador = new TrabajadorDto();
        trabajador.setIdTrabajador(1);

        MetodoPagoDto metodoPago = new MetodoPagoDto();
        metodoPago.setIdMetodoPago(1);

        VentaDto venta = new VentaDto();
        venta.setCliente(cliente);
        venta.setTrabajador(trabajador);
        venta.setMetodoPago(metodoPago);
        venta.setCanalVenta(CanalVenta.PRESENCIAL);
        venta.setObservaciones("Venta de prueba");
        int resIns = dao.insertar(venta);
        idVentaCreada = venta.getIdVenta(); // guarda el ID
        System.out.println("Insertar venta: " + (resIns == 1 ? "Éxito ID: " + venta.getIdVenta() : "Error"));

        if (resIns == 1) {
            VentaDto ventaBuscada = dao.buscarPorID(venta.getIdVenta());
            System.out.println("Buscar venta: " + (ventaBuscada != null
                    ? "Encontrada, Canal: " + ventaBuscada.getCanalVenta() : "No encontrada"));

            int resMod = dao.modificar(venta);
            System.out.println("Completar venta (modificar): " + (resMod == 1 ? "Éxito" : "Error"));

            List<VentaDto> lista = dao.listarTodos();
            System.out.println("Listar todas las ventas. Cantidad: " + lista.size());

            // int resElim = dao.eliminar(venta.getIdVenta());
            // System.out.println("Anular venta (eliminar): " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    // =========================================================
    //  DETALLE VENTA
    // =========================================================
    private static void pruebaDetalleVenta() {
        System.out.println("\n------------ PRUEBA DETALLE VENTA ------------");
        DetalleVentaDtoImpl dao = new DetalleVentaDtoImpl();

        ProductoDto producto = new ProductoDto();
        producto.setIdProducto(1);

        DetalleVentaDto detalle = new DetalleVentaDto();
        detalle.setIdPadreVenta(idVentaCreada); // ID de la venta creada en pruebaVenta()
        detalle.setProducto(producto);
        detalle.setCantidad(3);
        int resIns = dao.insertar(detalle);
        System.out.println("Insertar detalle venta: " + (resIns == 1 ? "Éxito ID: " + detalle.getIdDetalleVenta() : "Error"));

        if (resIns == 1) {
            DetalleVentaDto detalleBuscado = dao.buscarPorID(detalle.getIdDetalleVenta());
            System.out.println("Buscar detalle venta: " + (detalleBuscado != null
                    ? "Encontrado, Cantidad: " + detalleBuscado.getCantidad() : "No encontrado"));

            detalle.setCantidad(5);
            int resMod = dao.modificar(detalle);
            System.out.println("Modificar detalle venta: " + (resMod == 1 ? "Éxito" : "Error"));

            List<DetalleVentaDto> lista = dao.listarTodos();
            System.out.println("Listar todos los detalles de venta. Cantidad: " + lista.size());

            int resElim = dao.eliminar(detalle.getIdDetalleVenta());
            System.out.println("Eliminar detalle venta: " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    // =========================================================
    //  PEDIDO
    // =========================================================
    private static void pruebaPedido() {
        System.out.println("\n------------ PRUEBA PEDIDO ------------");
        PedidoDtoImpl dao = new PedidoDtoImpl();

        ClienteDto cliente = new ClienteDto();
        cliente.setIdCliente(1);

        PedidoDto pedido = new PedidoDto();
        pedido.setCliente(cliente);
        pedido.setDireccionEntrega("Av. Universitaria 1801, San Miguel");
        pedido.setModalidadVenta(ModalidadVenta.DELIVERY);
        pedido.setObservaciones("Pedido de prueba");
        int resIns = dao.insertar(pedido);
        idPedidoCreado = pedido.getIdPedido();
        System.out.println("Insertar pedido: " + (resIns == 1 ? "Éxito ID: " + pedido.getIdPedido() : "Error"));

        if (resIns == 1) {
            PedidoDto pedidoBuscado = dao.buscarPorID(pedido.getIdPedido());
            System.out.println("Buscar pedido: " + (pedidoBuscado != null
                    ? "Encontrado, Estado: " + pedidoBuscado.getEstadoPedido() : "No encontrado"));

            pedido.setEstadoPedido(EstadoPedido.EN_PROCESO);
            int resMod = dao.modificar(pedido);
            System.out.println("Modificar estado pedido: " + (resMod == 1 ? "Éxito" : "Error"));

            List<PedidoDto> lista = dao.listarTodos();
            System.out.println("Listar todos los pedidos. Cantidad: " + lista.size());

            // int resElim = dao.eliminar(pedido.getIdPedido());
            // System.out.println("Eliminar pedido: " + (resElim == 1 ? "Éxito" : "Error"));
        }
    }

    // =========================================================
    //  DETALLE PEDIDO
    // =========================================================
    private static void pruebaDetallePedido() {
        System.out.println("\n------------ PRUEBA DETALLE PEDIDO ------------");
        DetallePedidoDtoImpl dao = new DetallePedidoDtoImpl();

        ProductoDto producto = new ProductoDto();
        producto.setIdProducto(1);

        DetallePedidoDto detalle = new DetallePedidoDto();
        detalle.setIdPadrePedido(idPedidoCreado); // ID de un pedido existente en la BD
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        int resIns = dao.insertar(detalle);
        System.out.println("Insertar detalle pedido: " + (resIns == 1 ? "Éxito ID: " + detalle.getIdDetallePedido() : "Error"));

        if (resIns == 1) {
            DetallePedidoDto detalleBuscado = dao.buscarPorID(detalle.getIdDetallePedido());
            System.out.println("Buscar detalle pedido: " + (detalleBuscado != null
                    ? "Encontrado, Cantidad: " + detalleBuscado.getCantidad() : "No encontrado"));

            detalle.setCantidad(4);
            int resMod = dao.modificar(detalle);
            System.out.println("Modificar detalle pedido: " + (resMod == 1 ? "Éxito" : "Error"));

            List<DetallePedidoDto> lista = dao.listarTodos();
            System.out.println("Listar todos los detalles de pedido. Cantidad: " + lista.size());

            int resElim = dao.eliminar(detalle.getIdDetallePedido());
            System.out.println("Eliminar detalle pedido: " + (resElim == 1 ? "Éxito" : "Error"));
        }

    }
}
