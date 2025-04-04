package com.github.KreeZeG123.snakeOnline.utils;

import java.util.ArrayList;


public class FeaturesSnake {



	ArrayList<Position> positions;
	
	private AgentAction lastAction;
	
	ColorSnake colorSnake;
	String skinID;
	
	boolean isInvincible;
	boolean isSick;

	int id;
	
	public FeaturesSnake(ArrayList<Position> positions, AgentAction lastAction, ColorSnake colorSnake, boolean isInvincible, boolean isSick, int id) {
		
		this.positions = positions;
		this.colorSnake = colorSnake;
		this.lastAction = lastAction;
		
		this.isInvincible = isInvincible;
		
		this.isSick = isSick;
		this.id = id;
		
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


	public boolean isInvincible() {
		return isInvincible;
	}


	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}


	public boolean isSick() {
		return isSick;
	}


	public void setSick(boolean isSick) {
		this.isSick = isSick;
	}


	public AgentAction getLastAction() {
		return lastAction;
	}


	public void setLastAction(AgentAction lastAction) {
		this.lastAction = lastAction;
	}

	public int getId() { return id; }


}
