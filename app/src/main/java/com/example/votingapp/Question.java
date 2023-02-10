package com.example.votingapp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Question implements Serializable {
    private String mTitle;
    private boolean isSelected;
    private Date date;
    private List<Answer> mAnswers;
    private String mId;
    private Boolean mIsDisabled;

    private String mGroupId;

    public Question(){}

    public Question(String title, List<Answer> answers, String groupId) {
        this.mTitle = title;
        this.mAnswers = answers;
        this.mId = UUID.randomUUID().toString();
        this.date = new Date();
        this.isSelected = false;
        this.mGroupId = groupId;
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String groupId) {
        mGroupId = groupId;
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

    public void setDisabled(Boolean disabled) {
        mIsDisabled = disabled;
    }

    public int getVoteCount() {
        int count = 0;
        for (Answer answer : mAnswers) {
            count += answer.getVotes();
        }
        return count;
    }
}
