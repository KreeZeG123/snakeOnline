package model.dto.mainMenu;

import model.dto.DTOInterface;

public abstract class MainMenuDTO implements DTOInterface {
    public String getDataType() {
        return "model.dto.mainMenu." + this.getClass().getSimpleName();
    }
}
