-- ============================================================
-- SCRIPT PARA LIMPIAR BASE DE DATOS FH2
-- Elimina todos los datos relacionados excepto:
-- - metodos_pago
-- - atributos y valores de atributos (color, talla, etc.)
-- - usuario admin
-- ============================================================

-- Desactivar verificación de claves foráneas temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- ========== ELIMINAR DATOS DE VENTAS ==========
DELETE FROM venta_detalle;
DELETE FROM pagos;
DELETE FROM ventas;

-- ========== ELIMINAR DATOS DE INVENTARIO Y MOVIMIENTOS ==========
DELETE FROM movimientos_inventario;
DELETE FROM inventario;

-- ========== ELIMINAR DATOS DE PRODUCTOS ==========
DELETE FROM producto_garantias;
DELETE FROM producto_garantia;
DELETE FROM variante_atributos;
DELETE FROM producto_variantes;
DELETE FROM producto_atributos;
DELETE FROM producto_categoria;
DELETE FROM productos;

-- ========== NO SE ELIMINAN ATRIBUTOS NI VALORES DE ATRIBUTOS ==========
-- (Se mantienen color, talla, y otros atributos)

-- ========== ELIMINAR DATOS DE CATEGORÍAS ==========
DELETE FROM categoria;

-- ========== ELIMINAR DATOS DE GARANTÍAS ==========
DELETE FROM garantias;

-- ========== ELIMINAR DATOS DE CLIENTES ==========
DELETE FROM pqrs;
DELETE FROM cliente;

-- ========== ELIMINAR DATOS DE USUARIOS (excepto admin) ==========
DELETE FROM model_has_roles WHERE model_id NOT IN (SELECT id FROM users WHERE email = 'admin@fh.com');
DELETE FROM users WHERE email != 'admin@fh.com';

-- ========== ELIMINAR DATOS DE PROVEEDORES Y ÓRDENES ==========
DELETE FROM ordenes_compra;
DELETE FROM proveedores;

-- ========== ELIMINAR UBICACIONES ==========
DELETE FROM ubicacion;

-- ========== RESETEAR AUTO_INCREMENT ==========
ALTER TABLE venta_detalle AUTO_INCREMENT = 1;
ALTER TABLE ventas AUTO_INCREMENT = 1;
ALTER TABLE inventario AUTO_INCREMENT = 1;
ALTER TABLE productos AUTO_INCREMENT = 1;
ALTER TABLE categoria AUTO_INCREMENT = 1;
ALTER TABLE cliente AUTO_INCREMENT = 1;
-- (users no se resetea para mantener el admin)
-- (atributos y atributo_valores no se resetean porque se mantienen)

-- Reactivar verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- Mensaje de confirmación
SELECT 'Base de datos limpiada. Se mantuvieron: metodos_pago, atributos, valores_atributos y usuario admin' AS Resultado;
