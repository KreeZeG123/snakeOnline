package com.github.KreeZeG123.snakeOnline.model.factory;

import com.github.KreeZeG123.snakeOnline.model.agent.Snake;
import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesSnake;
import com.github.KreeZeG123.snakeOnline.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class SnakeFactory {

    public static Snake createSnake(FeaturesSnake featuresSnake, SnakeGame snakeGame) {

        // Copy des positions du Snake
        Position pos = featuresSnake.getPositions().get(0);
        Position posCopy = new Position(pos.getX(), pos.getY());


        // Création du Snake
        Snake newSnake = new Snake(
                new ArrayList<>(List.of(posCopy)),
                featuresSnake.getLastAction(),
                featuresSnake.getColorSnake(),
                snakeGame
        );

        return newSnake;
    }

}
