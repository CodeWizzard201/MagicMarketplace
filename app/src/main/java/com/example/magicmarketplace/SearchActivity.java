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
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.magicmarketplace.adapters.ListingAdapter;
import com.example.magicmarketplace.adapters.SearchAdapter;
import com.example.magicmarketplace.adapters.UserAdapter;
import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.Card;
import com.example.magicmarketplace.obj.CardListing;

import java.util.ArrayList;
import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is where the program displays the search bar and it's findings
 */
public class SearchActivity extends AppCompatActivity {
    private CardListDAO mCardListingDAO;
    private SearchView mSearchBar;
    private List<CardListing> listings = new ArrayList<>();
    private boolean firstTime = true;
    Menu menu;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getDatabase();
        wireUpDisplay();


    }

    private void refreshDisplay(boolean wholeList) {

        if(wholeList){
            listings.clear();
            fillCardListingList();//Only when the refresh is for  bringing up the whole list
        }
        mAdapter = new SearchAdapter(listings,this,1);
        recyclerView.setAdapter(mAdapter);

    }

    private void wireUpDisplay() {
        mSearchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.searchList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        refreshDisplay(true);
        mSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Cannot find a card with that name.", Toast.LENGTH_SHORT).show();
                    refreshDisplay(true);
                }else {
                    listings = checkSearch(query);
                    refreshDisplay(listings.size() == 0);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    refreshDisplay(true);
                }else {
                    listings = checkSearch(newText);
                    refreshDisplay(listings.size() == 0);
                }
                return false;
            }
        });

    }

    private List<CardListing> checkSearch(String search) {
        List<CardListing> temp = new ArrayList<>();
        for(CardListing cl: listings){
            if(search.equalsIgnoreCase(cl.getCardInfo().getCardName())||cl.getCardInfo().getCardName().toLowerCase().contains(search.toLowerCase())){
                temp.add(cl);
            }
        }
        return temp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.return_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.returnItem) {//Return to landing activity
            Intent intent = LandingActivity.intentFactory(getApplicationContext());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillCardListingList() {
        listings = mCardListingDAO.getAllCardListings();
    }



    private void getDatabase() {
        mCardListingDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, SearchActivity.class);

        return intent;
    }
}