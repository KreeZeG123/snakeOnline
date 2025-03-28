package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameUpdateDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.SnakeActionDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameStartDTO;
import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.*;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Server {
    private int max_player;
    private InputMap map;
    private boolean serverIsOn;
    private Vector<Socket> clientsSocket;
    private Vector<Thread> threadsClients;
    private ServerSocket serveurSocket;
    private AtomicInteger nbJoueurs = new AtomicInteger(0);
    private SnakeGame snakeGame;
    private int port;
    private String ip;
    private Client[] clients;
    public void code_gestion_client(Socket so) {
        try {
            while (this.serverIsOn) {
                System.out.println("Server on");
                BufferedReader entree = new BufferedReader(new InputStreamReader(so.getInputStream()));
                PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                String receivedMessage = entree.readLine();
                Protocol receivedProtocol = Protocol.deserialize(receivedMessage);
                if (receivedMessage == null) {
                    System.out.println("Client déconnecté");
                    clientsSocket.remove(so);
                    stop_serveur();
                    so.close();
                    break;
                } else {
                    System.out.println("server a reçu : |" + receivedProtocol.getMessage() + "|");
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
                            clientsSocket.remove(so);
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
        try {
            System.out.println("Arret du serveur");
            this.serverIsOn = false;
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
        for (Socket so : clientsSocket) {
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
        this.snakeGame = new SnakeGame(10000, map);
        // Boucle de jeu
        boolean gameIsFinished = false;
        while (this.serverIsOn) {
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
                        GameUpdateDTO infoGameEndedDTO = this.snakeGame.getGameData();
                        Protocol snakeGameEndProtocol = new Protocol(
                                "Server",
                                "Client",
                                (new Date()).toString(),
                                "SnakeGameServerEndGame",
                                infoGameEndedDTO
                        );
                        notifier_clients(snakeGameEndProtocol, null);
                    }
                } else {
                    if (gameIsFinished) {
                        System.out.println("Server La partie est terminée.");
                        stop_serveur();
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

    public Server(InputMap map, MainServer mainServer) {
        this.map = map;
        this.max_player = map.getStart_snakes().size();
        this.serverIsOn = true;
        clientsSocket = new Vector<Socket>();

        // Lance le thread pour le jeu (temporaire)
        Thread runningGameThread = new Thread(() -> running_game(map));
        runningGameThread.start();

        // Ouvre le serveur
        try {
            serveurSocket = new ServerSocket(0); // on crée le serveur
            this.port = serveurSocket.getLocalPort();
            this.ip = getEthernetAddress();
            System.out.println("IP du serveur : " + this.ip);
            System.out.println("Port du serveur : " + this.port);
            mainServer.setServerInitialization(this.ip, this.port);
            System.out.println("Serveur mis en place");

            // Boucle pour accepter les nouvelles connexions (arrêt avec stop et stopAll)
            while (this.serverIsOn) {
                Socket so = serveurSocket.accept(); // Accepte une nouvelle connexion d'un client
                clientsSocket.add(so); // Ajoute le client à la liste des clients
                // Crée un thread pour chaque écouter chaque client
                Thread threadClient = new Thread(() -> code_gestion_client(so));
                threadClient.start();
            }

        } catch (SocketException e) {
            if (!this.serverIsOn) {
                System.out.println("Le serveur a été arrêté.");
            } else {
                System.out.println("Erreur inattendue : " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("problème\n"+e);
        }

    }

    public static String getEthernetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                // Filtrer les interfaces filaires, typiquement 'eth0', 'en0', ou similaires
                if (ni != null && !ni.isLoopback() && ni.isUp()) {
                    // Vérifier si l'interface est Ethernet
                    if (ni.getDisplayName().toLowerCase().contains("eth") || ni.getDisplayName().toLowerCase().contains("en")) {
                        Enumeration<InetAddress> addresses = ni.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            InetAddress addr = addresses.nextElement();
                            // Retourner l'adresse IPv4
                            if (addr instanceof Inet4Address) {
                                return addr.getHostAddress();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0.0.0"; // Retourner par défaut si aucune adresse n'est trouvée
    }

    public int getPort(){
        return this.port;
    }

    public String getIP(){
        return this.ip;
    }
}
