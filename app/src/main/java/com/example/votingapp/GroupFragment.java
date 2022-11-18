package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class GroupFragment extends Fragment {

    public GroupFragment() {
        super(R.layout.fragment_group);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        ImageButton createGroupButton = v.findViewById(R.id.btn_add_group);
        Button listButton = v.findViewById(R.id.btn_list_items);

        listButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), QuestionListActivity.class);
            startActivity(intent);
        });

        createGroupButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), GroupActivity.class);
            intent.putExtra("create_group", "create_group");
            startActivity(intent);
        });

        if (savedInstanceState != null) {
            Log.d("GroupFragment", "onViewStateRestored: " + savedInstanceState.getString("group_name"));
        }

        return v;
    }

}
