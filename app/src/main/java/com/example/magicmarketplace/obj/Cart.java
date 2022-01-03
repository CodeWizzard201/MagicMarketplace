package com.example.magicmarketplace.obj;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.magicmarketplace.db.AppDatabase;

@Entity(tableName = AppDatabase.CART_TABLE)
public class Cart {//This holds the IDs for the carts.
    @PrimaryKey(autoGenerate = true)
    private int cartID;

    private int userID;
    private int listingID;
    private int count;

    public Cart(int userID, int listingID, int count) {
        this.userID = userID;
        this.listingID = listingID;
        this.count = count;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getListingID() {
        return listingID;
    }

    public void setListingID(int listingID) {
        this.listingID = listingID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
