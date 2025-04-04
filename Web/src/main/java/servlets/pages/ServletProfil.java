package servlets.pages;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.beans.Item;
import model.beans.Joueur;
import model.dao.factory.DAOFactory;
import model.dao.interfaces.ItemDAO;
import model.dao.interfaces.JoueurDao;

/**
 * Servlet implementation class ServletProfil
 */
@WebServlet("/profil")
public class ServletProfil extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ITEMS_ATTR = "items";
	private static final String ATT_JOUEUR = "joueur";
	private static final String ATT_JOUEUR_ID_SESSION = "joueurID";
	
	private static final String PROFIL_JSP = "/WEB-INF/pages/Profil.jsp";
       
	private JoueurDao joueurDAO;
	private ItemDAO itemDao;
    
    public void init() throws ServletException{
    	// Récupère la DAOFactory depuis le servletContext
    	DAOFactory daoFactory = DAOFactory.getInstanceFromContext( this.getServletContext() );
    	this.joueurDAO = daoFactory.getJoueurDao();
    	this.itemDao = daoFactory.getItemDAO();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// Obtention du joueur
		HttpSession session = request.getSession();
		Long joueurIDString = (Long) session.getAttribute(ATT_JOUEUR_ID_SESSION);
		Joueur joueur = this.joueurDAO.trouverParId(joueurIDString);
		
		// Obtention des skins
		ArrayList<Item> items;
		if ( joueur != null) {
			items = itemDao.getItemsById(joueur.getSkins());
		} else {
			items = new ArrayList<Item>();
		}
		
		request.setAttribute( ATT_JOUEUR, joueur );
		request.setAttribute( ITEMS_ATTR, items );
		
		this.getServletContext().getRequestDispatcher(PROFIL_JSP).forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
