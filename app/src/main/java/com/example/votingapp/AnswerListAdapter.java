package com.example.votingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.AnswerHolder> {

    public ArrayList<Answer> mAnswers;
    Context mContext;
    Question mQuestion;
    private int checkedPosition = -1;
    public Boolean mIsInDetailsView = false;

    public class AnswerHolder extends RecyclerView.ViewHolder {
        public TextView mAnswerTextView;
        public RadioButton mRadioButton;
        public ProgressBar mProgressBar;

        public AnswerHolder(View itemView) {
            super(itemView);
            mAnswerTextView = itemView.findViewById(R.id.tv_answer_title);
            mRadioButton = itemView.findViewById(R.id.rb_answer);
            mProgressBar = itemView.findViewById(R.id.answer_progress);
        }

        public void bind(Answer answer) {
            mAnswerTextView.setText(answer.getAnswerTitle());
//            mRadioButton.setText(getAdapterPosition());
            mRadioButton.setChecked(getAdapterPosition() == checkedPosition);

            answer.setProgressBar(mProgressBar);

            // If checked position is -1 don't select anything by default.
            if (checkedPosition == -1) {
                mRadioButton.setChecked(false);
            }
            // Set the click listener for the radio button.
            mRadioButton.setOnClickListener(v -> {
                // If the current position does not equal the recent selected position then reset the previous button.
                if (!answer.isChecked()) {
                    for (Answer a : mAnswers) {
                        a.setChecked(false);
                    }
                    answer.setChecked(true);
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }
            });

            if (mIsInDetailsView) {
                mRadioButton.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

                enableProgressBar(answer);
            }



        }


        private void enableProgressBar(Answer answerBind) {
//            int progress = (answerBind.getVotes() * 100) / mQuestion.getVoteCount();
//            answerBind.getProgressBar().setProgress(progress);

            DatabaseReference rootRef = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference();

            Query voteQuery = rootRef.child("Group")
                    .child(mQuestion.getGroupId())
                    .child("Question")
                    .child(mQuestion.getId())
                    .child("answers");

            voteQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Answer> newAnswers = new ArrayList<>();
                    int totalVotes = 0;

                    for (DataSnapshot answerSnapshot : snapshot.getChildren()) {
                        Log.d("AnswerListAdapter", "onDataChange: answers" + answerSnapshot.getValue());
                        Answer answer = answerSnapshot.getValue(Answer.class);
                        assert answer != null;
                        if (answer.getId().equals(answerBind.getId())) {
                            answer.setProgressBar(answerBind.getProgressBar());
                        }
                        totalVotes += answer.getVotes();
                        newAnswers.add(answer);
                    }

                    for (Answer answer : newAnswers) {
                        if (answer.getProgressBar() != null) {
                            int progress = (answer.getVotes() * 100) / totalVotes;
//                            answer.getProgressBar().setMax(totalVotes);
                            answer.getProgressBar().setProgress(progress);
                        }
                    }
                    mQuestion.setAnswers(newAnswers);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(mContext, "Progress Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
        }




    }

    public AnswerListAdapter(ArrayList<Answer> answers, Question question, Context context) {
        this.mContext = context;
        this.mQuestion = question;
        this.mAnswers = answers;
    }

    public void setIsInDetailsView(Boolean isInDetailsView) {
        this.mIsInDetailsView = isInDetailsView;
    }

    @NonNull
    @Override
    public AnswerListAdapter.AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_item, parent, false);
        return new AnswerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerListAdapter.AnswerHolder holder, int position) {
        Answer currentAnswer = mAnswers.get(position);
        currentAnswer.setProgressBar(holder.mProgressBar);
        holder.bind(currentAnswer);
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }
}
