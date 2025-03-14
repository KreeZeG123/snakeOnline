package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.SnakeActionDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameStartDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameUpdateDTO;
import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Server {

    private int max_player;

    private InputMap map;

    private boolean serverIsOn;
    private Vector<Socket> clients;
    private Vector<Thread> threadsClients;
    private ServerSocket serveurSocket;

    private AtomicInteger nbJoueurs = new AtomicInteger(0);
    private Gson gson = new Gson();

    private GameUpdateDTO gameUpdateDTO;

    private SnakeGame snakeGame;
    private int port;
    private String ip;
    public void code_gestion_client(Socket so) {
        try {
            while (serverIsOn) {
                BufferedReader entree = new BufferedReader(new InputStreamReader(so.getInputStream()));
                PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);

                String receivedMessage = entree.readLine();
                Protocol receivedProtocol = gson.fromJson(receivedMessage, Protocol.class);

                System.out.println("server a reçu : |" + receivedProtocol.getMessage() + "|");
                if (receivedMessage == null) {
                    System.out.println("Client déconnecté");
                    clients.remove(so);
                    so.close();
                    break;
                } else {
                    switch (receivedProtocol.getMessage()) {
                        case "SnakeGameClientJoin": {
                            if (nbJoueurs.get() < this.max_player) {

                                GameStartDTO gameStartDTO = new GameStartDTO(
                                        this.map,
                                        ColorSnake.values()[nbJoueurs.get()]
                                );

                                // Envoi des informations de début de partie
                                Protocol gameStartProtocol = new Protocol(
                                        "SnakeGame Server " + so.getLocalAddress(),
                                        "SnakeGame Client " + so.getInetAddress(),
                                        (new Date()).toString(),
                                        "SnakeGameServerStartInfo",
                                        gameStartDTO
                                );
                                sortie.println(gameStartProtocol.serialize());

                                // Incrémentation du nombre de joueurs
                                nbJoueurs.incrementAndGet();

                            } else {
                                sortie.println("stop");
                            }
                            break;
                        }
                        case "SnakeGameClientLeave": {
                            System.out.println("Déconnexion du client");
                            clients.remove(so);
                            so.close();
                            break;
                        }
                        case "SnakeGameClientNewAction": {
                            if (nbJoueurs.get() >= this.max_player) {

                                // Extraction des informations
                                SnakeActionDTO snakeActionDTO = receivedProtocol.getData();

                                this.snakeGame.updatePlayerInput(
                                        snakeActionDTO.colorSnake.name(),
                                        snakeActionDTO.action,
                                        false
                                );
                            }
                            break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop_serveur() {
        serverIsOn = false;
        try {
            serverIsOn = false;
            Protocol serverStopProtocol = new Protocol(
                    null,
                    null,
                    (new Date()).toString(),
                    "SnakeGameServerStop",
                    null
            );
            notifier_clients(serverStopProtocol,null);
            sleep(1000);
            serveurSocket.close();
        } catch (IOException e) {
            System.out.println("problème\n"+e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifier_clients(Protocol protocol, Socket emetteur) {
        for (Socket so : clients) {
            if (so != emetteur) {
                new Thread(() -> {
                    try {
                        PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                        protocol.setSender("SnakeGame Server " + so.getLocalAddress());
                        protocol.setReceiver("SnakeGame Client " + so.getInetAddress());
                        sortie.println(protocol.serialize());
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
                if (nbJoueurs.get() == this.max_player && !gameIsFinished) {
                    this.snakeGame.step();
                    if (this.snakeGame.gameContinue()) {
                        // Prévenir les clients de la mise à jour
                        Protocol snakGameUpdateProtocol = new Protocol(
                                null,
                                null,
                                (new Date()).toString(),
                                "SnakeGameServerUpdate",
                                this.snakeGame.getGameData()
                        );
                        notifier_clients(snakGameUpdateProtocol, null);
                    }
                    else {
                        gameIsFinished = true;
                        // Prévenir les clients de la fin de la partie
                        Protocol snakeGameEndProtocol = new Protocol(
                                null,
                                null,
                                (new Date()).toString(),
                                "SnakeGameServerEndGame",
                                null
                        );
                        notifier_clients(snakeGameEndProtocol, null);
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

    public Server(InputMap map) {
        this.map = map;
        this.max_player = map.getStart_snakes().size();
        serverIsOn = true;
        clients = new Vector<Socket>();

        // Lance le thread pour le jeu (temporaire)
        Thread runningGameThread = new Thread(() -> running_game(map));
        runningGameThread.start();

        // Ouvre le serveur
        try {
            serveurSocket = new ServerSocket(4321); // on crée le serveur
            this.port = serveurSocket.getLocalPort();
            this.ip = serveurSocket.getInetAddress().getHostAddress();
            System.out.println(this.ip);
            System.out.println(this.port);
            server.setIPPort(this.port,this.ip);
            server.setServerInitialization(this.ip, this.port);

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

    public int getPort(){
        return this.port;
    }

    public String getIP(){
        return this.ip;
    }
}
