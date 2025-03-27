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
import model.dao.JoueurDao;

/**
 * Servlet implementation class ServletProfil
 */
@WebServlet("/profil")
public class ServletProfil extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ITEMS_ATTR = "items";
	
	private static final String PROFIL_JSP = "/WEB-INF/pages/Profil.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletProfil() {
        super();
        // TODO Auto-generated constructor stub
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
		joueur.setSkins("1 2");
		
		request.setAttribute("joueur", joueur);
		
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
