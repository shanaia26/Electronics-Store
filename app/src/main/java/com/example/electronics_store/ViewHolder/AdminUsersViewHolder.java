package com.example.electronics_store.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronics_store.Interface.ItemClickListener;
import com.example.electronics_store.R;


public class AdminUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView adminUserName;
    public TextView adminUserPhone;
    public TextView adminUserEmail;

    private ItemClickListener itemClickListener;

    public AdminUsersViewHolder(@NonNull View itemView) {
        super(itemView);

        adminUserName = itemView.findViewById(R.id.admin_user_name);
        adminUserPhone = itemView.findViewById(R.id.admin_user_phone);
        adminUserEmail = itemView.findViewById(R.id.admin_user_email);
    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
