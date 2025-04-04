package com.github.KreeZeG123.snakeOnline.model.dto.snakeGame;

import com.github.KreeZeG123.snakeOnline.model.dto.DTOInterface;
import com.github.KreeZeG123.snakeOnline.utils.ColorSnake;

public class SnakeActionDTO extends SnakeGameDTO {
    public String action;
    public int id;

    public SnakeActionDTO(String action, int id) {
        this.action = action;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ActionData{" +
                "action='" + this.action + '\'' +
                ", id=" + this.id +
                '}';
    }
}
