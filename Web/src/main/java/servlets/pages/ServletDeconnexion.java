package servlets.pages;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ServletDeconnexion
 */
@WebServlet("/deconnexion")
public class ServletDeconnexion extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ACCUEIL_JSP = "/WEB-INF/pages/Accueil.jsp";
	
	private static final String ATT_JOUEUR_ID_SESSION = "joueurID";
	private static final String ATT_JOUEUR_USERNAME_SESSION = "joueurUsername";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDeconnexion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* Enlève l'id du joueur en séssion */
        HttpSession session = request.getSession();
        session.removeAttribute(ATT_JOUEUR_ID_SESSION);
        session.removeAttribute(ATT_JOUEUR_USERNAME_SESSION);
		
		this.getServletContext().getRequestDispatcher(ACCUEIL_JSP).forward(request, response);
	}


}
