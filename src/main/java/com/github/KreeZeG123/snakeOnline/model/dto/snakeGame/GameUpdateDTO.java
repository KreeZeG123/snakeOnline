package com.github.KreeZeG123.snakeOnline.model.dto.snakeGame;

import com.github.KreeZeG123.snakeOnline.utils.FeaturesItem;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesSnake;

import java.util.ArrayList;

public class GameUpdateDTO extends SnakeGameDTO {

    public ArrayList<FeaturesItem> items;
    public ArrayList<FeaturesSnake> snakes;
    public String snakeInfos;

    public GameUpdateDTO(ArrayList<FeaturesItem> items, ArrayList<FeaturesSnake> snakes, String snakeInfos) {
        this.items = items;
        this.snakes = snakes;
        this.snakeInfos = snakeInfos;
    }
}
