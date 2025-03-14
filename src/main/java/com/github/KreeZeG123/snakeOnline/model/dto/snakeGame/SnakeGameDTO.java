package com.github.KreeZeG123.snakeOnline.model.dto.snakeGame;

import com.github.KreeZeG123.snakeOnline.model.dto.DTOInterface;

public abstract class SnakeGameDTO implements DTOInterface {

    public String getDataType() {
        return "com.github.KreeZeG123.snakeOnline.model.dto.snakeGame." + this.getClass().getSimpleName();
    }

}
