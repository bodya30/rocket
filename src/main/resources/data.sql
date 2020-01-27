-- If the record exists, it will be overwritten or if it does not yet exist, it will be created
USE rocket;
REPLACE INTO authority SET name = 'ROLE_USER';
REPLACE INTO authority SET name = 'ROLE_ADMIN';
REPLACE INTO authority SET name = 'ROLE_SOCIAL_USER';