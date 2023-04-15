package com.example.votingapp;

import android.widget.ProgressBar;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Answer implements Serializable {
    private String mAnswerTitle;
    private String mDecryptedAnswerTitle;
    private String mVotes;
    private String mDecryptedVotes;
    private String mId;
    private boolean isChecked;
    private ArrayList<String> mVoters;
    private ArrayList<String> mDecryptedVoters;
    private ProgressBar mProgressBar;
    @Exclude
    private String mGroupEncryptionKey;

    public Answer(){}

    public Answer(String answerTitle, String groupEncryptionKey) {
        this.mAnswerTitle = EncryptionUtils.encrypt(answerTitle, groupEncryptionKey);
        this.isChecked = false;
        this.mVotes = Objects.requireNonNull(EncryptionUtils.encrypt("0", groupEncryptionKey));
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
    public String getVotes() {
        return mVotes;
    }

    @PropertyName("votes")
    public void setVotes(String votes) {
        mVotes = votes;
    }

    @Exclude
    public String getDecryptedVotes() {
        // If the decrypted votes are null or the decrypted votes are not equal to the encrypted votes
        String decryptedVotes = EncryptionUtils.decrypt(mVotes, mGroupEncryptionKey);
        if (mDecryptedVotes == null || !Objects.equals(decryptedVotes, mDecryptedVotes)) {
            if (decryptedVotes != null && !decryptedVotes.isEmpty()) {
                this.mDecryptedVotes = decryptedVotes;
            }
        }
        return mDecryptedVotes;
    }

    public void incrementVotes() {
        mVotes = EncryptionUtils.encrypt(String.valueOf(Integer.parseInt(getDecryptedVotes()) + 1), mGroupEncryptionKey);
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
        String encryptedVoter = EncryptionUtils.encrypt(voter, mGroupEncryptionKey);
        mVoters.add(encryptedVoter);
    }

    public ArrayList<String> getDecryptedVoters() {
        mDecryptedVoters = new ArrayList<>();
        if (mVoters != null) {
            for (String voter : mVoters) {
                String decryptedVoter = EncryptionUtils.decrypt(voter, mGroupEncryptionKey);
                mDecryptedVoters.add(decryptedVoter);
            }
        }
        return mDecryptedVoters;
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

    @Exclude
    public String getGroupEncryptionKey() {
        return mGroupEncryptionKey;
    }
    @Exclude
    public void setGroupEncryptionKey(String key) {
        mGroupEncryptionKey = key;
    }

    @Exclude
    public int getVotesAsInt() {
        return Integer.parseInt(getDecryptedVotes());
    }
}


