package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.StringMapDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.mainMenu.*;
import com.google.gson.Gson;


import javax.management.StringValueExp;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainServer {

    private static final String WEB_SERVER_ADRESS = "http://localhost:8080/Web/";

    final List<String[]> serverList = new CopyOnWriteArrayList<>();
    private int port;
    private String ip;
    private int portServer;
    private String ipServer;
    private boolean serverIsOn;
    private ServerSocket serveurSocket;
    private Vector<Socket> clients;
    private Vector<Thread> threadsClients;
    private final Gson gson = new Gson();
    public MainServer() throws Exception {
        this.clients = new Vector<>();
        serverIsOn = true;
        try {
            serveurSocket = new ServerSocket(4321); // on crée le serveur
            this.port = serveurSocket.getLocalPort();
            this.ip = serveurSocket.getInetAddress().getHostAddress();
            System.out.println("IP du main server : " + this.ip);
            System.out.println("Port du main server : " + this.port);
            System.out.println("Serveur mis en place");
            // Boucle pour accepter les nouvelles connexions (arrêt avec stop et stopAll)
            while (serverIsOn) {
                Socket so = serveurSocket.accept(); // Accepte une nouvelle connexion d'un client
                System.out.println("Client accepté");
                clients.add(so); // Ajoute le client à la liste des clients
                // Crée un thread pour chaque écouter chaque client
                new Thread(() -> code_gestion_client(so)).start();
            }
        } catch (SocketException e) {
            if (!serverIsOn) {
                System.out.println("Le serveur a été arrêté.");
            } else {
                System.out.println("Erreur inattendue : " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Problème : " + e);
        }
    }

    public void code_gestion_client(Socket so) {
        try {
            while (serverIsOn) {
                BufferedReader entree = new BufferedReader(new InputStreamReader(so.getInputStream()));
                PrintWriter sortie = new PrintWriter(so.getOutputStream(), true);
                String message = entree.readLine();
                Protocol receivedProtocol = Protocol.deserialize(message);
                System.out.println("Main server a reçu : |" + message + "|");
                if(message == null){
                    System.out.println("Client déconnecté du main server" );
                    clients.remove(so);
                    so.close();
                    break;
                }
                else{
                switch(receivedProtocol.getMessage()){
                    case "MainMenuClientDemandeConnexion":
                        System.out.println("Demande de connexion");

                        // Remplace les infos du client par celle du serveur
                        receivedProtocol.setSender("MainServer");
                        receivedProtocol.setReceiver("WebServer");
                        receivedProtocol.setMessage( receivedProtocol.getMessage().replace("MainMenu", "MainServer"));

                        // Communique les informations au serveur web
                        Protocol userInfoLoginProtocol = Protocol.sendHttpProtocolRequest(receivedProtocol,  WEB_SERVER_ADRESS + "api/connexion");

                        // Remplace les infos du serveur web par celles du main server
                        userInfoLoginProtocol.setSender("MainServer");
                        userInfoLoginProtocol.setReceiver("MainMenuClient");
                        userInfoLoginProtocol.setMessage( userInfoLoginProtocol.getMessage().replace("WebServer", "MainServer") );

                        sortie.println(userInfoLoginProtocol.serialize());
                        break;
                    case "MainMenuClientDemandeServerList" :
                        System.out.println("Demande de liste de serveurs");
                        //Demande au serveur web la liste des serveurs
                        ServerListDTO serverListDTO = new ServerListDTO();
                        serverListDTO.addServer(serverList);
                        Protocol sendingProtocolDemandeServerList = new Protocol("MainServer","MainMenuClient", (new Date()).toString(), "RetourServerList", serverListDTO);
                        sortie.println(sendingProtocolDemandeServerList.serialize());
                        break;
                    case "CreationServeur" :
                        System.out.println("Creation de serveur");
                        NewServerDTO newServerDTO = receivedProtocol.getData();
                        String[] infoServer = creerServeur(newServerDTO.getMap());
                        InfoServerDTO infoServerDTO = new InfoServerDTO(infoServer[0],infoServer[1],Integer.parseInt(infoServer[2]));
                        Protocol sendingProtocolCreationServeur = new Protocol("MainServer","MainMenuClicent", (new Date()).toString(), "RetourDemandeCreationServeur",infoServerDTO);
                        sortie.println(sendingProtocolCreationServeur.serialize());
                        break;
                    case "MainMenuClientInscription" :
                        System.out.println("Demande d'inscription");
                        System.out.println(message);

                        // Remplace les infos du client par celle du serveur
                        receivedProtocol.setSender("MainServer");
                        receivedProtocol.setReceiver("WebServer");
                        receivedProtocol.setMessage( receivedProtocol.getMessage().replace("MainMenuClient", "MainServer"));

                        // Communique les informations au serveur web
                        Protocol userInfoInscriptionProtocol = Protocol.sendHttpProtocolRequest(receivedProtocol,  WEB_SERVER_ADRESS + "api/inscription");

                        // Remplace les infos du serveur web par celles du main server
                        userInfoInscriptionProtocol.setSender("MainServer");
                        userInfoInscriptionProtocol.setReceiver("MainMenuClient");
                        userInfoInscriptionProtocol.setMessage( userInfoInscriptionProtocol.getMessage().replace("WebServer", "MainServer") );

                        sortie.println(userInfoInscriptionProtocol.serialize());
                        break;
                    case "MainMenuClientDemandeInfoUser" :
                        System.out.println("Demande informations user");
                        StringMapDTO stringMapDTO = receivedProtocol.getData();

                        // Remplace les infos du client par celle du serveur
                        receivedProtocol.setSender("MainServer");
                        receivedProtocol.setReceiver("WebServer");
                        receivedProtocol.setMessage( receivedProtocol.getMessage().replace("MainMenu", "MainServer"));

                        // Communique les informations au serveur web
                        Protocol userInfoDemandeProtocol = Protocol.sendHttpProtocolRequest(receivedProtocol, WEB_SERVER_ADRESS + "api/infoProfil");

                        // Remplace les infos du serveur web par celles du main server
                        userInfoDemandeProtocol.setSender("MainServer");
                        userInfoDemandeProtocol.setReceiver("MainMenuClient");
                        userInfoDemandeProtocol.setMessage( userInfoDemandeProtocol.getMessage().replace("WebServer", "MainServer") );

                        sortie.println(userInfoDemandeProtocol.serialize());
                        break;
                    default :
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Problème : "+e);
        }
    }

    public String[] creerServeur(String map) throws Exception {
        InputMap inputMap;
        String[] result = new String[3];
        inputMap = new InputMap(map);

        new Thread(() -> {
            new Server(inputMap, this);
        }).start();
        waitForServerInitialization();
        String[] newServerInfo = new String[]{"Server", this.ipServer, String.valueOf(this.portServer)};
        synchronized (this.serverList) {
            this.serverList.add(newServerInfo);
            for(int i =0;i<serverList.size();i++){
                System.out.println("Server " + i + " " + serverList.get(i)[1]);
            }
        }
        this.portServer = 0;
        this.ipServer = null;
        result = newServerInfo; // Mettre à jour la variable partieResult
        return result; // Retourner les infos du nouveau serveur
    }

    public void removeServer(String ip, int port) {
        synchronized (this.serverList) {
            Iterator<String[]> iterator = this.serverList.iterator();
            while (iterator.hasNext()) {
                String[] server = iterator.next();
                if (server[1].equals(ip) && Integer.parseInt(server[2]) == port) {
                    iterator.remove();  // Retirer l'élément de la liste
                    System.out.println("Serveur supprimé : " + ip + ":" + port);
                    break;
                }
            }

            // Optionnel : Affichage de la liste des serveurs après suppression
            for (int i = 0; i < serverList.size(); i++) {
                System.out.println("Serveur " + i + " : " + serverList.get(i)[1] + ":" + serverList.get(i)[2]);
            }
        }
    }


    private synchronized void waitForServerInitialization() {
        while (this.portServer == 0 || this.ipServer == null) {
            try {
                wait(); // Attendre que l'initialisation des variables soit terminée
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setServerInitialization(String ip, Integer port) {
        this.ipServer = ip;
        this.portServer = port;
        notifyAll(); // Signale que l'initialisation est terminée
    }

    public static void main(String[] args) throws Exception {
        new MainServer();
    }
}
