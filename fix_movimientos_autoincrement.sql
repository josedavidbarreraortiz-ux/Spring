-- ============================================================
-- SCRIPT PARA AGREGAR AUTO_INCREMENT A movimientos_inventario
-- Ejecutar en HeidiSQL antes de usar la aplicación
-- ============================================================

-- Modificar la columna movimiento_id para que sea AUTO_INCREMENT
ALTER TABLE movimientos_inventario 
MODIFY COLUMN movimiento_id INT AUTO_INCREMENT;

SELECT 'Tabla movimientos_inventario actualizada correctamente' AS Resultado;
