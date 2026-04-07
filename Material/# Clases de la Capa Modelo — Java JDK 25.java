# Clases de la Capa Modelo — Java JDK 25
## Proyecto: Shiligama (Sistema de Gestión de Minimarket)

---

## Estructura de Paquetes

```
pe.edu.pucp.shiligama.model/
├── usuario/
│   ├── Usuario.java          (abstract)
│   ├── Cliente.java           (extends Usuario)
│   ├── Administrador.java     (extends Usuario)
│   └── Trabajador.java        (extends Usuario)
├── producto/
│   ├── Categoria.java
│   └── Producto.java
├── venta/
│   ├── Venta.java
│   ├── DetalleVenta.java
│   ├── Pedido.java
│   ├── DetallePedido.java
│   ├── MetodoPago.java
│   └── Boleta.java
├── proveedor/
│   ├── Proveedor.java
│   ├── ProductoProveedor.java
│   ├── OrdenReabastecimiento.java
│   ├── DetalleOrdenReabastecimiento.java
│   ├── MovimientoInventario.java
│   ├── Promocion.java
│   ├── PromocionProducto.java
│   ├── Turno.java
│   ├── Devolucion.java
│   └── AlertaStock.java
└── enums/
    ├── CanalVenta.java
    ├── EstadoVenta.java
    ├── EstadoPedido.java
    ├── TipoMovimiento.java
    ├── EstadoOrden.java
    ├── TipoDescuento.java
    └── TipoDevolucion.java
```

---

## 1. ENUMS

```java
// ============================================================
// pe.edu.pucp.shiligama.model.enums.CanalVenta.java
// ============================================================
package pe.edu.pucp.shiligama.model.enums;

public enum CanalVenta {
    PRESENCIAL,
    WHATSAPP
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.enums.EstadoVenta.java
// ============================================================
package pe.edu.pucp.shiligama.model.enums;

public enum EstadoVenta {
    REGISTRADA,
    COMPLETADA,
    ANULADA
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.enums.EstadoPedido.java
// ============================================================
package pe.edu.pucp.shiligama.model.enums;

public enum EstadoPedido {
    RECIBIDO,
    EN_PROCESO,
    ATENDIDO,
    RECHAZADO,
    CANCELADO
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.enums.TipoMovimiento.java
// ============================================================
package pe.edu.pucp.shiligama.model.enums;

public enum TipoMovimiento {
    ENTRADA,
    SALIDA,
    AJUSTE,
    DEVOLUCION
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.enums.EstadoOrden.java
// ============================================================
package pe.edu.pucp.shiligama.model.enums;

public enum EstadoOrden {
    PENDIENTE,
    ENVIADA,
    RECIBIDA,
    CANCELADA
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.enums.TipoDescuento.java
// ============================================================
package pe.edu.pucp.shiligama.model.enums;

public enum TipoDescuento {
    PORCENTAJE,
    MONTO_FIJO
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.enums.TipoDevolucion.java
// ============================================================
package pe.edu.pucp.shiligama.model.enums;

public enum TipoDevolucion {
    CLIENTE,
    MERMA,
    VENCIMIENTO,
    DEFECTO
}
```

---

## 2. ENTIDADES_USUARIO (con herencia)

