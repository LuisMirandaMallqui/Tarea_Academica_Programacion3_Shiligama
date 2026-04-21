package pe.edu.pucp.shiligama;

import pe.edu.pucp.model.operaciones.Promocion;
import pe.edu.pucp.model.operaciones.Devolucion;
import pe.edu.pucp.model.operaciones.MovimientoInventario;
import pe.edu.pucp.persistance.dao.operaciones.Impl.PromocionImpl;
import pe.edu.pucp.persistance.dao.operaciones.Impl.DevolucionImpl;
import pe.edu.pucp.persistance.dao.operaciones.Impl.MovimientoInventarioImpl;

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
        d.setIdVenta(1); // asumiendo que existe venta con id 1
        d.setMotivo("Producto dañado");
        d.setFechaSolicitud(LocalDateTime.now());
        d.setEstado("Pendiente");
        
        int resIns = dao.insertar(d);
        System.out.println("Insertar devolución: " + (resIns == 1 ? "Exito ID: " + d.getIdDevolucion() : "Error"));

        if (resIns == 1) {
            // 2. Modificar
            d.setEstado("Aprobada");
            int resMod = dao.modificar(d);
            System.out.println("Modificar devolución: " + (resMod == 1 ? "Exito" : "Error"));

            // 3. Buscar
            Devolucion dBuscada = dao.buscarPorID(d.getIdDevolucion());
            System.out.println("Buscar devolución: " + (dBuscada != null ? "Encontrada, Estado: " + dBuscada.getEstado() : "No encontrada"));

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
        mov.setUsuarioCreacion(1);

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
}
