package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.data.ActionData;
import com.github.KreeZeG123.snakeOnline.model.data.LoginSnakeData;
import com.github.KreeZeG123.snakeOnline.model.data.RunningGameData;
import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Server {

    private final int MAX_PLAYERS = 2;

    private InputMap map;

    private boolean serverIsOn;
    private Vector<Socket> clients;
    private Vector<Thread> threadsClients;
    private ServerSocket serveurSocket;

    private AtomicInteger nbJoeurs = new AtomicInteger(0);
    private Gson gson = new Gson();

    private RunningGameData runningGameData;

    private SnakeGame snakeGame;

    public void code_gestion_client(Socket so) {
        try {
            while (serverIsOn) {
                BufferedReader entree = new BufferedReader(new InputStreamReader(so.getInputStream()));
                PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                String message = entree.readLine();
                System.out.println("server a reçu : |" + message + "|");
                if (message == null) {
                    System.out.println("Client déconnecté");
                    clients.remove(so);
                    so.close();
                    break;
                }
                else if (message.equals("connexion")) {
                    if (nbJoeurs.get() < MAX_PLAYERS) {

                        LoginSnakeData loginSnakeData = new LoginSnakeData(
                                this.map,
                                ColorSnake.values()[nbJoeurs.get()]
                        );

                        // Envoi des informations de connexion
                        sortie.println(gson.toJson(loginSnakeData));

                        // Incrémentation du nombre de joueurs
                        nbJoeurs.incrementAndGet();

                    } else {
                        sortie.println("stop");
                    }
                }
                else if (message.equals("stop")) {
                    System.out.println("Déconnexion du client");
                    clients.remove(so);
                    so.close();
                } else if (message.equals("stopAll")) {
                    notifier_clients(message, so);
                } else {
                    if (nbJoeurs.get() >= MAX_PLAYERS) {

                        ActionData actionData = gson.fromJson(message, ActionData.class);

                        this.snakeGame.updatePlayerInput(
                                actionData.colorSnake.name(),
                                actionData.action,
                                false
                        );
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("problème\n"+e);
        }
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
                new Thread(() -> {
                    try {
                        PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                        sortie.println(message);
                    } catch (IOException e) {
                        System.out.println("problème\n"+e);
                    }
                }).start();
            }
        }
    }

    public void running_game(InputMap map) {
        // Création du jeu
        this.snakeGame = new SnakeGame(100, map);

        // Boucle de jeu
        boolean gameIsFinished = false;
        while (serverIsOn) {
            try {
                // S'il y a assez de joueurs
                if (nbJoeurs.get() == MAX_PLAYERS && !gameIsFinished) {
                    this.snakeGame.step();
                    if (this.snakeGame.gameContinue()) {
                        // Prévenir les clients de la mise à jour
                        notifier_clients(gson.toJson(this.snakeGame.getGameData()), null);
                    }
                    else {
                        gameIsFinished = true;
                        // Prévenir les clients de la fin de la partie
                        notifier_clients("fin", null);
                    }
                } else {
                    if (gameIsFinished) {
                        System.out.println("La partie est terminée.");
                    } else {
                        System.out.println("En attente de joueurs...");
                    }
                }

                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Server(int port, InputMap map) {
        this.map = map;

        serverIsOn = true;
        clients = new Vector<Socket>();

        // Lance le thread pour le jeu (temporaire)
        Thread runningGameThread = new Thread(() -> running_game(map));
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
}
