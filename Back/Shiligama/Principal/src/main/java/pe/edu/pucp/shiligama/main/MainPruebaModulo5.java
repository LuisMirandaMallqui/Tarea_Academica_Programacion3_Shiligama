package pe.edu.pucp.shiligama.main;

import pe.edu.pucp.model.operaciones.Promocion;
import pe.edu.pucp.model.operaciones.Devolucion;
import pe.edu.pucp.model.operaciones.MovimientoInventario;
import pe.edu.pucp.model.producto.CategoriaDto;
import pe.edu.pucp.model.producto.ProductoDto;
import pe.edu.pucp.persistance.dao.operaciones.Impl.PromocionImpl;
import pe.edu.pucp.persistance.dao.operaciones.Impl.DevolucionImpl;
import pe.edu.pucp.persistance.dao.operaciones.Impl.MovimientoInventarioImpl;
import pe.edu.pucp.persistance.dao.producto.Impl.CategoriaImpl;
import pe.edu.pucp.persistance.dao.producto.Impl.ProductoImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainPruebaModulo5 {

    public static void main(String[] args) {
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
}
