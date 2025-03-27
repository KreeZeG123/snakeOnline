package model.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.beans.Item;
import model.dao.exceptions.DAOException;
import model.dao.factory.DAOFactory;
import model.dao.interfaces.ItemDAO;
import model.dao.utils.DAOUtilitaire;

public class ItemDaoImpl implements ItemDAO{
	private DAOFactory daoFactory;
	
	private static final String SQL_SELECT_ITEMS = "SELECT id, title, description, price, image FROM items";
	private static final String SQL_SELECT_ITEMS_BY_ID = "SELECT id, title, description, price, image FROM items WHERE id INT (?)";

	
    public ItemDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }
    
    public ArrayList<Item> getItems() throws DAOException{
    	Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Item> items = new ArrayList<>();
    	try{
    		connexion = daoFactory.getConnection();
    		preparedStatement = connexion.prepareStatement(SQL_SELECT_ITEMS);
    		resultSet = preparedStatement.executeQuery();
    		/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
    		while ( resultSet.next() ) {
    			items.add(map(resultSet));
    		}
    	} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			DAOUtilitaire.fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		}
    	return items;
    }
    
    public ArrayList<Item> getItemsById(String ids) throws DAOException{
    	Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Item> items = new ArrayList<>();
	    if (ids != null && ids.endsWith(",")) {
	        ids = ids.substring(0, ids.length() - 1);
	    }
    	try{
    		connexion = daoFactory.getConnection();
    		preparedStatement = DAOUtilitaire.initialisationRequetePreparee(connexion,SQL_SELECT_ITEMS_BY_ID, false, ids );
    		resultSet = preparedStatement.executeQuery();
    		/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
    		while ( resultSet.next() ) {
    			items.add(map(resultSet));
    		}
    	} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			DAOUtilitaire.fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		}
    	return items;
    }
    
    private static Item map( ResultSet resultSet ) throws SQLException {
		Item item = new Item();
		item.setId( resultSet.getLong( "id" ) );
		item.setTitle( resultSet.getString( "title" ) );
		item.setDescription( resultSet.getString( "description") );
		item.setPrice( resultSet.getInt( "price" ) );
		item.setImage( resultSet.getString( "image" ) );
		return item;
	}

}
