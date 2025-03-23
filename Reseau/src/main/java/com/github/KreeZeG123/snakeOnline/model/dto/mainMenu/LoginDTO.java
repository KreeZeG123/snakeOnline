package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;

//Informations rentrées dans le formulaire de connexion au début
public class LoginDTO extends MainMenuDTO{
    public String login;
    public String password;
    public LoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }
}
