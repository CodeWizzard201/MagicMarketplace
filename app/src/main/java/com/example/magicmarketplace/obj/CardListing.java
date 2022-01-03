package com.example.magicmarketplace.obj;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.obj.Card;

import java.util.Date;

@Entity(tableName = AppDatabase.MTG_TABLE)
public class CardListing {//This is the object that holds the information on cards on the marketplace.

    @PrimaryKey(autoGenerate = true)
    private int mListingId;

    @Embedded
    public Card mCardInfo;


    private double mCardPrice;
    private int mCardInStock;

    public CardListing(double cardPrice, int cardInStock) {

        mCardInfo = new Card();

        mCardPrice = cardPrice;

        mCardInStock = cardInStock;

    }

    public Card getCardInfo() {
        return mCardInfo;
    }

    public void setCardInfo(Card mCardInfo) {
        this.mCardInfo = mCardInfo;
    }

    public int getCardInStock() {
        return mCardInStock;
    }

    public void setCardInStock(int mCardInStock) {
        this.mCardInStock = mCardInStock;
    }

    public double getCardPrice() {
        return mCardPrice;
    }

    public void setCardPrice(double mCardPrice) {
        this.mCardPrice = mCardPrice;
    }

    public int getListingId() {
        return mListingId;
    }

    public void setListingId(int mLogId) {
        this.mListingId = mLogId;
    }

}
