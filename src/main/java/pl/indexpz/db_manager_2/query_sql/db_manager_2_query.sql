BEGIN;
CREATE DATABASE IF NOT EXISTS db_workshop2
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
COMMIT;

USE db_workshop2;

BEGIN;
CREATE TABLE users
(
    id       INT AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
COMMIT;

SELECT * FROM users;

SELECT * FROM users WHERE id = ?;

INSERT INTO users (name, email, password) VALUES (?, ?, ?);

UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?;

DELETE FROM users WHERE id = ?;
