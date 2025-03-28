package com.github.KreeZeG123.snakeOnline.view;

import com.github.KreeZeG123.snakeOnline.main.Client;
import com.github.KreeZeG123.snakeOnline.main.MainServer;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.mainMenu.*;

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
import java.util.Date;
import java.util.Objects;


public class MainMenu {
    private static final int TAILLE_INPUT_X = 300;
    private static final int TAILLE_INPUT_Y = 40;
    private static final String ip = "0.0.0.0";
    private static final int port = 4321;
    private static final Font inputFont = new Font("Arial", Font.PLAIN, 30);
    private static final Font titleFont = new Font("Arial", Font.PLAIN, 45);
    private static final Font subtitleFont = new Font("Arial", Font.PLAIN, 15);
    private Socket socket;
    private BufferedReader entree;
    private PrintWriter sortie;
    private int nbPieces;
    private String[] cosmetiques;
    private String userName;
    private String skinChoisie;
    public MainMenu() throws Exception {
        this.nbPieces = 0;
        cosmetiques = new String[3];
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

        JPanel createServerPanel = new JPanel(new GridBagLayout());

        JPanel profilPanel = new JPanel(new GridBagLayout());

        cardPanel.add(loginPanel, "loginPanel");
        cardPanel.add(registerPanel, "registerPanel");
        cardPanel.add(partyPanel, "partyPanel");
        cardPanel.add(createServerPanel, "createServerPanel");
        cardPanel.add(profilPanel, "profilPanel");

        setGBC(gbc);
        setLoginPanel(loginPanel,cardLayout,cardPanel,gbc,partyPanel,profilPanel);
        setBackButton(backButton,cardLayout,cardPanel);
        setRegisterPanel(registerPanel,gbc,cardLayout,cardPanel,partyPanel,profilPanel);
        setCreateServerPanel(createServerPanel,gbc,cardLayout,cardPanel);

        frame.setLayout(new BorderLayout());
        frame.add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "loginPanel");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setLoginPanel(JPanel loginPanel, CardLayout cardLayout, JPanel cardPanel, GridBagConstraints gbc,JPanel partyPanel,JPanel profilPanel) {

        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(titleFont);

        JLabel loginLabel = new JLabel("Nom d'utilisateur");
        loginLabel.setFont(subtitleFont);

        JTextField loginField = new JTextField();
        loginField.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        loginField.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        loginField.setFont(inputFont);

        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(subtitleFont);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        passwordField.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        passwordField.setFont(inputFont);

        JButton loginButton = new JButton("Se connecter");
        loginButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        JButton registerButton = new JButton("S'inscrire");
        registerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "registerPanel");
            }
        });

        JLabel erreurInforamtionsLabel = new JLabel("Informations de connexion éronnées");
        erreurInforamtionsLabel.setFont(subtitleFont);
        erreurInforamtionsLabel.setForeground(Color.RED);
        erreurInforamtionsLabel.setVisible(false);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDTO loginDTO = new LoginDTO(loginField.getText(),Arrays.toString(passwordField.getPassword()));
                Protocol sendingProtocol =  new Protocol("MainMenuClient", "MainServer",(new Date()).toString(),"MainMenuClientDemandeConnexion",loginDTO);
                sortie.println(sendingProtocol.serialize());
                String messageRecu = null;
                try {
                    messageRecu = entree.readLine();
                    Protocol receivedProtocol = Protocol.deserialize(messageRecu);
                    InfoUserDTO infoUserDTO = receivedProtocol.getData();
                    if(receivedProtocol.getMessage().equals("ConnexionAcceptée")){
                        System.out.println("Connexion acceptée");
                        nbPieces = infoUserDTO.getNbPieces();
                        cosmetiques = infoUserDTO.getCosmetiques();
                        userName = infoUserDTO.getUserName();
                        System.out.println("USername : " + userName);
                        setPartyPanel(partyPanel,gbc,cardLayout,cardPanel,profilPanel);
                        cardLayout.show(cardPanel, "partyPanel");
                    }
                } catch (Exception ex) {
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

    public void setRegisterPanel(JPanel registerPanel, GridBagConstraints gbc, CardLayout cardLayout, JPanel cardPanel, JPanel partyPanel,JPanel profilPanel){

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

        JLabel erreurInforamtionsLabel2 = new JLabel("Login already used");
        erreurInforamtionsLabel2.setFont(subtitleFont);
        erreurInforamtionsLabel2.setForeground(Color.RED);
        erreurInforamtionsLabel2.setVisible(false);

        registerButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RegisterDTO registerDTO = new RegisterDTO(loginField2.getText(), Arrays.toString(passwordField2.getPassword()));
                    Protocol sendingProtocolRegister = new Protocol("MainMenuClient","MainServer",(new Date()).toString(),"MainMenuClientDemandeEnregistrement",registerDTO);
                    sortie.println(sendingProtocolRegister.serialize());
                    String messageRecu = entree.readLine();
                    Protocol receivedProtocolRegister = Protocol.deserialize(messageRecu);
                    InfoUserDTO infoUserDTO = receivedProtocolRegister.getData();
                    if(receivedProtocolRegister.getMessage().equals("EnregistrementAccepté")){
                        setPartyPanel(partyPanel,gbc,cardLayout,cardPanel,profilPanel);
                        userName = infoUserDTO.getUserName();
                        cardLayout.show(cardPanel, "partyPanel");
                    }else if(receivedProtocolRegister.getMessage().equals("EnregistrementRefusé")){
                        erreurInforamtionsLabel2.setVisible(true);
                    }

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton backButton = new JButton("Retour");
        backButton.setFont(subtitleFont);

        backButton.addActionListener(actionEvent -> cardLayout.show(cardPanel, "loginPanel"));

        registerPanel.add(backButton, gbc);
        registerPanel.add(titleLabel2, gbc);
        registerPanel.add(loginLabel2, gbc);
        registerPanel.add(loginField2,gbc);
        registerPanel.add(passwordLabel2, gbc);
        registerPanel.add(passwordField2,gbc);
        registerPanel.add(registerButton2,gbc);
    }

    public void setPartyPanel(JPanel partyPanel, GridBagConstraints gbc, CardLayout cardLayout, JPanel cardPanel, JPanel profilPanel) throws IOException {

        JLabel titleLabel3 = new JLabel("Liste des serveurs");
        titleLabel3.setFont(titleFont);
        DefaultListModel<String> serverListDisplay = new DefaultListModel<>();
        ArrayList<String[]> serverListInfo = new ArrayList<>();
        demandeServerList(serverListInfo,serverListDisplay);


        JList<String> serversList = new JList<>(serverListDisplay);
        serversList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serversList.setFont(inputFont);

        JScrollPane serversScrollPane = new JScrollPane(serversList);
        serversScrollPane.setPreferredSize(new Dimension(TAILLE_INPUT_X,500));

        JButton connexionServerButton = new JButton("Connexion");
        connexionServerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        connexionServerButton.addActionListener(actionEvent -> {
            int selectedIndex = serversList.getSelectedIndex();
            if(selectedIndex != -1){
                new Client(serverListInfo.get(selectedIndex)[1], Integer.parseInt(serverListInfo.get(selectedIndex)[2]));
            }
        });

        JButton createNewServerButton = new JButton("Créer un serveur");
        createNewServerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        createNewServerButton.addActionListener(actionEvent -> {
            cardLayout.show(cardPanel,"createServerPanel");
        });


        JButton profilButton = new JButton("Profil");
        profilButton.setFont(subtitleFont);

        LoginDTO loginDTO = new LoginDTO(this.userName,null);
        System.out.println("nooooooooooom" + loginDTO.getLogin());
        Protocol sendingDemandeInfoUserProtocol = new Protocol("MainMenuClient","MainServer",(new Date()).toString(),"MainMenuClientDemandeInfoUser",loginDTO);
        sortie.println(sendingDemandeInfoUserProtocol.serialize());
        try {
            String messageRecu = entree.readLine();
            Protocol receivedProtocolRegister = Protocol.deserialize(messageRecu);
            InfoUserDTO infoUserDTO = receivedProtocolRegister.getData();
            nbPieces = infoUserDTO.getNbPieces();
            cosmetiques = infoUserDTO.getCosmetiques();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setProfilPanel(profilPanel,cardLayout,cardPanel,gbc);

        profilButton.addActionListener(actionEvent -> {
            updateCosmetiques(profilPanel, gbc);
            cardLayout.show(cardPanel, "profilPanel");
        });

        partyPanel.add(profilButton);
        partyPanel.add(titleLabel3, gbc);
        partyPanel.add(serversScrollPane, gbc);
        partyPanel.add(connexionServerButton, gbc);
        partyPanel.add(createNewServerButton, gbc);
    }

    public void setCreateServerPanel(JPanel createServerPanel, GridBagConstraints gbc, CardLayout cardLayout, JPanel cardPanel){
        JButton backButton = new JButton("Retour") ;
        File dossierCible = new File("src/main/resources/layouts");
        File[] files = dossierCible.listFiles();
        int tailleGrid = (int) Math.sqrt(Objects.requireNonNull(dossierCible.listFiles()).length);
        JPanel serverListPanel = new JPanel(new GridLayout(tailleGrid,tailleGrid));
        JButton[] mapListButton = new JButton[Objects.requireNonNull(dossierCible.listFiles()).length];
        for(int i = 0; i < Objects.requireNonNull(files).length; i++){
            mapListButton[i] = new JButton(files[i].getName());
            mapListButton[i].setFont(new Font("Arial", Font.PLAIN, 20));
            int finalI = i;
            mapListButton[i].addActionListener(actionEvent -> {
                try {
                    NewServerDTO newServerDTO = new NewServerDTO("layouts/"+ files[finalI].getName());
                    Protocol sendingProtocol = new Protocol("MainMenuClient","MainServer",(new Date()).toString(),"CreationServeur",newServerDTO);
                    sortie.println(sendingProtocol.serialize());
                    String messageRecu = entree.readLine();
                    Protocol receivedProtocol = Protocol.deserialize(messageRecu);
                    InfoServerDTO infoServerDTO = receivedProtocol.getData();
                    System.out.println("Port du serveur crée : " + infoServerDTO.getPort());
                    new Client(infoServerDTO.getIp(),infoServerDTO.getPort());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            serverListPanel.add(mapListButton[i]);
        }

        backButton.addActionListener(actionEvent -> {
            cardLayout.show(cardPanel,"partyPanel");
        });
        JLabel choixMapLabel = new JLabel("Veuillez choisir une map :");
        choixMapLabel.setFont(titleFont);
        createServerPanel.add(choixMapLabel, gbc);
        createServerPanel.add(serverListPanel, gbc);
        createServerPanel.add(backButton,gbc);
    }

    public void demandeServerList(ArrayList<String[]> serverListInfo,DefaultListModel<String> serverListDisplay){
        Protocol sendingProtocolGameList = new Protocol("MainMenuClient","MainServer",(new Date()).toString(),"MainMenuClientDemandeServerList",null);
        sortie.println(sendingProtocolGameList.serialize());
        String messageRecu;
        try{
            messageRecu = entree.readLine();
            Protocol receivedProtocol = Protocol.deserialize(messageRecu);
            ServerListDTO serverListDTO = receivedProtocol.getData();
            serverListInfo.clear();
            serverListInfo.addAll(serverListDTO.getServerList());
            for(int i = 0; i < serverListInfo.size(); i++){
                serverListDisplay.add(i,serverListInfo.get(i)[0] + "IP : " + serverListInfo.get(i)[1] + "Port : " + serverListInfo.get(i)[2]);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void setProfilPanel(JPanel profilPanel, CardLayout cardLayout,JPanel cardPanel, GridBagConstraints gbc){
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(actionEvent -> cardLayout.show(cardPanel, "partyPanel"));

        JLabel userNameLabel = new JLabel("Profil de : " + this.userName);
        userNameLabel.setFont(titleFont);

        JLabel nbPiecesLabel = new JLabel("Nombre de pièces : " + this.nbPieces);
        nbPiecesLabel.setFont(inputFont);

        JLabel cosmetiquesLabel = new JLabel("Liste des cosmétiques : ");
        cosmetiquesLabel.setFont(inputFont);

        JPanel cosmetiqueGrid = new JPanel(new GridLayout(1,cosmetiques.length));
        for(int i = 0 ; i < cosmetiques.length; i++){
            cosmetiqueGrid.add(new JButton(cosmetiques[i]));
        }

        profilPanel.add(backButton, gbc);
        profilPanel.add(userNameLabel, gbc);
        profilPanel.add(nbPiecesLabel,gbc);
        profilPanel.add(cosmetiquesLabel,gbc);
        profilPanel.add(cosmetiqueGrid,gbc);
    }

    public void updateCosmetiques(JPanel profilPanel, GridBagConstraints gbc){
        ImageIcon imageIcon;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        profilPanel.remove(4);
        LoginDTO loginDTO =  new LoginDTO(this.userName,null);
        Protocol sendingProtocolGameList = new Protocol("MainMenuClient","MainServer",(new Date()).toString(),"MainMenuClientDemandeInfoUser",loginDTO);
        sortie.println(sendingProtocolGameList.serialize());
        String messageRecu;
        try{
            System.out.println("bonjour");
            messageRecu = entree.readLine();
            System.out.println("bonjour2");
            Protocol receivedProtocol = Protocol.deserialize(messageRecu);
            InfoUserDTO infoUserDTO = receivedProtocol.getData();
            cosmetiques = infoUserDTO.getCosmetiques();
            System.out.println("Cosmetqiuese : " + Arrays.toString(cosmetiques));
            JPanel cosmetiqueGrid = new JPanel(new GridLayout(1,cosmetiques.length));
            for(int i = 0 ; i < cosmetiques.length; i++){
                URL imageUrl = classLoader.getResource("skins/" + cosmetiques[i]);
                if (imageUrl == null) {
                    System.out.println("⚠️ Image non trouvée : skins/" + cosmetiques[i] + ".png");
                } else {
                    System.out.println("✔ Image trouvée : " + imageUrl);
                }
                assert imageUrl != null;
                imageIcon = new ImageIcon(imageUrl);
                JButton button = new JButton(imageIcon);
                button.addActionListener(actionEvent -> {
                    this.skinChoisie = imageUrl.toString();
                });
                button.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
                cosmetiqueGrid.add(button);
            }
            profilPanel.add(cosmetiqueGrid,gbc);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        new MainMenu();
    }
}
