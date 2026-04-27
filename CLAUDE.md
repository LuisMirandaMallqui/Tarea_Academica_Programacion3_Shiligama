# Shiligama — Sistema de Gestión de Minimarket

## Contexto del proyecto
Tarea Académica del curso Programación 3 (1INF30) — PUCP 2026-1.
Profesor: Dr. Freddy Alberto Paz Espinoza.
Equipo: Team Script (5 integrantes).
Sistema web para el minimarket "Shiligama's Minimarket" ubicado en Jr. Santa Sabina 210, Cercado de Lima.

## Arquitectura
Monolítica por capas (Maven multi-módulo). Java 25 + IntelliJ IDEA. MySQL 8+ en AWS RDS.

### Módulos Maven (orden de compilación):
1. **Model** — DTOs (POJOs) y enums. Paquete base: `pe.edu.pucp.model`
2. **DBManager** — Singleton para conexión MySQL. Paquete: `pe.edu.pucp.db`
3. **Persistance** — Interfaces DAO + implementaciones (Impl). Paquete: `pe.edu.pucp.persistance`
4. **Business** — Capa de negocio (validaciones, reglas). Paquete: `pe.edu.pucp.bo` / `pe.edu.pucp.ventas`
5. **Principal** — Main de pruebas. Paquete: `pe.edu.pucp.shiligama.main`

### Patrón DAO (estilo profesor Paz):
- **IDAO<T>**: interfaz genérica con `insertar`, `modificar`, `eliminar`, `buscarPorID`, `listarTodos`
- **XxxDAO**: interfaz específica que extiende IDAO<XxxDto> (puede agregar métodos extra)
- **XxxDaoImpl**: implementación concreta
- **DAOImplBase**: clase base abstracta heredada del ciclo anterior (template method para SELECTs)

### Convenciones de código:
- **DML (INSERT/UPDATE/DELETE)**: usar `CallableStatement` con stored procedures: `{call NOMBRE_SP(?,?)}`
- **SELECT (buscarPorID/listarTodos)**: usar `PreparedStatement` directo o heredar de DAOImplBase
- **Parámetros OUT**: `cs.registerOutParameter("_id", Types.INTEGER)` para obtener IDs generados
- **Soft delete**: actualizar campo ACTIVO a 0, nunca DELETE físico
- **Nombres de SP**: INSERTAR_X, MODIFICAR_X, ELIMINAR_X, BUSCAR_X_POR_ID, LISTAR_X
- **Cierre de recursos**: siempre en bloque `finally` con try-catch individual

### Conexión a BD:
```java
con = DBManager.getInstance().getConnection();
// ... operaciones
// finally: cs.close(), con.close()
```

### Base de datos MySQL:
- Schema: `shiligama`
- 14 tablas principales (ver BaseDeDatos/ShiligamaDDL.sql)
- Stored procedures en BaseDeDatos/ShiligamaProcedimientos.sql
- Triggers de auditoría en BaseDeDatos/ShiligamaTriggers.sql
- Datos iniciales en BaseDeDatos/ShiligamaInserts.sql

### Tablas principales:
usuarios (padre), clientes, trabajadores, administradores (herencia joined),
categorias (jerárquica), productos, movimientos_inventario (inmutable),
devoluciones, metodos_pago, ventas, detalles_venta,
pedidos, detalles_pedido, promociones, promociones_productos

### Enums Java ↔ ENUM MySQL:
- CanalVenta: PRESENCIAL, WEB
- EstadoVenta: REGISTRADA, COMPLETADA, ANULADA
- EstadoPedido: RECIBIDO, EN_PROCESO, ATENDIDO, RECHAZADO, CANCELADO
- ModalidadVenta: DELIVERY, RECOJO_TIENDA
- TipoMovimiento: ENTRADA, SALIDA, AJUSTE, DEVOLUCION
- TipoDescuento: PORCENTAJE, MONTO_FIJO
- TipoDevolucion: CLIENTE, MERMA, VENCIMIENTO, DEFECTO

## Estructura de carpetas
```
Tarea_Academica_Programacion3/
├── Back/Shiligama/           ← Proyecto Maven
│   ├── Model/                ← DTOs y enums
│   ├── DBManager/            ← Singleton conexión BD
│   ├── Persistance/          ← DAO + Impl + DAOImplBase
│   ├── Business/             ← Capa lógica de negocio (en desarrollo)
│   └── Principal/            ← Main de pruebas
├── BaseDeDatos/              ← Scripts SQL
│   ├── ShiligamaDDL.sql
│   ├── ShiligamaProcedimientos.sql
│   ├── ShiligamaTriggers.sql
│   ├── ShiligamaInserts.sql
│   └── ShiligamaDER_PNG.png
└── Front/                    ← (Futuro: C# Blazor)
```

## Módulos de negocio (sin proveedores — eliminado del alcance):
1. Usuarios (herencia: Cliente, Trabajador, Administrador)
2. Catálogo (Categorías + Productos)
3. Inventario (MovimientoInventario — log inmutable)
4. Ventas (MetodoPago, Venta, DetalleVenta)
5. Pedidos (Pedido, DetallePedido — portal web)
6. Promociones (Promocion, PromocionProducto)
7. Devoluciones (Devolucion)

## Estado actual
- Model: 100% (DTOs + 7 enums alineados con MySQL). Archivos a eliminar manualmente: `operaciones/PromocionProducto.java` (duplicado), `enums/EstadoOrden.java` (huérfano).
- DBManager: 100% (Singleton funcional, conectado a AWS RDS)
- Persistance: ~98% (13 Impl codificados; 9 usan DaoImplBase, 4 independientes — ambos estilos válidos)
- Business: ~40% (Módulo Usuarios completo: ClienteBoImpl, TrabajadorBoImpl, AdministradorBoImpl; Módulo Ventas: PedidoBoImpl. Faltan: Catálogo, Inventario, Ventas-detalle, Promociones, Devoluciones)
- Principal: MainPruebaModulo5 con tests manuales (todos los módulos cubiertos)
- SQL: DDL 14 tablas + ~60 stored procedures + triggers BEFORE (llenan USUARIO_CREACION/MODIFICACION con USER()) + inserts corregidos

### Todos los Impl extienden DaoImplBase:
- Template method completo (SELECTs via PreparedStatement):
  ProductoDaoImpl, CategoriaDaoImpl, ClienteDaoImpl, TrabajadorDaoImpl, AdministradorDaoImpl,
  VentaDaoImpl, DetalleVentaDaoImpl, PedidoDaoImpl, DetallePedidoDaoImpl, MetodoPagoDaoImpl
- Solo conexión/transacciones de DaoImplBase (SELECTs via SPs con CallableStatement):
  DevolucionDaoImpl, MovimientoInventarioDaoImpl, PromocionDaoImpl

### Estructura Business (capa de negocio):
- BaseBo<T>: interfaz genérica con insertar, modificar, eliminar, buscarPorID, listarTodos
- XxxBo: interfaz específica que extiende BaseBo<XxxDto> (puede agregar métodos extra)
- XxxBoImpl: implementación con validaciones antes de delegar al DAO
- Paquetes: pe.edu.pucp.usuarios.bo / .impl, pe.edu.pucp.ventas.bo / .impl

## Pendiente para siguiente entrega (Lab 8 — Semana 8):
- Completar capa de negocio (Business) para módulos restantes: Catálogo, Inventario, Ventas, Promociones, Devoluciones
- Crear interfaces gráficas en C# con Blazor
- Script SQL completo y consolidado
