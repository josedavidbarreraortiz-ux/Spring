-- Migration script to update existing users with role "CLIENTE" to "USER"
-- This fixes the 403 Forbidden error for users created before the role fix

-- Update all users with role "CLIENTE" to "USER"
UPDATE users 
SET role = 'USER' 
WHERE role = 'CLIENTE';

-- Verify the update
SELECT id, name, email, role, enabled 
FROM users 
WHERE role IN ('USER', 'ADMIN');
