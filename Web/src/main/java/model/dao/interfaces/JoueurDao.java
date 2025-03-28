package model.dao.interfaces;

import model.beans.Joueur;
import model.dao.exceptions.DAOException;

public interface JoueurDao {

	Joueur trouverParId(Long joueurID) throws DAOException;
	
	Joueur trouverParUsername( String username ) throws DAOException;

    Joueur trouverParEmail( String email ) throws DAOException;

	void creerJoueur(Joueur joueur) throws DAOException;
	
	void updateJoueur( Joueur joueur ) throws DAOException;

}