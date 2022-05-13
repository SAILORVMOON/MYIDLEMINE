package com.example.myidlemine;

import java.io.Serializable;

public class Booster implements Serializable {
    int idPict;
    String price, effect;

    public Booster(int idPict, String price, String effect) {
        this.idPict = idPict;
        this.price = price;
        this.effect = effect;
    }

    public int getIdPict() {
        return idPict;
    }

    public String getPrice() {
        return price;
    }

    public String getEffect() {
        return effect;
    }
}
