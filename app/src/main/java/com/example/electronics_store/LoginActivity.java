package com.example.electronics_store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.electronics_store.Admin.AdminMainActivity;
import com.example.electronics_store.Common.Common;
import com.example.electronics_store.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText loginPhone;
    private EditText loginPassword;
    private TextView registerUser;
    private Button loginButton;

    private TextView adminLink;
    private TextView notAdminLink;

    private ProgressDialog progressDialog;

    private String parentDBName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginPhone = findViewById(R.id.login_phone);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        registerUser = findViewById(R.id.register_user);

        adminLink = findViewById(R.id.admin_panel_link);
        notAdminLink = findViewById(R.id.not_admin_panel_link);

        progressDialog = new ProgressDialog(this);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDBName = "Admin";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
            }
        });


    }

    private void LoginUser() {
        String uPhone = loginPhone.getText().toString().trim();
        String uPassword = loginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(uPhone) || TextUtils.isEmpty(uPassword)) {
            Toast.makeText(LoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
        } else if (loginPassword.length() < 6) {
            Toast.makeText(LoginActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
        } else {
            //Allow user to log in
            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AllowAccessToAccount(uPhone, uPassword);
        }
    }

    private void AllowAccessToAccount(String phone, String password) {
        final DatabaseReference rootReference;
        rootReference = FirebaseDatabase.getInstance().getReference();

        rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDBName).child(phone).exists()) {
                    Users userData = snapshot.child(parentDBName).child(phone).getValue(Users.class);

                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {
                            if(parentDBName.equals("Admin")){
                                Toast.makeText(LoginActivity.this, "Admin logged in successfully.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else if(parentDBName.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Common.currentUser = userData;
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " does not exist", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}