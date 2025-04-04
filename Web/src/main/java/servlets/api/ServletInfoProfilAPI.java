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

/**
 * Servlet implementation class ServletInfoProilAPI
 */
@WebServlet("/api/infoProfil")
public class ServletInfoProfilAPI extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
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
		try {
			StringMapDTO stringMapDTO = receivedProtocol.getData();
			usernameATTR = stringMapDTO.get("username");
		} catch (Exception e) {
			// Ne rien faire car les données seront juste pas remplis ce qui trouvera aucun joueur correspondant
		}
		
		Joueur joueur = joueurDAO.trouverParUsername(usernameATTR);
        
        Protocol protocol;
        if (joueur != null) {
    
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

            // Objet Protocol pour la réponse (joueur valid)
            protocol = new Protocol(
                "WebServer",
                "MainServer",
                new java.util.Date().toString(),
                "WebServerUserInfo",
                infoUserDTO
            );
        } else {
        	// Objet Protocol pour la réponse (erreur)
            protocol = new Protocol(
                "WebServer",
                "MainServer",
                new java.util.Date().toString(),
                "WebServerUserInfoError",
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
