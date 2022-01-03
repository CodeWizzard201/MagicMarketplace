package com.example.magicmarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.User;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is where the program allows the user to add a new account
 */

public class CreationActivity extends AppCompatActivity {

    private EditText mUserNameField;
    private EditText mUserPasswordField;
    private Button mButton;

    private String mUsername;
    private String mPassword;

    private User mUser;

    private CardListDAO mCardListingDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        getDatabase();
        wireUpDisplay();

    }

    private void getDatabase(){
        mCardListingDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();
    }

    private void wireUpDisplay(){
        mUserNameField = findViewById(R.id.editTextCreateUsername);
        mUserPasswordField = findViewById(R.id.editTextCreatePassword);

        mButton = findViewById(R.id.buttonCreateLogin);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(!checkForExistingUser()){
                    addUserToDatabase();
                    Intent intent = LandingActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                    startActivity(intent);
                }
            }
        });

    }

    private void addUserToDatabase() {
        mUser = new User(mUsername,mPassword,false);//Create
        mCardListingDAO.insert(mUser);//Insert
        mUser = mCardListingDAO.getUserByUsername(mUsername);//Retrieve
        addUserToPrefs(mUser.getUserId());
    }

    private void addUserToPrefs(int userId) {
        if(MainActivity.mPreferences == null){
            MainActivity.mPreferences = this.getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = MainActivity.mPreferences.edit();
        editor.putInt(MainActivity.USER_ID_KEY, userId);
        editor.apply();
    }

    private boolean checkForExistingUser() {//Checks if there is a Username that matches the username inputted on the creation screen.
        mUser = mCardListingDAO.getUserByUsername(mUsername);
        if(mUser == null){
            return false;//This is when no usernames match and we are good to continue deeper in making the account.
        }
        Toast.makeText(this, "User: " + mUsername + " found. Try another name.", Toast.LENGTH_SHORT).show();
        return true;//This is when a username does match and we have to try again.
    }

    private void getValuesFromDisplay(){
        mUsername = mUserNameField.getText().toString();
        mPassword = mUserPasswordField.getText().toString();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, CreationActivity.class);

        return intent;
    }
}