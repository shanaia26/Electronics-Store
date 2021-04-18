package com.example.electronics_store.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.electronics_store.Common.Common;
import com.example.electronics_store.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProductActivity extends AppCompatActivity {
    private EditText newProductTitle;
    private EditText newProductManufacturer;
    private EditText newProductPrice;
    private EditText newProductQuantity;
    private ImageView newProductImage;
    private Button addProductButton;

    private String categoryName;
    private String productTitle;
    private String productManufacturer;
    private String productPrice;
    private String productQuantity;

    private String saveCurrentDate;
    private String saveCurrentTime;
    private String productID;

    private String downloadImageURL;
    private static final int GalleryPick = 1;
    private Uri imageURI;

    private StorageReference productImagesReference;
    private DatabaseReference productReference;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        newProductTitle = findViewById(R.id.new_product_title);
        newProductManufacturer = findViewById(R.id.new_product_manufacturer);
        newProductPrice = findViewById(R.id.new_product_price);
        newProductQuantity = findViewById(R.id.new_product_quantity);
        newProductImage = findViewById(R.id.new_product_image);
        addProductButton = findViewById(R.id.add_product_button);

        progressDialog = new ProgressDialog(this);

        categoryName = getIntent().getExtras().get("category").toString();
        productImagesReference = FirebaseStorage.getInstance().getReference().child("Product Images");
        productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        newProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    //Getting the image user added and adding to database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            //Display Image
            newProductImage.setImageURI(imageURI);
        }
    }

    private void ValidateProductData() {
        productTitle = newProductTitle.getText().toString().trim();
        productManufacturer = newProductManufacturer.getText().toString().trim();
        productPrice = newProductPrice.getText().toString().trim();
        productQuantity = newProductQuantity.getText().toString().trim();

        //Check if user added an image
        if (imageURI == null) {
            Toast.makeText(this, Common.ImageRequiredKey, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(productTitle) || TextUtils.isEmpty(productManufacturer) || TextUtils.isEmpty(productPrice) || TextUtils.isEmpty(productQuantity)) {
            Toast.makeText(this, Common.EmptyCredentialsKey, Toast.LENGTH_LONG).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        progressDialog.setTitle("Add New Product");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        //Time & Date user added new product
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Creates a unique key
        productID = saveCurrentDate + saveCurrentTime;

        //Unique Key for storing image
        StorageReference filePath = productImagesReference.child(imageURI.getLastPathSegment() + productID + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, Common.ImageUploadSuccessKey, Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        //Retrieving the image URI
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            //Getting image link
                            downloadImageURL = task.getResult().toString();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productID", productID);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("title", productTitle);
        productMap.put("manufacturer", productManufacturer);
        productMap.put("price", productPrice);
        productMap.put("quantity", productQuantity);
        productMap.put("image", downloadImageURL);
        productMap.put("category", categoryName);

        productReference.child(productID).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AdminAddProductActivity.this, AdminMainActivity.class);
                    startActivity(intent);

                    progressDialog.dismiss();
                    Toast.makeText(AdminAddProductActivity.this, Common.ProductAddedSuccessKey, Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminAddProductActivity.this, AdminMainActivity.class);
        startActivity(intent);
        finish();
    }
}