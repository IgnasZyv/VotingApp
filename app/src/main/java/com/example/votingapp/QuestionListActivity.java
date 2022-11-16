package com.example.votingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionListActivity extends AppCompatActivity {

        public QuestionListActivity() {
            super(R.layout.activity_fragment);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container, QuestionListFragment.class, null)
                        .commit();
            }
        }
}
