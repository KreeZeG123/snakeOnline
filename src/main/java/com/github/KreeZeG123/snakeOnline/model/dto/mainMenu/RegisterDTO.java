package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;

//Login et password

public class RegisterDTO extends MainMenuDTO{
    private String login;
    private String password;
    public RegisterDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
