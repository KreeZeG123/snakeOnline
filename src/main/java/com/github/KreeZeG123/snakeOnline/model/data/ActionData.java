package com.github.KreeZeG123.snakeOnline.model.data;

import com.github.KreeZeG123.snakeOnline.utils.ColorSnake;

public class ActionData {
    public String action;
    public ColorSnake colorSnake;

    public ActionData(String action, ColorSnake colorSnake) {
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
