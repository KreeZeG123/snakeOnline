package model.dao.interfaces;

import java.util.ArrayList;

import model.beans.Item;
import model.dao.exceptions.DAOException;

public interface ItemDAO {
	
	ArrayList<Item> getItems() throws DAOException;
	
	ArrayList<Item> getItemsById(String ids) throws DAOException;
	
	Item getItemById(String id) throws DAOException;

}
