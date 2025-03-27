package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.beans.Item;
import model.dao.factory.DAOFactory;
import model.dao.implementations.ItemDaoImpl;
import model.dao.interfaces.ItemDAO;
import model.dao.interfaces.JoueurDao;

/**
 * Servlet implementation class ServletBoutique
 */
@WebServlet("/boutique")
public class ServletBoutique extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String BOUTIQUE_JSP = "/WEB-INF/pages/Boutique.jsp";
       
	private ItemDAO itemDAO;
    
    public void init() throws ServletException{
    	DAOFactory daoFactory = DAOFactory.getInstance();
    	this.itemDAO = daoFactory.getItemDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<Item> items = itemDAO.getItems();
		
		request.setAttribute("items", items);
		
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
