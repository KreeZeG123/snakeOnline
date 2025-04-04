package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.agent.Snake;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.StringMapDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.*;
import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Server {
    private static final String WEB_SERVER_ADRESS = "http://localhost:8080/Web/";
    private final int max_player;
    private final InputMap map;
    private final MainServer mainServer;
    private boolean serverIsOn;
    private boolean gameStarted = false;
    private final Vector<Socket> clientsSocket;
    private ServerSocket serveurSocket;
    private final AtomicInteger nbJoueurs = new AtomicInteger(0);
    private SnakeGame snakeGame;
    private int port;
    private String ip;
    private final Map<String, String> players;

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

                                StringMapDTO stringMapDTO = receivedProtocol.getData();
                                String playerName = stringMapDTO.get("username");

                                // Ajout dans la liste des jouers
                                this.players.put(String.valueOf(nbJoueurs), playerName);


                                // Envoi des informations de début de partie
                                GameStartDTO gameStartDTO = new GameStartDTO(
                                        this.map,
                                        nbJoueurs.get()
                                );
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
                                System.out.println(snakeActionDTO.id);
                                this.snakeGame.updatePlayerInput(
                                        snakeActionDTO.id,
                                        snakeActionDTO.action,
                                        false
                                );
                            }
                            break;
                        }
                    }

                }
            }
        }
        catch (SocketException se) {
            if ( se.getMessage().startsWith("Socket is closed") ) {
                System.out.println("Le client a coupé la connexion.");
            } else {
                throw new RuntimeException( se );
            }
        }
        catch (Exception e) {
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
        synchronized (clientsSocket) {  // Synchronisation pour éviter les modifications concurrentes
            Iterator<Socket> iterator = clientsSocket.iterator();
            while (iterator.hasNext()) {
                Socket so = iterator.next();
                if (so != emetteur) {
                    new Thread(() -> {
                        try {
                            PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                            protocol.setSender("SnakeGame Server " + so.getLocalAddress());
                            protocol.setReceiver("SnakeGame Client " + so.getInetAddress());
                            sortie.println(protocol.serialize());
                        } catch (IOException e) {
                            System.out.println("problème\n" + e);
                        }
                    }).start();
                }
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
                if (nbJoueurs.get() == this.max_player && !gameIsFinished && clientsSocket != null && !clientsSocket.isEmpty()) {
                    gameStarted = true;
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

                        try {
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

                            // GameEndDTO
                            List<String> orderedPlayers = new ArrayList<>();
                            List<Integer> orderedScores = new ArrayList<>();
                            for (Snake s : this.snakeGame.getSnakeAgents()) {
                                orderedPlayers.add(this.players.get(s.getColorSnake().toString()));
                                orderedScores.add(s.getPoints());
                            }
                            GameEndDTO gameEndDTO = new GameEndDTO(
                                    orderedPlayers,
                                    orderedScores
                            );

                            // Notifie le server web de la fin de partie
                            Protocol gameEndProtocol = new Protocol(
                                    "SnakeGameServer",
                                    "WebServer",
                                    (new Date()).toString(),
                                    "SnakeGameServerEndGame",
                                    gameEndDTO
                            );

                            Protocol endGameReponseprotocol = Protocol.sendHttpProtocolRequest(gameEndProtocol, WEB_SERVER_ADRESS + "api/gameEnd");
                            if ( endGameReponseprotocol != null && endGameReponseprotocol.getMessage().equals("WebServerEndGameSucess"))  {
                                System.out.println("Remonté des informations de partie finie avec succes");
                            }else {
                                System.out.println("Echec dans la remonté des informations de partie finie");
                            }

                            mainServer.removeServer(ip, port);
                        } catch (Exception e) {
                            System.out.println("Problème 2 : "+e);
                        }
                    }
                } else {
                    if (gameIsFinished) {
                        System.out.println("Server La partie est terminée.");
                        stop_serveur();
                    } else if (!gameStarted) {
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
        this.players = new HashMap<>();
        this.mainServer = mainServer;
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
            this.mainServer.setServerInitialization(this.ip, this.port);
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
