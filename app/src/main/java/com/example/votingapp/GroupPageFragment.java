package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GroupPageFragment extends Fragment {

    private RecyclerView mQuestionRecyclerView;
    private LinearLayout mCreateQuestionLayout;
    private Button mCreateQuestionButton;
    private LinearLayout mAddAnswerLayout;
    private EditText mQuestionTitle;
    private QuestionAdapter mAdapter;

    private DAOQuestion mDAOQuestion;

    public GroupPageFragment() {
        super(R.layout.fragment_group_page);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_group_page, container, false);

        mDAOQuestion = new DAOQuestion();

        mQuestionRecyclerView = v.findViewById(R.id.rv_questions);
        mQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCreateQuestionLayout = v.findViewById(R.id.ll_create_question);
        ImageButton createQuestionButton = v.findViewById(R.id.ib_create_question_layout);
        ImageButton addAnwswerButton = v.findViewById(R.id.ib_add_answer);
        mAddAnswerLayout = v.findViewById(R.id.ll_choice_layout);
        mQuestionTitle = v.findViewById(R.id.et_question_title);
        mCreateQuestionButton = v.findViewById(R.id.btn_create_question);

        mCreateQuestionButton.setOnClickListener(v3 -> {
            createQuestion();
            updateUi();
            createQuestionLayout();
        });

        createQuestionButton.setOnClickListener(v1 -> {
            createQuestionLayout();
        });

        addAnwswerButton.setOnClickListener(v2 -> {
            final EditText editText = new EditText(getActivity());
            editText.setId(View.generateViewId());
            editText.setHint("Choice");
            editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            editText.setPadding(20, 20, 20, 20);

            if (mAddAnswerLayout != null) {
                mAddAnswerLayout.addView(editText);
            }
        });

//        updateUi();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateUi();
    }

    private void updateUi() {
        Bundle bundle = getArguments();
        assert bundle != null;
        Group group = (Group) bundle.getSerializable("group");
        QuestionLab questionLab = QuestionLab.get(getActivity());

//        List<Question> questions = group.getQuestions();
        List<Question> questions = group.getQuestions();


        if (mAdapter == null) {
            mAdapter = new QuestionAdapter(questions);
            mQuestionRecyclerView.setAdapter(mAdapter);
        } else {
//            mAdapter.notifyItemInserted(questions.size() - 1);
        }
    }

    private void createQuestionLayout() {
        if (mCreateQuestionLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(mCreateQuestionLayout, new AutoTransition());
            mCreateQuestionLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(mCreateQuestionLayout, new AutoTransition());
            mCreateQuestionLayout.setVisibility(View.GONE);
        }
    }

    private void createQuestion() {

        ArrayList<Answer> choices = new ArrayList<>();

        Bundle bundle = getArguments();
        assert bundle != null;
        Group group = (Group) bundle.getSerializable("group");

        String questionTitle = mQuestionTitle.getText().toString();

        for (int i = 0; i < mAddAnswerLayout.getChildCount(); i++) {
            View view = mAddAnswerLayout.getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                Answer answer = new Answer(editText.getText().toString());
                choices.add(answer);
            }
        }



        Question question = new Question(questionTitle, choices);

//        mDAOQuestion.get().child(Group.class.getSimpleName()).child(group.getId()).setValue(question).addOnSuccessListener(aVoid -> {
//            Toast.makeText(getActivity(), "Question created", Toast.LENGTH_SHORT).show();
//            group.addQuestion(question);
//        }).addOnFailureListener(e -> {
//            Toast.makeText(getActivity(), "Question not created", Toast.LENGTH_SHORT).show();
//        });

        mDAOQuestion.add(question, group.getId()).addOnSuccessListener(success -> {
            Log.w("CreateQuestionFragment", "Question created successfully");
        }).addOnFailureListener(failure -> {
            Log.w("CreateQuestionFragment", "Question creation failed");
        });


//        group.addQuestion(question);
//        Group group = new Group(groupName.getText().toString());
//        daoGroup.add(group).addOnSuccessListener(success -> {
//            Log.w("CreateGroupFragment", "Group created successfully");
//        }).addOnFailureListener(failure -> {
//            Log.w("CreateGroupFragment", "Group creation failed");
//        });
    }


    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mQuestionTitleTextView;
        private final TextView mQuestionDateTextView;
        private final RecyclerView mAnswerRecyclerView;
        private final Button mDetailButton;

        private final ImageView mExpandLayoutImage;
        private final ConstraintLayout mExpandLayout;


        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.question_list_item, parent, false));

            mQuestionTitleTextView = itemView.findViewById(R.id.tv_item_title);
            mQuestionDateTextView = itemView.findViewById(R.id.tv_item_date);
            mAnswerRecyclerView = itemView.findViewById(R.id.rv_item_answers);
            mDetailButton = itemView.findViewById(R.id.btn_detail);

            mExpandLayout = itemView.findViewById(R.id.constraint_layout);
            mExpandLayoutImage = itemView.findViewById(R.id.iv_arrow);

        }


        @Override
        public void onClick(View v) {
            expandableLayout();
        }

        public void bind(Question question) {
            mQuestionTitleTextView.setText(question.getTitle());
            mQuestionDateTextView.setText(question.getDate().toString());

//            boolean isExpanded = question.isExpanded();
//            mExpandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mAnswerRecyclerView.setLayoutManager(layoutManager);
            mAnswerRecyclerView.setHasFixedSize(true);

            ArrayList<Answer> answers = new ArrayList<>(question.getAnswers());

            AnswerListAdapter answerListAdapter = new AnswerListAdapter(answers, getContext());
            mAnswerRecyclerView.setAdapter(answerListAdapter);

            mDetailButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", question.getId());
                bundle.putInt("position", getAdapterPosition());
                bundle.putSerializable("answers", answers);
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });

            mExpandLayout.setVisibility(View.GONE);

            expandableLayout();

        }


        private void expandableLayout() {
            mExpandLayoutImage.setOnClickListener(v -> {
                if (mExpandLayout.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(mExpandLayout, new AutoTransition());
                    mExpandLayout.setVisibility(View.VISIBLE);
                    mExpandLayoutImage.setImageResource(R.drawable.ic_baseline_expand_less_24);
                } else {
                    TransitionManager.beginDelayedTransition(mExpandLayout, new AutoTransition());
                    mExpandLayout.setVisibility(View.GONE);
                    mExpandLayoutImage.setImageResource(R.drawable.ic_baseline_expand_more_24);
                }
            });
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
            if (mQuestions == null) {
                return 0;
            } else {
                return mQuestions.size();
            }
        }
    }
}

