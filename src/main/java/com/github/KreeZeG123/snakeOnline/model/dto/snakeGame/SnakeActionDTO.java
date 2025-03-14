package com.github.KreeZeG123.snakeOnline.model.dto.snakeGame;

import com.github.KreeZeG123.snakeOnline.model.dto.DTOInterface;
import com.github.KreeZeG123.snakeOnline.utils.ColorSnake;

public class SnakeActionDTO extends SnakeGameDTO {
    public String action;
    public ColorSnake colorSnake;

    public SnakeActionDTO(String action, ColorSnake colorSnake) {
        this.action = action;
        this.colorSnake = colorSnake;
    }

    @Override
    public String toString() {
        return "ActionData{" +
                "action='" + action + '\'' +
                ", colorSnake=" + colorSnake +
                '}';
    }
}
