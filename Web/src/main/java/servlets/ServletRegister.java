package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bdd.Joueurs;
import model.beans.Joueur;
import model.dao.DAOFactory;
import model.dao.JoueurDao;

/**
 * Servlet implementation class ServletRegister
 */
@WebServlet("/inscription")
public class ServletRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String REGISTER_JSP = "/WEB-INF/pages/Register.jsp";
    private JoueurDao joueurDAO;
    
    public void init() throws ServletException{
    	DAOFactory daoFactory = DAOFactory.getInstance();
    	this.joueurDAO = daoFactory.getJoueurDao();    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.getServletContext().getRequestDispatcher(REGISTER_JSP).forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Joueur joueur = new Joueur();
		joueur.setUsername(request.getParameter("nom"));
		joueur.setMotDePasse(request.getParameter("prenom"));
		joueur.setNbPieces(0);
        
        Joueurs tableJoueurs = new Joueurs();
        joueurDAO.ajouterJoueur(joueur);
        
        request.setAttribute("utilisateurs", tableJoueurs.recupererJoueurs());
        
        this.getServletContext().getRequestDispatcher("/WEB-INF/Accueil.jsp").forward(request, response);
	}

}


