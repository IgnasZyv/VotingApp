package com.example.votingapp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Question {

    private final UUID mId;
    private String mTitle;
    private boolean isSelected;
    private Date date;
    private List<Answer> mAnswers;

    public Question(String title, List<Answer> answers) {
        mTitle = title;
        mAnswers = answers;
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


    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Answer> getAnswers() {
        return mAnswers;
    }

    public void setAnswers(List<Answer> answers) {
        mAnswers = answers;
    }



}
