package com.github.KreeZeG123.snakeOnline.model;

import com.github.KreeZeG123.snakeOnline.utils.ItemType;
import com.github.KreeZeG123.snakeOnline.utils.Position;

public class Item {

    private int x;

    private int y;

    private ItemType itemType;

    private boolean used;

    public static double PROBA_BONUS = 0.2;

    public Item(int x, int y, ItemType itemType) {
        this.x = x;
        this.y = y;
        this.itemType = itemType;
        this.used = false;
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

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Position getPosition() {
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return "Item{" +
                "x=" + x +
                ", y=" + y +
                ", itemType=" + itemType +
                ", used=" + used +
                '}';
    }
}
