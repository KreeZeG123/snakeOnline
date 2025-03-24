package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.beans.Classement;

/**
 * Servlet implementation class ServletClassement
 */
@WebServlet("/classement")
public class ServletClassement extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CLASSEMENTS_ATTR = "classements";
	
	private static final String CLASSEMENT_JSP = "/WEB-INF/pages/Classement.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletClassement() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<Classement> classements = new ArrayList<>();
		
		Classement classement1 = new Classement();
		classement1.setJoueur("Joueur 1");
		classement1.setScore(500);
		classements.add(classement1);

		Classement classement2 = new Classement();
		classement2.setJoueur("Joueur 2");
		classement2.setScore(400);
		classements.add(classement2);
		
		Classement classement3 = new Classement();
		classement3.setJoueur("Joueur 3");
		classement3.setScore(300);
		classements.add(classement3);
        
        // Ajouter la liste des classements à la requête
        request.setAttribute(CLASSEMENTS_ATTR, classements);
		
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
