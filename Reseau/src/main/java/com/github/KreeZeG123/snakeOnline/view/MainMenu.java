package com.github.KreeZeG123.snakeOnline.view;

import com.github.KreeZeG123.snakeOnline.main.Client;
import com.github.KreeZeG123.snakeOnline.model.dto.StringMapDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.mainMenu.*;
import com.github.KreeZeG123.snakeOnline.utils.PasswordEncryptor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.List;


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
    private String cosmetiques;
    private String userName;
    private String skinChoisie = null;
    private DefaultListModel<String> serverListDisplay = new DefaultListModel<>();
    private ArrayList<String[]> serverListInfo = new ArrayList<>();

    public MainMenu() throws Exception {
        JFrame frame = new JFrame("Menu principal");
        frame.setSize(800, 800);
        Dimension windowSize = frame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        frame.setLocation(dx, dy);

        this.nbPieces = 0;
        cosmetiques = "";

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        JButton backButton = new JButton("Retour");

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel waitingForServerPanel = new JPanel(new GridBagLayout());

        JPanel loginPanel = new JPanel(new GridBagLayout());

        JPanel registerPanel = new JPanel(new GridBagLayout());

        JPanel partyPanel = new JPanel(new GridBagLayout());

        JPanel createServerPanel = new JPanel(new GridBagLayout());

        JPanel profilPanel = new JPanel(new GridBagLayout());

        cardPanel.add(waitingForServerPanel, "waitingForServerPanel");
        cardPanel.add(loginPanel, "loginPanel");
        cardPanel.add(registerPanel, "registerPanel");
        cardPanel.add(partyPanel, "partyPanel");
        cardPanel.add(createServerPanel, "createServerPanel");
        cardPanel.add(profilPanel, "profilPanel");

        setGBC(gbc);
        setWaitingForServerPanel(waitingForServerPanel, cardLayout, cardPanel, gbc);
        setLoginPanel(loginPanel,cardLayout,cardPanel,gbc,partyPanel,profilPanel);
        setBackButton(backButton,cardLayout,cardPanel);
        setRegisterPanel(registerPanel,gbc,cardLayout,cardPanel,partyPanel,profilPanel);
        setCreateServerPanel(createServerPanel,gbc,cardLayout,cardPanel);
        setProfilPanel(profilPanel, cardLayout,cardPanel,gbc);

        frame.setLayout(new BorderLayout());
        frame.add(cardPanel, BorderLayout.CENTER);


        try {
            this.socket = new Socket(ip, port);
            this.entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.sortie = new PrintWriter(socket.getOutputStream(), true);
            cardLayout.show(cardPanel, "loginPanel");
        } catch (ConnectException e) {
            cardLayout.show(cardPanel, "waitingForServerPanel");
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setWaitingForServerPanel(JPanel waitingForServerPanel, CardLayout cardLayout, JPanel cardPanel, GridBagConstraints gbc) {
        JLabel titleLabel = new JLabel("En attente du serveur principal ...");
        titleLabel.setFont(titleFont);

        JButton retryButton = new JButton("Réessayer");
        retryButton.setPreferredSize(new Dimension(100, 40));
        retryButton.setFont(new Font("Arial", Font.PLAIN, 15));
        retryButton.setBorder(new EmptyBorder(10, 10, 10, 10)); // Un peu de marge autour du bouton
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new Socket(ip, port);
                    entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    sortie = new PrintWriter(socket.getOutputStream(), true);
                    cardLayout.show(cardPanel, "loginPanel");
                } catch (ConnectException ec) {
                    // Ne rien faire
                }
                catch (IOException eio) {
                    throw new RuntimeException(eio);
                }
            }
        });

        waitingForServerPanel.add(titleLabel, gbc);
        waitingForServerPanel.add(retryButton, gbc);
    }

    public void setLoginPanel(JPanel loginPanel, CardLayout cardLayout, JPanel cardPanel, GridBagConstraints gbc,JPanel partyPanel,JPanel profilPanel) {

        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(titleFont);

        JLabel loginLabel = new JLabel("Nom d'utilisateur");
        loginLabel.setFont(subtitleFont);

        JLabel loginErrorLabel = new JLabel("");
        loginErrorLabel.setFont(subtitleFont);
        loginErrorLabel.setForeground(Color.RED);


        JTextField loginField = new JTextField();
        loginField.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        loginField.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        loginField.setFont(inputFont);

        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(subtitleFont);

        JLabel passwordErrorLabel = new JLabel("");
        passwordErrorLabel.setFont(subtitleFont);
        passwordErrorLabel.setForeground(Color.RED);

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
                LoginDTO loginDTO = new LoginDTO(
                        loginField.getText(),
                        PasswordEncryptor.encryptPassword(String.valueOf(passwordField.getPassword()))
                );
                Protocol sendingProtocol =  new Protocol(
                        "MainMenuClient",
                        "MainServer",(new Date()).toString(),
                        "MainMenuClientDemandeConnexion",
                        loginDTO
                );
                sortie.println(sendingProtocol.serialize());
                try {
                    String messageRecu = entree.readLine();
                    Protocol receivedProtocol = Protocol.deserialize(messageRecu);
                    String message = receivedProtocol.getMessage();
                    switch (message) {
                        case "MainServerUserInfoAcknowledged":
                            System.out.println("Connexion acceptée");
                            InfoUserDTO infoUserDTO = receivedProtocol.getData();
                            nbPieces = infoUserDTO.getNbPieces();
                            cosmetiques = infoUserDTO.getCosmetiques();
                            userName = infoUserDTO.getUserName();
                            System.out.println("USername : " + userName);
                            setPartyPanel(partyPanel,gbc,cardLayout,cardPanel,profilPanel);
                            loginLabel.setText("");
                            loginErrorLabel.setText("");
                            passwordLabel.setText("");
                            passwordErrorLabel.setText("");
                            cardLayout.show(cardPanel, "partyPanel");
                            break;
                        case "MainServerUserInfoRefused":
                            System.out.println("Connexion refusé");
                            StringMapDTO stringMapDTO = receivedProtocol.getData();
                            loginErrorLabel.setText("");
                            passwordErrorLabel.setText("");
                            if (stringMapDTO.get("status").equals("echec")) {
                                String usernameAndPasswordError = stringMapDTO.get("usernameAndPassword");
                                if (usernameAndPasswordError != null) {
                                    loginErrorLabel.setText(usernameAndPasswordError);
                                    passwordErrorLabel.setText(usernameAndPasswordError);
                                } else {
                                    String usernameError = stringMapDTO.get("username");
                                    if ( usernameError != null ) {
                                        loginErrorLabel.setText(usernameError);
                                    }

                                    String passwordError = stringMapDTO.get("password");
                                    if ( passwordError != null ) {
                                        passwordErrorLabel.setText(passwordError);
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        registerButton.addActionListener(actionEvent -> cardLayout.show(cardPanel, "registerPanel"));

        loginPanel.add(titleLabel, gbc);
        loginPanel.add(loginLabel, gbc);
        loginPanel.add(loginErrorLabel, gbc);
        loginPanel.add(loginField,gbc);
        loginPanel.add(passwordLabel, gbc);
        loginPanel.add(passwordErrorLabel, gbc);
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

        JLabel loginLabel2 = new JLabel("Login :");
        loginLabel2.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel loginErrorLabel2 = new JLabel("");
        loginErrorLabel2.setFont(subtitleFont);
        loginErrorLabel2.setForeground(Color.RED);

        JTextField loginField2 = new JTextField();
        loginField2.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        loginField2.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        loginField2.setFont(inputFont);

        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel emailErrorLabel = new JLabel("");
        emailErrorLabel.setFont(subtitleFont);
        emailErrorLabel.setForeground(Color.RED);

        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        emailField.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        emailField.setFont(inputFont);

        JLabel passwordLabel1 = new JLabel("Password :");
        passwordLabel1.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel passwordErrorLabel1 = new JLabel("");
        passwordErrorLabel1.setFont(subtitleFont);
        passwordErrorLabel1.setForeground(Color.RED);

        JPasswordField passwordField1 = new JPasswordField();
        passwordField1.setMaximumSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        passwordField1.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        JLabel passwordLabel2 = new JLabel("Password (Confirm) :");
        passwordLabel2.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel passwordErrorLabel2 = new JLabel("");
        passwordErrorLabel2.setFont(subtitleFont);
        passwordErrorLabel2.setForeground(Color.RED);

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
                    RegisterDTO registerDTO = new RegisterDTO(
                            loginField2.getText(),
                            emailField.getText(),
                            PasswordEncryptor.encryptPassword(String.valueOf(passwordField1.getPassword())),
                            PasswordEncryptor.encryptPassword(String.valueOf(passwordField2.getPassword()))
                    );
                    Protocol sendingProtocolRegister = new Protocol(
                            "MainMenuClient",
                            "MainServer",
                            (new Date()).toString(),
                            "MainMenuClientInscription",
                            registerDTO
                    );

                    sortie.println(sendingProtocolRegister.serialize());
                    try {
                        String messageRecu = entree.readLine();
                        Protocol receivedProtocol = Protocol.deserialize(messageRecu);
                        String message = receivedProtocol.getMessage();
                        System.out.println(messageRecu);
                        switch (message) {
                            case "MainServerInscriptionAcknowledged":
                                System.out.println("Inscription acceptée");
                                InfoUserDTO infoUserDTO = receivedProtocol.getData();
                                nbPieces = infoUserDTO.getNbPieces();
                                cosmetiques = infoUserDTO.getCosmetiques();
                                userName = infoUserDTO.getUserName();
                                System.out.println("USername : " + userName);
                                setPartyPanel(partyPanel,gbc,cardLayout,cardPanel,profilPanel);
                                cardLayout.show(cardPanel, "partyPanel");
                                break;
                            case "MainServerInscriptionRefused":
                                System.out.println("Inscription refusé");
                                StringMapDTO stringMapDTO = receivedProtocol.getData();
                                loginErrorLabel2.setText("");
                                emailErrorLabel.setText("");
                                passwordErrorLabel1.setText("");
                                passwordErrorLabel2.setText("");
                                if (stringMapDTO.get("status").equals("echec")) {
                                    String usernameError = stringMapDTO.get("username");
                                    if ( usernameError != null ) {
                                        loginErrorLabel2.setText(usernameError);
                                    }

                                    String emailError = stringMapDTO.get("email");
                                    if ( emailError != null ) {
                                        emailErrorLabel.setText(emailError);
                                    }

                                    String passwordError = stringMapDTO.get("password");
                                    if ( passwordError != null ) {
                                        passwordErrorLabel1.setText(passwordError);
                                    }

                                    String passwordError2 = stringMapDTO.get("confirm-password");
                                    if ( passwordError2 != null ) {
                                        passwordErrorLabel2.setText(passwordError2);
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
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
        registerPanel.add(loginErrorLabel2, gbc);
        registerPanel.add(loginField2,gbc);
        registerPanel.add(emailLabel, gbc);
        registerPanel.add(emailErrorLabel, gbc);
        registerPanel.add(emailField, gbc);
        registerPanel.add(passwordLabel1, gbc);
        registerPanel.add(passwordErrorLabel1, gbc);
        registerPanel.add(passwordField1,gbc);
        registerPanel.add(passwordLabel2, gbc);
        registerPanel.add(passwordErrorLabel2, gbc);
        registerPanel.add(passwordField2,gbc);
        registerPanel.add(registerButton2,gbc);
    }

    public void setPartyPanel(JPanel partyPanel, GridBagConstraints gbc, CardLayout cardLayout, JPanel cardPanel, JPanel profilPanel) throws IOException {

        partyPanel.removeAll();

        JLabel titleLabel3 = new JLabel("Liste des serveurs");
        titleLabel3.setFont(titleFont);
        serverListDisplay = new DefaultListModel<>();
        serverListInfo = new ArrayList<>();
        demandeServerList(serverListInfo,serverListDisplay);


        JList<String> serversList = new JList<>(serverListDisplay);
        serversList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serversList.setFont(inputFont);

        JScrollPane serversScrollPane = new JScrollPane(serversList);
        serversScrollPane.setPreferredSize(new Dimension(TAILLE_INPUT_X,500));

        JButton refreshServerListButton = new JButton("Rafraichir");
        refreshServerListButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        refreshServerListButton.addActionListener(actionEvent -> {
            demandeServerList(serverListInfo,serverListDisplay);
        });

        JButton connexionServerButton = new JButton("Connexion");
        connexionServerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));

        connexionServerButton.addActionListener(actionEvent -> {
            int selectedIndex = serversList.getSelectedIndex();
            if(selectedIndex != -1){
                new Client(serverListInfo.get(selectedIndex)[1], Integer.parseInt(serverListInfo.get(selectedIndex)[2]), this.userName, this.skinChoisie);
            }
        });

        JButton createNewServerButton = new JButton("Créer un serveur");
        createNewServerButton.setPreferredSize(new Dimension(TAILLE_INPUT_X,TAILLE_INPUT_Y));
        createNewServerButton.addActionListener(actionEvent -> {
            cardLayout.show(cardPanel,"createServerPanel");
        });


        JButton profilButton = new JButton("Profil");
        profilButton.setFont(subtitleFont);

        profilButton.addActionListener(actionEvent -> {
            System.out.println("test");
            updateCosmetiques(profilPanel, gbc);
            System.out.println("profilPanel");
            cardLayout.show(cardPanel, "profilPanel");
        });

        partyPanel.add(profilButton);
        partyPanel.add(titleLabel3, gbc);
        partyPanel.add(serversScrollPane, gbc);
        partyPanel.add(refreshServerListButton, gbc);
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
                    new Client(infoServerDTO.getIp(),infoServerDTO.getPort(), this.userName, this.skinChoisie);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            serverListPanel.add(mapListButton[i]);
        }

        backButton.addActionListener(actionEvent -> {
            demandeServerList(this.serverListInfo, this.serverListDisplay);
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
            serverListDisplay.clear();
            for(int i = 0; i < serverListInfo.size(); i++){
                serverListDisplay.add(i,serverListInfo.get(i)[0] + " IP : " + serverListInfo.get(i)[1] + " Port : " + serverListInfo.get(i)[2]);
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

        List<String> cosmetiquesList = Arrays.asList(cosmetiques.split(","));
        JPanel cosmetiqueGrid = new JPanel(new GridLayout(1,cosmetiquesList.size()));
        for (String c : cosmetiquesList){
            cosmetiqueGrid.add(new JButton(c));
        }

        JButton logoutButton = new JButton("Se Deconnecter");
        logoutButton.addActionListener(actionEvent -> {
            cardLayout.show(cardPanel, "loginPanel");
        });

        profilPanel.add(backButton, gbc);
        profilPanel.add(userNameLabel, gbc);
        profilPanel.add(nbPiecesLabel,gbc);
        profilPanel.add(cosmetiquesLabel,gbc);
        profilPanel.add(cosmetiqueGrid,gbc);
        profilPanel.add(logoutButton, gbc);

        Component[] components = profilPanel.getComponents();
        for (int i = 0 ; i < components.length ; i++){
            if (components[i] instanceof JButton && ((JButton) components[i]).getText().equals("Se Deconnecter")){
                System.out.println("logout : " + i);
            }
        }
    }

    public void updateCosmetiques(JPanel profilPanel, GridBagConstraints gbc){
        System.out.println("updateCosmetique");
        System.out.println("username : " + this.userName);
        ImageIcon imageIcon;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Sauvegarde le bouton logout et le retire (dernier element)
        JButton btnLogout = (JButton) profilPanel.getComponent(5);
        profilPanel.remove(profilPanel.getComponents().length - 1);
        // Retire encore le denier element (grid cosmetique)
        profilPanel.remove(profilPanel.getComponents().length - 1);
        StringMapDTO stringMapDTO = new StringMapDTO("username", this.userName);
        Protocol sendingDemandeInfoUserProtocol = new Protocol(
                "MainMenuClient",
                "MainServer",
                (new Date()).toString(),
                "MainMenuClientDemandeInfoUser",
                stringMapDTO
        );
        sortie.println(sendingDemandeInfoUserProtocol.serialize());
        System.out.println("envoyé");
        String messageRecu;
        try{
            messageRecu = entree.readLine();

            Protocol receivedProtocol = Protocol.deserialize(messageRecu);
            InfoUserDTO infoUserDTO = receivedProtocol.getData();

            this.userName = infoUserDTO.getUserName();
            this.nbPieces = infoUserDTO.getNbPieces();
            this.cosmetiques = infoUserDTO.getCosmetiques();

            ( (JLabel) profilPanel.getComponent(1)).setText("Profil de : " + infoUserDTO.getUserName());
            ( (JLabel) profilPanel.getComponent(2)).setText("Nombre de pièces : " + infoUserDTO.getNbPieces());

            List<String> cosmetiquesList = Arrays.asList(cosmetiques.split(","));
            JPanel cosmetiqueGrid = new JPanel(new GridLayout(1,cosmetiquesList.size()));
            final List<JButton> btns = new ArrayList<>();

            // Ajout btn skin defaut
            URL defaultSkinImgURL = classLoader.getResource("skins/snake_default_green.png" );
            if ( defaultSkinImgURL != null ){
                ImageIcon imageIconDefaultSkin = new ImageIcon(defaultSkinImgURL);
                JButton button = new JButton(imageIconDefaultSkin);
                button.addActionListener(actionEvent -> {
                    for (JButton b : btns){
                        b.setEnabled(true);
                    }
                    button.setEnabled(false);
                    this.skinChoisie = null;
                });
                button.setPreferredSize(new Dimension(imageIconDefaultSkin.getIconWidth(), imageIconDefaultSkin.getIconHeight()));
                if ( this.skinChoisie  == null ){
                    button.setEnabled(false);
                }
                cosmetiqueGrid.add(button);
                btns.add(button);
            }

            // Ajout btn skins unlocked
            for (String cosmetiqueID : cosmetiquesList){
                URL imageUrl = classLoader.getResource("skins/snake_skin_" + cosmetiqueID + ".png" );
                if (imageUrl == null) {
                    System.out.println("⚠️ Image non trouvée : skins/snake_skin_" + cosmetiqueID + ".png");
                } else {
                    System.out.println("✔ Image trouvée : " + imageUrl);
                }
                assert imageUrl != null;
                imageIcon = new ImageIcon(imageUrl);
                JButton button = new JButton(imageIcon);
                button.addActionListener(actionEvent -> {
                    for (JButton b : btns){
                       b.setEnabled(true);
                    }
                    button.setEnabled(false);
                    this.skinChoisie = cosmetiqueID;
                });
                button.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
                System.out.println(this.skinChoisie + " vs " + cosmetiqueID);
                if ( cosmetiqueID.equals(this.skinChoisie )){
                    button.setEnabled(false);
                }
                cosmetiqueGrid.add(button);
                btns.add(button);
            }

            profilPanel.add(cosmetiqueGrid,gbc);
            profilPanel.add(btnLogout,gbc);

            // Forcer le redessin du panel après la modification
            profilPanel.revalidate();
            profilPanel.repaint();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        new MainMenu();
    }
}
