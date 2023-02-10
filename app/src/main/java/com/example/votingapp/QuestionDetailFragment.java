package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class QuestionDetailFragment extends Fragment {

    private Question mQuestion;
    private AnswerListAdapter mAdapter;

    public QuestionDetailFragment() {
        super(R.layout.fragment_question_detail);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_detail, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        mQuestion = (Question) bundle.getSerializable("question");
        ArrayList<Answer> answers = (ArrayList<Answer>) mQuestion.getAnswers();

        RecyclerView answerRecyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        answerRecyclerView.setLayoutManager(layoutManager);
        answerRecyclerView.setHasFixedSize(true);


        mAdapter = new AnswerListAdapter(answers, mQuestion, getContext());
        mAdapter.setIsInDetailsView(true);
        answerRecyclerView.setAdapter(mAdapter);


        return view;
    }

}
