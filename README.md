# Snake Online - README

## Présentation
Snake Online est un jeu multijoueur où vous pouvez incarner un serpent et affronter d'autres joueurs en ligne. Ce projet utilise Java EE pour le serveur web et une architecture réseau pour la communication entre clients et serveur.

## Installation et Configuration

### 1. Configuration de la base de données (MySQL)
Avant de lancer le projet, il est nécessaire de créer et configurer la base de données.

#### Création de l'utilisateur et de la base de données
Exécutez les commandes SQL suivantes dans votre terminal MySQL :

```sql
CREATE USER 'javaSnakeOnlineUser'@'localhost' IDENTIFIED WITH caching_sha2_password BY 'javaSnakeOnline';
CREATE DATABASE snake_online_db;
GRANT SELECT, INSERT, UPDATE, DELETE ON snake_online_db.* TO 'javaSnakeOnlineUser'@'localhost';
FLUSH PRIVILEGES;
```

#### Création des tables

```sql
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

#### Insertion de données initiales

```sql
INSERT INTO items (id, title, description, price, image)
VALUES (1, 'Skin Serpent Camouflage', 'Un magnifique skin camouflage pour votre serpent.', 100, '/images/items/skin_camouflage.png');

INSERT INTO items (id, title, description, price, image)
VALUES (2, 'Skin Serpent Néon', 'Un serpent lumineux aux couleurs vives avec un effet néon dynamique.', 150, '/images/items/skin_neon.png');

INSERT INTO items (id, title, description, price, image)
VALUES (3, 'Skin Serpent Dragon Chinois', 'Transformez votre serpent en un majestueux dragon chinois aux écailles dorées.', 200, '/images/items/skin_dragon_chinois.png');
```

#### Insertion de données de test

```sql
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

### 2. Lancement du serveur Java EE
Dans le dossier `Web/`, vous trouverez le projet Java EE qui gère la logique côté serveur. Assurez-vous d'utiliser un serveur d'application compatible (Tomcat, GlassFish, etc.), puis déployez le projet et lancez le serveur.

### 3. Lancement du serveur de jeu
Rendez-vous dans le dossier `Reseau/`, puis exécutez `MainServer` qui se trouve dans le package `main`. Ce serveur gère les connexions des joueurs et la communication en temps réel.

### 4. Lancement du client
Pour joueur rendez-vous dans le dossier `Reseau/`, puis exécutez `MainMenu` qui se trouve dans le package `main`. Ce fichier lance l'interface graphique permettant aux joueurs de se connecter et de rejoindre une partie.

## Auteur
Ce projet a été développé par [KreeZeG123](https://github.com/KreeZeG123) et [Nathan RACCOUARD](https://github.com/Nathan-zvk).

## Licence

Ce projet est sous licence MIT - voir la licence ci-dessous pour plus de détails.

```
MIT License

Copyright (c) 2025 MANFALOTI Yamis

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


