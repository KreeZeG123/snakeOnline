package com.github.KreeZeG123.snakeOnline.utils;

public class Utils {

    public static AgentAction invertAction(AgentAction action) {
        switch (action) {
            case MOVE_UP :
                return AgentAction.MOVE_DOWN;
            case MOVE_DOWN :
                return AgentAction.MOVE_UP;
            case MOVE_LEFT :
                return AgentAction.MOVE_RIGHT;
            case MOVE_RIGHT :
                return AgentAction.MOVE_LEFT;
            default:
                throw new UnsupportedOperationException("L'action "+action+" n'est pas inversible");


        }
    }

    public static Position movePosition(Position position, AgentAction action, int maxX, int maxY) {
        int x = position.getX();
        int y = position.getY();

        switch (action) {
            case MOVE_UP :
                y--;
                break;
            case MOVE_DOWN :
                y++;
                break;
            case MOVE_LEFT :
                x--;
                break;
            case MOVE_RIGHT :
                x++;
                break;
            default:
                throw new UnsupportedOperationException("L'action "+action+" n'est pas prise en charge");
        }

        if ( x >= maxX ) {
            x = 0;
        }
        if ( y >= maxY ) {
            y = 0;
        }
        if ( x < 0 ) {
            x = maxX-1;
        }
        if ( y < 0 ) {
            y = maxY-1;
        }

        return new Position(x, y);
    }

}
