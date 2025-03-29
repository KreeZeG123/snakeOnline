package model.dto.snakeGame;

import model.dto.DTOInterface;

public abstract class SnakeGameDTO implements DTOInterface {

    public String getDataType() {
        return "model.dto.snakeGame." + this.getClass().getSimpleName();
    }

}
