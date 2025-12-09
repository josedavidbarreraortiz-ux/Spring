-- ============================================================
-- SCRIPT PARA ELIMINAR TABLAS NO USADAS (Laravel/Duplicadas)
-- Base de datos: fh2
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ========== TABLAS DE LARAVEL (no se usan en Spring Boot) ==========

-- Spatie Laravel Permissions
DROP TABLE IF EXISTS `role_has_permissions`;
DROP TABLE IF EXISTS `model_has_roles`;
DROP TABLE IF EXISTS `model_has_permissions`;
DROP TABLE IF EXISTS `permissions`;
DROP TABLE IF EXISTS `roles`;

-- Laravel Core
DROP TABLE IF EXISTS `failed_jobs`;
DROP TABLE IF EXISTS `migrations`;
DROP TABLE IF EXISTS `password_reset_tokens`;
DROP TABLE IF EXISTS `personal_access_tokens`;

-- ========== TABLAS DUPLICADAS ==========

-- metodo_pago está duplicada (la correcta es metodos_pago)
DROP TABLE IF EXISTS `pago`;
DROP TABLE IF EXISTS `metodo_pago`;

SET FOREIGN_KEY_CHECKS = 1;

-- Verificar tablas restantes
SELECT 'Tablas eliminadas exitosamente. Tablas restantes en la BD:' AS Mensaje;
SHOW TABLES;
