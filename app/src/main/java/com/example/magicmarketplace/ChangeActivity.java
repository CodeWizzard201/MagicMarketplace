package com.example.magicmarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.Card;
import com.example.magicmarketplace.obj.CardListing;
import com.example.magicmarketplace.obj.User;

import org.w3c.dom.Text;

public class ChangeActivity extends AppCompatActivity {

    private CardListDAO mCardListingDAO;
    private Button mButtonAdd;
    private Button mButtonModify;
    private Button mButtonDelete;
    private Button mButtonBack;
    private TextView mTextID;
    private EditText mTextCardName;
    private EditText mTextCardSet;
    private EditText mTextCardRarity;
    private EditText mTextIsFoil;
    private EditText mTextCardPrice;
    private EditText mTextCardStock;

    private int id = -1;//Takes in either entries ids.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        getDatabase();
        wireUpDisplay();



    }


    private void addNewListing() {
        String booleanConvert = mTextIsFoil.getText().toString();
        boolean convertString;
        if(booleanConvert.equalsIgnoreCase("true")||booleanConvert.equalsIgnoreCase("yes")){
            convertString = true;
        }else if(booleanConvert.equalsIgnoreCase("false")||booleanConvert.equalsIgnoreCase("no")){
            convertString = false;
        }else{
            Toast.makeText(this, "Please give either a True/Yes or a False/No for the Foil entry", Toast.LENGTH_SHORT).show();
            return;
        }
        Card card = new Card(mTextCardName.getText().toString(), mTextCardSet.getText().toString(), mTextCardRarity.getText().toString(), convertString);
        CardListing listing = new CardListing(Double.parseDouble(mTextCardPrice.getText().toString()),Integer.parseInt(mTextCardStock.getText().toString()));
        listing.setCardInfo(card);
        mCardListingDAO.insert(listing);

    }

    private void wireUpDisplay() {
        mButtonAdd = findViewById(R.id.buttonAdd);
        mButtonModify = findViewById(R.id.buttonModify);
        mButtonDelete = findViewById(R.id.buttonDelete);
        mButtonBack = findViewById(R.id.buttonBack);
        mTextID = findViewById(R.id.viewListingIDNumber);
        mTextCardName = findViewById(R.id.editCardName);
        mTextCardSet = findViewById(R.id.editCardSet);
        mTextCardRarity = findViewById(R.id.editCardRarity);
        mTextIsFoil = findViewById(R.id.editCardFoil);
        mTextCardPrice = findViewById(R.id.editCardPrice);
        mTextCardStock = findViewById(R.id.editInStock);

        checkIntent();


        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add Listing
                addNewListing();
                Intent intent = AdminActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mButtonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Edit a Listing
                editListing();
                Intent intent = AdminActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove Listing
                removeListing();
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void removeListing() {
        if(id < 0){
            Toast.makeText(this, "Please select the listing you would like to delete on the other screen.", Toast.LENGTH_SHORT).show();
            return;
        }
        mCardListingDAO.delete(mCardListingDAO.getCardListingById(id));
        Intent intent = AdminActivity.intentFactory(getApplicationContext());
        startActivity(intent);

    }

    private void editListing() {
        String booleanConvert = mTextIsFoil.getText().toString();
        boolean convertString;
        if(booleanConvert.equalsIgnoreCase("true")||booleanConvert.equalsIgnoreCase("yes")){
            convertString = true;
        }else if(booleanConvert.equalsIgnoreCase("false")||booleanConvert.equalsIgnoreCase("no")){
            convertString = false;
        }else{
            Toast.makeText(this, "Please give either a True/Yes or a False/No for the Foil entry", Toast.LENGTH_SHORT).show();
            return;
        }
        Card card = new Card(mTextCardName.getText().toString(), mTextCardSet.getText().toString(), mTextCardRarity.getText().toString(), convertString);
        CardListing listing = new CardListing(Double.parseDouble(mTextCardPrice.getText().toString()),Integer.parseInt(mTextCardStock.getText().toString()));
        listing.setCardInfo(card);
        listing.setListingId(id);
        mCardListingDAO.update(listing);
    }

    private void checkIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("id")) {
            Bundle extras = getIntent().getExtras();
            id = extras.getInt("id", -1);
            if (id < 0) {
                return;
            } else if (id >= 0) {
                CardListing listing = mCardListingDAO.getCardListingById(id);
                //Put the info of the listing in the activity
                mTextID.setText(String.valueOf(id));
                mTextCardName.setText(listing.getCardInfo().getCardName());
                mTextCardSet.setText(listing.getCardInfo().getCardSet());
                mTextCardRarity.setText(listing.getCardInfo().getCardRarity());
                mTextIsFoil.setText(String.valueOf(listing.getCardInfo().isFoil()));
                mTextCardPrice.setText(String.valueOf(listing.getCardPrice()));
                mTextCardStock.setText(String.valueOf(listing.getCardInStock()));

            }
        }
    }

    private void getDatabase() {
        mCardListingDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, ChangeActivity.class);

        return intent;
    }
}