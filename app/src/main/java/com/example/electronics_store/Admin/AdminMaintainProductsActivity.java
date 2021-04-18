package com.example.electronics_store.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.electronics_store.Common.Common;
import com.example.electronics_store.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private EditText maintainTitle;
    private EditText maintainManufacturer;
    private EditText maintainPrice;
    private EditText maintainQuantity;

    private ImageView maintainImage;
    private Button applyChangesButton;
    private Button removeProductButton;

    //Get productID of item user clicked
    private String productID = "";

    private DatabaseReference productsReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("productID");
        productsReference = FirebaseDatabase.getInstance().getReference()
                .child("Products")
                .child(productID);

        maintainTitle = findViewById(R.id.maintain_product_title);
        maintainManufacturer = findViewById(R.id.maintain_product_manufacturer);
        maintainPrice = findViewById(R.id.maintain_product_price);
        maintainQuantity = findViewById(R.id.maintain_product_quantity);
        maintainImage = findViewById(R.id.maintain_product_image);
        applyChangesButton = findViewById(R.id.apply_changes_button);
        removeProductButton = findViewById(R.id.remove_product_button);

        displaySpecificProductInfo();

        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        removeProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeThisProduct();
            }
        });
    }

    private void removeThisProduct() {
        productsReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(AdminMaintainProductsActivity.this, Common.ItemRemovedSuccessKey, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void applyChanges() {
        String title = maintainTitle.getText().toString();
        String manufacturer = maintainManufacturer.getText().toString();
        String price = maintainPrice.getText().toString();
        String quantity = maintainQuantity.getText().toString();

        //Tell admin if anything is empty
        if(title.equals("") || price.equals("") || manufacturer.equals("") || quantity.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,
                    Common.EmptyCredentialsKey, Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("productID", productID);
            productMap.put("title", title);
            productMap.put("manufacturer", manufacturer);
            productMap.put("price", price);
            productMap.put("quantity", quantity);

            productsReference.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, Common.ChangesSuccessKey, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String title = snapshot.child("title").getValue().toString();
                    String manufacturer = snapshot.child("manufacturer").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();
                    String quantity = snapshot.child("quantity").getValue().toString();
                    String image = snapshot.child("image").getValue().toString();

                    maintainTitle.setText(title);
                    maintainManufacturer.setText(manufacturer);
                    maintainPrice.setText(price);
                    maintainQuantity.setText(quantity);
                    Picasso.get().load(image).into(maintainImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}