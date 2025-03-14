package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.model.data.ActionData;
import com.github.KreeZeG123.snakeOnline.model.data.LoginSnakeData;
import com.github.KreeZeG123.snakeOnline.utils.ColorSnake;
import com.github.KreeZeG123.snakeOnline.utils.Partie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class MainServer {
    final List<Partie> listeParties;
    private int port;
    private String ip;
    private boolean serverIsOn;
    private ServerSocket serveurSocket;
    private Vector<Socket> clients;
    private Vector<Thread> threadsClients;
    public MainServer() throws Exception {
        this.listeParties = new CopyOnWriteArrayList<>();
        this.clients = new Vector<>();
        fillListeParties();
        serverIsOn = true;
        try {
            serveurSocket = new ServerSocket(4321); // on crée le serveur
            this.port = serveurSocket.getLocalPort();
            this.ip = serveurSocket.getInetAddress().getHostAddress();
            System.out.println(this.ip);
            System.out.println(this.port);
            System.out.println("Serveur mis en place");
            // Boucle pour accepter les nouvelles connexions (arrêt avec stop et stopAll)
            while (serverIsOn) {
                Socket so = serveurSocket.accept(); // Accepte une nouvelle connexion d'un client
                System.out.println("Client accepted");
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
                } else if (message.equals("connexion")) {
                    System.out.println("Message connexion recu");
                    String message_informations = entree.readLine();
                    if(message_informations.equals("true")){
                        sortie.println("connexion acceptée");
                    }else{
                        sortie.println("connexion refusée");
                    }
                }else if (message.equals("register")){
                    System.out.println("Message register recu");
                    System.out.println("Envoie des données sur le server web");
                    sortie.println("register accepté");
                }else if (message.equals("creer serveur")) {
                    message = entree.readLine();
                    if(message.equals("medium_alone_no_walls.lay")){
                        Partie partie = creerServeur(message);
                        sortie.println(partie.getIp());
                        sortie.println(partie.getPort());
                        //listeParties.add(partie.getIp());
                    }
                } else if(message.equals("get list party")) {
                    sortie.println("a");
                    sortie.println("b");
                    sortie.println("c");
                    sortie.println("null");
                } else if (message.equals("stop")) {
                }
            }
        } catch (Exception e) {
            System.out.println("problème\n"+e);
        }
    }

    public boolean verificationInformationsConnexion(String login, String password){
        return true;
    }

    public boolean inscription(String login, String password){
        return true;
    }

    public List<Partie> getListeParties() {
        return listeParties;
    }

    public Partie creerServeur(String map) throws Exception {
        InputMap inputMap;
        final Partie[] partieResult = new Partie[1];
        inputMap = new InputMap(map);

        new Thread(() -> {
            new Server(inputMap, this);
        }).start();

        waitForServerInitialization();
        Partie partie = new Partie(inputMap, this.port, this.ip);
        synchronized (this.listeParties) {
            this.listeParties.add(partie);
        }
        System.out.println(this.listeParties.get(5));
        partieResult[0] = partie; // Mettre à jour la variable partieResult
        return partieResult[0]; // Retourner la partie
    }

    private synchronized void waitForServerInitialization() {
        while (this.port == 0 || this.ip == null) {
            try {
                wait(); // Attendre que l'initialisation des variables soit terminée
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setServerInitialization(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        notifyAll(); // Signale que l'initialisation est terminée
    }


    public void fillListeParties() throws Exception {
        this.listeParties.add(new Partie(new InputMap("bonjour"), 4321, "172.186.16.58"));
        this.listeParties.add(new Partie(new InputMap("dzdzd"), 4321, "172.186.16.58"));
        this.listeParties.add(new Partie(new InputMap("dz"), 4321, "172.186.16.58"));
        this.listeParties.add(new Partie(new InputMap("dz"), 4321, "172.186.16.58"));
        this.listeParties.add(new Partie(new InputMap("ddzd"), 4321, "172.186.16.58"));
    }

    public void setIPPort(int port, String ip){
        this.ip = ip;
        this.port = port;
    }

    public String getIP(){
        return this.ip;
    }

    public int getPort(){
        return this.port;
    }

    public static void main(String[] args) throws Exception {
        new MainServer();
    }
}
