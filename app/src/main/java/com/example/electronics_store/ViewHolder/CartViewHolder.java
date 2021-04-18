package com.example.electronics_store.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.Interface.ItemClickListener;
import com.example.electronics_store.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView textProductTitle;
    public TextView textProductManufacturer;
    public TextView textProductPrice;
    public TextView textProductQuantity;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        textProductTitle = itemView.findViewById(R.id.cart_product_title);
        textProductManufacturer = itemView.findViewById(R.id.cart_product_manufacturer);
        textProductPrice = itemView.findViewById(R.id.cart_product_price);
        textProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
