package com.github.KreeZeG123.snakeOnline.model.data;

import com.github.KreeZeG123.snakeOnline.utils.FeaturesItem;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesSnake;

import java.util.ArrayList;

public class RunningGameData {

    public ArrayList<FeaturesItem> items;
    public ArrayList<FeaturesSnake> snakes;

    public RunningGameData(ArrayList<FeaturesItem> items, ArrayList<FeaturesSnake> snakes) {
        this.items = items;
        this.snakes = snakes;
    }
}
