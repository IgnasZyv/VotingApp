package com.example.votingapp;

import android.widget.ProgressBar;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Answer implements Serializable {
    private String mAnswerTitle;
    private String mDecryptedAnswerTitle;
    private int mVotes;
    private int mDecryptedVotes;
    private String mId;
    private boolean isChecked;
    private ArrayList<String> mVoters;
    private ProgressBar mProgressBar;
    private String mGroupEncryptionKey;

    public Answer(){}

    public Answer(String answerTitle, String groupEncryptionKey) {
        this.mAnswerTitle = EncryptionUtils.encrypt(answerTitle, groupEncryptionKey);
        this.isChecked = false;
        this.mVotes = 0;
        this.mId = UUID.randomUUID().toString();
        this.mVoters = new ArrayList<>();
    }

    @PropertyName("answerTitle")
    public String getAnswerTitle() {
        return mAnswerTitle;
    }

    @PropertyName("answerTitle")
    public void setAnswerTitle(String answerTitle) {
        mAnswerTitle = answerTitle;
    }

    @Exclude
    public String getDecryptedAnswerTitle() {
        if (mDecryptedAnswerTitle == null) {
            mDecryptedAnswerTitle = EncryptionUtils.decrypt(mAnswerTitle, mGroupEncryptionKey);
        }
        return mDecryptedAnswerTitle;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @PropertyName("votes")
    public int getVotes() {
        return mVotes;
    }

    @PropertyName("votes")
    public void setVotes(int votes) {
        mVotes = votes;
    }

    @Exclude
    public int getDecryptedVotes() {
        if (mDecryptedVotes == 0 && mVotes != 0) {
            String decryptedVotes = EncryptionUtils.decrypt(String.valueOf(mVotes), mGroupEncryptionKey);
            if (decryptedVotes != null && !decryptedVotes.isEmpty()) {
                mDecryptedVotes = Integer.parseInt(decryptedVotes);
            }
        }
        return mDecryptedVotes;
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

    public String getGroupEncryptionKey() {
        return mGroupEncryptionKey;
    }
    public void setGroupEncryptionKey(String key) {
        mGroupEncryptionKey = key;
    }


}


