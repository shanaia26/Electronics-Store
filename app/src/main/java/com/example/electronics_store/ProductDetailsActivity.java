package com.example.electronics_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.electronics_store.Common.Common;
import com.example.electronics_store.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView detailsImage;
    private TextView detailsTitle;
    private TextView detailsManufacturer;
    private TextView detailsPrice;
    private ElegantNumberButton elegantNumberButton;
    private Button addProductCartButton;

    private DatabaseReference orderReference;

    //Get productID of item user clicked
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        detailsTitle = findViewById(R.id.details_title);
        detailsManufacturer = findViewById(R.id.details_manufactuer);
        detailsPrice = findViewById(R.id.details_price);
        detailsImage = findViewById(R.id.details_image);
        elegantNumberButton = findViewById(R.id.elegant_number_button);
        addProductCartButton = findViewById(R.id.add_to_cart_button);

        productID = getIntent().getStringExtra("productID");

        GetProductDetails(productID);

        addProductCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCartList();
            }
        });
    }

    private void GetProductDetails(String productID) {
        DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("Products");
        productReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    detailsTitle.setText(products.getTitle());
                    detailsManufacturer.setText(products.getManufacturer());
                    detailsPrice.setText(products.getPrice());
                    //Getting product image
                    Picasso.get().load(products.getImage()).into(detailsImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AddToCartList() {
        //Store in firebase
        final DatabaseReference cartListReference =
                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("orderID", "Not Available");
        cartMap.put("productID", productID);
        cartMap.put("title", detailsTitle.getText().toString());
        cartMap.put("manufacturer", detailsManufacturer.getText().toString());
        cartMap.put("price", detailsPrice.getText().toString());
        cartMap.put("quantity", elegantNumberButton.getNumber());
        cartMap.put("shipmentStatus", "Order Not Shipped");

        cartListReference
                .child("User View")
                .child(Common.currentUser.getPhone())
                .child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            //Add to Admin View DB - for Admin to see
                            cartListReference
                                    .child("Admin View")
                                    .child(Common.currentUser.getPhone())
                                    .child("Products")
                                    .child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProductDetailsActivity.this, "Added to cart.", Toast.LENGTH_LONG).show();
                                            //Go to Cart
                                            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                                            intent.putExtra("productID", productID);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    }
                });

    }
}
