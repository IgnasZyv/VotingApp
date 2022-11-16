package com.example.votingapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionLab {
    private static QuestionLab sQuestionLab;

    private final List<Question> mQuestions;

    public static QuestionLab get(Context context) {
        if (sQuestionLab == null) {
            sQuestionLab = new QuestionLab(context);
        }
        return sQuestionLab;
    }

    private QuestionLab(Context context) {
        mQuestions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Question question = new Question();
            question.setTitle("Question #" + i);
            mQuestions.add(question);
        }
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public Question getQuestion(UUID id) {
        for (Question question : mQuestions) {
            if (question.getId().equals(id)) {
                return question;
            }
        }
        return null;
    }

}
