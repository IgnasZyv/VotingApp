package com.example.votingapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group implements Serializable {

    private String mName;
    private String mId;
    private ArrayList<String> mMembers;
    private ArrayList<String> mAdministrators;
    private ArrayList<Question> mQuestions;

    public Group(){}

    public Group(String name) {
        this.mName = name;
        this.mId = UUID.randomUUID().toString();
        this.mMembers = new ArrayList<>();
        this.mAdministrators = new ArrayList<>();
        this.mQuestions = new ArrayList<>();
    }
    @PropertyName("name")
    public String getName() {
        return mName;
    }

    @PropertyName("name")
    public void setName(String name) {
        mName = name;
    }

    @PropertyName("id")
    public String getId() {
        return mId;
    }

    @PropertyName("id")
    public void setId(String id) {
        mId = id;
    }

    @PropertyName("members")
    public ArrayList<String> getMembers() {
        return mMembers;
    }

    @PropertyName("members")
    public void setMembers(ArrayList<String> members) {
        mMembers = members;
    }

    @PropertyName("administrators")
    public ArrayList<String> getAdministrators() {
        return mAdministrators;
    }

    @PropertyName("administrators")
    public void setAdministrators(ArrayList<String> administrators) {
        mAdministrators = administrators;
    }

    public void addAdministrator(String administrator) {
        mAdministrators.add(administrator);
    }

    @PropertyName("questions")
    public ArrayList<Question> getQuestions() {
        return mQuestions;
    }

    @PropertyName("questions")
    public void setQuestions(ArrayList<Question> questions) {
        mQuestions = questions;
    }

    public void addQuestion(Question question) {
        if (mQuestions == null) {
            mQuestions = new ArrayList<>();
        }
        mQuestions.add(question);
    }

    public Question getQuestion(String id) {
        for (Question question : mQuestions) {
            if (question.getId().equals(id)) {
                return question;
            }
        }
        return null;
    }
}
