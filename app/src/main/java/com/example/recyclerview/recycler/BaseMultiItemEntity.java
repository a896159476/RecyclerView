package com.example.recyclerview.recycler;

import androidx.annotation.LayoutRes;

public class BaseMultiItemEntity {

    private int itemType;

    public BaseMultiItemEntity(@LayoutRes int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(@LayoutRes int itemType) {
        this.itemType = itemType;
    }
}
