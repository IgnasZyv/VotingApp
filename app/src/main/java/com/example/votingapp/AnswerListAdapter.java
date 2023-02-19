package com.example.votingapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.utils.ColorTemplate;
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
    Group mGroup;
    private int checkedPosition = -1;
    public Boolean mIsInDetailsView = false;

    public class AnswerHolder extends RecyclerView.ViewHolder {
        public TextView mAnswerTextView;
        public RadioButton mRadioButton;
        public ProgressBar mProgressBar;
        public CardView mAnswerCard;
        public TextView mVoteCount;

        public AnswerHolder(View itemView) {
            super(itemView);
            mAnswerTextView = itemView.findViewById(R.id.tv_answer_title);
            mRadioButton = itemView.findViewById(R.id.rb_answer);
            mProgressBar = itemView.findViewById(R.id.answer_progress);
            mAnswerCard = itemView.findViewById(R.id.answer_card);
            mVoteCount = itemView.findViewById(R.id.tv_vote_count);
        }

        public void bind(Answer answer) {
            answer.setGroupEncryptionKey(mGroup.getGroupEncryptionKey());
            mAnswerTextView.setText(answer.getDecryptedAnswerTitle());
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

            // If the user is in the details view, then hide the radio button and show the progress bar.
            if (mIsInDetailsView) {
                mRadioButton.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mAnswerCard.getLayoutParams();
                params.setMargins(20, 20, 20, 20);
                mAnswerCard.setLayoutParams(params);
                mVoteCount.setVisibility(View.VISIBLE);

                // Set the progress bar
                enableProgressBar(answer);

            }



        }


        private void enableProgressBar(Answer answerBind) {
//            int progress = (answerBind.getVotes() * 100) / mQuestion.getVoteCount();
//            answerBind.getProgressBar().setProgress(progress);

            DatabaseReference rootRef = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference();

            // Query to set a listener on the answers
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
                    int count = 0;
                    // Get all answers and update the progress bar
                    for (DataSnapshot answerSnapshot : snapshot.getChildren()) {
                        Log.d("AnswerListAdapter", "onDataChange: answers" + answerSnapshot.getValue());
                        // Get the answer
                        Answer answer = answerSnapshot.getValue(Answer.class);
                        assert answer != null;
                        int[] colours = ColorTemplate.MATERIAL_COLORS;
                        // if the answer is the same as the one we are binding, set the progress bar
                        if (answer.getId().equals(answerBind.getId())) {
                            answer.setProgressBar(answerBind.getProgressBar());
                            // Set the colour of the progress bar
                            answer.getProgressBar().setProgressTintList(ColorStateList.valueOf(colours[count]));
                        }
                        count++;
                        // Add the answer to the list and update the total votes
                        totalVotes += answer.getVotes();
                        newAnswers.add(answer);
                    }

                    // Update the progress bar
                    for (Answer answer : newAnswers) {
                        // If the progress bar is not null, update the progress bar
                        if (answer.getProgressBar() != null) {
                            int progress = (answer.getVotes() * 100) / totalVotes;
//                            answer.getProgressBar().setMax(totalVotes);
                            int currentProgress = answer.getProgressBar().getProgress();

                            ObjectAnimator animation = ObjectAnimator.ofInt(answer.getProgressBar(), "progress", progress);
                            animation.setDuration(900);
                            animation.setInterpolator(new LinearInterpolator());
                            animation.start();

                            String countText = answer.getVotes() + " (" + progress + " %)" + " votes";
                            mVoteCount.setText(countText);
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

    public AnswerListAdapter(ArrayList<Answer> answers, Question question, Group group, Context context) {
        this.mContext = context;
        this.mQuestion = question;
        this.mGroup = group;
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
