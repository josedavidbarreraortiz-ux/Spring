-- =========================================
-- SCRIPT DE CORRECCIÓN DE BASE DE DATOS
-- Agrega constraints de FK faltantes
-- =========================================

USE `fh2`;

-- 1. Agregar FK de venta_detalle a ventas
ALTER TABLE `venta_detalle`
ADD CONSTRAINT `fk_venta_detalle_venta` 
FOREIGN KEY (`venta_codigo`) REFERENCES `ventas` (`venta_codigo`) 
ON DELETE CASCADE;

-- 2. Agregar FK de pagos a ventas
ALTER TABLE `pagos`
ADD CONSTRAINT `fk_pagos_venta` 
FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`venta_codigo`) 
ON DELETE CASCADE;

-- Verificación
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE
    TABLE_SCHEMA = 'fh2'
    AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME;
