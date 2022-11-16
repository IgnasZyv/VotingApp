package com.example.votingapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Question {
    private final UUID mId;
    private String mTitle;
    private boolean isSelected;
    private Date date;
    private List<String> mAnswers;
    private boolean isExpanded;

    public Question() {
        mAnswers = new ArrayList<>();
        mId = UUID.randomUUID();
        date = new Date();
        isSelected = false;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getAnswers() {
        return mAnswers;
    }

    public void setAnswers(List<String> answers) {
        mAnswers = answers;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

}
