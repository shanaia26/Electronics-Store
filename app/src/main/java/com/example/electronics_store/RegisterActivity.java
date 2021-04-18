package com.example.electronics_store;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.electronics_store.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerName;
    private EditText registerEmail;
    private EditText registerPhone;
    private EditText registerPassword;
    private Button registerButton;
    private TextView loginUser;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.register_name);
        registerEmail = findViewById(R.id.register_email);
        registerPhone = findViewById(R.id.register_phone);
        registerPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register_button);
        loginUser = findViewById(R.id.login_user);

        progressDialog = new ProgressDialog(this);

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }

            private void CreateAccount() {
                String name = registerName.getText().toString().trim();
                String email = registerEmail.getText().toString().trim();
                String phone = registerPhone.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, Common.EmptyCredentialsKey, Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setTitle("Register Account");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    ValidatePhone(name, email, phone, password);
                }
            }

            private void ValidatePhone(String name, String email, String phone, String password) {
                final DatabaseReference rootReference;
                rootReference = FirebaseDatabase.getInstance().getReference();

                rootReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!(snapshot.child("Users").child(phone).exists())) {
                            HashMap<String, Object> userDataMap = new HashMap<>();
                            userDataMap.put("name", name);
                            userDataMap.put("email", email);
                            userDataMap.put("phone", phone);
                            userDataMap.put("password", password);

                            rootReference.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, Common.RegisterSuccessKey, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, Common.ErrorKey, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, Common.NumberRegisteredAlreadyKey, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, Common.RegisterFailKey, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
    }
}