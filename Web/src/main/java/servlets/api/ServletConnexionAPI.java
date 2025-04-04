package servlets.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.beans.Joueur;
import model.dao.factory.DAOFactory;
import model.dao.interfaces.JoueurDao;
import model.dto.StringMapDTO;
import model.dto.Protocol;
import model.dto.mainMenu.InfoUserDTO;
import model.dto.mainMenu.LoginDTO;
import model.forms.FormConnexion;
import model.utils.Decryptor;

/**
 * Servlet implementation class ServletLoginAPI
 */
@WebServlet("/api/connexion")
public class ServletConnexionAPI extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	private static final String SECRET_KEY = "qMl8G+yPzX5A9TkWyq7GQ==";

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
		
		// Récupération des données
		String usernameATTR = "";
		String passwordATTR = "";
		try {
			LoginDTO loginDTO = receivedProtocol.getData();
			usernameATTR = loginDTO.getLogin();
			passwordATTR = loginDTO.getPassword();
		} catch (ClassNotFoundException e) {
			// Ne rien faire car les données seront juste pas remplis ce qui causera un refus du formulaire
		}
		
		/* Préparation de l'objet formulaire */
        FormConnexion form = new FormConnexion( joueurDAO );

        /* Traitement de la requête et récupération du bean en résultant */
        Joueur joueur = form.connecterJoueur(usernameATTR, Decryptor.decrypt(SECRET_KEY, passwordATTR));
        
        Protocol protocol;
        if (form.getErreurs().isEmpty()) {
    
        	// Récupération des informations
        	Long joueurID = joueur.getId();
            String username = joueur.getUsername();
            String skins = joueur.getSkins();
            int nbPieces = joueur.getNbPieces();
        	
        	// Créer un objet InfoUserDTO avec les informations reçues
            InfoUserDTO infoUserDTO = new InfoUserDTO(
        		Math.toIntExact(joueurID),
        		username,
        		skins,
        		nbPieces
    		);

            // Objet Protocol pour la réponse (accepté)
            protocol = new Protocol(
                "WebServer",
                "MainServer",
                new java.util.Date().toString(),
                "WebServerUserInfoAcknowledged",
                infoUserDTO
            );
        } else {
        	// Objet Protocol pour la réponse (refus)
            protocol = new Protocol(
                "WebServer",
                "MainServer",
                new java.util.Date().toString(),
                "WebServerUserInfoRefused",
                new StringMapDTO(form.getErreurs())
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
