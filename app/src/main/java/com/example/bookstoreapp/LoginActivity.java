package com.example.bookstoreapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText inputUsername, inputPassword;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("users"); // Updated reference

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String usernameTxt = inputUsername.getText().toString();
                final String passwordTxt = inputPassword.getText().toString();

                if (usernameTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your username or password", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child(usernameTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Inside the onDataChange method of LoginActivity
                            if (snapshot.exists()) {
                                boolean found = false;
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    if (child.getKey().equals("password")) {
                                        for (DataSnapshot pwdSnapshot : child.getChildren()) {
                                            final String getPassword = pwdSnapshot.getValue(String.class);
                                            if (getPassword.equals(passwordTxt)) {
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (found) break;
                                }

                                if (found) {
                                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    public void signUpClick(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
