ALTER TABLE WebserverCheckEntity ADD COLUMN name VARCHAR(255) NULL;

UPDATE WebserverCheckEntity
SET name = url
WHERE name IS NULL;