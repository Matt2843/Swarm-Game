CREATE TABLE users (
    id VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    ranking INT UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);