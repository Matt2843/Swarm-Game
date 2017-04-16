CREATE TABLE authentication_nodes(
	id VARCHAR(255) NOT NULL,
	ip_address VARCHAR(255) NOT NULL,
	port VARCHAR(255) NOT NULL,
	user_count INT UNSIGNED NOT NULL,
	PRIMARY KEY(id)
);