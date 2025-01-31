package com.github.KreeZeG123.snakeOnline.model.factory;

import com.github.KreeZeG123.snakeOnline.model.Item;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesItem;

public class ItemFactory {

    public static Item createItem(FeaturesItem featuresItem) {

        return new Item(
                featuresItem.getX(),
                featuresItem.getY(),
                featuresItem.getItemType()
        );
    }

}