```
Jerarquía de herencia (CORRECTA — Table-per-Subclass en BD):

             ┌──────────────┐
             │   Usuario    │  (abstract)
             │  {abstract}  │
             └──────┬───────┘
                    │
        ┌───────────┼───────────┐
        │           │           │
┌───────┴──┐  ┌─────┴──────┐  ┌┴───────────┐
│ Cliente  │  │Administrador│  │ Trabajador │
└──────────┘  └────────────┘  └────────────┘

Sobre el Rol: No necesitas una entidad separada.
La herencia YA define el rol (polimorfismo → obtenerRol()).
Trabajador NO extiende Administrador: viola IS-A y LSP.
Cada subclase tiene su propia tabla en MySQL (joined strategy).
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.usuario.Usuario.java
// ============================================================
package pe.edu.pucp.shiligama.model.usuario;

import java.time.LocalDateTime;

public abstract class Usuario {
    protected int idUsuario;
    protected String nombreUsuario;
    protected String contrasena;
    protected String nombres;
    protected String apellidos;
    protected String dni;
    protected String telefono;
    protected String email;
    protected String direccion;
    protected boolean estado;
    protected LocalDateTime fechaCreacion;

    // Constructor vacío (necesario para persistencia)
    public Usuario() {
    }

    // Constructor completo
    public Usuario(int idUsuario, String nombreUsuario, String contrasena,
                   String nombres, String apellidos, String dni,
                   String telefono, String email, String direccion,
                   boolean estado, LocalDateTime fechaCreacion) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // Métodos de negocio (declarados, se implementan en capa de negocio)
    public abstract String obtenerRol();
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.usuario.Cliente.java
// ============================================================
package pe.edu.pucp.shiligama.model.usuario;

import java.time.LocalDateTime;

public class Cliente extends Usuario {
    private int idCliente;
    private String telefonoWhatsapp;
    private String direccionEntrega;
    private LocalDateTime fechaRegistro;

    // Constructor vacío
    public Cliente() {
        super();
    }

    // Constructor completo
    public Cliente(int idUsuario, String nombreUsuario, String contrasena,
                   String nombres, String apellidos, String dni,
                   String telefono, String email, String direccion,
                   boolean estado, LocalDateTime fechaCreacion,
                   int idCliente, String telefonoWhatsapp,
                   String direccionEntrega, LocalDateTime fechaRegistro) {
        super(idUsuario, nombreUsuario, contrasena, nombres, apellidos,
              dni, telefono, email, direccion, estado, fechaCreacion);
        this.idCliente = idCliente;
        this.telefonoWhatsapp = telefonoWhatsapp;
        this.direccionEntrega = direccionEntrega;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getTelefonoWhatsapp() { return telefonoWhatsapp; }
    public void setTelefonoWhatsapp(String telefonoWhatsapp) { this.telefonoWhatsapp = telefonoWhatsapp; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // Implementación del método abstracto (polimorfismo)
    @Override
    public String obtenerRol() {
        return "CLIENTE";
    }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.usuario.Administrador.java
// ============================================================
package pe.edu.pucp.shiligama.model.usuario;

import java.time.LocalDateTime;

public class Administrador extends Usuario {
    private int idAdministrador;

    // Constructor vacío
    public Administrador() {
        super();
    }

    // Constructor completo
    public Administrador(int idUsuario, String nombreUsuario, String contrasena,
                         String nombres, String apellidos, String dni,
                         String telefono, String email, String direccion,
                         boolean estado, LocalDateTime fechaCreacion,
                         int idAdministrador) {
        super(idUsuario, nombreUsuario, contrasena, nombres, apellidos,
              dni, telefono, email, direccion, estado, fechaCreacion);
        this.idAdministrador = idAdministrador;
    }

    // Getters y Setters
    public int getIdAdministrador() { return idAdministrador; }
    public void setIdAdministrador(int idAdministrador) { this.idAdministrador = idAdministrador; }

    // Implementación del método abstracto (polimorfismo)
    @Override
    public String obtenerRol() {
        return "ADMINISTRADOR";
    }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.usuario.Trabajador.java
// ============================================================
package pe.edu.pucp.shiligama.model.usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Trabajador extiende directamente de Usuario (NO de Administrador).
// Un Trabajador NO ES-UN Administrador → violación de IS-A / LSP.
public class Trabajador extends Usuario {
    private int idTrabajador;
    private String cargo;
    private LocalDate fechaIngreso;

    // Constructor vacío
    public Trabajador() {
        super();
    }

    // Constructor completo
    public Trabajador(int idUsuario, String nombreUsuario, String contrasena,
                      String nombres, String apellidos, String dni,
                      String telefono, String email, String direccion,
                      boolean estado, LocalDateTime fechaCreacion,
                      int idTrabajador, String cargo, LocalDate fechaIngreso) {
        super(idUsuario, nombreUsuario, contrasena, nombres, apellidos,
              dni, telefono, email, direccion, estado, fechaCreacion);
        this.idTrabajador = idTrabajador;
        this.cargo = cargo;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters
    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    // Implementación del método abstracto (polimorfismo)
    @Override
    public String obtenerRol() {
        return "TRABAJADOR";
    }
}
```

