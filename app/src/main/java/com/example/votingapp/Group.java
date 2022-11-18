package com.example.votingapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group {

    UUID mGroupId;
    private String mName;
    private ArrayList<String> mMembers;
    private List<Question> mQuestions;

    public Group(String name) {
        mName = name;
    }

    public UUID getGroupId() {
        return mGroupId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<String> getMembers() {
        return mMembers;
    }

    public void setMembers(ArrayList<String> members) {
        mMembers = members;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }
}
