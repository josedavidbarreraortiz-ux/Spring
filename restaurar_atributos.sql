-- ============================================================
-- SCRIPT PARA RESTAURAR ATRIBUTOS
-- Ejecutar después de limpiar la base de datos
-- ============================================================

-- Insertar atributos
INSERT INTO `atributos` (`atributo_id`, `atributo_nombre`, `atributo_tipo`) VALUES
	(1, 'Color', 'color'),
	(2, 'Tamaño', 'seleccion'),
	(3, 'Capacidad', 'seleccion'),
	(4, 'Material', 'seleccion'),
	(5, 'Peso', 'numero');

-- Insertar valores de atributos (colores)
INSERT INTO `atributo_valores` (`valor_id`, `atributo_id`, `valor`) VALUES
	(1, 1, 'Negro'),
	(2, 1, 'Blanco'),
	(3, 1, 'Rojo'),
	(4, 1, 'Azul'),
	(5, 1, 'Plateado'),
	(6, 1, 'Morado');

SELECT 'Atributos restaurados exitosamente' AS Resultado;
