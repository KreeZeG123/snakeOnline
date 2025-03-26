package bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import model.beans.Joueur;

public class Joueurs {
    private Connection connexion;
    
    public List<Joueur> recupererJoueurs() {
        List<Joueur> joueurs = new ArrayList<Joueur>();
        Statement statement = null;
        ResultSet resultat = null;

        loadDatabase();
        
        try {
            statement = connexion.createStatement();

            // Exécution de la requête
            resultat = statement.executeQuery("SELECT username, password, nbPieces FROM joueurs;");

            // Récupération des données
            while (resultat.next()) {
                String username = resultat.getString("username");
                String password = resultat.getString("password");
                int nbPieces = resultat.getInt("nbPieces");
                
                Joueur joueur = new Joueur();
                joueur.setUsername(username);
                joueur.setMotDePasse(password);
                
                joueurs.add(joueur);
            }
        } catch (SQLException e) {
        } finally {
            // Fermeture de la connexion
            try {
                if (resultat != null)
                    resultat.close();
                if (statement != null)
                    statement.close();
                if (connexion != null)
                    connexion.close();
            } catch (SQLException ignore) {
            }
        }
        
        return joueurs;
    }
    
    private void loadDatabase() {
        // Chargement du driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }

        try {
            connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaee", "root", "mysqlroot");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void ajouterJoueur(Joueur joueur) {
        loadDatabase();
        
        try {
            PreparedStatement preparedStatement = connexion.prepareStatement("INSERT INTO joueurs(username, password, nbPieces) VALUES(?, ?, 0);");
            preparedStatement.setString(1, joueur.getUsername());
            preparedStatement.setString(2, joueur.getMotDePasse());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}