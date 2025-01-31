package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.controller.ControllerSnakeGame;
import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.data.LoginSnakeData;
import com.github.KreeZeG123.snakeOnline.model.data.RunningGameData;
import com.github.KreeZeG123.snakeOnline.utils.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private Gson gson = new Gson();

    private Socket so;
    private ControllerSnakeGame controller;

    public static void main(String[] args) {
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("========== SnakeOnline ==========");
        System.out.print("IP : ");
        String ip = scanner.nextLine();
        System.out.print("Port : ");
        int port = scanner.nextInt();

        new Client(ip, port);
        */

        new Client("localhost", 4321);
    }

    public void gestion_recepetion_in_game() {
        try {
            BufferedReader entreeDuClient = new BufferedReader(new InputStreamReader(so.getInputStream()));
            String message;
            while (true) {
                message = entreeDuClient.readLine();
                if (message == null) {
                    System.out.println("Le serveur a coupé la connexion.");
                    break;
                }else if (message.equals("fin")) {
                    System.out.println("La partie est terminée.");
                    break;
                } else {
                    // Extraction des informations
                    RunningGameData parsedRunningData = gson.fromJson(message, RunningGameData.class);
                    this.controller.update(
                            parsedRunningData.snakes,
                            parsedRunningData.items
                    );
                    System.out.println(parsedRunningData.snakes.get(0).getPositions().get(0).getX());
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Client(String ip, int port) {

        try{
            // On se connecte au serveur
            so = new Socket(ip, port);
            PrintWriter sortieVersServeur = new PrintWriter(so.getOutputStream(), true);
            BufferedReader entreeDuClient = new BufferedReader(new InputStreamReader(so.getInputStream()));

            // On envoie un message de connexion
            sortieVersServeur.println("connexion");

            // On attend la réponse du serveur
            String startingInfoJSON = entreeDuClient.readLine();

            // Extraction des informations
            LoginSnakeData parsedLoginData = gson.fromJson(startingInfoJSON, LoginSnakeData.class);

            // Création de la map
            InputMap inputMap = new InputMap(parsedLoginData);
            ColorSnake clientColor = parsedLoginData.clientColor;

            // Création de l'affichage
            controller = new ControllerSnakeGame(inputMap, this);

            // On crée un thread pour écouter les messages du serveur quand la partie est en cours
            Thread threadEcoute = new Thread(this::gestion_recepetion_in_game);
            threadEcoute.start();


            //so.close(); // on ferme la connexion
        }
        catch (Exception e) {
            System.out.println(e);
        }
        /* catch (UnknownHostException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println("Aucun serveur n’est rattaché au port ");
        }*/
    }
}