---

## 3. ENTIDADES_PRODUCTO

```java
// ============================================================
// pe.edu.pucp.shiligama.model.producto.Categoria.java
// ============================================================
package pe.edu.pucp.shiligama.model.producto;

public class Categoria {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private Categoria categoriaPadre;
    private boolean estado;

    // Constructor vacío
    public Categoria() {
    }

    // Constructor completo
    public Categoria(int idCategoria, String nombre, String descripcion,
                     Categoria categoriaPadre, boolean estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoriaPadre = categoriaPadre;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Categoria getCategoriaPadre() { return categoriaPadre; }
    public void setCategoriaPadre(Categoria categoriaPadre) { this.categoriaPadre = categoriaPadre; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.producto.Producto.java
// ============================================================
package pe.edu.pucp.shiligama.model.producto;

import java.time.LocalDateTime;

public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int stock;
    private int stockMinimo;
    private String unidadMedida;
    private String codigoBarras;
    private String imagenUrl;
    private boolean estado;
    private LocalDateTime fechaRegistro;
    private Categoria categoria;

    // Constructor vacío
    public Producto() {
    }

    // Constructor completo
    public Producto(int idProducto, String nombre, String descripcion,
                    double precioUnitario, int stock, int stockMinimo,
                    String unidadMedida, String codigoBarras,
                    String imagenUrl, boolean estado,
                    LocalDateTime fechaRegistro, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.unidadMedida = unidadMedida;
        this.codigoBarras = codigoBarras;
        this.imagenUrl = imagenUrl;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    // Métodos de negocio (declarados)
    public boolean tieneStockBajo() { return stock <= stockMinimo; }
}
```

---

## 4. ENTIDADES_VENTA

