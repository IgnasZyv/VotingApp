package com.example.votingapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Objects;

public class DAOGroup {
    private final DatabaseReference mDatabase;

    public DAOGroup() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mDatabase = db.getReference(Group.class.getSimpleName()).child(Objects.requireNonNull(auth.getUid()));
    }

    public Task<Void> add(Group group) {
        return mDatabase.child(group.getId()).setValue(group);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return mDatabase.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return mDatabase.child(key).removeValue();
    }

    public Query get() {
        return mDatabase.orderByKey();
    }
}
