package com.example.votingapp;

import static android.icu.text.DateFormat.getDateInstance;
import static android.icu.text.DateFormat.getDateTimeInstance;

import android.os.Build;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Question implements Serializable {
    private String mTitle;
    private boolean isSelected;
    private Date mDate;
    private String mFormattedDateString;
    private List<Answer> mAnswers;
    private String mId;
    private Boolean mIsDisabled;
    private String mGroupId;
    private int mVoteCount;

    public Question(){}

    public Question(String title, List<Answer> answers, String groupId) {
        this.mTitle = title;
        this.mAnswers = answers;
        this.mId = UUID.randomUUID().toString();
        this.mDate = new Date();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.mFormattedDateString = String.valueOf(getDateTimeInstance().format(mDate));
        }
        this.isSelected = false;
        this.mGroupId = groupId;
    }

    @PropertyName("id")
    public String getId() {
        return mId;
    }

    @PropertyName("id")
    public void setId(String id) {
        mId = id;
    }

    @PropertyName("groupId")
    public String getGroupId() {
        return mGroupId;
    }

    @PropertyName("groupId")
    public void setGroupId(String groupId) {
        mGroupId = groupId;
    }

    @PropertyName("title")
    public String getTitle() {
        return mTitle;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        mTitle = title;
    }

    @PropertyName("date")
    public String getFormattedDateString() {
        return mFormattedDateString;
    }

    @PropertyName("date")
    public void setFormattedDateString(String dateString) {
        this.mFormattedDateString = dateString;
    }

    @Exclude
    public Date getDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mFormattedDateString = getDateTimeInstance().format(mDate);
        }
        return mDate;
    }

    @Exclude
    public void setDate(Date date) {
        this.mDate = date;
    }

    @PropertyName("answers")
    public List<Answer> getAnswers() {
        return mAnswers;
    }

    @PropertyName("answers")
    public void setAnswers(List<Answer> answers) {
        mAnswers = answers;
    }

    @Exclude
    public Answer getAnswer(int index) {
        return mAnswers.get(index);
    }

    @Exclude
    public void setDisabled(Boolean disabled) {
        mIsDisabled = disabled;
    }

    @PropertyName("votes")
    public int getVoteCount() {
        int count = 0;
        for (Answer answer : mAnswers) {
            count += answer.getVotes();
        }
        return count;
    }

    @PropertyName("votes")
    public void setVoteCount(int votes) {
        mVoteCount = votes;
    }
}
