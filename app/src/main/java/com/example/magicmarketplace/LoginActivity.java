package com.example.magicmarketplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.User;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is where the program logs in existing users
 */
public class LoginActivity extends AppCompatActivity {//The user either logs into an an already created account or they create a new account


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
        setContentView(R.layout.activity_login);
        getDatabase();
        wireUpDisplay();

    }

    private void wireUpDisplay(){
        mUserNameField = findViewById(R.id.editTextCreateUsername);
        mUserPasswordField = findViewById(R.id.editTextCreatePassword);

        mButton = findViewById(R.id.buttonCreateLogin);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }else{
                        addUserToPrefs(mUser.getUserId());
                        Intent intent = LandingActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);
                    }
                }

            }
        });
    }

    private void addUserToPrefs(int userId) {
        if(MainActivity.mPreferences == null){
            MainActivity.mPreferences = this.getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = MainActivity.mPreferences.edit();
        editor.putInt(MainActivity.USER_ID_KEY, userId);
        editor.apply();
    }

    private boolean validatePassword(){
        return mUser.getUserPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUsername = mUserNameField.getText().toString();
        mPassword = mUserPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mCardListingDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this, "no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void getDatabase(){
        mCardListingDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);

        return intent;
    }
}