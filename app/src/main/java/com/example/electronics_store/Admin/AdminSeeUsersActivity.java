package com.example.electronics_store.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.electronics_store.Model.UserOrders;
import com.example.electronics_store.Model.Users;
import com.example.electronics_store.R;
import com.example.electronics_store.ViewHolder.AdminOrdersViewHolder;
import com.example.electronics_store.ViewHolder.AdminUsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSeeUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerUsers;
    private DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_see_users);

        usersReference = FirebaseDatabase.getInstance().getReference()
                .child("Users");

        recyclerUsers = findViewById(R.id.recycler_users);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(usersReference, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users, AdminUsersViewHolder> adapter
                = new FirebaseRecyclerAdapter<Users, AdminUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminUsersViewHolder holder, int position, @NonNull Users model) {
                holder.adminUserName.setText("Name: " + model.getName());
                holder.adminUserPhone.setText("Phone Number: " + model.getPhone());
                holder.adminUserEmail.setText("Email: " + model.getEmail());
            }

            @NonNull
            @Override
            public AdminUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_users_layout, parent, false);
                return new AdminUsersViewHolder(view);
            }
        };
        recyclerUsers.setAdapter(adapter);
        adapter.startListening();
    }
}