package com.example.magicmarketplace.obj;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.obj.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 5, 2021
 *Explanation: This is the POJO for User.
 */
@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mUserPassword;
    private boolean mIsAdmin;


    public User(){

    }

    public User(String userName, String userPassword, boolean isAdmin)  {
        mUserName = userName;
        mUserPassword = userPassword;
        mIsAdmin = isAdmin;
    }

    public boolean getIsAdmin() {
        return mIsAdmin;
    }

    public void setIsAdmin(boolean mIsAdmin) {
        this.mIsAdmin = mIsAdmin;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String mUserPassword) {
        this.mUserPassword = mUserPassword;
    }
}
