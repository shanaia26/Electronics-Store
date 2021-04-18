package com.example.electronics_store;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronics_store.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {
    private EditText shipmentName;
    private EditText shipmentPhone;
    private EditText shipmentAddress;
    private TextView subtotalPrice;
    private TextView totalPrice;
    private Button stripeButton;

    private String saveCurrentDate;
    private String saveCurrentTime;
    private String orderID;

    private String totalAmount;
    private String productID;
    private String subtotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        shipmentName = findViewById(R.id.shipment_name);
        shipmentPhone = findViewById(R.id.shipment_phone);
        shipmentAddress = findViewById(R.id.shipment_address);
        subtotalPrice = findViewById(R.id.subtotal_price);
        totalPrice = findViewById(R.id.total_price);
        stripeButton = findViewById(R.id.stripe_button);

        productID = getIntent().getStringExtra("productID");
        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price: €" + totalAmount, Toast.LENGTH_SHORT).show();

        subtotalPrice.setText(totalAmount);
        totalPrice.setText(totalAmount);

        stripeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    private void Check() {
        String aShipmentName = shipmentName.getText().toString().trim();
        String aShipmentPhone = shipmentPhone.getText().toString().trim();
        String aShipmentAddress = shipmentAddress.getText().toString().trim();

        if (TextUtils.isEmpty(aShipmentName) || TextUtils.isEmpty(aShipmentPhone) ||
                TextUtils.isEmpty(aShipmentAddress)) {
            Toast.makeText(CheckoutActivity.this, Common.EmptyCredentialsKey, Toast.LENGTH_LONG).show();
        }else{
            ConfirmOrder(aShipmentName, aShipmentPhone, aShipmentAddress);
        }
    }

    private void ConfirmOrder(String aShipmentName, String aShipmentPhone, String aShipmentAddress) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        //Creates a unique key
        orderID = saveCurrentDate + saveCurrentTime;

        final DatabaseReference orderHistoryReference = FirebaseDatabase.getInstance().getReference()
                .child("Order History")
                .child(Common.currentUser.getPhone())
                .child(orderID);

        final DatabaseReference adminOrderReference = FirebaseDatabase.getInstance().getReference()
                .child("Admin Orders")
                .child(Common.currentUser.getPhone());

        final DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(Common.currentUser.getPhone())
                .child("Products");

        String shipmentStatus = "Order Not Shipped";
        String paymentStatus = "Payment Pending";

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderID", orderID);
        orderMap.put("shipmentName", aShipmentName);
        orderMap.put("phone", aShipmentPhone);
        orderMap.put("address", aShipmentAddress);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("shipmentStatus", shipmentStatus);
        orderMap.put("paymentStatus", paymentStatus);

        orderHistoryReference
                .updateChildren(orderMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        adminOrderReference
                                .updateChildren(orderMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Add details for Admin DB
                                            HashMap<String, Object> updateOrderID = new HashMap<String, Object>();
                                            String newOrderID = orderID;
                                            String oldOrderID = "Not Available";

                                            //Update Admin View Map too - Add Order ID to products ordered
                                            adminReference
                                                    .orderByChild("orderID")
                                                    .equalTo(oldOrderID)
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                    updateOrderID.put(childSnapshot.getKey() + "/orderID", newOrderID);
                                                                }

                                                                adminReference.updateChildren(updateOrderID);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                            Intent intent = new Intent(CheckoutActivity.this, StripePaymentActivity.class);
                                            intent.putExtra("orderID", orderID);
                                            intent.putExtra("productID", productID);
                                            intent.putExtra("totalAmount", totalAmount);
                                            startActivity(intent);
                                        }
                                    }
                                });
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}