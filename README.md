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
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    nb_pieces INT DEFAULT 0,
    date_inscription DATE NOT NULL DEFAULT (CURRENT_DATE)
);
```

CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image VARCHAR(25) NOT NULL
);


INSERT INTO items (title, description, price, image)
VALUES ('Skin Serpent Camouflage', 'Un magnifique skin camouflage pour votre serpent.', 100, '/images/items/skin_camouflage.png');

INSERT INTO items (title, description, price, image)
VALUES ('Skin Serpent Néon', 'Un serpent lumineux aux couleurs vives avec un effet néon dynamique.', 150, '/images/items/skin_neon.png');

INSERT INTO items (title, description, price, image)
VALUES ('Skin Serpent Dragon Chinois', 'Transformez votre serpent en un majestueux dragon chinois aux écailles dorées.', 200, '/images/items/skin_dragon_chinois.png');
