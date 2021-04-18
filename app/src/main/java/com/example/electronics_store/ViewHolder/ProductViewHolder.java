package com.example.electronics_store.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.Interface.ItemClickListener;
import com.example.electronics_store.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productTitle;
    public TextView productManufacturer;
    public TextView productPrice;
    public ImageView productImage;

    public ItemClickListener listener;

    public ProductViewHolder(View itemView){
        super(itemView);

        productTitle = (TextView) itemView.findViewById(R.id.product_title);
        productManufacturer = (TextView) itemView.findViewById(R.id.product_manufacturer);
        productPrice = (TextView) itemView.findViewById(R.id.product_price);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
