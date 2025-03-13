package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.controller.ControllerSnakeGame;
import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.data.ActionData;
import com.github.KreeZeG123.snakeOnline.model.data.LoginSnakeData;
import com.github.KreeZeG123.snakeOnline.model.data.RunningGameData;
import com.github.KreeZeG123.snakeOnline.utils.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private final Gson GSON = new Gson();

    private Socket so;
    private ControllerSnakeGame controller;
    private ColorSnake clientColor;

    private PrintWriter sortieVersServeur;

    public void gestion_recepetion_in_game() {
        try {
            BufferedReader entreeDuClient = new BufferedReader(new InputStreamReader(so.getInputStream()));
            String message;
            while (true) {
                message = entreeDuClient.readLine();
                //System.out.println("client a reçu : |" + message + "|");
                if (message == null) {
                    System.out.println("Le serveur a coupé la connexion.");
                    break;
                }else if (message.equals("fin")) {
                    this.controller.update(new ArrayList<>(), null);
                    System.out.println("La partie est terminée.");
                    break;
                }
                else if (message.equals("stop")) {
                    System.out.println("Le serveur a coupé la connexion.");
                    so.close();
                    break;
                } else {
                    // Extraction des informations
                    RunningGameData parsedRunningData = GSON.fromJson(message, RunningGameData.class);
                    this.controller.update(
                            parsedRunningData.snakes,
                            parsedRunningData.items
                    );
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void envoyerAction(String action) {
        try {
            PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
            ActionData newActionData = new ActionData(action, this.clientColor);
            sortie.println(GSON.toJson(newActionData));
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
            sortieVersServeur.println("connexion");

            // On attend la réponse du serveur
            String startingInfoJSON = entreeDuClient.readLine();

            // Extraction des informations
            LoginSnakeData parsedLoginData = GSON.fromJson(startingInfoJSON, LoginSnakeData.class);

            // Création de la map
            InputMap inputMap = new InputMap(parsedLoginData);
            this.clientColor = parsedLoginData.clientColor;

            // Création de l'affichage
            this.controller = new ControllerSnakeGame(inputMap, this);

            // On crée un thread pour écouter les messages du serveur quand la partie est en cours
            Thread threadEcoute = new Thread(this::gestion_recepetion_in_game);
            threadEcoute.start();


            //so.close(); // on ferme la connexion
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
