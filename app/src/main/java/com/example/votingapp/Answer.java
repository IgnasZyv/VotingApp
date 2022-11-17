package com.example.votingapp;

import java.io.Serializable;

public class Answer implements Serializable {
    private String mAnswerTitle;
    private int mAnswerCount;
    private boolean isChecked;

    public Answer(String answerTitle) {
        mAnswerTitle = answerTitle;
    }

    public String getAnswerTitle() {
        return mAnswerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        mAnswerTitle = answerTitle;
    }

    public int getAnswerCount() {
        return mAnswerCount;
    }

    public void setAnswerCount(int answerCount) {
        mAnswerCount = answerCount;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
