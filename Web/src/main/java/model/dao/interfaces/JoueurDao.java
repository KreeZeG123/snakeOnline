package model.dao.interfaces;

import java.util.List;

import model.beans.Joueur;
import model.dao.exceptions.DAOException;

public interface JoueurDao {

	Joueur trouverParId(Long joueurID) throws DAOException;
	
	Joueur trouverParUsername( String username ) throws DAOException;
	
	List<Joueur> trouverParUsernames( List<String> usernames ) throws DAOException;

    Joueur trouverParEmail( String email ) throws DAOException;
    
    List<Joueur> classementJoueurs( int limit ) throws DAOException;

	void creerJoueur(Joueur joueur) throws DAOException;
	
	void updateJoueur( Joueur joueur ) throws DAOException;

}