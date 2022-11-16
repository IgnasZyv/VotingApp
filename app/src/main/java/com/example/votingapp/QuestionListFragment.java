package com.example.votingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionListFragment extends Fragment {

    private RecyclerView mQuestionRecyclerView;
    private RecyclerView mAnswerRecyclerView;
    private QuestionAdapter mAdapter;
    public Context mContext;

    public QuestionListFragment() {
        super(R.layout.fragment_question_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_question_list, container, false);
        View v2 = inflater.inflate(R.layout.question_list_item, container, false);

        mQuestionRecyclerView = v.findViewById(R.id.rv_questions);
        mQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        updateUi();

        return v;
    }

    private void updateUi() {
        QuestionLab questionLab = QuestionLab.get(getActivity());
        List<Question> questions = questionLab.getQuestions();

        mAdapter = new QuestionAdapter(questions);
        mQuestionRecyclerView.setAdapter(mAdapter);
    }

    private class QuestionHolder extends RecyclerView.ViewHolder {

        private final TextView mQuestionTitleTextView;
        private final TextView mQuestionDateTextView;
        private RelativeLayout mExpandableLayout;
        private RecyclerView mAnswerRecyclerView;
        private ConstraintLayout mConstraintLayout;
        private List<Question> mAnswers;

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.question_list_item, parent, false));

            mQuestionTitleTextView = itemView.findViewById(R.id.tv_item_title);
            mQuestionDateTextView = itemView.findViewById(R.id.tv_item_date);

            mExpandableLayout = itemView.findViewById(R.id.rl_recycler_view);
            mAnswerRecyclerView = itemView.findViewById(R.id.rv_item_answers);
            mConstraintLayout = itemView.findViewById(R.id.constraint_layout);
        }

        public void bind(Question question) {
            mQuestionTitleTextView.setText(question.getTitle());
            mQuestionDateTextView.setText(question.getDate().toString());

//            boolean isExpanded = question.isExpanded();
//            mExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mAnswerRecyclerView.setLayoutManager(layoutManager);
            mAnswerRecyclerView.setHasFixedSize(true);

            ArrayList<Answer> answers = new ArrayList<>();
            answers.add(new Answer("Answer 1"));
            answers.add(new Answer("Answer 2"));
            answers.add(new Answer("Answer 3"));

            AnswerListAdapter answerListAdapter = new AnswerListAdapter(answers, getContext());
            mAnswerRecyclerView.setAdapter(answerListAdapter);

        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        private final List<Question> mQuestions;

        public QuestionAdapter(List<Question> questions) {
            mQuestions = questions;
        }

        @NonNull
        @Override
        public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new QuestionHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
            Question question = mQuestions.get(position);
            holder.bind(question);
        }

        @Override
        public int getItemCount() {
            return mQuestions.size();
        }
    }
}

