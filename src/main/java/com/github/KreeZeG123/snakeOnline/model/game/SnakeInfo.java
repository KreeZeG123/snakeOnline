package com.github.KreeZeG123.snakeOnline.model.game;

public class SnakeInfo {
    String color;
    int score;
    boolean active;

    SnakeInfo(String color, int score) {
        this.color = color;
        this.score = score;
        this.active = true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}