package com.example.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnswerListAdapter extends RecyclerView.Adapter<AnswerListAdapter.AnswerHolder> {

    public ArrayList<Answer> mAnswers;
    Context mContext;

    public static class AnswerHolder extends RecyclerView.ViewHolder {
        public TextView mAnswerTextView;
        public CheckBox mCheckBox;

        public AnswerHolder(View itemView) {
            super(itemView);
            mAnswerTextView = itemView.findViewById(R.id.tv_answer_title);
            mCheckBox = itemView.findViewById(R.id.cb_answer);
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
        holder.mAnswerTextView.setText(currentAnswer.getAnswerTitle());
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }
}
