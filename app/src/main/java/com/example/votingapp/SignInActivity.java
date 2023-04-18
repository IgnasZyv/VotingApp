package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText mEmail, mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Log.d(TAG, "onCreate: ");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);

        Button submitButton = findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v -> {
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(this, GroupActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear the back stack
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

}
