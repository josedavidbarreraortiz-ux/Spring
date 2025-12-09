-- Fix existing users with NULL enabled field
-- This allows them to login successfully

UPDATE users 
SET enabled = 1 
WHERE enabled IS NULL OR enabled = 0;

-- Verify the update
SELECT id, name, email, role, enabled 
FROM users;
