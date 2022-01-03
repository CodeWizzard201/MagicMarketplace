package com.example.magicmarketplace.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.magicmarketplace.MainActivity;
import com.example.magicmarketplace.R;
import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.User;
import com.example.magicmarketplace.obj.Watchlist;

import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is the adapter that displays the User list in AdminActivity
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    List<User> users;
    Context context;
    CardListDAO mCardListingDAO;

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_user_layout,parent, false);
        UserAdapter.MyViewHolder holder = new UserAdapter.MyViewHolder(view);
        getDatabase();
        return holder;
    }

    private void getDatabase() {
        mCardListingDAO = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getCardListDAO();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvUsername.setText(users.get(position).getUserName());
        holder.tvWatchlist.setText(String.valueOf(mCardListingDAO.getAllCartListingsOfUser(users.get(position).getUserId()).size()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

                alertBuilder.setMessage("Do you want to delete this user?");

                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Remove the selected user from the database
                        removeUser(users.get(holder.getBindingAdapterPosition()).getUserId(), holder.getBindingAdapterPosition());
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
        });
    }

    private void removeUser(int id,int position) {

        User user = mCardListingDAO.getUserByUserId(id);
        if(user.getIsAdmin()||user.getUserId() == MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1)){
            Toast.makeText(context, "Can't delete admins/the account you are currently logged into.", Toast.LENGTH_SHORT).show();
            return;
        }
        mCardListingDAO.delete(user);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvWatchlist;
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.viewUsername);
            tvWatchlist = itemView.findViewById(R.id.viewWatchlistCount);
            parentLayout = itemView.findViewById(R.id.oneUserLayout);
        }
    }
    
    
}
