package com.example.magicmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.magicmarketplace.adapters.SearchAdapter;
import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.Card;
import com.example.magicmarketplace.obj.CardListing;
import com.example.magicmarketplace.obj.Cart;
import com.example.magicmarketplace.obj.Inventory;
import com.example.magicmarketplace.obj.User;
import com.example.magicmarketplace.obj.Watchlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is where the program displays the list corresponding to which list is read in
 */
public class LookActivity extends AppCompatActivity {

    private CardListDAO mCardListingDAO;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        getDatabase();
        wireUpDisplay();


    }

    private void checkType() {
        Intent intent = getIntent();
        if(intent.hasExtra("List")){
            Bundle extras = intent.getExtras();
            type = extras.getInt("List", -1);
            switch(type){
                case 1:
                    displayWatchlist();
                    return;
                case 2:
                    displayCart();
                    return;
                case 3:
                    displayInventory();
                    return;
            }
        }
    }

    private void displayInventory() {
        List<Inventory> inventory = mCardListingDAO.getAllInventoryListingsOfUser(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1));
        List<CardListing> cl = new ArrayList<>();
        for(Inventory inv : inventory){
            cl.add(mCardListingDAO.getCardListingById(inv.getListingID()));
        }
        mAdapter = new SearchAdapter(cl,this,4);
        recyclerView.setAdapter(mAdapter);
    }

    private void displayCart() {
        List<Cart> cart = mCardListingDAO.getAllCartListingsOfUser(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1));
        List<CardListing> cl = new ArrayList<>();
        for(Cart c :cart){
            cl.add(mCardListingDAO.getCardListingById(c.getListingID()));
        }
        mAdapter = new SearchAdapter(cl,this,3);
        recyclerView.setAdapter(mAdapter);
    }

    private void displayWatchlist() {
        List<Watchlist> watchlists = mCardListingDAO.getAllWatchListingsOfUser(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1));
        List<CardListing> cl = new ArrayList<>();
        for(Watchlist w : watchlists){
            cl.add(mCardListingDAO.getCardListingById(w.getListingID()));
        }
        mAdapter = new SearchAdapter(cl,this,2);
        recyclerView.setAdapter(mAdapter);


    }



    private void wireUpDisplay() {
        recyclerView = findViewById(R.id.lookList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        checkType();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.return_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.returnItem) {
            Intent intent = LandingActivity.intentFactory(getApplicationContext());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDatabase() {
        mCardListingDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LookActivity.class);

        return intent;
    }
}