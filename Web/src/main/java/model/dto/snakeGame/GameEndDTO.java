package model.dto.snakeGame;

import java.util.List;

public class GameEndDTO extends SnakeGameDTO {

    private final List<String> players;
    private final List<Integer> scores;

    public GameEndDTO(List<String> players, List<Integer> scores) {
        this.players = players;
        this.scores = scores;
    }

    public List<String> getPlayers() {
        return players;
    }

    public List<Integer> getScores() {
        return scores;
    }
}
