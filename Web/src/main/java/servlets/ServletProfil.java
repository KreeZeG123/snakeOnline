package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	private static final String PROFIL_JSP = "/WEB-INF/pages/Profil.jsp";
       
	private ItemDAO itemDao;
    
    public void init() throws ServletException{
    	DAOFactory daoFactory = DAOFactory.getInstance();
    	this.itemDao = daoFactory.getItemDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Joueur joueur = new Joueur();
		joueur.setNbPieces(55);
		joueur.setUsername("Nathan");
		joueur.setMotDePasse("nathanmdp");
		joueur.setScore(350);
		joueur.setSkins("1,2");
		
		request.setAttribute("joueur", joueur);
		
		ArrayList<Item> items = itemDao.getItemsById(joueur.getSkins());
		
		request.setAttribute(ITEMS_ATTR, items);
		
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
