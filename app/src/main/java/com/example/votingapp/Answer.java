package com.example.votingapp;

import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Answer implements Serializable {
    private String mAnswerTitle;
    private int mVotes;
    private String mId;
    private boolean isChecked;
    private ArrayList<String> mVoters;
    private ProgressBar mProgressBar;

    public Answer(){}

    public Answer(String answerTitle) {
        this.mAnswerTitle = answerTitle;
        this.isChecked = false;
        this.mVotes = 0;
        this.mId = UUID.randomUUID().toString();
        this.mVoters = new ArrayList<>();
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

    public ArrayList<String> getVoters() {
        if (mVoters == null) {
            mVoters = new ArrayList<>();
        }
        return mVoters;
    }

    public void setVoters(ArrayList<String> voters) {
        mVoters = voters;
    }

    public void addVoter(String voter) {
        if (mVoters == null) {
            mVoters = new ArrayList<>();
        }
        mVoters.add(voter);
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }
}
