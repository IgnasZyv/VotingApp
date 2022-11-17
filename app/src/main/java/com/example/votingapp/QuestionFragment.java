package com.example.votingapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class QuestionFragment extends Fragment {
    private Question mQuestion;
    private ListView mAnswerListView;
    private UUID mQuestionId;
    private List<Answer> mAnswers;

    public QuestionFragment() {
        super(R.layout.fragment_question);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        assert bundle != null;
        mQuestionId = (UUID) bundle.getSerializable("question");
        mAnswers = (List<Answer>) bundle.getSerializable("answers");
        mQuestion = QuestionLab.get(getActivity()).getQuestion(mQuestionId);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        TextView questionTitle = v.findViewById(R.id.tv_question_title);
        questionTitle.setText(mQuestion.getTitle());

        mAnswerListView = v.findViewById(R.id.lv_answers);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mAnswerListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice,
                    Arrays.stream(mAnswers.toArray()).map (
                        answer -> ((Answer) answer).getAnswerTitle())
                            .toArray(Object[]::new)));
        }
        
        mAnswerListView.setOnItemClickListener((parent, view, position, id) -> {
            mAnswerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mAnswerListView.setItemChecked(position, true);
        });

        Button submitButton = v.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(v1 -> {
            int position = mAnswerListView.getCheckedItemPosition();
            if (position != ListView.INVALID_POSITION) {
                String answer = mQuestion.getAnswers().get(position);
                mQuestion.setSelected(true);
                TextView result = v.findViewById(R.id.tv_submitted_answer);
                result.setText(answer);
            }
        });
        return v;
    }
}
