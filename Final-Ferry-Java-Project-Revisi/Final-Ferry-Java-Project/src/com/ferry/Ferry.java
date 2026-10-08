package com.ferry;

/**
 * Class Ferry
 * Menyimpan informasi kapal yang digunakan pada sistem.
 */
public class Ferry {
    private String shipName;
    private String classType;
    private int deck;

    public Ferry(String shipName, String classType, int deck) {
        this.shipName = shipName;
        this.classType = classType;
        this.deck = deck;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void setDeck(int deck) {
        this.deck = deck;
    }

    public String getShipName() {
        return shipName;
    }

    public String getClassType() {
        return classType;
    }

    public int getDeck() {
        return deck;
    }
}
