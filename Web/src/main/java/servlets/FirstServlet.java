package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.beans.Joueur;
import model.forms.FormulaireConnexion;


/**
 * Servlet implementation class FirstServlet
 */
@WebServlet("/main")
public class FirstServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String PAGE1_JSP = "/WEB-INF/pages/page1.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FirstServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Joueur joueur = new Joueur();
		joueur.setUsername("Nathan");
		joueur.setNbPieces(10);
		
		Joueur joueur1 = new Joueur();
		joueur1.setUsername("Yamis");
		joueur1.setNbPieces(20);
		
		Joueur joueur2 = new Joueur();
		joueur2.setUsername("Arina");
		joueur2.setNbPieces(30);
		
		Joueur[] joueurs = {joueur1 , joueur2 , joueur};
		request.setAttribute("joueurs", joueurs);
		this.getServletContext().getRequestDispatcher(PAGE1_JSP).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FormulaireConnexion form = new FormulaireConnexion();
		form.verif(request);
		request.setAttribute("form", form);
		
		HttpSession session = request.getSession();
		
		session.setAttribute("username", form.getUsername());


		this.getServletContext().getRequestDispatcher("/WEB-INF/page1.jsp").forward(request,response);
	}

}
