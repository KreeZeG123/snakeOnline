package com.github.KreeZeG123.snakeOnline.utils;

public class PlayerInput {
    private String lastKey;      // Dernière touche pressée
    private boolean used;        //  Indique si l'input a déja été pris en compte

    public PlayerInput(String lastKey, boolean used) {
        this.lastKey = lastKey;
        this.used = used;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }

    public boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @Override
    public String toString() {
        return "Key: " + lastKey + ", Time: " + used;
    }
}