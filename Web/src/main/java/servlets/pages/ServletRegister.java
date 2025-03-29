package servlets.pages;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.beans.Joueur;
import model.dao.interfaces.JoueurDao;
import model.forms.FormInscription;
import model.dao.factory.DAOFactory;

/**
 * Servlet implementation class ServletRegister
 */
@WebServlet("/inscription")
public class ServletRegister extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String REGISTER_JSP = "/WEB-INF/pages/Register.jsp";
	private static final String CONNEXION_JSP = "/WEB-INF/pages/Connexion.jsp";
	
	private static final String ATT_JOUEUR = "joueur";
	private static final String ATT_FORM = "form";
	private static final String ATT_JOUEUR_ID_SESSION = "joueurID";
	private static final String ATT_JOUEUR_USERNAME_SESSION = "joueurUsername";
	
    private JoueurDao joueurDAO;
    
    public void init() throws ServletException{
    	DAOFactory daoFactory = DAOFactory.getInstance();
    	this.joueurDAO = daoFactory.getJoueurDao();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.getServletContext().getRequestDispatcher(REGISTER_JSP).forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        /* Préparation de l'objet formulaire */
        FormInscription form = new FormInscription( joueurDAO );

        /* Traitement de la requête et récupération du bean en résultant */
        Joueur joueur = form.inscrireUtilisateur( request );

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_JOUEUR, joueur);
        
        if (form.getErreurs().isEmpty()) {
        	/* Stockage de l'idée du joueur en séssion */
            HttpSession session = request.getSession();
            session.setAttribute(ATT_JOUEUR_ID_SESSION, joueur.getId());
            session.setAttribute(ATT_JOUEUR_USERNAME_SESSION, joueur.getUsername());
            
            this.getServletContext().getRequestDispatcher(CONNEXION_JSP).forward(request, response);
        }
        
        
       doGet(request, response);
	}

}


