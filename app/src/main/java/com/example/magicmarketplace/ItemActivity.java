package com.example.magicmarketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.CardListing;
import com.example.magicmarketplace.obj.Cart;
import com.example.magicmarketplace.obj.Inventory;
import com.example.magicmarketplace.obj.User;
import com.example.magicmarketplace.obj.Watchlist;

import java.util.ArrayList;
import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is where the program displays an item of the list selected
 */
public class ItemActivity extends AppCompatActivity {//This is the activity that displays the Cardlistings or Users to be deleted.

    private CardListDAO mCardListingDAO;
    private Button mButtonAWL;//Add to Watchlist
    private Button mButtonAC;//Add to Cart
    private Button mButtonRWL;//Remove from Watchlist
    private Button mButtonRC;//Remove from Cart
    private Button mButtonP;//Purchase
    private TextView mTVCaNa;//CardName textview
    private TextView mTVCaSe;//CardSet textview
    private TextView mTVCaRa;//CardRarity textview
    private TextView mTVCaFo;//CardIsFoil textview
    private TextView mTVCaPr;//CardPrice textview
    private TextView mTVCaSt;//CardInStock textview
    private int id,type;
    private int count = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getDatabase();
        wireUpDisplay();
        checkIntent();
        checkType();
    }

    private void wireUpDisplay() {
        mTVCaNa = findViewById(R.id.tvCN);
        mTVCaSe = findViewById(R.id.tvCS);
        mTVCaRa = findViewById(R.id.tvCR);
        mTVCaFo = findViewById(R.id.tvCF);
        mTVCaPr = findViewById(R.id.tvPriceOfCard);
        mTVCaSt = findViewById(R.id.tvInventoryStock);
        mButtonAWL = findViewById(R.id.buttonAddWL);
        mButtonAC = findViewById(R.id.buttonAddCart);
        mButtonRWL = findViewById(R.id.buttonRemoveWL);
        mButtonRC = findViewById(R.id.buttonRemoveCart);
        mButtonP = findViewById(R.id.buttonPurchase);

        mButtonAWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWatchlist();
                Intent intent = SearchActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mButtonAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
                Intent intent = SearchActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mButtonRWL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeWatchlist();
                Intent intent = LookActivity.intentFactory(getApplicationContext());
                intent.putExtra("List", 1);
                startActivity(intent);

            }
        });

        mButtonRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCart();
                Intent intent = LookActivity.intentFactory(getApplicationContext());
                intent.putExtra("List",2);
                startActivity(intent);
            }
        });

        mButtonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseItem();
                Intent intent = LookActivity.intentFactory((getApplicationContext()));
                intent.putExtra("List", 2);
                startActivity(intent);
            }
        });
    }


    private void removeCart() {//Coming from cart activity
        Cart cart = mCardListingDAO.getSpecficCart(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1),id);
        mCardListingDAO.delete(cart);

    }

    private void removeWatchlist() {//Coming from watchlist activity
        Watchlist watch = mCardListingDAO.getSpecficWatchlist(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1),id);
        mCardListingDAO.delete(watch);
    }

    private void addCart() {//Coming from the search activity, adding to an already existing cart item and adding a new item to the cart
        CardListing cl = mCardListingDAO.getCardListingById(id);
        if(cl.getCardInStock() <= 0){
            Toast.makeText(this, "Not in Stock.", Toast.LENGTH_SHORT).show();
            return;
        }
        Cart cart = mCardListingDAO.getSpecficCart(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1),id);
        if(cart != null){
            cart.setCount(cart.getCount()+1);
            mCardListingDAO.update(cart);
        }else{
            Cart newItem = new Cart(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1),id, 1);
            mCardListingDAO.insert(newItem);
        }

    }

    private void addWatchlist() {//Coming from the search activity
        Watchlist watch = new Watchlist(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1),id);
        List<Watchlist> wl = mCardListingDAO.getAllWatchListingsOfUser(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1));
        for( Watchlist list : wl){
            if(watch.getListingID() == list.getListingID()){
                return;
            }
        }
        //Adding the item to the watchlist
        mCardListingDAO.insert(watch);
    }

    private void purchaseItem() {//Coming from cart activity
        User user = mCardListingDAO.getUserByUserId(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1));
        Cart cart = mCardListingDAO.getSpecficCart(user.getUserId(),id);
        CardListing cl = mCardListingDAO.getCardListingById(id);
        int countStock = cl.getCardInStock();
        if(cl.getCardInStock() <= 0|| countStock - cart.getCount() < 0){
            Toast.makeText(this, cl.getCardInfo().getCardName() + " does not have enough copies!", Toast.LENGTH_SHORT).show();
           return;
        }
        cl.setCardInStock(countStock - cart.getCount());
        Inventory inv = mCardListingDAO.getSpecficInventory(user.getUserId(),id);
        if(inv != null){
            inv.setCount(inv.getCount()+ cart.getCount());
            mCardListingDAO.update(inv);
        }else{
            Inventory newItem = new Inventory(cart.getUserID(),cart.getListingID(),cart.getCount());
            mCardListingDAO.insert(newItem);
        }
        mCardListingDAO.delete(cart);
        Toast.makeText(this, cl.getCardInfo().getCardName() + " was bought successfully!", Toast.LENGTH_SHORT).show();

    }

    private void checkIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("id")) {
            Bundle extras = getIntent().getExtras();
            id = extras.getInt("id", -1);
            if (id >= 0) {
                CardListing listing = mCardListingDAO.getCardListingById(id);
                //Put the info of the listing in the activity
                mTVCaNa.setText(listing.getCardInfo().getCardName());
                mTVCaSe.setText(listing.getCardInfo().getCardSet());
                mTVCaRa.setText(listing.getCardInfo().getCardRarity());
                mTVCaFo.setText(String.valueOf(listing.getCardInfo().isFoil()));
                mTVCaPr.setText(String.valueOf(listing.getCardPrice()));
                mTVCaSt.setText(String.valueOf(listing.getCardInStock()));

            }
        }
    }

    private void checkType() {
        Intent intent = getIntent();
        if(intent.hasExtra("Type")){
            Bundle extras = intent.getExtras();
            type = extras.getInt("Type", -1);
            switch(type){
                case 1:
                    //Viewing a listing to ADD to Watchlist or Cart
                    mButtonAWL.setVisibility(View.VISIBLE);
                    mButtonAC.setVisibility(View.VISIBLE);
                    return;
                case 2:
                    //Viewing a listing to REMOVE from watchlist
                    mButtonRWL.setVisibility(View.VISIBLE);
                    return;
                case 3:
                    //Viewing a listing to REMOVE from cart, or PURCHASE the item
                    mButtonRC.setVisibility(View.VISIBLE);
                    mButtonP.setVisibility(View.VISIBLE);
                    return;
                case 4:
                    //Viewing a listing in the inventory
                    return;
            }
        }
    }

    private List<CardListing> findCartListings(List<Cart> cartOfUser) {//Turns the Cart list to the CardListings list
        List<CardListing> cl = new ArrayList<>();
        for(Cart cart: cartOfUser){
            cl.add(mCardListingDAO.getCardListingById(cart.getListingID()));
        }
        return cl;
    }

    private List<CardListing> findWatchListings(List<Watchlist> watchOfUser) {
        List<CardListing> cl = new ArrayList<>();
        for(Watchlist watch: watchOfUser){
            cl.add(mCardListingDAO.getCardListingById(watch.getListingID()));
        }
        return cl;
    }

    private List<CardListing> findInvListings(List<Inventory> inventoryOfUser) {
        List<CardListing> cl = new ArrayList<>();
        for(Inventory inventory: inventoryOfUser){
            cl.add(mCardListingDAO.getCardListingById(inventory.getListingID()));
        }
        return cl;

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
            switch(type){
                case 1:
                    //Return to SearchActivity
                    Intent intent = SearchActivity.intentFactory(getApplicationContext());
                    startActivity(intent);
                    return true;
                case 2:
                    //Returning to LookActivity under Watchlist context
                    Intent intentWL = LookActivity.intentFactory(getApplicationContext());
                    intentWL.putExtra("List", 1 );
                    startActivity(intentWL);
                    return true;

                case 3:
                    //Returning to LookActivity under Cart context
                    Intent intentC = LookActivity.intentFactory(getApplicationContext());
                    intentC.putExtra("List", 2 );
                    startActivity(intentC);
                    return true;
                case 4:
                    //Returning to LookActivity under Inventory context
                    Intent intentInv = LookActivity.intentFactory(getApplicationContext());
                    intentInv.putExtra("List", 3 );
                    startActivity(intentInv);
                    return true;
            }

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
        Intent intent = new Intent(context, ItemActivity.class);

        return intent;
    }

}