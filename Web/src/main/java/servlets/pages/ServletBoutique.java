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
import model.forms.FormAcheterItem;

/**
 * Servlet implementation class ServletBoutique
 */
@WebServlet("/boutique")
public class ServletBoutique extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ATTR_ITEMS = "items";
	private static final String ATTR_UNLOCKED_ITEMS = "unlockedItems";
	private static final String ATTR_FORM = "form";
	
	private static final String ATTR_JOUEUR_ID_SESSION = "joueurID";
	
	private static final String BOUTIQUE_JSP = "/WEB-INF/pages/Boutique.jsp";
       
	private JoueurDao joueurDAO;
	private ItemDAO itemDAO;
    
    public void init() throws ServletException{
    	// Récupère la DAOFactory depuis le servletContext
    	DAOFactory daoFactory = DAOFactory.getInstanceFromContext( this.getServletContext() );
    	this.joueurDAO = daoFactory.getJoueurDao();
    	this.itemDAO = daoFactory.getItemDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		ArrayList<Item> items = itemDAO.getItems();
	
		ArrayList<Long> unlockedItems = new ArrayList<Long>();
		Long joueurID = (Long) session.getAttribute(ATTR_JOUEUR_ID_SESSION);
		if ( joueurID != null ) {
			Joueur joueur = this.joueurDAO.trouverParId(joueurID);
			if ( joueur != null ) {
				String skins = joueur.getSkins();
				if ( !skins.isBlank() ) {
					for (String itemIDStr : skins.split(",")) {
						unlockedItems.add(Long.valueOf(itemIDStr));
					}
				}
			}
		}
		
		
		/* Récupérer le formulaire d'achat d'objet stocké dans la session (message tentative d'achat précedente) */
	    FormAcheterItem form = (FormAcheterItem) session.getAttribute("form");
	    session.removeAttribute("form");
		
		request.setAttribute(ATTR_ITEMS, items);
		request.setAttribute(ATTR_UNLOCKED_ITEMS, unlockedItems);
	    request.setAttribute(ATTR_FORM, form);
		
		this.getServletContext().getRequestDispatcher(BOUTIQUE_JSP).forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
