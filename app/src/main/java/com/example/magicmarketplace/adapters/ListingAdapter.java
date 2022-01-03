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

import com.example.magicmarketplace.ChangeActivity;
import com.example.magicmarketplace.R;
import com.example.magicmarketplace.obj.CardListing;

import java.util.List;
/*
 *Name: Gabe Williams
 *Date: December 13, 2021
 *Explanation: This is the adapter that displays the CardListings list in the AdminActivity
 */

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.MyViewHolder> {
    List<CardListing> listings;
    Context context;

    public ListingAdapter(List<CardListing> listings, Context context) {
        this.listings = listings;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_listing_layout,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvCardName.setText(listings.get(position).getCardInfo().getCardName());
        holder.tvCardSet.setText(listings.get(position).getCardInfo().getCardSet());
        holder.tvCardRarity.setText(listings.get(position).getCardInfo().getCardRarity());
        holder.tvCardPrice.setText(context.getString(R.string.Price_in_Dollars, String.valueOf(listings.get(position).getCardPrice())));
        holder.tvCardInStock.setText(String.valueOf(listings.get(position).getCardInStock()));
        if(listings.get(position).getCardInfo().isFoil()){
            holder.tvCardIsFoil.setText(R.string.is_a_foil);
        }else {
            holder.tvCardIsFoil.setText(R.string.is_not_a_foil);
        }
        holder.parentLayout. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChangeActivity.intentFactory(context);
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
        ConstraintLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            tvCardSet = itemView.findViewById(R.id.tvCardSet);
            tvCardRarity = itemView.findViewById(R.id.tvCardRarity);
            tvCardPrice = itemView.findViewById(R.id.tvCardPrice);
            tvCardInStock = itemView.findViewById(R.id.tvNumberInStock);
            tvCardIsFoil = itemView.findViewById(R.id.tvIsFoil);
            parentLayout = itemView.findViewById(R.id.oneListingLayout);
        }
    }
}
