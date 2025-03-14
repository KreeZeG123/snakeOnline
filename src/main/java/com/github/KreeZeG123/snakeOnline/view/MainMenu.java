package com.github.KreeZeG123.snakeOnline.view;

import com.github.KreeZeG123.snakeOnline.main.Client;
import com.github.KreeZeG123.snakeOnline.main.MainServer;
import com.github.KreeZeG123.snakeOnline.main.Server;
import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.utils.Partie;

import javax.smartcardio.Card;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.KreeZeG123.snakeOnline.view.MapChooser.listMapInFolder;


public class MainMenu {
    public static final int TAILLE_INPUT_X = 300;
    public static final int TAILLE_INPUT_Y = 40;
    public static final String ip = "127.0.0.1";
    public static final int port = 4321;
    public static final Font inputFont = new Font("Arial", Font.PLAIN, 30);
    public static final Font titleFont = new Font("Arial", Font.PLAIN, 45);
    public static final Font subtitleFont = new Font("Arial", Font.PLAIN, 15);
    MainServer mainServer;
    Socket socket;
    private BufferedReader entree;
    private PrintWriter sortie;
    private String message;
    public MainMenu() throws Exception {
        this.socket = new Socket(ip, port);
        this.entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.sortie = new PrintWriter(socket.getOutputStream(), true);


        JFrame frame = new JFrame("Menu principal");
        frame.setSize(800, 800);
        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        frame.setLocation(dx, dy);

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        JButton backButton = new JButton("Retour");

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel loginPanel = new JPanel(new GridBagLayout());

        JPanel registerPanel = new JPanel(new GridBagLayout());

        JPanel partyPanel = new JPanel(new GridBagLayout());

        setGBC(gbc);
        setLoginPanel(loginPanel,cardLayout,cardPanel,gbc,partyPanel);
        setBackButton(backButton,cardLayout,cardPanel);
        setRegisterPanel(registerPanel,gbc,backButton,cardLayout,cardPanel);


        cardPanel.add(loginPanel, "loginPanel");
        cardPanel.add(registerPanel, "registerPanel");
        cardPanel.add(partyPanel, "partyPanel");

        frame.setLayout(new BorderLayout());
        frame.add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "loginPanel");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setLoginPanel(JPanel loginPanel, CardLayout cardLayout, JPanel cardPanel, GridBagConstraints gbc,JPanel partyPanel) {

        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(titleFont);

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(subtitleFont);

        JTextField loginField = new JTextField();
        loginField.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        loginField.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        loginField.setFont(inputFont);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(subtitleFont);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        passwordField.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        passwordField.setFont(inputFont);

        JButton loginButton = new JButton("Se connecter");
        loginButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        JButton registerButton = new JButton("S'inscrire");
        registerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        JLabel erreurInforamtionsLabel = new JLabel("Informations de connexion éronnées");
        erreurInforamtionsLabel.setFont(subtitleFont);
        erreurInforamtionsLabel.setForeground(Color.RED);
        erreurInforamtionsLabel.setVisible(false);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortie.println("connexion");
                sortie.println("true");
                String reponse = null;
                try {
                    reponse = entree.readLine();
                    if(reponse.equals("connexion acceptée")){
                        setPartyPanel(partyPanel,gbc);
                        cardLayout.show(cardPanel, "partyPanel");
                    }else{
                        erreurInforamtionsLabel.setVisible(true);
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        registerButton.addActionListener(actionEvent -> cardLayout.show(cardPanel, "registerPanel"));

        loginPanel.add(titleLabel, gbc);
        loginPanel.add(loginLabel, gbc);
        loginPanel.add(loginField,gbc);
        loginPanel.add(passwordLabel, gbc);
        loginPanel.add(passwordField,gbc);
        loginPanel.add(erreurInforamtionsLabel,gbc);
        loginPanel.add(loginButton,gbc);
        loginPanel.add(registerButton,gbc);
    }

    public void setBackButton(JButton backButton, CardLayout cardLayout, JPanel cardPanel){
        backButton = new JButton("Retour");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Arial", Font.PLAIN, 15));
        backButton.setBorder(new EmptyBorder(10, 10, 10, 10)); // Un peu de marge autour du bouton
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retourner à la page de login
                cardLayout.show(cardPanel, "loginPanel");
            }
        });
    }

    public void setGBC(GridBagConstraints gbc){
        gbc.gridx = 0; // Colonne 0
        gbc.gridy = GridBagConstraints.RELATIVE; // Placement relatif (ligne suivante à chaque ajout)
        gbc.insets = new Insets(5, 0, 5, 0); // Marges (haut, gauche, bas, droite)
        gbc.anchor = GridBagConstraints.CENTER; //
    }

    public void setRegisterPanel(JPanel registerPanel, GridBagConstraints gbc, JButton backButton, CardLayout cardLayout, JPanel cardPanel){

        JLabel titleLabel2 = new JLabel("Register");
        titleLabel2.setFont(titleFont);

        JLabel loginLabel2 = new JLabel("Login");
        loginLabel2.setFont(new Font("Arial", Font.PLAIN, 15));

        JTextField loginField2 = new JTextField();
        loginField2.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        loginField2.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        loginField2.setFont(inputFont);

        JLabel passwordLabel2 = new JLabel("Password");
        passwordLabel2.setFont(new Font("Arial", Font.PLAIN, 15));

        JPasswordField passwordField2 = new JPasswordField();
        passwordField2.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        passwordField2.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        passwordField2.setFont(inputFont);

        JButton registerButton2 = new JButton("S'inscrire");
        registerButton2.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        registerButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortie.println("register");
                String reponse = null;
                try {
                    reponse = entree.readLine();
                    System.out.println(reponse);
                    if(reponse.equals("register accepté")){
                        cardLayout.show(cardPanel, "partyPanel");
                    }else{
                        System.out.println("non");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        registerPanel.add(backButton, gbc);
        registerPanel.add(titleLabel2, gbc);
        registerPanel.add(loginLabel2, gbc);
        registerPanel.add(loginField2,gbc);
        registerPanel.add(passwordLabel2, gbc);
        registerPanel.add(passwordField2,gbc);
        registerPanel.add(registerButton2,gbc);
    }

    public void setPartyPanel(JPanel partyPanel, GridBagConstraints gbc) throws IOException {

        JLabel titleLabel3 = new JLabel("List of servers");
        titleLabel3.setFont(titleFont);

        sortie.println("get list party");
        String reponse = entree.readLine();
        DefaultListModel<String> arrayParties = new DefaultListModel<>();
        while(!reponse.equals("null")){
            System.out.println("bien");
            arrayParties.addElement(reponse);
            reponse = entree.readLine();
        }

        JList<String> serversList = new JList<>(arrayParties);
        serversList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serversList.setFont(inputFont);

        JScrollPane serversScrollPane = new JScrollPane(serversList);
        serversScrollPane.setPreferredSize(new Dimension(TAILLE_INPUT_X,500));

        JButton connexionServerButton = new JButton("Connexion");
        connexionServerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        connexionServerButton.addActionListener(actionEvent -> {
            if(serversList.getSelectedValue() != null){
                serversList.getSelectedIndex();
                String ip = arrayParties.getElementAt(3);
                String port = arrayParties.getElementAt(4);
                new Client(ip, (Integer.parseInt(port)));
            }
        });

        JButton createNewServerButton = new JButton("Create a server");
        createNewServerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        createNewServerButton.addActionListener(actionEvent -> {
            sortie.println("creer serveur");
            sortie.println("medium_alone_no_walls.lay");
            try {
                String reponse_creation = entree.readLine();
                if(reponse_creation.equals("creation reussi")){
                    String ip = entree.readLine();
                    String port = entree.readLine();
                    new Client(ip, Integer.parseInt(port));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        partyPanel.add(createNewServerButton, gbc);
        partyPanel.add(titleLabel3, gbc);
        partyPanel.add(serversScrollPane, gbc);
        partyPanel.add(connexionServerButton, gbc);
    }

    public static void main(String[] args) throws Exception {
        new MainMenu();
    }
}
