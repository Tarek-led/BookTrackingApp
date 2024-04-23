package com.example.bookstoreapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); 

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        EditText usernameEditText = findViewById(R.id.username); 
        EditText passwordEditText = findViewById(R.id.password);
        Button signUpButton = findViewById(R.id.btn_signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String usernameTxt = usernameEditText.getText().toString();
                final String passwordTxt = passwordEditText.getText().toString();

                if (usernameTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child(usernameTxt).child("username").push().setValue(usernameTxt);
                    databaseReference.child(usernameTxt).child("password").push().setValue(passwordTxt);
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                }
            }
        });
    }
}