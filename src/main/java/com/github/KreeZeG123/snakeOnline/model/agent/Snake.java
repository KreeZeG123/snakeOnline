package com.github.KreeZeG123.snakeOnline.model.agent;

import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.*;

import java.util.ArrayList;


public class Snake {

    private ArrayList<Position> positions;
    private AgentAction lastAction;
    private ColorSnake colorSnake;
    private int invincibleTime;
    private int sickTime;
    private boolean dead;
    private SnakeGame game;
    private String playerName;

    public Snake(ArrayList<Position> positions, AgentAction lastAction, ColorSnake color, SnakeGame game) {
        this.positions = positions;
        this.lastAction = lastAction;
        this.colorSnake = color;
        this.dead = false;
        this.game = game;
    }

    public Snake(ArrayList<Position> positions, AgentAction lastAction, ColorSnake color, SnakeGame game, String playerName) {
        this.positions = positions;
        this.lastAction = lastAction;
        this.colorSnake = color;
        this.dead = false;
        this.game = game;
        this.playerName = playerName;
    }

    public void move(Position newPos) {
        this.positions.add(0,newPos);
        this.positions.remove(positions.size() - 1);
    }

    public void decreaseStatusTime() {
        if ( this.invincibleTime > 0) {
            this.invincibleTime--;
        }
        if ( this.sickTime > 0) {
            this.sickTime--;
        }
    }

    public void makeAction() {
        //
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }

    public ColorSnake getColorSnake() {
        return colorSnake;
    }

    public void setColorSnake(ColorSnake colorSnake) {
        this.colorSnake = colorSnake;
    }

    public int getInvincibleTime() {
        return invincibleTime;
    }

    public void setInvincibleTime(int invincibleTime) {
        this.invincibleTime = invincibleTime;
    }

    public int getSickTime() {
        return sickTime;
    }

    public void setSickTime(int sickTime) {
        this.sickTime = sickTime;
    }

    public AgentAction getLastAction() {
        return lastAction;
    }

    public void setLastAction(AgentAction lastAction) {
        this.lastAction = lastAction;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void growBody() {
        Position posNewBodyPart = Utils.movePosition(
                this.getPositions().get(0),
                Utils.invertAction(this.lastAction),
                this.game.getMap().getSizeX(),
                this.game.getMap().getSizeY()
        );
        this.positions.add(posNewBodyPart);
    }

    public void setEffect(ItemType itemType) {
        switch (itemType) {
            case APPLE -> {
                this.growBody();
            }
            case BOX -> {
                switch (this.game.random.nextInt(2)) {
                    case 0 -> this.setEffect(ItemType.SICK_BALL);
                    case 1 -> this.setEffect(ItemType.INVINCIBILITY_BALL);
                    default -> throw new IllegalStateException("Unexpected value: " + itemType);
                }
            }
            case SICK_BALL -> {
                this.setSickTime(20);
            }
            case INVINCIBILITY_BALL -> {
                this.setInvincibleTime(20);
            }
            default -> {
                throw new IllegalArgumentException("Illegal item type: " + itemType);
            }
        }
    }

    public int getSize() {
        return this.getPositions().size();
    }

    // Renvoie le nombre de points d'un snake (nombre de pommes mangées)
    public int getPoints() {
        return this.positions.size() - 1;
    }
}
