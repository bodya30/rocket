USE rocket_test;
INSERT INTO user(id, first_name, last_name, email, password, enabled) VALUES (1, 'firstName', 'lastName', 'test@email.com', 'password', TRUE );
INSERT INTO authority(id, name) VALUES (1, 'ROLE_USER');
INSERT INTO user_authority(user_id, authority_id) VALUES (1 ,1);
INSERT INTO token(id, token, user, expiration) VALUES (1, 'token', 1, NOW() + INTERVAL 1 DAY);