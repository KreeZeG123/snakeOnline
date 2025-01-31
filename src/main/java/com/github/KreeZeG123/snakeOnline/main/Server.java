package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.data.LoginSnakeData;
import com.github.KreeZeG123.snakeOnline.model.data.RunningGameData;
import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class Server {

    private boolean serverIsOn;
    private Vector<Socket> clients;
    private Vector<Thread> threadsClients;
    private ServerSocket serveurSocket;

    private int nbJoeurs = 0;
    private Gson gson = new Gson();

    private RunningGameData runningGameData;

    public void code_gestion_client(Socket so) {
        try {
            while (serverIsOn) {
                BufferedReader entree = new BufferedReader(new InputStreamReader(so.getInputStream()));
                PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                String message = entree.readLine();
                System.out.println("on a reçu : |" + message + "|");
                if (message == null) {
                    System.out.println("Client déconnecté");
                    clients.remove(so);
                    so.close();
                    break;
                }
                else if (message.equals("connexion")) {
                    if (nbJoeurs == 0) {
                        LoginSnakeData loginSnakeData = getMap();
                        this.runningGameData = new RunningGameData(
                                loginSnakeData.startItems,
                                loginSnakeData.startSnakes
                        );
                    }

                    sortie.println(getMapString());
                    nbJoeurs++;
                }
                else if (message.equals("stop")) {
                    System.out.println("Déconnexion du client");
                    clients.remove(so);
                    so.close();
                    break;
                } else if (!message.equals("stopAll")) {
                    notifier_clients(message, so);
                } else {
                    stop_serveur();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("problème\n"+e);
        }
    }

    // Fonction temporaire de test
    public LoginSnakeData getMap() {
        try {
            InputMap map = new InputMap("layouts/alone.lay");
            return new LoginSnakeData(map, ColorSnake.Red);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Fonction temporaire de test
    public String getMapString() {
        Gson gson = new Gson();

        LoginSnakeData config = getMap();

        // Transformation en JSON String
        return gson.toJson(config);
    }

    public void stop_serveur() {
        serverIsOn = false;
        try {
            serverIsOn = false;
            notifier_clients("stop",null);
            sleep(1000);
            serveurSocket.close();
        } catch (IOException e) {
            System.out.println("problème\n"+e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifier_clients(String message, Socket emetteur) {
        for (Socket so : clients) {
            if (so != emetteur) {
                try {
                    PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                    sortie.println(message);
                } catch (IOException e) {
                    System.out.println("problème\n"+e);
                }
            }
        }
    }

    public void running_game() {
        while (serverIsOn) {
            try {
                sleep(1000);
                if (nbJoeurs >= 0) {
                    System.out.println("nbJoeurs : " + nbJoeurs);
                    notifier_clients(updateSnakePos(), null);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String updateSnakePos() {
        ArrayList<FeaturesSnake> snakes = this.runningGameData.snakes;
       for (FeaturesSnake s : snakes) {
           s.getPositions().set(0, new Position(s.getPositions().get(0).getX() + 1, s.getPositions().get(0).getY()));
       }

        RunningGameData runningGameData = new RunningGameData(
                this.runningGameData.items,
                snakes
        );

        return gson.toJson(runningGameData);
    }

    public Server(int port) {
        serverIsOn = true;
        clients = new Vector<Socket>();

        // Lance le thread pour le jeu (temporaire)
        Thread runningGameThread = new Thread(this::running_game);
        runningGameThread.start();

        // Ouvre le serveur
        try {
            serveurSocket = new ServerSocket(port); // on crée le serveur
            System.out.println("Serveur mis en place");

            // Boucle pour accepter les nouvelles connexions (arrêt avec stop et stopAll)
            while (serverIsOn) {
                Socket so = serveurSocket.accept(); // Accepte une nouvelle connexion d'un client
                clients.add(so); // Ajoute le client à la liste des clients
                // Crée un thread pour chaque écouter chaque client
                Thread threadClient = new Thread(() -> code_gestion_client(so));
                threadClient.start();
            }


        } catch (SocketException e) {
            if (!serverIsOn) {
                System.out.println("Le serveur a été arrêté.");
            } else {
                System.out.println("Erreur inattendue : " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("problème\n"+e);
        }

    }

    public static void main(String[] args) {
        new Server(4321);
    }
}
