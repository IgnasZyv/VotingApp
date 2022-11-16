package com.example.votingapp;

import java.util.UUID;

public class Answer {

    private String mAnswer;
    private UUID mAnswerId;
    private int mVotes;

    public Answer(String answer) {
        mAnswer = answer;
        mAnswerId = UUID.randomUUID();
        mVotes = 0;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public UUID getAnswerId() {
        return mAnswerId;
    }


    public int getVotes() {
        return mVotes;
    }

    public void setVotes(int votes) {
        mVotes = votes;
    }
}
