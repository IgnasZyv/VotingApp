package com.example.votingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionListFragment extends Fragment {

    private RecyclerView mQuestionRecyclerView;
    private QuestionAdapter mAdapter;

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

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.question_list_item, parent, false));

            mQuestionTitleTextView = itemView.findViewById(R.id.tv_item_title);
            mQuestionDateTextView = itemView.findViewById(R.id.tv_item_date);
//            mAnswerListView = itemView.findViewById(R.id.list_item_answers);
        }

        public void bind(Question question) {
            mQuestionTitleTextView.setText(question.getTitle());
            mQuestionDateTextView.setText(question.getDate().toString());

//            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice, mQuestion.getAnswers());
//
////            mAnswerListView.getLayoutParams().height = 100 * (mQuestion.getAnswers().size() + 5);
//            mAnswerListView.setAdapter(adapter);
//            mAnswerListView.requestLayout();
//
//
//
//            mAnswerListView.setOnItemClickListener((parent, view, position, id) -> {
//                mAnswerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//                mAnswerListView.setItemChecked(position, true);
//            });
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

