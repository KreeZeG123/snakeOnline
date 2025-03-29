package servlets.pages;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.beans.Joueur;
import model.dao.factory.DAOFactory;
import model.dao.interfaces.JoueurDao;

/**
 * Servlet implementation class ServletClassement
 */
@WebServlet("/classement")
public class ServletClassement extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CLASSEMENTS_ATTR = "classements";
	
	private static final String CLASSEMENT_JSP = "/WEB-INF/pages/Classement.jsp";
	
	private static final int CLASSEMENT_MAX_NUMBER_OF_PLAYERS_DISPLAYED = 15;

	private JoueurDao joueurDAO;
    
    public void init() throws ServletException{
    	DAOFactory daoFactory = DAOFactory.getInstance();
    	this.joueurDAO = daoFactory.getJoueurDao();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Joueur> classementJoueurs = this.joueurDAO.classementJoueurs(CLASSEMENT_MAX_NUMBER_OF_PLAYERS_DISPLAYED);
        
        // Ajouter la liste des classements à la requête
        request.setAttribute(CLASSEMENTS_ATTR, classementJoueurs);
		
		this.getServletContext().getRequestDispatcher(CLASSEMENT_JSP).forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
