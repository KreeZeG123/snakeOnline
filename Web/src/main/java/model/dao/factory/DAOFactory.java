package model.dao.factory;

import javax.servlet.ServletContext;

import model.dao.interfaces.ItemDAO;
import model.dao.interfaces.JoueurDao;

public interface DAOFactory {
	
	public final String ATT_DAO_FACTORY = "daofactory";
	
	public JoueurDao getJoueurDao();
    
    public ItemDAO getItemDAO();
	
    public static DAOFactory getInstanceFromContext( ServletContext servletContext ) {
    	return (DAOFactory) servletContext.getAttribute(ATT_DAO_FACTORY);
	}
}
