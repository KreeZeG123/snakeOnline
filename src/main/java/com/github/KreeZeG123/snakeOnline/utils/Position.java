package com.github.KreeZeG123.snakeOnline.utils;

import java.util.Objects;

public class Position {

	private int x;
	private int y;


	public Position(int x, int y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Pos(x=" + x + ", y=" + y + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Position position)) return false;
        return x == position.x && y == position.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public static int distManhattan(Position position1, Position position2) {
		return Math.abs(position2.getX() - position1.getX()) + Math.abs(position2.getY() - position1.getY());
	}

	public static int distManhattanWrap(Position position1, Position position2, int width, int height) {
		// Calcul des distances directes
		int dx = Math.abs(position2.getX() - position1.getX());
		int dy = Math.abs(position2.getY() - position1.getY());

		// Prendre en compte la distance via les bords opposés
		dx = Math.min(dx, width - dx);
		dy = Math.min(dy, height - dy);

		// Retourner la distance de Manhattan adaptée
		return dx + dy;
	}

}
