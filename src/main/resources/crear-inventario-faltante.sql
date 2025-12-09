-- Script para crear inventario para productos existentes que no lo tienen
-- Ejecutar este script en HeidiSQL contra la base de datos fh2

-- Insertar inventario para productos que no tienen registro de inventario
INSERT INTO inventario (producto_id, inventario_stock_actual, inventario_stock_minimo, inventario_stock_maximo)
SELECT 
    p.producto_id,
    0 AS inventario_stock_actual,
    5 AS inventario_stock_minimo,
    100 AS inventario_stock_maximo
FROM producto p
LEFT JOIN inventario i ON p.producto_id = i.producto_id
WHERE i.inventario_id IS NULL;

-- Verificar los productos que ahora tienen inventario
SELECT 
    p.producto_id,
    p.producto_nombre,
    COALESCE(i.inventario_stock_actual, 'SIN INVENTARIO') AS stock_actual
FROM producto p
LEFT JOIN inventario i ON p.producto_id = i.producto_id
ORDER BY p.producto_id;
