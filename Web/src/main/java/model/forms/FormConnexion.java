package model.forms;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import model.beans.Joueur;
import model.dao.exceptions.DAOException;
import model.dao.interfaces.JoueurDao;

public class FormConnexion extends FormBase {
	
	private static final String CHAMP_USERNAME    = "username";
    private static final String CHAMP_MOT_DE_PASSE   = "password";
    private static final String CHAMP_USERNAME_ET_MOT_DE_PASSE   = "usernameAndPassword";
    
    private static final String ALGO_CHIFFREMENT = "SHA-256";
    
	private JoueurDao joueurDao;
	
	public FormConnexion( JoueurDao joueurDao ) {
	    this.joueurDao = joueurDao;
	}

	public Joueur connecterJoueur( HttpServletRequest request ) {
		String username = getValeurChamp( request, CHAMP_USERNAME );
	    String motDePasse = getValeurChamp( request, CHAMP_MOT_DE_PASSE );
	    
	    return connecterJoueur(username, motDePasse);
	}
	
	public Joueur connecterJoueur( String username, String motDePasse ) {
	    Joueur joueur = new Joueur();
	    try {
	    	joueur = traiterUsername( username );
	    	if (!joueur.getMotDePasse().isBlank()) {
	    		String motDePasseChiffreStocke = joueur.getMotDePasse();
		    	traiterMotsDePasse(motDePasse, motDePasseChiffreStocke);
	    	}else {
	    		
	    	}
	        if ( erreurs.isEmpty() ) {
	            resultat = "Succès de la connexion.";
	        } else {
	        	setErreur("status", "echec");
	            resultat = "Échec de la connexion.";
	        }
	    } catch ( DAOException e ) {
	    	setErreur("status", "echec");
	        resultat = "Échec de la connexion : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
	        e.printStackTrace();
	    }
	    
	    return joueur;
	}
	
	private Joueur traiterUsername( String username ) {
	    try {
	        return validationUsername( username );
	    } catch ( FormValidationException e ) {
	    	String specifiedAttrString = ((FormValidationException)e).getSpecifiedAttr();
	    	if ( specifiedAttrString != null ) {
	    		setErreur(specifiedAttrString, e.getMessage());
	    	}
	    	else {
	    		setErreur( CHAMP_USERNAME, e.getMessage() );
	    	}
	    }
	    return new Joueur();
	}
	
	private void traiterMotsDePasse( String motDePasse, String motDePasseChiffreStocke ) {
	    try {
	        validationMotDePasse( motDePasse, motDePasseChiffreStocke );
	    } catch ( FormValidationException e ) {
	    	String specifiedAttrString = ((FormValidationException)e).getSpecifiedAttr();
	    	if ( specifiedAttrString != null ) {
	    		setErreur(specifiedAttrString, e.getMessage());
	    	}
	    	else {
	    		setErreur( CHAMP_MOT_DE_PASSE, e.getMessage() );
	    		if ( getErreur(CHAMP_USERNAME) == null ) {
	    			setErreur( CHAMP_USERNAME, "");
	    		}
	    	}
	    }
	    
	}
	
	private Joueur validationUsername(String username) throws FormValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new FormValidationException("Merci d'entrer un nom d'utilisateur.");
        }

        Joueur joueur = joueurDao.trouverParUsername(username);
        if (joueur == null) {
            throw new FormValidationException("Mauvais username ou mot de passe.", CHAMP_USERNAME_ET_MOT_DE_PASSE);
        }

        return joueur;
    }

    private void validationMotDePasse(String motDePasse, String motDePasseChiffreStocke) throws FormValidationException {
        if (motDePasse == null || motDePasse.isBlank()) {
            throw new FormValidationException("Merci d'entrer un mot de passe.");
        }

        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm(ALGO_CHIFFREMENT);
        passwordEncryptor.setPlainDigest(false);

        if (!passwordEncryptor.checkPassword(motDePasse, motDePasseChiffreStocke)) {
            throw new FormValidationException("Mauvais username ou mot de passe.", CHAMP_USERNAME_ET_MOT_DE_PASSE);
        }
    }
	
}