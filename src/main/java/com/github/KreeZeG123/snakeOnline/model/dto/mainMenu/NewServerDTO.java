package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;

//InputMap et mainServer

public class NewServerDTO extends MainMenuDTO{
    String map;
    public NewServerDTO(String map) {
        this.map = map;
    }

    public String getMap() {
        return map;
    }
}
