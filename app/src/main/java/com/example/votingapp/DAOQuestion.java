package com.example.votingapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOQuestion {
    private final DatabaseReference mDatabase;

    public DAOQuestion() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = db.getReference(Group.class.getSimpleName());
    }

    public Task<Void> add(Question question, String groupId) {
        return mDatabase.child(groupId).child(Question.class.getSimpleName()).child(question.getId()).setValue(question);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return mDatabase.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key) {
        return mDatabase.child(key).removeValue();
    }

    public DatabaseReference get() {
        return mDatabase;
    }
}
