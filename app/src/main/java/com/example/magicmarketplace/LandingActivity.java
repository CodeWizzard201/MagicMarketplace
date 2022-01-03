package com.example.magicmarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.User;

import java.util.List;

public class LandingActivity extends AppCompatActivity {
    private TextView mWelcomeMessage;
    private Button mSearchButton;
    private Button mWatchListButton;
    private Button mCartButton;
    private Button mInventoryButton;
    private Button mLogOutButton;
    private Button mAdminButton;


    private int mUserID;
    private User mUser;

    private CardListDAO mCardListDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mCardListDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();

        wireUpDisplay();


    }

    private void wireUpDisplay() {
        mWelcomeMessage = findViewById(R.id.welcomeTextView);

        mSearchButton = findViewById(R.id.buttonSearch);
        mWatchListButton = findViewById(R.id.buttonWatchlist);
        mInventoryButton = findViewById(R.id.buttonInvetory);
        mCartButton = findViewById(R.id.buttonCart);
        mLogOutButton = findViewById(R.id.buttonLogOut);
        mAdminButton = findViewById(R.id.buttonAdmin);



        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchActivity.intentFactory(getApplicationContext());
                startActivity(intent);

            }
        });

        mWatchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LookActivity.intentFactory(getApplicationContext());
                intent.putExtra("List", 1);
                startActivity(intent);

            }
        });

        mInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LookActivity.intentFactory(getApplicationContext());
                intent.putExtra("List", 3);
                startActivity(intent);
            }
        });

        mCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LookActivity.intentFactory(getApplicationContext());
                intent.putExtra("List", 2);
                startActivity(intent);

            }
        });

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogOut();//Takes the User back to the Main Activity
            }
        });

        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext());
                startActivity(intent);//Goes to the Admin Menu
            }
        });

        checkForAdmin();
        String newWelcomeMessage = "Welcome, " + mUser.getUserName();
        mWelcomeMessage.setText(newWelcomeMessage);


    }

    private void confirmLogOut() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.log_out_message);

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Clear User from Intent
                clearUserFromIntent();
                //Clear User from Pref
                clearUserFromPrefs();
                Intent intent = MainActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Nothing we need to do here.
            }
        });

        alertBuilder.create().show();
    }

    private void clearUserFromPrefs() {
        if(MainActivity.mPreferences == null){
            MainActivity.mPreferences = this.getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = MainActivity.mPreferences.edit();
        editor.putInt(MainActivity.USER_ID_KEY, -1);
        editor.apply();
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(MainActivity.USER_ID_KEY,-1);
    }

    private void checkForAdmin(){//Grabs the user from the preferences and checks if the isAdmin boolean is true or not.

        if(MainActivity.mPreferences.contains(MainActivity.USER_ID_KEY)) {
            mUser = mCardListDAO.getUserByUserId(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY, -1));

            if (mUser.getIsAdmin()) {
                mAdminButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LandingActivity.class);

        return intent;
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra(MainActivity.USER_ID_KEY, userId);

        return intent;
    }
}