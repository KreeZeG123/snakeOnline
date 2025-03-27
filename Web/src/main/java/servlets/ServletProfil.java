package servlets;

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
    
    public void init() throws ServletException{
    	DAOFactory daoFactory = DAOFactory.getInstance();
    	this.joueurDAO = daoFactory.getJoueurDao();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Item> items = new ArrayList<>();
		
		Item itemCamouflage = new Item();
		itemCamouflage.setId(1);
		itemCamouflage.setTitle("Skin Serpent Camouflage");
		itemCamouflage.setDescription("Un magnifique skin camouflage pour votre serpent.");
		itemCamouflage.setPrice(100);
		itemCamouflage.setImage("/images/items/skin_camouflage.png");
		items.add(itemCamouflage);
		
		Item itemNeon = new Item();
		itemNeon.setId(2);
		itemNeon.setTitle("Skin Serpent Néon");
		itemNeon.setDescription("Un serpent lumineux aux couleurs vives avec un effet néon dynamique.");
		itemNeon.setPrice(150);
		itemNeon.setImage("/images/items/skin_neon.png");
		items.add(itemNeon);
		
		Item itemDragon = new Item();
		itemDragon.setId(3);
		itemDragon.setTitle("Skin Serpent Dragon Chinois");
		itemDragon.setDescription("Transformez votre serpent en un majestueux dragon chinois aux écailles dorées.");
		itemDragon.setPrice(200);
		itemDragon.setImage("/images/items/skin_dragon_chinois.png");
		items.add(itemDragon);
		
		items.add(itemNeon);
		items.add(itemCamouflage);
		
		// Obtention du joueur
		HttpSession session = request.getSession();
		Long joueurIDString = (Long) session.getAttribute(ATT_JOUEUR_ID_SESSION);
		Joueur joueur = this.joueurDAO.trouverParId(joueurIDString);
		
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
