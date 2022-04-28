package com.example.myidlemine;

import java.io.Serializable;

public class Data implements Serializable {
    private long id;
    private String level;
    private String money;
    private String progress;
    private String multiplier;
    private String factories;

    public Data(long id, String level, String money, String progress, String multiplier, String factories) {
        this.id = id;
        this.level = level;
        this.money = money;
        this.progress = progress;
        this.multiplier = multiplier;
        this.factories=factories;
    }

    public long getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public String getMoney() {
        return money;
    }

    public String getProgress() {
        return progress;
    }

    public String getMultiplier() {
        return multiplier;
    }

    public String getFactories() {
        return factories;
    }
}