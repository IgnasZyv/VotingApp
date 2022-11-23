package com.example.votingapp;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Question {
    private String mTitle;
    private boolean isSelected;
    private Date date;
    private List<Answer> mAnswers;


    private String mId;

    public Question(){}

    public Question(String title, List<Answer> answers) {
        this.mTitle = title;
        this.mAnswers = answers;
        this.mId = UUID.randomUUID().toString();
        this.date = new Date();
        this.isSelected = false;
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
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
