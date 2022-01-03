package com.example.magicmarketplace.obj;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.magicmarketplace.db.AppDatabase;

@Entity(tableName = AppDatabase.INVENTORY_TABLE)
public class Inventory {
    @PrimaryKey(autoGenerate = true)
    private int inventoryID;

    private int userID;
    private int listingID;
    private int count;

    public Inventory(int userID, int listingID, int count) {
        this.userID = userID;
        this.listingID = listingID;
        this.count = count;
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
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
