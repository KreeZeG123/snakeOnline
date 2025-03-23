package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.mainMenu.*;
import com.github.KreeZeG123.snakeOnline.utils.Game;
import com.google.gson.Gson;

import javax.management.StringValueExp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainServer {
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
                        LoginDTO loginDTO = receivedProtocol.getData();
                        System.out.println("njnn" + loginDTO.getLogin());
                        ///Verification dans la base données + retour d'informations sur l'utilisateur
                        String[] cosmetiques = {"Chapeau","Lunettes"};
                        InfoUserDTO infoUserDTOLogin = new InfoUserDTO(loginDTO.getLogin(), cosmetiques, 10);
                        Protocol sendingProtocolConnexion =  new Protocol("MainServer", "MainMenuClient", (new Date()).toString(), "ConnexionAcceptée", infoUserDTOLogin);
                        sortie.println(sendingProtocolConnexion.serialize());
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
                    case "MainMenuClientDemandeEnregistrement" :
                        System.out.println("Demande d'enregistrement");
                        RegisterDTO registerDTO = receivedProtocol.getData();
                        ///Verification du login si il est deja utilisé
                        InfoUserDTO infoUserDTORegister = new InfoUserDTO(registerDTO.getLogin(),new String[0], 0);
                        //Protocol sendingProtocolRegister = new Protocol("MainServer", "MainMenuClient", (new Date()).toString(), "EnregistrementRefusé", null);
                        Protocol sendingProtocolRegister = new Protocol("MainServer", "MainMenuClient", (new Date()).toString(), "EnregistrementAccepté", infoUserDTORegister);
                        sortie.println(sendingProtocolRegister.serialize());
                        break;
                    case "MainMenuClientDemandeInfoUser" :
                        System.out.println("Demande informations user");
                        LoginDTO loginDTODemandeInfo = receivedProtocol.getData();
                        ///Verification du login si il est deja utilisé
                        InfoUserDTO infoUserDTODemandeInfo = new InfoUserDTO(loginDTODemandeInfo.getLogin(),new String[0], 0);
                        System.out.println("huhuihhhuh" + loginDTODemandeInfo.getLogin());
                        //Protocol sendingProtocolRegister = new Protocol("MainServer", "MainMenuClient", (new Date()).toString(), "EnregistrementRefusé", null);
                        Protocol sendingProtocolDemandeInfoUser = new Protocol("MainServer", "MainMenuClient", (new Date()).toString(), "RetourDemandeInfoUser", infoUserDTODemandeInfo);
                        sortie.println(sendingProtocolDemandeInfoUser.serialize());
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
