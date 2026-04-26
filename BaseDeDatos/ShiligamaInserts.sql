-- =====================================================================
-- SHILIGAMA - Datos iniciales (catálogos y datos de prueba)
-- =====================================================================
USE shiligama;

-- =============================================================
-- DATOS MAESTROS / CATÁLOGOS
-- =============================================================

-- Métodos de pago
INSERT INTO metodos_pago(NOMBRE, DESCRIPCION) VALUES
('Efectivo', 'Pago en efectivo en tienda'),
('Yape', 'Pago mediante aplicación Yape'),
('Plin', 'Pago mediante aplicación Plin'),
('Tarjeta de débito', 'Pago con tarjeta de débito');

-- Categorías raíz
INSERT INTO categorias(NOMBRE, DESCRIPCION) VALUES
('Abarrotes', 'Productos de primera necesidad'),
('Bebidas', 'Bebidas con y sin alcohol'),
('Limpieza', 'Productos de limpieza del hogar'),
('Cuidado Personal', 'Productos de higiene y cuidado personal'),
('Lácteos', 'Leche, yogurt, quesos'),
('Snacks', 'Botanas, galletas, golosinas');

-- Subcategorías
INSERT INTO categorias(NOMBRE, DESCRIPCION, CATEGORIA_PADRE_ID) VALUES
('Arroz y Menestras', 'Arroz, lentejas, frejoles', 1),
('Aceites', 'Aceites vegetales y de oliva', 1),
('Fideos', 'Pastas y fideos', 1),
('Gaseosas', 'Bebidas gaseosas', 2),
('Jugos', 'Jugos y néctares', 2),
('Agua', 'Agua mineral y de mesa', 2),
('Detergentes', 'Detergentes y suavizantes', 3),
('Desinfectantes', 'Limpiadores y desinfectantes', 3);

-- =============================================================
-- DATOS DE PRUEBA
-- =============================================================

-- Administrador (contraseña: admin123 — en producción usar bcrypt)
INSERT INTO usuarios(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES( '$2a$10$placeholder_bcrypt_hash', 'Carlos', 'García López', '12345678', '987654321', 'admin@shiligama.pe');
INSERT INTO administradores(USUARIO_ID) VALUES(1);

-- Trabajadores
INSERT INTO usuarios(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('$2a$10$placeholder_bcrypt_hash', 'Juan', 'Pérez Quispe', '23456789', '987654322', 'jperez@shiligama.pe');
INSERT INTO trabajadores(USUARIO_ID, CARGO, FECHA_INGRESO) VALUES(2, 'Cajero', '2025-01-15');

INSERT INTO usuarios(CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES('$2a$10$placeholder_bcrypt_hash', 'María', 'Rodríguez Silva', '34567890', '987654323', 'mrodriguez@shiligama.pe');
INSERT INTO trabajadores(USUARIO_ID, CARGO, FECHA_INGRESO) VALUES(3, 'Almacenero', '2025-03-01');

-- Clientes
INSERT INTO usuarios( CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES( '$2a$10$placeholder_bcrypt_hash', 'Ana', 'Costa Medina', '45678901', '987654324', 'acosta@gmail.com');
INSERT INTO clientes(USUARIO_ID, DIRECCION_ENTREGA)
VALUES(4, 'Av. Los Pinos 300');

INSERT INTO usuarios( CONTRASENA, NOMBRES, APELLIDOS, DNI, TELEFONO, CORREO)
VALUES( '$2a$10$placeholder_bcrypt_hash', 'Luis', 'Huamán Torres', '56789012', '987654325', 'lhuaman@gmail.com');
INSERT INTO clientes(USUARIO_ID, DIRECCION_ENTREGA)
VALUES(5, 'Jr. Arequipa 150');

-- Productos de ejemplo
INSERT INTO productos(CATEGORIA_ID, NOMBRE, DESCRIPCION, PRECIO_UNITARIO, STOCK, STOCK_MINIMO, UNIDAD_MEDIDA, CODIGO_BARRAS) VALUES
(7, 'Arroz Costeño 5kg', 'Arroz extra graneado', 22.50, 50, 10, 'Bolsa', '7750001000001'),
(7, 'Lentejas La Costeña 500g', 'Lentejas secas', 4.50, 30, 5, 'Bolsa', '7750001000002'),
(8, 'Aceite Primor 1L', 'Aceite vegetal', 9.90, 40, 8, 'Botella', '7750001000003'),
(9, 'Fideos Don Vittorio Spaghetti', 'Fideos largos 500g', 3.80, 60, 10, 'Paquete', '7750001000004'),
(10, 'Coca-Cola 1.5L', 'Gaseosa', 7.50, 100, 20, 'Botella', '7750001000005'),
(11, 'Frugos Durazno 1L', 'Néctar de durazno', 4.20, 45, 10, 'Caja', '7750001000006'),
(12, 'San Luis sin gas 625ml', 'Agua mineral', 2.00, 80, 15, 'Botella', '7750001000007'),
(13, 'Ariel 2kg', 'Detergente en polvo', 18.90, 25, 5, 'Bolsa', '7750001000008'),
(14, 'Poett Lavanda 900ml', 'Limpiador desinfectante', 8.50, 35, 8, 'Botella', '7750001000009'),
(1, 'Azúcar Rubia 1kg', 'Azúcar rubia', 4.20, 55, 10, 'Bolsa', '7750001000010');

-- Promoción de ejemplo
INSERT INTO promociones(NOMBRE, DESCRIPCION, TIPO_DESCUENTO, VALOR_DESCUENTO, FECHA_INICIO, FECHA_FIN, CONDICIONES)
VALUES('Semana del Abarrote', 'Descuento en abarrotes seleccionados', 'PORCENTAJE', 10.00, '2026-04-20', '2026-04-27', 'Aplica en productos seleccionados');
INSERT INTO promociones_productos(PROMOCION_ID, PRODUCTO_ID) VALUES (1, 1), (1, 2), (1, 4);
