-- Database upgrade script for MYSQL

-- Openbravo POS v2.30.2 -> NORD POS mobi v1.0.0

SET foreign_key_checks = 0;
SET @unique_category_code=UUID();
UPDATE CATEGORIES SET ID=@unique_category_code WHERE id='000';
UPDATE PRODUCTS SET CATEGORY=@unique_category_code WHERE CATEGORY='000';
SET foreign_key_checks = 1;

ALTER TABLE CATEGORIES ADD COLUMN CODE VARCHAR(255);
