-- Script para limpiar usuarios y clientes y empezar de cero
-- ADVERTENCIA: Esto borrará TODOS los usuarios y clientes excepto el admin

-- 1. Primero deshabilitamos las verificaciones de claves foráneas
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Borramos todas las ventas y sus detalles (dependen de clientes)
DELETE FROM venta_detalle;
DELETE FROM pagos;
DELETE FROM ventas;

-- 3. Borramos todos los clientes
DELETE FROM cliente;

-- 4. Borramos todos los usuarios EXCEPTO el admin
DELETE FROM users WHERE email != 'admin@fh.com';

-- 5. Borramos las relaciones de roles de usuarios que ya no existen
DELETE FROM model_has_roles WHERE model_id NOT IN (SELECT id FROM users);

-- 6. Reactivamos las verificaciones de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- 7. Verificamos que solo quede el admin
SELECT id, name, email, role, enabled FROM users;

-- 8. Verificamos que no haya clientes
SELECT COUNT(*) as total_clientes FROM cliente;

-- 9. Verificamos que no haya ventas
SELECT COUNT(*) as total_ventas FROM ventas;

-- RESULTADO ESPERADO:
-- - Solo debe existir el usuario admin@fh.com
-- - 0 clientes
-- - 0 ventas
