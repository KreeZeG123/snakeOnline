package model.dao;

import model.beans.Joueur;

public interface JoueurDao {

    void creer( Joueur joueur ) throws DAOException;

    Joueur trouver( String email ) throws DAOException;

}