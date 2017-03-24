-- Host: 127.0.0.1    Database: swarmer
-- ------------------------------------------------------
-- Server version	5.5.5-10.2.4-MariaDB

SET GLOBAL time_zone="+01:00";
CREATE DATABASE IF NOT EXISTS swarmer;
USE swarmer;

DROP TABLE IF EXISTS authentication_nodes;
CREATE TABLE authentication_nodes(
	id VARCHAR(255) NOT NULL,
	user_count INT UNSIGNED NOT NULL,
	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS lobby_nodes;
CREATE TABLE lobby_nodes(
	id VARCHAR(255) NOT NULL,
	user_count INT UNSIGNED NOT NULL,
	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS game_nodes;
CREATE TABLE game_nodes(
	id VARCHAR(255) NOT NULL,
	user_count INT UNSIGNED NOT NULL,
	PRIMARY KEY(id)
);

DROP TABLE IF EXISTS user_information;
CREATE TABLE user_information(
  id VARCHAR(255) NOT NULL,
	user_name VARCHAR(255) NOT NULL,
	user_password VARCHAR(255) NOT NULL,
	PRIMARY KEY(id)
);
