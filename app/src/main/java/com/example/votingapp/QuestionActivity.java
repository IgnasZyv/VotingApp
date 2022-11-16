package com.example.votingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class QuestionActivity extends AppCompatActivity {

    public QuestionActivity() {
        super(R.layout.activity_fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, QuestionFragment.class, null)
                    .commit();
        }
    }
}