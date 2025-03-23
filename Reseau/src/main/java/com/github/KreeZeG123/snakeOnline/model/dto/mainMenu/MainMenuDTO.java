package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;

import com.github.KreeZeG123.snakeOnline.model.dto.DTOInterface;

public abstract class MainMenuDTO implements DTOInterface {
    public String getDataType() {
        return "com.github.KreeZeG123.snakeOnline.model.dto.mainMenu." + this.getClass().getSimpleName();
    }
}
