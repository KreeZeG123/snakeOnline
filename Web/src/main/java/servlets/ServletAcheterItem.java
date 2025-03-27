package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.factory.DAOFactory;
import model.dao.interfaces.ItemDAO;
import model.dao.interfaces.JoueurDao;
import model.forms.FormAcheterItem;

/**
 * Servlet implementation class ServletAcheterItem
 */
@WebServlet("/acheterItem")
public class ServletAcheterItem extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
   
       
	private static final String ATT_FORM = "form";
	
	private JoueurDao joueurDAO;
	private ItemDAO itemDao;
    
    public void init() throws ServletException{
    	DAOFactory daoFactory = DAOFactory.getInstance();
    	this.joueurDAO = daoFactory.getJoueurDao();
    	this.itemDao = daoFactory.getItemDAO();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Préparation de l'objet formulaire */
		FormAcheterItem form = new FormAcheterItem(joueurDAO, itemDao);
		
		/* Traitement de la requête et récupération du résultant */
		form.acheterItem(request);
		
		/* Stockage du formulaire dans la session pour l'envoyé a boutique */
		request.getSession().setAttribute( ATT_FORM, form );
		
		// Redirect vers la boutique
		response.sendRedirect(request.getContextPath() + "/boutique");
	}

}
