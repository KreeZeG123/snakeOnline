package model.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.beans.Joueur;
import model.dao.interfaces.JoueurDao;
import model.dao.exceptions.DAOException;
import model.dao.factory.DAOFactory;
import model.dao.utils.DAOUtilitaire;

public class JoueurDaoImpl implements JoueurDao {
	
	private DAOFactory daoFactory;
	
	private static final String SQL_SELECT_PAR_ID = "SELECT id, username, mot_de_passe, email, nb_pieces, skins, score, date_inscription FROM joueurs WHERE id = ?";
	private static final String SQL_SELECT_PAR_USERNAME = "SELECT id, username, mot_de_passe, email, nb_pieces, skins, score, date_inscription FROM joueurs WHERE username = ?";
	private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, username, mot_de_passe, email, nb_pieces, skins, score, date_inscription FROM joueurs WHERE email = ?";
	private static final String SQL_UPDATE_JOUEUR = "UPDATE joueurs SET username = ?, mot_de_passe = ?, email = ?, nb_pieces = ?, skins = ?, score = ? WHERE id = ?";
	
	private static final String SQL_INSERT = "INSERT INTO joueurs (username, email, mot_de_passe, nb_pieces, skins, score, date_inscription) VALUES (?, ?, ?, ?, ?, ?, NOW())";
	
    public JoueurDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }
    
    /* Implémentation de la méthode définie dans l'interface JoueurDao */
	@Override
	public Joueur trouverParId( Long joueurID ) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Joueur Joueur = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee( connexion, SQL_SELECT_PAR_ID, false, joueurID );
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if ( resultSet.next() ) {
				Joueur = map( resultSet );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			DAOUtilitaire.fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		}

		return Joueur;
	}
    
    /* Implémentation de la méthode définie dans l'interface JoueurDao */
	@Override
	public Joueur trouverParUsername( String username ) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Joueur Joueur = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee( connexion, SQL_SELECT_PAR_USERNAME, false, username );
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if ( resultSet.next() ) {
				Joueur = map( resultSet );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			DAOUtilitaire.fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		}

		return Joueur;
	}
	
	/* Implémentation de la méthode définie dans l'interface JoueurDao */
	@Override
	public Joueur trouverParEmail( String email ) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Joueur Joueur = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if ( resultSet.next() ) {
				Joueur = map( resultSet );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			DAOUtilitaire.fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		}

		return Joueur;
	}

    /* Implémentation de la méthode définie dans l'interface JoueurDao */
	@Override
	public void creerJoueur( Joueur joueur ) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet valeursAutoGenerees = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee( connexion, SQL_INSERT, true, joueur.getUsername(), joueur.getEmail(), joueur.getMotDePasse(), joueur.getNbPieces(), joueur.getSkins(), joueur.getScore() );
			int statut = preparedStatement.executeUpdate();
			/* Analyse du statut retourné par la requête d'insertion */
			if ( statut == 0 ) {
				throw new DAOException( "Échec de la création du Joueur, aucune ligne ajoutée dans la table." );
			}
			/* Récupération de l'id auto-généré par la requête d'insertion */
			valeursAutoGenerees = preparedStatement.getGeneratedKeys();
			if ( valeursAutoGenerees.next() ) {
				/* Puis initialisation de la propriété id du bean Joueur avec sa valeur */
				joueur.setId( valeursAutoGenerees.getLong( 1 ) );
			} else {
				throw new DAOException( "Échec de la création du Joueur en base, aucun ID auto-généré retourné." );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			DAOUtilitaire.fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
		}
	}
	
	@Override
	public void updateJoueur(Joueur joueur) throws DAOException {
	    Connection connexion = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        // Récupération d'une connexion depuis la Factory
	        connexion = daoFactory.getConnection();
	        
	        // Préparation de la requête
	        preparedStatement = DAOUtilitaire.initialisationRequetePreparee(
	            connexion, SQL_UPDATE_JOUEUR, false, 
	            joueur.getUsername(), 
	            joueur.getMotDePasse(), 
	            joueur.getEmail(), 
	            joueur.getNbPieces(), 
	            joueur.getSkins(), 
	            joueur.getScore(), 
	            joueur.getId()
	        );

	        // Exécution de la requête
	        int statut = preparedStatement.executeUpdate();
	        
	        // Vérification du statut de la requête
	        if (statut == 0) {
	            throw new DAOException("Échec de la mise à jour du joueur, aucune ligne modifiée.");
	        }

	    } catch (SQLException e) {
	        throw new DAOException("Erreur lors de la mise à jour du Joueur : " + e.getMessage(), e);
	    } finally {
	        // Fermeture des ressources
	        DAOUtilitaire.fermeturesSilencieuses(preparedStatement, connexion);
	    }
	}
	

	/*
	 * Simple méthode utilitaire permettant de faire la correspondance (le
	 * mapping) entre une ligne issue de la table des Joueurs (un
	 * ResultSet) et un bean Joueur.
	 */
	private static Joueur map( ResultSet resultSet ) throws SQLException {
		Joueur joueur = new Joueur();
		joueur.setId( resultSet.getLong( "id" ) );
		joueur.setUsername( resultSet.getString( "username" ) );
		joueur.setEmail( resultSet.getString( "email") );
		joueur.setMotDePasse( resultSet.getString( "mot_de_passe" ) );
		joueur.setNbPieces( resultSet.getInt( "nb_pieces" ) );
		joueur.setSkins( resultSet.getString( "skins" ) );
		joueur.setScore( resultSet.getInt( "score" ) );
		return joueur;
	}


}