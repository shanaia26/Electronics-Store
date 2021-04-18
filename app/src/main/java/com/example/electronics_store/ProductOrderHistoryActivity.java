package com.example.electronics_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.electronics_store.Common.Common;
import com.example.electronics_store.Model.Cart;
import com.example.electronics_store.ViewHolder.ProductHistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductOrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerProductsHistory;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference orderHistoryReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order_history);

        recyclerProductsHistory = findViewById(R.id.recycler_product_order_history);
        recyclerProductsHistory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerProductsHistory.setLayoutManager(layoutManager);


        orderHistoryReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(Common.currentUser.getPhone())
                .child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(orderHistoryReference, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, ProductHistoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, ProductHistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductHistoryViewHolder holder, int position, @NonNull Cart model) {
                        holder.productHistoryOrderID.setText("Order ID: " + model.getOrderID());
                        holder.productHistoryShipmentStatus.setText("Shipment Status: " + model.getShipmentStatus());
                        holder.productHistoryProductTitle.setText(model.getTitle());
                        holder.productHistoryPrice.setText("Price: €" + model.getPrice());
                        holder.productHistoryQuantity.setText("Quantity: " + model.getQuantity());
                    }

                    @NonNull
                    @Override
                    public ProductHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_order_history_layout, parent, false);
                        ProductHistoryViewHolder holder = new ProductHistoryViewHolder(view);
                        return holder;
                    }
                };
        recyclerProductsHistory.setAdapter(adapter);
        adapter.startListening();
    }
}