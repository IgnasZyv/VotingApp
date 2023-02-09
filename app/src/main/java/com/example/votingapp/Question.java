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
    private Boolean mIsDisabled;

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

    public void setTitle(String title) {
        mTitle = title;
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

    public Answer getAnswer(int index) {
        return mAnswers.get(index);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Boolean getDisabled() {
        if (mIsDisabled == null) {
            mIsDisabled = false;
        }
        return mIsDisabled;
    }

    public void setDisabled(Boolean disabled) {
        mIsDisabled = disabled;
    }
}