```java
// ============================================================
// pe.edu.pucp.shiligama.model.venta.MetodoPago.java
// ============================================================
package pe.edu.pucp.shiligama.model.venta;

public class MetodoPago {
    private int idMetodoPago;
    private String nombre;
    private String descripcion;
    private boolean estado;

    // Constructor vacío
    public MetodoPago() {
    }

    // Constructor completo
    public MetodoPago(int idMetodoPago, String nombre,
                      String descripcion, boolean estado) {
        this.idMetodoPago = idMetodoPago;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.venta.Venta.java
// ============================================================
package pe.edu.pucp.shiligama.model.venta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.shiligama.model.enums.CanalVenta;
import pe.edu.pucp.shiligama.model.enums.EstadoVenta;
import pe.edu.pucp.shiligama.model.usuario.Cliente;
import pe.edu.pucp.shiligama.model.usuario.Trabajador;

public class Venta {
    private int idVenta;
    private LocalDateTime fechaHora;
    private double montoTotal;
    private double montoDescuento;
    private CanalVenta canalVenta;
    private EstadoVenta estadoVenta;
    private String observaciones;
    private Cliente cliente;
    private Trabajador trabajador;
    private MetodoPago metodoPago;
    private List<DetalleVenta> detalles;

    // Constructor vacío
    public Venta() {
        this.detalles = new ArrayList<>();
    }

    // Constructor completo
    public Venta(int idVenta, LocalDateTime fechaHora, double montoTotal,
                 double montoDescuento, CanalVenta canalVenta,
                 EstadoVenta estadoVenta, String observaciones,
                 Cliente cliente, Trabajador trabajador,
                 MetodoPago metodoPago) {
        this.idVenta = idVenta;
        this.fechaHora = fechaHora;
        this.montoTotal = montoTotal;
        this.montoDescuento = montoDescuento;
        this.canalVenta = canalVenta;
        this.estadoVenta = estadoVenta;
        this.observaciones = observaciones;
        this.cliente = cliente;
        this.trabajador = trabajador;
        this.metodoPago = metodoPago;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public double getMontoDescuento() { return montoDescuento; }
    public void setMontoDescuento(double montoDescuento) { this.montoDescuento = montoDescuento; }

    public CanalVenta getCanalVenta() { return canalVenta; }
    public void setCanalVenta(CanalVenta canalVenta) { this.canalVenta = canalVenta; }

    public EstadoVenta getEstadoVenta() { return estadoVenta; }
    public void setEstadoVenta(EstadoVenta estadoVenta) { this.estadoVenta = estadoVenta; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Trabajador getTrabajador() { return trabajador; }
    public void setTrabajador(Trabajador trabajador) { this.trabajador = trabajador; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }

    // Métodos de negocio (declarados)
    public void agregarDetalle(DetalleVenta detalle) { }
    public void eliminarDetalle(DetalleVenta detalle) { }
    public double calcularMontoTotal() { return 0; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.venta.DetalleVenta.java
// ============================================================
package pe.edu.pucp.shiligama.model.venta;

import pe.edu.pucp.shiligama.model.producto.Producto;

public class DetalleVenta {
    private int idDetalleVenta;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private Venta venta;
    private Producto producto;

    // Constructor vacío
    public DetalleVenta() {
    }

    // Constructor completo
    public DetalleVenta(int idDetalleVenta, int cantidad,
                        double precioUnitario, double subtotal,
                        Venta venta, Producto producto) {
        this.idDetalleVenta = idDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.venta = venta;
        this.producto = producto;
    }

    // Getters y Setters
    public int getIdDetalleVenta() { return idDetalleVenta; }
    public void setIdDetalleVenta(int idDetalleVenta) { this.idDetalleVenta = idDetalleVenta; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    // Métodos de negocio
    public double calcularSubtotal() { return cantidad * precioUnitario; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.venta.Pedido.java
// ============================================================
package pe.edu.pucp.shiligama.model.venta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.shiligama.model.enums.EstadoPedido;
import pe.edu.pucp.shiligama.model.usuario.Cliente;

public class Pedido {
    private int idPedido;
    private LocalDateTime fechaHora;
    private double montoTotal;
    private EstadoPedido estadoPedido;
    private int prioridad;
    private String direccionEntrega;
    private String observaciones;
    private String payloadJson;
    private Cliente cliente;
    private Venta venta;
    private List<DetallePedido> detalles;

    // Constructor vacío
    public Pedido() {
        this.detalles = new ArrayList<>();
    }

    // Constructor completo
    public Pedido(int idPedido, LocalDateTime fechaHora, double montoTotal,
                  EstadoPedido estadoPedido, int prioridad,
                  String direccionEntrega, String observaciones,
                  String payloadJson, Cliente cliente, Venta venta) {
        this.idPedido = idPedido;
        this.fechaHora = fechaHora;
        this.montoTotal = montoTotal;
        this.estadoPedido = estadoPedido;
        this.prioridad = prioridad;
        this.direccionEntrega = direccionEntrega;
        this.observaciones = observaciones;
        this.payloadJson = payloadJson;
        this.cliente = cliente;
        this.venta = venta;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public EstadoPedido getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(EstadoPedido estadoPedido) { this.estadoPedido = estadoPedido; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    // Métodos de negocio (declarados)
    public void agregarDetalle(DetallePedido detalle) { }
    public boolean verificarDisponibilidad() { return false; }
    public Venta convertirAVenta() { return null; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.venta.DetallePedido.java
// ============================================================
package pe.edu.pucp.shiligama.model.venta;

import pe.edu.pucp.shiligama.model.producto.Producto;

public class DetallePedido {
    private int idDetallePedido;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private boolean disponible;
    private Pedido pedido;
    private Producto producto;

    // Constructor vacío
    public DetallePedido() {
    }

    // Constructor completo
    public DetallePedido(int idDetallePedido, int cantidad,
                         double precioUnitario, double subtotal,
                         boolean disponible, Pedido pedido,
                         Producto producto) {
        this.idDetallePedido = idDetallePedido;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.disponible = disponible;
        this.pedido = pedido;
        this.producto = producto;
    }

    // Getters y Setters
    public int getIdDetallePedido() { return idDetallePedido; }
    public void setIdDetallePedido(int idDetallePedido) { this.idDetallePedido = idDetallePedido; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    // Métodos de negocio
    public double calcularSubtotal() { return cantidad * precioUnitario; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.venta.Boleta.java
// ============================================================
package pe.edu.pucp.shiligama.model.venta;

import java.time.LocalDateTime;

public class Boleta {
    private int idBoleta;
    private String numeroBoleta;
    private LocalDateTime fechaEmision;
    private double montoTotal;
    private boolean sincronizado;
    private Venta venta;

    // Constructor vacío
    public Boleta() {
    }

    // Constructor completo
    public Boleta(int idBoleta, String numeroBoleta,
                  LocalDateTime fechaEmision, double montoTotal,
                  boolean sincronizado, Venta venta) {
        this.idBoleta = idBoleta;
        this.numeroBoleta = numeroBoleta;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.sincronizado = sincronizado;
        this.venta = venta;
    }

    // Getters y Setters
    public int getIdBoleta() { return idBoleta; }
    public void setIdBoleta(int idBoleta) { this.idBoleta = idBoleta; }

    public String getNumeroBoleta() { return numeroBoleta; }
    public void setNumeroBoleta(String numeroBoleta) { this.numeroBoleta = numeroBoleta; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public boolean isSincronizado() { return sincronizado; }
    public void setSincronizado(boolean sincronizado) { this.sincronizado = sincronizado; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
}
```

