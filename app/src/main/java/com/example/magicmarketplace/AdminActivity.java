package com.example.magicmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.magicmarketplace.adapters.ListingAdapter;
import com.example.magicmarketplace.adapters.UserAdapter;
import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.CardListing;
import com.example.magicmarketplace.obj.User;

import java.util.ArrayList;
import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is where the program displays the listview for Admin
 */

public class AdminActivity extends AppCompatActivity {//Check out the thing by the coding with guy.


    private CardListDAO mCardListingDAO;
    private Button mAddListing;
    private List<User> users = new ArrayList<>();
    private List<CardListing> listings = new ArrayList<>();
    Menu menu;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getDatabase();
        fillUserList();
        fillCardListingList();
        wireUpDisplay();
    }

    private void fillCardListingList() {
        listings = mCardListingDAO.getAllCardListings();
    }

    private void fillUserList() {
        users = mCardListingDAO.getAllUsers();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuUsers:
                mAddListing.setVisibility(View.INVISIBLE);//Sets the button to invisible since Admins can't add users through their screen
                //Display a list of users
                users.clear();
                fillUserList();
                //We have to build this
                mAdapter = new UserAdapter(users,this);
                recyclerView.setAdapter(mAdapter);

                return true;
            case R.id.menuCardListings:
                mAddListing.setVisibility(View.VISIBLE);//Sets button to visible
                //Display a list of Card Listings
                listings.clear();
                fillCardListingList();
                //We have to build this
                mAdapter = new ListingAdapter(listings,this);
                recyclerView.setAdapter(mAdapter);

                return true;
            case R.id.menuBack:
                //Return to landing activity
                Intent intent = LandingActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    private void wireUpDisplay() {
        mAddListing = findViewById(R.id.buttonAddListing);
        recyclerView = findViewById(R.id.rvList);

        mAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChangeActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    private void getDatabase() {
        mCardListingDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();
    }



    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, AdminActivity.class);

        return intent;
    }
}