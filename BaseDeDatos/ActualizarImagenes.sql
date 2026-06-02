-- ============================================================
-- ActualizarImagenes.sql
-- Asigna URLs de imagen a los productos de prueba.
-- Ejecutar una sola vez después de ShiligamaInserts.sql.
-- Las URLs son imágenes públicas de Unsplash (sin clave API).
-- ============================================================
USE shiligama;

-- Desactivar safe update mode (MySQL Workbench lo activa por defecto)
SET SQL_SAFE_UPDATES = 0;

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1586201375761-83865001e31c?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000001';  -- Arroz Costeño 5kg

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1510130387422-82bed34b37e9?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000002';  -- Lentejas 500g

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000003';  -- Aceite Primor 1L

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000004';  -- Fideos Don Vittorio

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000005';  -- Coca-Cola 1.5L

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000006';  -- Frugos Durazno 1L

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000007';  -- San Luis sin gas 625ml

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1582735689369-4fe89db7114c?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000008';  -- Ariel 2kg

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1585421514738-01798e348b17?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000009';  -- Poett Lavanda 900ml

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1621939514649-280e2ee25f60?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000010';  -- Azúcar Rubia 1kg

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1563636619-e9143da7973b?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000011';  -- Leche Gloria Entera 1L

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000012';  -- Yogurt Gloria Fresa 1kg

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000013';  -- Pringles Original 149g

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000014';  -- Galletas Oreo 176g

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1619280984807-a27b4d7f0a72?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000015';  -- Sal Marina 1kg

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1534482421-64566f976cfa?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000016';  -- Atún Florida 170g

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000017';  -- Inca Kola 1.5L

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1629203432180-71e9f76a3e57?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000018';  -- Pepsi 500ml

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000019';  -- Néctar Pulp Mango 1L

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000020';  -- Agua San Mateo 625ml

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1622916819787-2605d47a50d1?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000021';  -- Shampoo Head & Shoulders 200ml

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1600857544200-b2f666a9a2ec?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000022';  -- Jabón Dove 90g x3

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1558449028-b53a39d100fc?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000023';  -- Bolsa Basura Grande x10

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000024';  -- Aceite Vegetal Ideal 900ml

UPDATE producto SET IMAGEN_URL = 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=300&h=300&fit=crop'
WHERE CODIGO_BARRAS = '7750001000025';  -- Fideos Tallarin N°5 500g

-- Restaurar safe update mode
SET SQL_SAFE_UPDATES = 1;

-- Verificar
SELECT ID_PRODUCTO, NOMBRE, IMAGEN_URL FROM producto ORDER BY ID_PRODUCTO;
