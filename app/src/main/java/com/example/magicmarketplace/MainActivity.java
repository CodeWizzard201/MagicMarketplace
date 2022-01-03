package com.example.magicmarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.Card;
import com.example.magicmarketplace.obj.CardListing;
import com.example.magicmarketplace.obj.User;

import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is where the program starts
 */
public class MainActivity extends AppCompatActivity { //This is the main screen that the user starts on
    public static final String USER_ID_KEY = "com.example.magicmarketplace.userIdKey";
    public static final String PREFERENCES_KEY = "com.example.magicmarketplace.preferencesKey";
    int mUserId;
    private Button mLoginButton;
    private Button mNewAccountButton;
    private CardListDAO mCardListDAO;

    public static SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = findViewById(R.id.buttonSelectLogIn);
        mNewAccountButton = findViewById(R.id.buttonSelectNewAccount);

        mCardListDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();


        checkForUser();

        mLoginButton.setOnClickListener(new View.OnClickListener() {//When we click the Login Button...
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        mNewAccountButton.setOnClickListener(new View.OnClickListener() {//When we click on the Create a New Account Button...
            @Override
            public void onClick(View v) {
                Intent intent = CreationActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    private void checkForUser(){
        //Do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if(mUserId != -1){
            Intent intent = LandingActivity.intentFactory(getApplicationContext(), mUserId);
            startActivity(intent);
        }

        if(mPreferences == null){
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            Intent intent = LandingActivity.intentFactory(getApplicationContext(), mUserId);
            startActivity(intent);
        }

        List<User> users = mCardListDAO.getAllUsers();
        if(users.size() <= 0){
        User defaultUser = new User("testuser1","testuser1",false);
        User defaultAdmin = new User("admin2","admin2",true);
        mCardListDAO.insert(defaultUser, defaultAdmin);
        defaultCards();
        }
    }

    private void defaultCards() {//If the default users need to be made, it will make the cards too
        Card forest = new Card("Forest","M22","Common",false);
        Card mountain = new Card("Mountain","M22","Common",false);
        Card plains = new Card("Plains","M22","Common",false);
        Card island = new Card("Island","M22","Common",false);
        Card swamp = new Card("Swamp","M22","Common",false);
        CardListing clForest = new CardListing(0.25,100);
        CardListing clMountain = new CardListing(0.25,100);
        CardListing clIsland = new CardListing(0.25,100);
        CardListing clPlains = new CardListing(0.25,100);
        CardListing clSwamp = new CardListing(0.25,100);
        clForest.setCardInfo(forest);
        clMountain.setCardInfo(mountain);
        clIsland.setCardInfo(island);
        clPlains.setCardInfo(plains);
        clSwamp.setCardInfo(swamp);

        mCardListDAO.insert(clForest,clMountain,clIsland,clPlains,clSwamp);

    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
}