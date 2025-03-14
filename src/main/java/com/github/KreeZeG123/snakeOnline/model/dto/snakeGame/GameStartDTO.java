package com.github.KreeZeG123.snakeOnline.model.dto.snakeGame;

import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.utils.ColorSnake;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesItem;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesSnake;
import com.github.KreeZeG123.snakeOnline.utils.Position;

import java.util.ArrayList;

public class GameStartDTO extends SnakeGameDTO {

    public final int width;
    public final int height;
    public final ArrayList<Position> walls;
    public final ArrayList<FeaturesItem> startItems;
    public final ArrayList<FeaturesSnake> startSnakes;
    public final ColorSnake clientColor;

    public GameStartDTO(int width, int height, ArrayList<Position> walls, ArrayList<FeaturesItem> startItems, ArrayList<FeaturesSnake> startSnakes, ColorSnake clientColor) {
        this.width = width;
        this.height = height;
        this.walls = walls;
        this.startItems = startItems;
        this.startSnakes = startSnakes;
        this.clientColor = clientColor;
    }

    public GameStartDTO(InputMap map, ColorSnake clientColor) {
        this.width = map.getSizeX();
        this.height = map.getSizeY();
        this.walls = new ArrayList<>();
        for (int i = 0; i < map.getSizeX(); i++) {
            for (int j = 0; j < map.getSizeY(); j++) {
                if (map.get_walls()[i][j]) {
                    this.walls.add(new Position(i, j));
                }
            }
        }
        this.startItems = map.getStart_items();
        this.startSnakes = map.getStart_snakes();
        this.clientColor = clientColor;
    }
}
