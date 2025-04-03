package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;


public class InfoUserDTO extends MainMenuDTO{
    private int userID;
    private String userName;
    private String cosmetiques;
    private int nbPieces;

    public InfoUserDTO(int userID,String  username, String cosmetiques, int nbPieces) {
        this.userID = userID;
        this.userName = username;
        this.cosmetiques = cosmetiques;
        this.nbPieces = nbPieces;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getCosmetiques() {
        return this.cosmetiques;
    }

    public int getNbPieces() {
        return this.nbPieces;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}