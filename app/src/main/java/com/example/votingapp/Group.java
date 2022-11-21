package com.example.votingapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group{

    private String mName;
    private ArrayList<String> mMembers;
    private List<Question> mQuestions;

    public Group(){}

    public Group(String name) {
//        this.mQuestions = new ArrayList<>();
        this.mName = name;
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

    public void addQuestion(Question question) {
        mQuestions.add(question);
    }
}
