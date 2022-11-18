package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GroupActivity extends AppCompatActivity {

    public GroupActivity() {
        super(R.layout.activity_fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        intent.getStringExtra("create_group");

        if (savedInstanceState == null && !Objects.equals(intent.getStringExtra("create_group"), "create_group")) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, GroupFragment.class, null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, CreateGroupFragment.class, null)
                    .commit();
        }
    }


}
