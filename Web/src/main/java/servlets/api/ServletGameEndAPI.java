package servlets.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.beans.Joueur;
import model.dao.exceptions.DAOException;
import model.dao.factory.DAOFactory;
import model.dao.interfaces.JoueurDao;
import model.dto.Protocol;
import model.dto.snakeGame.GameEndDTO;

/**
 * Servlet implementation class ServletGameEndAPI
 */
@WebServlet("/api/gameEnd")
public class ServletGameEndAPI extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private static final int NB_PIECES_PAR_PARTIE_COMPLETEE = 50;
	private static final int NB_PIECES_BONUS_PAR_PARTIE_GAGNEE = 50;
       
	private JoueurDao joueurDAO;
    
    public void init() throws ServletException{
    	// Récupère la DAOFactory depuis le servletContext
    	DAOFactory daoFactory = DAOFactory.getInstanceFromContext( this.getServletContext() );
    	this.joueurDAO = daoFactory.getJoueurDao();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Obtention du protocol
		Protocol receivedProtocol = Protocol.obtainProtocol(request);
		
		Protocol protocol = null;
		try {
			// Récupération des données
	        GameEndDTO gameEndDTO = receivedProtocol.getData();
	        List<String> players = gameEndDTO.getPlayers();
	        List<Integer> scores = gameEndDTO.getScores();

	        // Recherche des joueurs dans la base de données
	        List<Joueur> joueurs = this.joueurDAO.trouverParUsernames(players);

	        // Création d'une map pour un accès rapide par username
	        Map<String, Joueur> joueurMap = new HashMap<>();
	        for (Joueur joueur : joueurs) {
	            joueurMap.put(joueur.getUsername(), joueur);
	        }

	        // Liste des joueurs valides et scores associés
	        Joueur winner = null;
	        int maxScore = Integer.MIN_VALUE;
	        for (int i = 0; i < players.size(); i++) {
	            Joueur joueur = joueurMap.get(players.get(i));
	            if (joueur != null) {
	            	System.out.println("Add Score : " + joueur.getUsername());
	                int score = scores.get(i);
	                joueur.addScore(score);
	                joueur.addPieces(NB_PIECES_PAR_PARTIE_COMPLETEE);

	                // Vérification du gagnant
	                if (score > maxScore) {
	                    maxScore = score;
	                    winner = joueur;
	                }
	            }
	        }

	        // Bonus pour le gagnant
	        if (winner != null) {
	            winner.addScore(NB_PIECES_BONUS_PAR_PARTIE_GAGNEE);
	        }
	        
	        // Update des joueurs en base de données
	        for (Joueur joueur : joueurMap.values()) {
	        	this.joueurDAO.updateJoueur(joueur);
	        }
			
			protocol = new Protocol(
                "WebServer",
                "SnakeServer",
                new java.util.Date().toString(),
                "WebServerEndGameSucess",
                null
            );
		} catch (DAOException e) {
			// Ne rien faire car le protocol de retours ne sera juste pas remplis et l'erreur est géré plus tard
		}
		catch (ClassNotFoundException e) {
			// Ne rien faire car le protocol de retours ne sera juste pas remplis et l'erreur est géré plus tard
		}
		
		// Gestion erreur dans la logique de l'api
		if ( protocol == null ) {
			protocol = new Protocol(
                "WebServer",
                "SnakeServer",
                new java.util.Date().toString(),
                "WebServerEndGameFail",
                null
            );
		}
        
        // Sérialiser l'objet Protocol en JSON
        String jsonResponse = protocol.serialize();
        
        // Définir le type de contenu de la réponse
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Envoyer la réponse JSON
        response.getWriter().print(jsonResponse);
        response.getWriter().flush();
	}

}
