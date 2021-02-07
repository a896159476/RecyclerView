package com.example.recyclerview.bean;

import com.example.recyclerview.recycler.BaseMultiItemEntity;

public class MultiMainBean extends BaseMultiItemEntity {

    private int id;
    private String name;

    public MultiMainBean(int itemType, int id, String name) {
        super(itemType);
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
