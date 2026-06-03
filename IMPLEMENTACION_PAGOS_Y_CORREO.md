# Implementación: Pasarela de pago (Izipay) y Recuperación de contraseña

Esta entrega añade dos funcionalidades al backend Shiligama siguiendo la
arquitectura por capas existente (Model → DBManager → Persistance → Business →
ShiligamaWS) y el patrón DAO con stored procedures.

- **Pasarela de pago externa (Izipay)** — aprendida del proyecto Compu-Rangers.
  Cubre el RF007 (métodos de pago asociados al pedido) y el RF008 (pasarela
  externa que valida la transacción y actualiza el estado del pedido).
- **Recuperación de contraseña por correo** — aprendida del proyecto
  SanchezLovers (envío SMTP con `jakarta.mail`). Complementa el RF015 (cuentas
  de cliente).

> Nota sobre el informe: el RF008 menciona *Mercado Pago* como ejemplo; aquí se
> implementó **Izipay** (mismo patrón de pasarela con redirección + callback
> firmado) por pedido del equipo. El diseño es agnóstico: cambiar de proveedor
> solo afecta a `PasarelaPagoService` y a la configuración.

---

## 1. Cambios en base de datos

`BaseDeDatos/` (ejecutar en orden DDL → Procedimientos → Inserts):

- **`ShiligamaDDL.sql`**: nuevas tablas
  - `pago` (PEDIDO_ID, METODO_PAGO_ID, MONTO, MONEDA, ESTADO,
    REFERENCIA, ORDER_ID, FECHA_PAGO, …). **No** almacena datos de tarjeta
    (cumple PCI-DSS): solo estado, monto, método y la referencia del proveedor.
  - `token_recuperacion` (USUARIO_ID, TOKEN, EXPIRACION, USADO).
- **`ShiligamaProcedimientos.sql`**: nuevos SP
  - Pagos: `INSERTAR_PAGO`, `MODIFICAR_PAGO`, `MODIFICAR_PAGO_X_ORDER`,
    `ELIMINAR_PAGO`, `BUSCAR_PAGO_X_ID`, `BUSCAR_PAGO_X_PEDIDO`,
    `BUSCAR_PAGO_X_ORDER`, `LISTAR_PAGOS`.
  - Recuperación: `BUSCAR_USUARIO_X_CORREO`, `ACTUALIZAR_CONTRASENA`,
    `INSERTAR_TOKEN_RECUPERACION`, `BUSCAR_TOKEN_RECUPERACION`,
    `MARCAR_TOKEN_USADO`.
- **`ShiligamaInserts.sql`**: se agregó el método de pago `IZIPAY`.

---

## 2. Clases nuevas por capa

| Capa | Clase |
|------|-------|
| Model | `enums/EstadoPago`, `venta/Pago`, `venta/IniciarPagoDto`, `venta/RespuestaIniciarPago`, `seguridad/TokenRecuperacion`, `seguridad/UsuarioBasicoDto`, `seguridad/SolicitarRecuperacionDto`, `seguridad/RestablecerPasswordDto` |
| Persistance | `venta/dao/PagoDao` + `venta/Impl/PagoDaoImpl`, `usuario/dao/RecuperacionDao` + `usuario/impl/RecuperacionDaoImpl` |
| Business | `config/Config`, `venta/pasarela/PasarelaPagoService`, `venta/bo/PagoBo` + `venta/impl/PagoBoImpl`, `correo/EnvioCorreo`, `usuario/bo/RecuperacionBo` + `usuario/impl/RecuperacionBoImpl` |
| ShiligamaWS | `venta/PagoWS`, `venta/IzipayCallbackWS`, `auth/RecuperacionWS` |

`Business/pom.xml` añade `org.json` (JSON de la pasarela) y `jakarta.mail-api` +
`org.eclipse.angus:angus-mail` (envío de correo).

---

## 3. Configuración (`Business/src/main/resources/shiligama-config.properties`)

Completar antes de usar (o sobreescribir con variables de entorno en EC2, p.ej.
`IZIPAY_MERCHANT_KEY`, `CORREO_PASSWORD`):

```
izipay.create.payment.url=...   # endpoint que devuelve { "redirectionUrl": "..." }
izipay.shop.id=...
izipay.merchant.key=...          # clave HMAC para validar el callback
izipay.return.url=...            # pantalla de resultado del frontend
correo.emailOrigen=micorreo@gmail.com
correo.password=APP_PASSWORD     # contraseña de aplicación de Gmail, NO la normal
app.frontend.url=http://localhost:3000
```

---

## 4. Endpoints REST

Base: `http://<host>:8080/shiligamaws-1.0-SNAPSHOT/api`

### Pagos
- `POST /pagos/iniciar` — body `{ "idPedido": 12, "email": "cli@x.com" }`
  → registra el pago en estado `PENDIENTE` y devuelve
  `{ "exito": true, "redirectionUrl": "...", "orderId": "...", "idPago": 5 }`.
  El frontend redirige al cliente a `redirectionUrl`.
- `POST /pagos/izipay/callback` — **lo llama Izipay (IPN)**, no el frontend.
  Valida la firma HMAC-SHA256; si el estado es `AUTHORISED`/`CAPTURED`, marca el
  pago como `AUTORIZADO` y avanza el pedido a `EN_PROCESO` (equivalente a
  "Pagado"; el enum `EstadoPedido` no tiene `PAGADO`).
  Esta URL debe ser **pública** (instancia EC2).
- `GET /pagos/pedido/{id}` — estado del pago de un pedido.
- `GET /pagos/{id}` — pago por id. `GET /pagos` — listar.

### Recuperación de contraseña
- `POST /recuperacion/solicitar` — body `{ "correo": "cli@x.com" }`
  → si la cuenta existe, envía correo con enlace
  `app.frontend.url/restablecer-password?token=...` (token de un solo uso,
  expira en `correo.token.minutos`). Respuesta genérica (no revela si el correo
  existe).
- `POST /recuperacion/restablecer` — body `{ "token": "...", "nuevaContrasena": "..." }`
  → valida el token (vigente y no usado) y actualiza la contraseña.

---

## 5. Flujo de pago (resumen)

1. Cliente confirma el pedido en el checkout → frontend llama `POST /pagos/iniciar`.
2. Backend crea `pago` PENDIENTE y pide a Izipay la `redirectionUrl`.
3. Cliente paga en Izipay.
4. Izipay notifica a `POST /pagos/izipay/callback` (firma HMAC).
5. Backend valida la firma, marca `pago = AUTORIZADO` y `pedido = EN_PROCESO`.
6. Frontend consulta `GET /pagos/pedido/{id}` para mostrar el resultado.

---

## 6. Pendientes / siguientes pasos

- **Frontend (Next.js)**: hoy usa datos estáticos (`lib/products.ts`) y no está
  conectado al backend. Falta crear el cliente HTTP que llame a `/pagos/iniciar`,
  redirija a `redirectionUrl` y a las pantallas `solicitar/restablecer` contraseña.
- **Credenciales Izipay y Gmail**: completar `shiligama-config.properties`.
- **Compilación**: requiere Java 25 + Maven (`mvn clean install` desde
  `Back/Shiligama`). El entorno de generación no tenía Maven/Java 25, por lo que
  no se ejecutó el build; se revisó la consistencia SP↔DAO y de firmas.
- **Seguridad**: el sistema guarda contraseñas en texto plano (igual que el login
  actual). Se recomienda migrar a hash (BCrypt) y servir todo por HTTPS (RNF006).
