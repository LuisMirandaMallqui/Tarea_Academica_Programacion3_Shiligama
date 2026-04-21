package pe.edu.pucp.shiligama.main;

import pe.edu.pucp.db.DBManager;
import pe.edu.pucp.model.operaciones.Promocion;
import pe.edu.pucp.model.operaciones.Devolucion;
import pe.edu.pucp.model.operaciones.MovimientoInventario;
import pe.edu.pucp.persistance.dao.operaciones.dao.PromocionDAO;
import pe.edu.pucp.persistance.dao.operaciones.Impl.PromocionImpl;
import pe.edu.pucp.persistance.dao.operaciones.dao.DevolucionDAO;
import pe.edu.pucp.persistance.dao.operaciones.Impl.DevolucionImpl;
import pe.edu.pucp.persistance.dao.operaciones.dao.MovimientoInventarioDAO;
import pe.edu.pucp.persistance.dao.operaciones.Impl.MovimientoInventarioImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase de prueba para todos los métodos correspondientes al Módulo 5 
 * (Operaciones y Fidelización).
 */
public class MainPruebaModulo5 {
    public static void main(String[] args) {
        System.out.println("====== INICIANDO PRUEBAS MÓDULO 5 ======");

        // Aseguramos que la conexión inicial se levanta correctamente
        DBManager.getInstance().getConnection();

        // 1. PRUEBAS PROMOCIÓN
        System.out.println("\n--- Pruebas de PROMOCIÓN ---");
        PromocionDAO promoDAO = new PromocionImpl();
        Promocion p = new Promocion();
        p.setNombre("Promo Navidad");
        p.setDescripcion("Descuento especial navideño");
        p.setTipoDescuento("PORCENTAJE");
        p.setValorDescuento(20.0);
        p.setFechaInicio(LocalDate.now());
        p.setFechaFin(LocalDate.now().plusDays(15));
        p.setCondiciones("Valido para todas las compras");
        p.setActivo(true);
        
        int rPromo = promoDAO.insertar(p);
        System.out.println("Insertar Promo: " + (rPromo == 1 ? "ÉXITO" : "ERROR") + " ID: " + p.getIdPromocion());
        
        System.out.println("Listado de promociones:");
        for(Promocion pr : promoDAO.listarTodos()){
            System.out.println("- " + pr.toString());
        }

        // 2. PRUEBAS DEVOLUCION
        System.out.println("\n--- Pruebas de DEVOLUCIÓN ---");
        DevolucionDAO devDAO = new DevolucionImpl();
        Devolucion d = new Devolucion();
        d.setIdVenta(1); // asumiendo que existe una venta 1, si hay FK puede dar error
        d.setMotivo("Producto dañado");
        d.setFechaSolicitud(LocalDateTime.now());
        d.setEstado("PENDIENTE");
        d.setActivo(true);

        int rDev = devDAO.insertar(d);
        System.out.println("Insertar Devolucion: " + (rDev == 1 ? "ÉXITO" : "ERROR") + " ID: " + d.getIdDevolucion());

        System.out.println("Listado de devoluciones:");
        for(Devolucion dev : devDAO.listarTodos()){
            System.out.println("- " + dev.toString());
        }

        // 3. PRUEBAS MOVIMIENTO INVENTARIO
        System.out.println("\n--- Pruebas de MOVIMIENTO INVENTARIO ---");
        MovimientoInventarioDAO movDAO = new MovimientoInventarioImpl();
        MovimientoInventario m = new MovimientoInventario();
        m.setIdProducto(1); // asumiendo que existe un producto con id 1
        m.setTipoMovimiento("ENTRADA");
        m.setCantidad(50);
        m.setMotivo("Nuevo Lote");
        m.setFechaMovimiento(LocalDateTime.now());
        m.setActivo(true);

        int rMov = movDAO.insertar(m);
        System.out.println("Insertar Movimiento: " + (rMov == 1 ? "ÉXITO" : "ERROR") + " ID: " + m.getIdMovimiento());

        System.out.println("Listado de movimientos:");
        for(MovimientoInventario mov : movDAO.listarTodos()){
            System.out.println("- " + mov.toString());
        }

        // Cierre general
        DBManager.getInstance().closeConnection();
        System.out.println("\n====== PRUEBAS FINALIZADAS ======");
    }
}
