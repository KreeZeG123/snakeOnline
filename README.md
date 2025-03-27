# SQL INIT (Mysql)

## Create Specified Mysql User
```
CREATE USER 'javaSnakeOnlineUser'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'javaSnakeOnline';
CREATE DATABASE snake_online_db;
GRANT SELECT, INSERT, UPDATE, DELETE ON snake_online_db.* TO 'javaSnakeOnlineUser'@'localhost';
FLUSH PRIVILEGES;
```

# Create Base Tables
```
USE snake_online_db;
CREATE TABLE joueurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    nb_pieces INT DEFAULT 0,
    skins VARCHAR(511) NOT NULL,
    score INT DEFAULT 0,
    date_inscription DATE NOT NULL DEFAULT (CURRENT_DATE)
);
```
