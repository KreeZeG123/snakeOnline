package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;

//Login, cosmétiques, points...
public class InfoUserDTO extends MainMenuDTO{
    private String userName;
    private String[] cosmetiques;
    private int nbPieces;
    public InfoUserDTO(String username, String[] cosmetiques, int nbPieces) {
        this.userName = username;
        this.cosmetiques = cosmetiques;
        this.nbPieces = nbPieces;
    }

    public String getUserName() {
        return userName;
    }

    public String[] getCosmetiques() {
        return cosmetiques;
    }

    public int getNbPieces() {
        return nbPieces;
    }
}
