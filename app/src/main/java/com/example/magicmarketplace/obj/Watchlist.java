package com.example.magicmarketplace.obj;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.magicmarketplace.db.AppDatabase;

@Entity(tableName = AppDatabase.WATCHLIST_TABLE)
public class Watchlist {
    @PrimaryKey(autoGenerate = true)
    private int watchID;

    private int userID;
    private int listingID;

    public Watchlist(int userID, int listingID) {
        this.userID = userID;
        this.listingID = listingID;
    }

    public int getWatchID() {
        return watchID;
    }

    public void setWatchID(int watchID) {
        this.watchID = watchID;
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
}
