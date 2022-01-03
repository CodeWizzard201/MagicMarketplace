package com.example.magicmarketplace.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.magicmarketplace.ChangeActivity;
import com.example.magicmarketplace.ItemActivity;
import com.example.magicmarketplace.MainActivity;
import com.example.magicmarketplace.R;
import com.example.magicmarketplace.SearchActivity;
import com.example.magicmarketplace.db.AppDatabase;
import com.example.magicmarketplace.db.CardListDAO;
import com.example.magicmarketplace.obj.Card;
import com.example.magicmarketplace.obj.CardListing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is the adapter that displays the lists in SearchActivity and LookActivity
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    List<CardListing> listings;
    HashMap<CardListing,Integer> cartListings;
    Context context;
    int type;
    CardListDAO mCardListingDAO;

    public SearchAdapter(List<CardListing> listings, Context context, int type) {
        this.listings = listings;
        this.context = context;
        this.type = type;
    }


    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_listing_layout,parent, false);
        SearchAdapter.MyViewHolder holder = new SearchAdapter.MyViewHolder(view);
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
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
            holder.tvCardName.setText(listings.get(position).getCardInfo().getCardName());
            holder.tvCardSet.setText(listings.get(position).getCardInfo().getCardSet());
            holder.tvCardRarity.setText(listings.get(position).getCardInfo().getCardRarity());
            holder.tvCardPrice.setText(context.getString(R.string.Price_in_Dollars, String.valueOf(listings.get(position).getCardPrice())));
            if(type == 4){
                holder.inStockText.setText(R.string.owned);
                holder.tvCardInStock.setText(String.valueOf(mCardListingDAO.getSpecficInventory(MainActivity.mPreferences.getInt(MainActivity.USER_ID_KEY,-1),listings.get(position).getListingId()).getCount()));
            }else{
                holder.inStockText.setText(R.string.in_stock);
                holder.tvCardInStock.setText(String.valueOf(listings.get(position).getCardInStock()));
            }

            if (listings.get(position).getCardInfo().isFoil()) {
                holder.tvCardIsFoil.setText(R.string.is_a_foil);
            } else {
                holder.tvCardIsFoil.setText(R.string.is_not_a_foil);
            }
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ItemActivity.intentFactory(context);
                    intent.putExtra("Type", type);//this is a part of the check in ItemActivity that decides which buttons to show or hide.
                    intent.putExtra("id", listings.get(holder.getBindingAdapterPosition()).getListingId());
                    context.startActivity(intent);

                }
            });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardName;
        TextView tvCardSet;
        TextView tvCardRarity;
        TextView tvCardPrice;
        TextView tvCardInStock;
        TextView tvCardIsFoil;
        TextView inStockText;
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            tvCardSet = itemView.findViewById(R.id.tvCardSet);
            tvCardRarity = itemView.findViewById(R.id.tvCardRarity);
            tvCardPrice = itemView.findViewById(R.id.tvCardPrice);
            tvCardInStock = itemView.findViewById(R.id.tvNumberInStock);
            tvCardIsFoil = itemView.findViewById(R.id.tvIsFoil);
            inStockText = itemView.findViewById(R.id.tvCardStock);
            parentLayout = itemView.findViewById(R.id.oneListingLayout);
        }
    }
}
