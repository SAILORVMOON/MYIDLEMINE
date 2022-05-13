package com.example.myidlemine;

import java.io.Serializable;

public class Datame implements Serializable {
    private long id;
    private String level;
    private String money;
    private String progress;
    private String multiplier;
    private String name;
    private String password;

    public Datame(long id, String level, String money, String progress, String multiplier, String name, String password) {
        this.id = id;
        this.level = level;
        this.money = money;
        this.progress = progress;
        this.multiplier = multiplier;
        this.name = name;
        this.password = password;
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

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}