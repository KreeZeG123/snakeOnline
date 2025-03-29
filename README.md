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
CREATE TABLE items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image VARCHAR(255) NOT NULL
);
```

# INIT
```
INSERT INTO items (id, title, description, price, image)
VALUES (1, 'Skin Serpent Camouflage', 'Un magnifique skin camouflage pour votre serpent.', 100, '/images/items/skin_camouflage.png');

INSERT INTO items (id, title, description, price, image)
VALUES (2, 'Skin Serpent Néon', 'Un serpent lumineux aux couleurs vives avec un effet néon dynamique.', 150, '/images/items/skin_neon.png');

INSERT INTO items (id, title, description, price, image)
VALUES (3, 'Skin Serpent Dragon Chinois', 'Transformez votre serpent en un majestueux dragon chinois aux écailles dorées.', 200, '/images/items/skin_dragon_chinois.png');

UPDATE joueurs SET nb_pieces = 250, score = 5000 WHERE username = "Test";

INSERT INTO joueurs (username, email, mot_de_passe, nb_pieces, skins, score, date_inscription)
VALUES
('Test', 'test@test.fr', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 250, '1,', 5000, CURDATE()),
('Joueur1', 'joueur1@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 100, '', 1500, CURDATE()),
('Joueur2', 'joueur2@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 200, '', 2500, CURDATE()),
('Joueur3', 'joueur3@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 50, '', 500, CURDATE()),
('Joueur4', 'joueur4@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 120, '', 1700, CURDATE()),
('Joueur5', 'joueur5@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 300, '', 3200, CURDATE()),
('Joueur6', 'joueur6@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 80, '', 1100, CURDATE()),
('Joueur7', 'joueur7@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 500, '', 4500, CURDATE()),
('Joueur8', 'joueur8@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 220, '', 2700, CURDATE()),
('Joueur9', 'joueur9@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 90, '', 1300, CURDATE()),
('Joueur10', 'joueur10@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 400, '', 3800, CURDATE()),
('Joueur11', 'joueur11@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 150, '', 2000, CURDATE()),
('Joueur12', 'joueur12@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 60, '', 800, CURDATE()),
('Joueur13', 'joueur13@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 350, '', 3400, CURDATE()),
('Joueur14', 'joueur14@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 180, '', 2300, CURDATE()),
('Joueur15', 'joueur15@example.com', 'y/5Q1MexEiouZsjzIhXdlLaZMR9ymNV6aW++93tkOdMyEI7AFaUGcA==', 250, '', 2900, CURDATE());

```
