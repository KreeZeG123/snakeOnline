package model.dao.interfaces;

import model.beans.Joueur;
import model.dao.exceptions.DAOException;

public interface JoueurDao {

	Joueur trouverUsername( String username ) throws DAOException;

    Joueur trouverEmail( String email ) throws DAOException;

	void creer(Joueur joueur) throws DAOException;

}