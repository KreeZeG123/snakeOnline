package model.forms;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import model.beans.Joueur;
import model.dao.exceptions.DAOException;
import model.dao.interfaces.JoueurDao;

public class FormInscription extends FormBase {
	
	private static final String CHAMP_USERNAME    = "username";
	private static final String CHAMP_EMAIL    = "email";
    private static final String CHAMP_MOT_DE_PASSE   = "password";
    private static final String CHAMP_CONFIRM_MOT_DE_PASSE   = "confirm-password";
    
    private static final String ALGO_CHIFFREMENT = "SHA-256";
    
	private JoueurDao joueurDao;

	public FormInscription( JoueurDao joueurDao ) {
	    this.joueurDao = joueurDao;
	}
	
	public Joueur inscrireUtilisateur( HttpServletRequest request ) {
		String username = getValeurChamp( request, CHAMP_USERNAME );
		String email = getValeurChamp( request, CHAMP_EMAIL );
	    String motDePasse = getValeurChamp( request, CHAMP_MOT_DE_PASSE );
	    String motDePasseConfirmation = getValeurChamp( request, CHAMP_CONFIRM_MOT_DE_PASSE );

	    Joueur joueur = new Joueur();
	    try {
	    	traiterUsername( username, joueur );
	        traiterEmail( email, joueur );
	        traiterMotsDePasse( motDePasse, motDePasseConfirmation, joueur );

	        if ( erreurs.isEmpty() ) {
	        	joueurDao.creer( joueur );
	            resultat = "Succès de l'inscription.";
	        } else {
	        	setErreur("status", "echec");
	            resultat = "Échec de l'inscription.";
	        }
	    } catch ( DAOException e ) {
	    	setErreur("status", "echec");
	        resultat = "Échec de l'inscription : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
	        e.printStackTrace();
	    }

	    return joueur;
	}
	
	private void traiterUsername( String username, Joueur joueur ) {
		try {
	        validationUsername( username );
	    } catch ( FormValidationException e ) {
	        setErreur( CHAMP_USERNAME, e.getMessage() );
	    }
	    joueur.setUsername(username);
	}
	
	
	/*
	 * Appel à la validation de l'adresse email reçue et initialisation de la
	 * propriété email du bean
	 */
	private void traiterEmail( String email, Joueur joueur ) {
	    try {
	        validationEmail( email );
	    } catch ( FormValidationException e ) {
	        setErreur( CHAMP_EMAIL, e.getMessage() );
	    }
	    joueur.setEmail( email );
	}

	/*
	 * Appel à la validation des mots de passe reçus, chiffrement du mot de
	 * passe et initialisation de la propriété motDePasse du bean
	 */
	private void traiterMotsDePasse( String motDePasse, String confirmation, Joueur joueur ) {
	    try {
	        validationMotsDePasse( motDePasse, confirmation );
	    } catch ( FormValidationException e ) {
	        setErreur( CHAMP_MOT_DE_PASSE, e.getMessage() );
	        setErreur( CHAMP_CONFIRM_MOT_DE_PASSE, null );
	    }

	    /*
	     * Utilisation de la bibliothèque Jasypt pour chiffrer le mot de passe
	     * efficacement.
	     * 
	     * L'algorithme SHA-256 est ici utilisé, avec par défaut un salage
	     * aléatoire et un grand nombre d'itérations de la fonction de hashage.
	     * 
	     * La String retournée est de longueur 56 et contient le hash en Base64.
	     */
	    ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
	    passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
	    passwordEncryptor.setPlainDigest( false );
	    String motDePasseChiffre = passwordEncryptor.encryptPassword( motDePasse );

	    joueur.setMotDePasse( motDePasseChiffre );
	}
	
	private void validationUsername( String username ) throws FormValidationException {
        if ( username != null ) {
            if ( username.length() < 3 ) {
                throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 3 caractères." );
            } else if ( joueurDao.trouverUsername( username ) != null ) {
	            throw new FormValidationException( "Cet nom d'utilisateur est déjà utilisé, merci d'en choisir un autre." );
	        }
        } else {
            throw new FormValidationException( "Merci d'entrer un nom d'utilisateur." );
        }
    }

	/* Validation de l'adresse email */
	private void validationEmail( String email ) throws FormValidationException {
	    if ( email != null ) {
	        if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
	            throw new FormValidationException( "Merci de saisir une adresse mail valide." );
	        } else if ( joueurDao.trouverEmail( email ) != null ) {
	            throw new FormValidationException( "Cette adresse email est déjà utilisée, merci d'en choisir une autre." );
	        }
	    } else {
	        throw new FormValidationException( "Merci de saisir une adresse mail." );
	    }
	}
	
	/**
	 * Valide les mots de passe saisis.
	 */
	private void validationMotsDePasse( String motDePasse, String confirmation ) throws FormValidationException{
	    if (motDePasse != null && motDePasse.trim().length() != 0 && confirmation != null && confirmation.trim().length() != 0) {
	        if (!motDePasse.equals(confirmation)) {
	            throw new FormValidationException("Les mots de passe entrés sont différents, merci de les saisir à nouveau.");
	        } else if (motDePasse.trim().length() < 3) {
	            throw new FormValidationException("Les mots de passe doivent contenir au moins 3 caractères.");
	        }
	    } else {
	        throw new FormValidationException("Merci de saisir et confirmer votre mot de passe.");
	    }
	}

}
