package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.controller.ControllerSnakeGame;
import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.SnakeActionDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameStartDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameUpdateDTO;
import com.github.KreeZeG123.snakeOnline.utils.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Client {

    private final Gson GSON = new Gson();

    private Socket so;
    private ControllerSnakeGame controller;
    private ColorSnake clientColor;

    private PrintWriter sortieVersServeur;

    public void gestion_reception_in_game() {
        try {
            BufferedReader entreeDuClient = new BufferedReader(new InputStreamReader(so.getInputStream()));
            while (so!=null && !so.isClosed()) {
                String receivedMessage = entreeDuClient.readLine();
                Protocol receivedProtocol = Protocol.deserialize(receivedMessage);
                System.out.println("client a reçu : |" + receivedProtocol.getMessage() + "|");
                if (receivedMessage == null) {
                    System.out.println("Le serveur a coupé la connexion.");
                    break;
                } else {
                    switch (receivedProtocol.getMessage()) {
                        case "SnakeGameServerEndGame" : {
                            this.controller.update(new ArrayList<>(), null, null);
                            System.out.println("Mainserver La partie est terminée.");
                            break;
                        }
                        case "SnakeGameServerStop" : {
                            System.out.println("Le serveur a coupé la connexion.");
                            so.close();
                            break;
                        }
                        case "SnakeGameServerUpdate" : {
                            // Extraction des informations
                            GameUpdateDTO gameUpdateDTO = receivedProtocol.getData();
                            this.controller.update(
                                    gameUpdateDTO.snakes,
                                    gameUpdateDTO.items,
                                    gameUpdateDTO.snakeInfos
                            );
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void envoyerAction(String action) {
        try {
            PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);

            SnakeActionDTO newSnakeActionDTO = new SnakeActionDTO(action, this.clientColor);
            Protocol snakeActionProtocol = new Protocol(
                    "SnakeGame Client " + so.getLocalAddress(),
                    "SnakeGame Server " + so.getInetAddress(),
                    (new Date()).toString(),
                    "SnakeGameClientNewAction",
                    newSnakeActionDTO
            );

            sortie.println(snakeActionProtocol.serialize());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Client(String ip, int port) {
        try{
            // On se connecte au serveur
            so = new Socket(ip, port);
            this.sortieVersServeur = new PrintWriter(so.getOutputStream(), true);
            BufferedReader entreeDuClient = new BufferedReader(new InputStreamReader(so.getInputStream()));

            // On envoie un message de connexion
            Protocol joinGameProtocol = new Protocol(
                    "SnakeGame Client " + so.getLocalAddress(),
                    "SnakeGame Server " + so.getInetAddress(),
                    (new Date()).toString(),
                    "SnakeGameClientJoin",
                    null
            );
            sortieVersServeur.println(joinGameProtocol.serialize());

            // On attend la réponse du serveur
            String startGameJSON = entreeDuClient.readLine();

            // Extraction des informations
            Protocol startGameProtocol = GSON.fromJson(startGameJSON, Protocol.class);
            GameStartDTO startGameDTO = startGameProtocol.getData();

            // Création de la map
            InputMap inputMap = new InputMap(startGameDTO);
            this.clientColor = startGameDTO.clientColor;

            // Création de l'affichage

            this.controller = new ControllerSnakeGame(inputMap, this);

            // On crée un thread pour écouter les messages du serveur quand la partie est en cours
            Thread threadEcoute = new Thread(this::gestion_reception_in_game);
            threadEcoute.start();


            //so.close(); // on ferme la connexion
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new Client("172.20.42.192",4321);
    }
}
