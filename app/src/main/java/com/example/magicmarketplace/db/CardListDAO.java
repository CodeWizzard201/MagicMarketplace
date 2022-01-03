package com.example.magicmarketplace.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.magicmarketplace.obj.CardListing;
import com.example.magicmarketplace.obj.Cart;
import com.example.magicmarketplace.obj.Inventory;
import com.example.magicmarketplace.obj.User;
import com.example.magicmarketplace.obj.Watchlist;

import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 5, 2021
 *Explanation: This is the DAO for the program, it holds the Cardlisting object as well as the User objects.
 */
@Dao
public interface CardListDAO {

    @Insert
    void insert(CardListing... cardListings);

    @Update
    void update(CardListing... cardListings);

    @Delete
    void delete(CardListing cardListing);

    @Query("SELECT * FROM " + AppDatabase.MTG_TABLE + " ORDER BY cardName")
    List<CardListing> getAllCardListings();

    @Query("SELECT * FROM " + AppDatabase.MTG_TABLE + " WHERE mListingId = :logId")
    CardListing getCardListingById(int logId);


    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :userName")
    User getUserByUsername(String userName);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(Cart... carts);

    @Update
    void update(Cart... carts);

    @Delete
    void delete(Cart cart);

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE + " WHERE userID = :userId AND listingID = :listingId")
    Cart getSpecficCart(int userId, int listingId);

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE + " WHERE userID = :userID")
    List<Cart> getAllCartListingsOfUser(int userID);

    @Insert
    void insert(Watchlist... watchlists);

    @Update
    void update(Watchlist... watchlists);

    @Delete
    void delete(Watchlist watchlist);

    @Query("SELECT * FROM " + AppDatabase.WATCHLIST_TABLE + " WHERE userID = :userId AND listingID = :listingId")
    Watchlist getSpecficWatchlist(int userId, int listingId);

    @Query("SELECT * FROM " + AppDatabase.WATCHLIST_TABLE + " WHERE userID = :userID")
    List<Watchlist> getAllWatchListingsOfUser(int userID);

    @Insert
    void insert(Inventory... inventories);

    @Update
    void update(Inventory... inventories);

    @Delete
    void delete(Inventory inventory);

    @Query("SELECT * FROM " + AppDatabase.INVENTORY_TABLE + " WHERE userID = :userId AND listingID = :listingId")
    Inventory getSpecficInventory(int userId, int listingId);

    @Query(" SELECT * FROM " + AppDatabase.INVENTORY_TABLE + " WHERE userID = :userID")
    List<Inventory> getAllInventoryListingsOfUser(int userID);

}
