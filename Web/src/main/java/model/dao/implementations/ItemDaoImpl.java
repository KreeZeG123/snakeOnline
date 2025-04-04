package model.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.beans.Item;
import model.dao.exceptions.DAOException;
import model.dao.factory.DAOFactoryImpl;
import model.dao.interfaces.ItemDAO;
import model.dao.utils.DAOUtilitaire;

public class ItemDaoImpl implements ItemDAO{
	private DAOFactoryImpl daoFactory;
	
	private static final String SQL_SELECT_ITEMS = "SELECT id, title, description, price, image FROM items";
	private static final String SQL_SELECT_ITEMS_BY_ID = "SELECT id, title, description, price, image FROM items WHERE id IN (?)";
	private static final String SQL_SELECT_ITEM_BY_ID = "SELECT id, title, description, price, image FROM items WHERE id = ?";

	
    public ItemDaoImpl( DAOFactoryImpl daoFactory ) {
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
    		/* Parcours des lignes de données de l'éventuel ResulSet retourné */
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
		if (ids.replace(",", "").isEmpty()) {
			return items;
		}
	    if (ids != null && ids.endsWith(",")) {
	        ids = ids.substring(0, ids.length() - 1);
	    }
    	try{
    		connexion = daoFactory.getConnection();
    		
    		List<String> idsList = Arrays.asList(ids.split(","));
    		StringBuilder inString = new StringBuilder();
    		inString.append("?,".repeat(idsList.size()));
    		inString.setLength(inString.length() - 1);
    		
    		String query = SQL_SELECT_ITEMS_BY_ID.replace("?", inString);
    		
    		preparedStatement = connexion.prepareStatement(query);
            for (int i = 0; i < idsList.size(); i++) {
                preparedStatement.setInt(i + 1, Integer.parseInt(idsList.get(i).trim()));
            }
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
    
    public Item getItemById(String id) throws DAOException{
    	Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Item item = null;

		try {
			/* Récupération d'une connexion depuis la Factory */
			connexion = daoFactory.getConnection();
			preparedStatement = DAOUtilitaire.initialisationRequetePreparee( connexion, SQL_SELECT_ITEM_BY_ID, false, id );
			resultSet = preparedStatement.executeQuery();
			/* Parcours de la ligne de données de l'éventuel ResulSet retourné */
			if ( resultSet.next() ) {
				item = map( resultSet );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			DAOUtilitaire.fermeturesSilencieuses( resultSet, preparedStatement, connexion );
		}

		return item;
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
