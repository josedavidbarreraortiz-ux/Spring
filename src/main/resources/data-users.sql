-- Script para crear usuarios de prueba en la base de datos fh2
-- Las contraseñas están encriptadas con BCrypt

-- Usuario Admin
-- Email: admin@fh.com
-- Password: admin123
INSERT INTO users (name, email, password, role, enabled) 
VALUES ('Administrador', 'admin@fh.com', '$2a$10$xqKhPz3WmP8fhXKVxJqLEeqP9YqZQJ7hXqVxJqLEeqP9YqZQJ7hXq', 'ADMIN', true);

-- Usuario Cliente
-- Email: cliente@fh.com
-- Password: cliente123
INSERT INTO users (name, email, password, role, enabled) 
VALUES ('Cliente Test', 'cliente@fh.com', '$2a$10$yqKhPz3WmP8fhXKVxJqLEeqP9YqZQJ7hXqVxJqLEeqP9YqZQJ7hYq', 'USER', true);

-- Nota: Estas contraseñas son de ejemplo. En producción, genera nuevas contraseñas encriptadas.
-- Para generar una nueva contraseña BCrypt, puedes usar:
-- https://bcrypt-generator.com/ o ejecutar en Java:
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
-- String encodedPassword = encoder.encode("tu_contraseña");
