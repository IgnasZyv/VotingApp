package com.example.votingapp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOAnswer {
    private final DatabaseReference mDatabase;

    public DAOAnswer() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/");
        mDatabase = db.getReference(Group.class.getSimpleName());
    }

    public Task<Void> add(Answer answer) {
        return mDatabase.push().setValue(answer);
    }

    public Task<Void> update(Answer answer, Question question, Group group) {
        return mDatabase.child(group.getId()).child(question.getId()).child(answer.getId()).setValue(answer);
    }

}
