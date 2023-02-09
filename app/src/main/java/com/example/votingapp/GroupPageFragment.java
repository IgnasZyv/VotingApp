package com.example.votingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class GroupPageFragment extends Fragment {

    private RecyclerView mQuestionRecyclerView;
    private LinearLayout mCreateQuestionLayout;
    private Button mCreateQuestionButton;
    private LinearLayout mAddAnswerLayout;
    private EditText mQuestionTitle;
    private QuestionAdapter mAdapter;

    private DAOQuestion mDAOQuestion;
    private ValueEventListener mValueEventListener;
    private Group mGroup;
    private Boolean mIsAdmin;
    private Question mQuestion;

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
            if (!(mAddAnswerLayout.getChildCount() >= 2)) {
                Toast.makeText(getActivity(), "More than one answer required", Toast.LENGTH_SHORT).show();
            } else {
                if (createQuestion()) { // if question is created
                    mQuestionTitle.setText("");
                    mAddAnswerLayout.removeAllViews();
                    updateUi();
                    createQuestionLayout();
                }
            }
        });

        // create question layout
        createQuestionButton.setOnClickListener(v1 -> {
            createQuestionLayout();
            // if answer layout is empty add a row
            if (mAddAnswerLayout.getChildCount() == 0) {
                addAnswerRow();
            }
        });

        addAnwswerButton.setOnClickListener(v2 -> {
            addAnswerRow();
        });

        updateUi();

        // if user is admin, the button to create a question is visible
        if (mGroup.getMembers() == null ||
                !mGroup.getMembers().contains(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
            mIsAdmin = true;
            mCreateQuestionButton.setEnabled(true);
        } else {
            mIsAdmin = false;
            mCreateQuestionButton.setEnabled(false);
        }

        return v;
    }

    private void addAnswerRow() {
        // Create a new row to add an answer
        LinearLayout horizontalAnswerLayout = new LinearLayout(getActivity());
        horizontalAnswerLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalAnswerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        horizontalAnswerLayout.setPadding(20, 20, 20, 20);
        horizontalAnswerLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);

        float factor = getResources().getDisplayMetrics().density; // get the density factor

        // Create a new EditText
        final EditText editText = new EditText(getActivity());
        editText.setId(View.generateViewId());
        editText.setHint("Choice");
        int pxWidthText = (int) (280 * factor); // set the width of the EditText
        editText.setLayoutParams(new LinearLayout.LayoutParams(pxWidthText, LinearLayout.LayoutParams.WRAP_CONTENT));

        int pxWidthButton = (int)(48 * factor);
        int pxHeightButton = (int)(48 * factor);

        // Create a new remove button
        ImageButton btnDelete = new ImageButton(getContext());
        btnDelete.setId(View.generateViewId());
        btnDelete.setLayoutParams(new LinearLayout.LayoutParams(pxWidthButton, pxHeightButton));
        btnDelete.setImageResource(R.drawable.ic_remove);
        btnDelete.setOnClickListener(new View.OnClickListener() { // remove the row
            @Override
            public void onClick(View v) {
                mAddAnswerLayout.removeView(horizontalAnswerLayout);
            }
        });

        if (mAddAnswerLayout != null) { // add the row to the layout
            horizontalAnswerLayout.addView(editText);
            horizontalAnswerLayout.addView(btnDelete);
            mAddAnswerLayout.addView(horizontalAnswerLayout);
        }
    }

    private void updateUi() {

        Log.d("pageFragment", "updateUi: called");
        Bundle bundle = getArguments();
        assert bundle != null;
        mGroup = (Group) bundle.getSerializable("group");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            ChildEventListener childEventListener = new ChildEventListener() {
                ArrayList<Question> previousQuestions;
                int questionCount = 0;

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Question question = snapshot.getValue(Question.class);
                    if (previousQuestions == null || previousQuestions.size() == 0) {
                        previousQuestions = new ArrayList<>();
                        mAdapter = new QuestionAdapter(sortQuestions(previousQuestions));
                        mQuestionRecyclerView.setAdapter(mAdapter);
                    }

                    if (!previousQuestions.contains(question)) {
                        previousQuestions.add(question);
                        questionCount++;
                    }
                    Log.d("updateUI", "onChildAdded: " + questionCount);
                    Log.d("updateUI", "onChildAdded:  children count" + snapshot.getChildrenCount());

                    sortQuestions(previousQuestions);

                    mQuestionRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mAdapter.notifyItemInserted(previousQuestions.indexOf(question));
//                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Question question = snapshot.getValue(Question.class);
                    int index = previousQuestions.indexOf(question);
                    if (index != -1) {
                        previousQuestions.set(index, question);
                        mAdapter.notifyItemChanged(index);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Question question = snapshot.getValue(Question.class);
                    int index = previousQuestions.indexOf(question);
                    if (index != -1) {
                        previousQuestions.remove(index);
                        mAdapter.notifyItemRemoved(index);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("GroupPageFrag", "onCancelled: " + error.getMessage());
                }

            };

            mDAOQuestion.get(mGroup.getId()).addChildEventListener(childEventListener);
            }
        }

        private ArrayList<Question> sortQuestions(ArrayList<Question> questions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(questions, Comparator.comparing(Question::getDate));
            }
            return questions;
        }

    @Override
    public void onPause() {
        super.onPause();
        if (mValueEventListener != null) {
            mDAOQuestion.get(mGroup.getId()).removeEventListener(mValueEventListener);
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

    private Boolean createQuestion() {

        ArrayList<Answer> choices = new ArrayList<>();

        Bundle bundle = getArguments();
        assert bundle != null;
        Group group = (Group) bundle.getSerializable("group");

        String questionTitle = mQuestionTitle.getText().toString();
        if (questionTitle.isEmpty()) {
            Toast.makeText(getActivity(), "Question title is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < mAddAnswerLayout.getChildCount(); i++) {
            View view = mAddAnswerLayout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout answerLayout = (LinearLayout) view;
                View child = answerLayout.getChildAt(0);
                if (child instanceof EditText) {
                    EditText editText = (EditText) child;
                    String answerText = editText.getText().toString();
                    if (!answerText.isEmpty()) {
                        Answer answer = new Answer(editText.getText().toString());
                        choices.add(answer);
                    } else {
                        Toast.makeText(getActivity(), "Empty answer", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }

        Question mQuestion = new Question(questionTitle, choices);

        mDAOQuestion.add(mQuestion, group.getId()).addOnSuccessListener(success -> {
            Toast.makeText(getActivity(), "Question created", Toast.LENGTH_SHORT).show();
            group.addQuestion(mQuestion);
//            updateUi();
        }).addOnFailureListener(failure -> {
            Toast.makeText(getActivity(), "Question not created", Toast.LENGTH_SHORT).show();
        });


        return true;

    }


    private class QuestionHolder extends RecyclerView.ViewHolder {

        private final TextView mQuestionTitleTextView;
        private final TextView mQuestionDateTextView;
        private final RecyclerView mAnswerRecyclerView;
        private final Button mDetailButton;

        private final ConstraintLayout mExpandLayout;
        private final Button mSubmitButton;

        private TextView mAnswerTitleTextView;
        private ConstraintLayout mVotedLayout;
        private Answer mPickedAnswer;
        private TextView mAnswerCount;



        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.question_list_item, parent, false));

            mQuestionTitleTextView = itemView.findViewById(R.id.tv_item_title);
            mQuestionDateTextView = itemView.findViewById(R.id.tv_item_date);
            mAnswerRecyclerView = itemView.findViewById(R.id.rv_item_answers);
            mDetailButton = itemView.findViewById(R.id.btn_detail);
            mSubmitButton = itemView.findViewById(R.id.btn_submit);
            mExpandLayout = itemView.findViewById(R.id.constraint_layout);
            mAnswerTitleTextView = itemView.findViewById(R.id.tv_answer_title);
            mVotedLayout = itemView.findViewById(R.id.cl_voted_layout);
            mAnswerCount = itemView.findViewById(R.id.tv_vote_count);
        }

        public void bind(Question question) {

            CardView cardView;
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

            FirebaseAuth auth = FirebaseAuth.getInstance();

            int count = 0;
            Answer votedAnswer = null;
            for (Answer answer : question.getAnswers()) {
                if (!answer.getVoters().isEmpty()) {
                    // If the user's id is in the list of voters, remember the answer
                    Log.d("bind disable answer", "bind: " + answer.getVoters().toString() + " " + auth.getUid());
                    if (answer.getVoters().contains(auth.getUid())) {
                        votedAnswer = answer;
                    }
                }
                count += answer.getVotes();
            }
            String concatCount = count + " votes";
            mAnswerCount.setText(concatCount);
            if (votedAnswer != null) {
//                disableQuestion(question, votedAnswer);
                mSubmitButton.setEnabled(false);
                cardView = QuestionHolder.this.itemView.findViewById(R.id.cardView);

                int borderWidth = 6; // specify the border width in pixels
                int borderColor = Color.rgb(51, 241, 235); // specify the border color, e.g. Color.BLACK

                cardView.setCardElevation(borderWidth);

                GradientDrawable gd = new GradientDrawable();
                gd.setStroke(borderWidth, borderColor); // set the border width and color
                gd.setCornerRadius(30);
                cardView.setBackground(gd);

                mSubmitButton.setTextColor(Color.parseColor("#F50A71"));
                mSubmitButton.setText(R.string.voted);
                mAnswerRecyclerView.setVisibility(View.GONE);
                mVotedLayout.setVisibility(View.VISIBLE);
                mAnswerTitleTextView.setText(votedAnswer.getAnswerTitle());
                question.setDisabled(true);
            }

            // get the reference to the root of the database
            DatabaseReference rootRef = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference();

            Query answerQuery = rootRef.child("Group")
                    .child(mGroup.getId())
                    .child("Question")
                    .child(question.getId())
                    .child("answers");

            mSubmitButton.setOnClickListener(v -> {
                for (Answer answer : answers) {
                    if (answer.isChecked()) {
                        answerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    String position = ds.getKey();
                                    mPickedAnswer = ds.getValue(Answer.class);

                                    if (mPickedAnswer != null && mPickedAnswer.getId().equals(answer.getId())) {
                                        mPickedAnswer.addVoter(auth.getUid());
                                        mPickedAnswer.incrementVotes();
                                        assert position != null;

                                        List<Answer> answersNew = question.getAnswers();
                                        answersNew.set(Integer.parseInt(position), mPickedAnswer);
                                        question.setAnswers(answersNew);

                                        ds.getRef().child("votes").setValue(mPickedAnswer.getVotes());
                                        ds.getRef().child("voters").setValue(mPickedAnswer.getVoters());

                                        mAdapter.notifyItemChanged(getAdapterPosition());
                                        Toast.makeText(getActivity(), "Vote submitted", Toast.LENGTH_SHORT).show();
                                        answerQuery.removeEventListener(this);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("GroupPageFrag", "onCancelled: " + error.getMessage());
                            }
                        });
                    }
                }

            });

            mDetailButton.setOnClickListener(v -> {
                Bundle bundle = getArguments();
                assert bundle != null;
                bundle.putString("question", question.getId());
                bundle.putInt("position", getAdapterPosition());
                bundle.putSerializable("answers", answers);
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });

            mExpandLayout.setVisibility(View.GONE);

            @SuppressLint("CutPasteId") CardView cardViewAnimation = QuestionHolder.this.itemView.findViewById(R.id.cardView);
            cardViewAnimation.setOnClickListener(view -> {
                expandableLayout();
            });

        }

        private void expandableLayout() {
            Transition transition = new AutoTransition();
            transition.setDuration(400);
            if (mExpandLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(mExpandLayout, transition);
                mExpandLayout.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(mExpandLayout, transition);
                mExpandLayout.setVisibility(View.GONE);
            }
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        private final List<Question> mQuestions;
        private int mPosition;

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
            mPosition = holder.getAdapterPosition();
            holder.bind(question);
        }

        public int getPosition() {
            return mPosition;
        }

        @Override
        public int getItemCount() {
            if (mQuestions == null) {
                return 0;
            } else {
                return mQuestions.size();
            }
        }

        public void setQuestions(List<Question> questions) {
            mQuestions.clear();
            mQuestions.addAll(questions);
        }


    }
}