---

## 5. ENTIDADES_PROVEEDOR y SOPORTE

```java
// ============================================================
// pe.edu.pucp.shiligama.model.proveedor.Proveedor.java
// ============================================================
package pe.edu.pucp.shiligama.model.proveedor;

public class Proveedor {
    private int idProveedor;
    private String razonSocial;
    private String ruc;
    private String telefono;
    private String email;
    private String direccion;
    private String contacto;
    private boolean estado;

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor completo
    public Proveedor(int idProveedor, String razonSocial, String ruc,
                     String telefono, String email, String direccion,
                     String contacto, boolean estado) {
        this.idProveedor = idProveedor;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.contacto = contacto;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.proveedor.ProductoProveedor.java
// ============================================================
package pe.edu.pucp.shiligama.model.proveedor;

import pe.edu.pucp.shiligama.model.producto.Producto;

public class ProductoProveedor {
    private Producto producto;
    private Proveedor proveedor;
    private double precioCompra;
    private int tiempoEntrega;

    // Constructor vacío
    public ProductoProveedor() {
    }

    // Constructor completo
    public ProductoProveedor(Producto producto, Proveedor proveedor,
                             double precioCompra, int tiempoEntrega) {
        this.producto = producto;
        this.proveedor = proveedor;
        this.precioCompra = precioCompra;
        this.tiempoEntrega = tiempoEntrega;
    }

    // Getters y Setters
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public int getTiempoEntrega() { return tiempoEntrega; }
    public void setTiempoEntrega(int tiempoEntrega) { this.tiempoEntrega = tiempoEntrega; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.proveedor.OrdenReabastecimiento.java
// ============================================================
package pe.edu.pucp.shiligama.model.proveedor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.shiligama.model.enums.EstadoOrden;
import pe.edu.pucp.shiligama.model.usuario.Trabajador;

public class OrdenReabastecimiento {
    private int idOrden;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEntregaEstimada;
    private EstadoOrden estadoOrden;
    private String observaciones;
    private Proveedor proveedor;
    private Trabajador trabajador;
    private List<DetalleOrdenReabastecimiento> detalles;

    // Constructor vacío
    public OrdenReabastecimiento() {
        this.detalles = new ArrayList<>();
    }

    // Constructor completo
    public OrdenReabastecimiento(int idOrden, LocalDateTime fechaCreacion,
                                 LocalDate fechaEntregaEstimada,
                                 EstadoOrden estadoOrden, String observaciones,
                                 Proveedor proveedor, Trabajador trabajador) {
        this.idOrden = idOrden;
        this.fechaCreacion = fechaCreacion;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estadoOrden = estadoOrden;
        this.observaciones = observaciones;
        this.proveedor = proveedor;
        this.trabajador = trabajador;
        this.detalles = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

    public EstadoOrden getEstadoOrden() { return estadoOrden; }
    public void setEstadoOrden(EstadoOrden estadoOrden) { this.estadoOrden = estadoOrden; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public Trabajador getTrabajador() { return trabajador; }
    public void setTrabajador(Trabajador trabajador) { this.trabajador = trabajador; }

    public List<DetalleOrdenReabastecimiento> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenReabastecimiento> detalles) { this.detalles = detalles; }

    // Métodos de negocio (declarados)
    public void agregarDetalle(DetalleOrdenReabastecimiento detalle) { }
    public void recibirOrden() { }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.proveedor.DetalleOrdenReabastecimiento.java
// ============================================================
package pe.edu.pucp.shiligama.model.proveedor;

import pe.edu.pucp.shiligama.model.producto.Producto;

public class DetalleOrdenReabastecimiento {
    private int idDetalleOrden;
    private int cantidadSolicitada;
    private int cantidadRecibida;
    private double precioCompra;
    private OrdenReabastecimiento orden;
    private Producto producto;

    // Constructor vacío
    public DetalleOrdenReabastecimiento() {
    }

    // Constructor completo
    public DetalleOrdenReabastecimiento(int idDetalleOrden,
                                        int cantidadSolicitada,
                                        int cantidadRecibida,
                                        double precioCompra,
                                        OrdenReabastecimiento orden,
                                        Producto producto) {
        this.idDetalleOrden = idDetalleOrden;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadRecibida = cantidadRecibida;
        this.precioCompra = precioCompra;
        this.orden = orden;
        this.producto = producto;
    }

    // Getters y Setters
    public int getIdDetalleOrden() { return idDetalleOrden; }
    public void setIdDetalleOrden(int idDetalleOrden) { this.idDetalleOrden = idDetalleOrden; }

    public int getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(int cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }

    public int getCantidadRecibida() { return cantidadRecibida; }
    public void setCantidadRecibida(int cantidadRecibida) { this.cantidadRecibida = cantidadRecibida; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public OrdenReabastecimiento getOrden() { return orden; }
    public void setOrden(OrdenReabastecimiento orden) { this.orden = orden; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.proveedor.MovimientoInventario.java
// ============================================================
package pe.edu.pucp.shiligama.model.proveedor;

import java.time.LocalDateTime;
import pe.edu.pucp.shiligama.model.enums.TipoMovimiento;
import pe.edu.pucp.shiligama.model.producto.Producto;
import pe.edu.pucp.shiligama.model.usuario.Trabajador;

public class MovimientoInventario {
    private int idMovimiento;
    private TipoMovimiento tipoMovimiento;
    private int cantidad;
    private int stockAnterior;
    private int stockResultante;
    private String motivo;
    private LocalDateTime fechaHora;
    private Producto producto;
    private Trabajador trabajador;

    // Constructor vacío
    public MovimientoInventario() {
    }

    // Constructor completo
    public MovimientoInventario(int idMovimiento, TipoMovimiento tipoMovimiento,
                                int cantidad, int stockAnterior,
                                int stockResultante, String motivo,
                                LocalDateTime fechaHora,
                                Producto producto, Trabajador trabajador) {
        this.idMovimiento = idMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockResultante = stockResultante;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.producto = producto;
        this.trabajador = trabajador;
    }

    // Getters y Setters
    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getStockAnterior() { return stockAnterior; }
    public void setStockAnterior(int stockAnterior) { this.stockAnterior = stockAnterior; }

    public int getStockResultante() { return stockResultante; }
    public void setStockResultante(int stockResultante) { this.stockResultante = stockResultante; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Trabajador getTrabajador() { return trabajador; }
    public void setTrabajador(Trabajador trabajador) { this.trabajador = trabajador; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.promocion.java
// ============================================================
package pe.edu.pucp.shiligama.model.promocion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.shiligama.model.enums.TipoDescuento;
import pe.edu.pucp.shiligama.model.producto.Producto;

public class Promocion {
    private int idPromocion;
    private String nombre;
    private String descripcion;
    private TipoDescuento tipoDescuento;
    private double valorDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String condiciones;
    private boolean estado;
    private List<Producto> productos;

    // Constructor vacío
    public Promocion() {
        this.productos = new ArrayList<>();
    }

    // Constructor completo
    public Promocion(int idPromocion, String nombre, String descripcion,
                     TipoDescuento tipoDescuento, double valorDescuento,
                     LocalDate fechaInicio, LocalDate fechaFin,
                     String condiciones, boolean estado) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoDescuento = tipoDescuento;
        this.valorDescuento = valorDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.condiciones = condiciones;
        this.estado = estado;
        this.productos = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdPromocion() { return idPromocion; }
    public void setIdPromocion(int idPromocion) { this.idPromocion = idPromocion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public TipoDescuento getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(TipoDescuento tipoDescuento) { this.tipoDescuento = tipoDescuento; }

    public double getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(double valorDescuento) { this.valorDescuento = valorDescuento; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    // Métodos de negocio (declarados)
    public boolean estaVigente() { return false; }
    public double calcularDescuento(double montoBase) { return 0; }
}
```

