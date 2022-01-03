package com.example.magicmarketplace.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.magicmarketplace.obj.CardListing;
import com.example.magicmarketplace.obj.Cart;
import com.example.magicmarketplace.obj.Inventory;
import com.example.magicmarketplace.obj.User;
import com.example.magicmarketplace.obj.Watchlist;

@Database(entities = {CardListing.class, User.class, Cart.class, Watchlist.class, Inventory.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "MTG_DATABASE";
    public static final String MTG_TABLE = "MTG_TABLE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String CART_TABLE = "CART_TABLE";
    public static final String WATCHLIST_TABLE = "WATCHLIST_TABLE";
    public static final String INVENTORY_TABLE = "INVENTORY_TABLE";

    public abstract CardListDAO getCardListDAO();


}
