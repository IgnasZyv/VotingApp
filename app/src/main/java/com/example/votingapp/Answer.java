package com.example.votingapp;

public class Answer {
    private String mAnswerTitle;
    private int mAnswerCount;

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

}
