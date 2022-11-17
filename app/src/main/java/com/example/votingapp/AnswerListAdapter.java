package com.example.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.AnswerHolder> {

    public ArrayList<Answer> mAnswers;
    Context mContext;
    private int checkedPosition = -1;

    public class AnswerHolder extends RecyclerView.ViewHolder {
        public TextView mAnswerTextView;
        public RadioButton mRadioButton;

        public AnswerHolder(View itemView) {
            super(itemView);
            mAnswerTextView = itemView.findViewById(R.id.tv_answer_title);
            mRadioButton = itemView.findViewById(R.id.rb_answer);
        }

        public void bind(Answer answer) {
            mAnswerTextView.setText(answer.getAnswerTitle());
//            mRadioButton.setText(getAdapterPosition());
            mRadioButton.setChecked(getAdapterPosition() == checkedPosition);

            // If checked position is -1 don't select anything by default.
            if (checkedPosition == -1) {
                mRadioButton.setChecked(false);
            } else {
                // If the current position is equal to the selected position then mark the view as selected.
                mRadioButton.setChecked(checkedPosition == getAdapterPosition());
            }

            // Set the click listener for the radio button.
            mRadioButton.setOnClickListener(v -> {
                //
                if (checkedPosition != getAdapterPosition()) {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }

            });

        }
    }

    public AnswerListAdapter(ArrayList<Answer> answers, Context context) {
        this.mContext = context;
        this.mAnswers = answers;
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
        holder.bind(currentAnswer);
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }
}
