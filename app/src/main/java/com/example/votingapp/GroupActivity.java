package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
//import com.google.firebase.appcheck.FirebaseAppCheck;
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GroupActivity extends AppCompatActivity {

    public static String groupId;


    public GroupActivity() {
        super(R.layout.activity_fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FirebaseApp.initializeApp(/*context=*/ this);
//        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory(
//                SafetyNetAppCheckProviderFactory.getInstance());

//        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/");
//        DatabaseReference myRef = database.getReference("message");
//
//
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = Objects.requireNonNull(snapshot.getValue()).toString();
//                Log.w("GroupActivity", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
//                Log.w("myRef", "Failed to read value.", error.toException());
//            }
//        });

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        groupId = intent.getStringExtra("groupId");
//        bundle.putSerializable("mUser", (Serializable) mUser);

        if (intent.getSerializableExtra("group") != null) {
            Group group = (Group) intent.getSerializableExtra("group");
            Objects.requireNonNull(getSupportActionBar()).setTitle(group.getName());
            bundle.putSerializable("group", group);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, GroupFragment.class, bundle)
                    .commit();
        }

        if (Objects.equals(intent.getStringExtra("create_group"), "create_group")) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, CreateGroupFragment.class, null)
                    .commit();
        }

        if (Objects.equals(intent.getStringExtra("group_page"), "group_page")) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, GroupPageFragment.class, bundle)
                    .commit();
        }
    }
}
