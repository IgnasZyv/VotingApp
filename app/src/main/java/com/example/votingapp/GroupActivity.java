package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class GroupActivity extends AppCompatActivity {

    public static String groupId;


    public GroupActivity() {
        super(R.layout.activity_fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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
