package model.forms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.beans.Item;
import model.beans.Joueur;
import model.dao.exceptions.DAOException;
import model.dao.interfaces.ItemDAO;
import model.dao.interfaces.JoueurDao;

public class FormAcheterItem extends FormBase {
	
	private static final String CHAMP_ITEM = "item";
	private static final String CHAMP_JOUEUR = "joueur";
    
	private static final String ATT_JOUEUR_ID_SESSION = "joueurID";
	private static final String PARAM_ITEM_ID = "itemId";
    
	private JoueurDao joueurDao;
	private ItemDAO itemDAO;
	
	public FormAcheterItem( JoueurDao joueurDao, ItemDAO itemDAO ) {
	    this.joueurDao = joueurDao;
	    this.itemDAO = itemDAO;
	}

	public void acheterItem( HttpServletRequest request ) {
		HttpSession session = request.getSession();
		
		String itemID = (String) request.getParameter(PARAM_ITEM_ID);
		Long joueurID = (Long) session.getAttribute(ATT_JOUEUR_ID_SESSION);

		Item item;
		Joueur joueur;
	    try {
	    	item = traiterItem( itemID );
	        joueur = traiterJoueur( joueurID );
	        
	        int nbPiecesRestantes = joueur.getNbPieces() - item.getPrice();
	        if (nbPiecesRestantes >= 0) {
	        	joueur.setNbPieces(nbPiecesRestantes);
	        	joueur.addSkins(item);
	        	this.joueurDao.updateJoueur(joueur);
	        	resultat = "Achat réussi ! Vous avez acheté \'" + item.getTitle() + "\' pour " + item.getPrice() + " pièces. Il vous reste " + joueur.getNbPieces() + " pièces.";
	        }else {
	        	resultat = "Vous n'avez pas assez de pieces (solde : "+joueur.getNbPieces()+" ; prix : "+item.getPrice()+")";
	        }

	        if ( !erreurs.isEmpty() ) {
	        	setErreur("status", "echec");
	            resultat = "Échec de l'achat.";
	        }
	    } catch ( DAOException e ) {
	    	setErreur("status", "echec");
	        resultat = "Échec de l'inscription : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
	        e.printStackTrace();
	    }
	}
	
	public Item traiterItem(String itemID) {
		try {
	        return validationItem( itemID );
	    } catch ( FormValidationException e ) {
	    	setErreur( CHAMP_ITEM, e.getMessage() );
	    }
	    return new Item();
	}

	private Item validationItem(String itemID) throws FormValidationException {
        if (itemID == null || itemID.trim().isEmpty()) {
            throw new FormValidationException("Aucun id pour l'item à acheter.");
        }

        Item item = this.itemDAO.getItemById(itemID);
        if (item == null) {
            throw new FormValidationException("Aucun item correspondant.");
        }

        return item;
    }
	
	public Joueur traiterJoueur(Long joueurID) {
		try {
	        return validationJoueur( joueurID );
	    } catch ( FormValidationException e ) {
	    	setErreur( CHAMP_JOUEUR, e.getMessage() );
	    }
	    return new Joueur();
	}

	private Joueur validationJoueur(Long joueurID) throws FormValidationException {
        if (joueurID == null || joueurID < 0) {
            throw new FormValidationException("Aucun id pour le joueur qui achète.");
        }

        Joueur joueur = this.joueurDao.trouverParId(joueurID);
        if (joueur == null) {
            throw new FormValidationException("Aucun joueur correspondant.");
        }

        return joueur;
    }
}
