package com.example.magicmarketplace.obj;

import androidx.room.Ignore;

public class Card {//This is a POJO that is the object that holds the cards information themselves
    private String cardName;
    private String cardSet;
    private String cardRarity;
    private boolean isFoil;

    @Ignore
    public Card(){
        cardName = "";
        cardSet = "";
        cardRarity = "";
        isFoil = false;
    }
    public Card(String cardName, String cardSet, String cardRarity, boolean isFoil) {
        this.cardName = cardName;
        this.cardSet = cardSet;
        this.cardRarity = cardRarity;
        this.isFoil = isFoil;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardSet() {
        return cardSet;
    }

    public void setCardSet(String cardSet) {
        this.cardSet = cardSet;
    }

    public String getCardRarity() {
        return cardRarity;
    }

    public void setCardRarity(String cardRarity) {
        this.cardRarity = cardRarity;
    }

    public boolean isFoil() {
        return isFoil;
    }

    public void setFoil(boolean foil) {
        isFoil = foil;
    }
}
