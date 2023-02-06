package com.example.votingapp;

import java.io.Serializable;
import java.util.UUID;

public class Answer implements Serializable {
    private String mAnswerTitle;
    private int mVotes;
    private String mId;
    private boolean isChecked;

    public Answer(){}

    public Answer(String answerTitle) {
        this.mAnswerTitle = answerTitle;
        this.isChecked = false;
        this.mVotes = 0;
        this.mId = UUID.randomUUID().toString();
    }

    public String getAnswerTitle() {
        return mAnswerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        mAnswerTitle = answerTitle;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getVotes() {
        return mVotes;
    }

    public void setVotes(int votes) {
        mVotes = votes;
    }

    public void incrementVotes() {
        mVotes++;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
