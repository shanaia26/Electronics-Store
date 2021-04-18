package com.example.electronics_store.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.Interface.ItemClickListener;
import com.example.electronics_store.R;

public class ProductHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productHistoryOrderID;
    public TextView productHistoryProductTitle;
    public TextView productHistoryPrice;
    public TextView productHistoryShipmentStatus;
    public TextView productHistoryQuantity;


    private ItemClickListener itemClickListener;

    public ProductHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        productHistoryOrderID = itemView.findViewById(R.id.product_history_order_id);
        productHistoryProductTitle = itemView.findViewById(R.id.product_history_product_title);
        productHistoryPrice = itemView.findViewById(R.id.product_history_product_price);
        productHistoryShipmentStatus = itemView.findViewById(R.id.product_history_product_shipment_status);
        productHistoryQuantity = itemView.findViewById(R.id.product_history_quantity);
    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}