```java
// ============================================================
// pe.edu.pucp.shiligama.model.promocionProducto.java
// ============================================================
package pe.edu.pucp.shiligama.model.promocion;

import pe.edu.pucp.shiligama.model.producto.Producto;

public class PromocionProducto {
    private Promocion promocion;
    private Producto producto;

    // Constructor vacío
    public PromocionProducto() {
    }

    // Constructor completo
    public PromocionProducto(Promocion promocion, Producto producto) {
        this.promocion = promocion;
        this.producto = producto;
    }

    // Getters y Setters
    public Promocion getPromocion() { return promocion; }
    public void setPromocion(Promocion promocion) { this.promocion = promocion; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
```


```java
// ============================================================
// pe.edu.pucp.shiligama.model.proveedor.AlertaStock.java
// ============================================================
package pe.edu.pucp.shiligama.model.proveedor;

import java.time.LocalDateTime;
import pe.edu.pucp.shiligama.model.producto.Producto;

public class AlertaStock {
    private int idAlerta;
    private LocalDateTime fechaHora;
    private int stockActual;
    private int stockMinimo;
    private boolean atendida;
    private Producto producto;

    // Constructor vacío
    public AlertaStock() {
    }

    // Constructor completo
    public AlertaStock(int idAlerta, LocalDateTime fechaHora,
                       int stockActual, int stockMinimo,
                       boolean atendida, Producto producto) {
        this.idAlerta = idAlerta;
        this.fechaHora = fechaHora;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.atendida = atendida;
        this.producto = producto;
    }

    // Getters y Setters
    public int getIdAlerta() { return idAlerta; }
    public void setIdAlerta(int idAlerta) { this.idAlerta = idAlerta; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public boolean isAtendida() { return atendida; }
    public void setAtendida(boolean atendida) { this.atendida = atendida; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
```

---

## Resumen de Principios OOP Aplicados

| Principio | Dónde se aplica |
|-----------|-----------------|
| **Encapsulamiento** | Todos los atributos `protected`/`private` con getters/setters públicos |
| **Herencia** | `Usuario (abstract) → Cliente`, `Usuario → Administrador → Trabajador` |
| **Polimorfismo** | Método `obtenerRol()` — cada subclase retorna su propio rol |
| **Abstracción** | `Usuario` es `abstract`, no se puede instanciar directamente |
| **Composición** | `Venta` contiene `List<DetalleVenta>`, `Pedido` contiene `List<DetallePedido>` |
| **Navegabilidad** | Referencias entre objetos (ej: `Venta.cliente`, `DetalleVenta.producto`) |
| **Enumerados** | 7 enums para estados finitos en lugar de Strings sueltos |

### Total: 21 clases + 7 enums = 28 archivos Java