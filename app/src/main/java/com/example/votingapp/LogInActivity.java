package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            Button signInButton = findViewById(R.id.button_login);
            Button signUpButton = findViewById(R.id.button_register);

            signInButton.setOnClickListener(v -> {
                Intent intent = new Intent(LogInActivity.this, SignInActivity.class);
                startActivity(intent);
            });

            signUpButton.setOnClickListener(v -> {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            });
        }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) { // user is already logged in so go to the recipe activity
            Intent intent = new Intent(this, GroupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear the back stack
            startActivity(intent);
        }
    }
}
