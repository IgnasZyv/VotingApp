package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupFragment extends Fragment {
    public GroupListAdapter mAdapter;
    public RecyclerView mGroupRecyclerView;
    private List<Group> mGroups;

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
        mGroupRecyclerView = v.findViewById(R.id.rv_group);

        GroupListAdapter groupListAdapter = new GroupListAdapter(mGroups, getContext());
        mGroupRecyclerView.setAdapter(groupListAdapter);

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

        updateUi();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    private void updateUi() {

        GroupLab groupLab = GroupLab.get(getActivity());
        mGroups = groupLab.getGroups();

        if (mAdapter == null) {
            mAdapter = new GroupListAdapter(mGroups);
            mGroupRecyclerView.setAdapter(mAdapter);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mGroupRecyclerView.setLayoutManager(layoutManager);
        mGroupRecyclerView.setHasFixedSize(true);

        GroupListAdapter groupListAdapter = new GroupListAdapter(mGroups, getContext());
        mGroupRecyclerView.setAdapter(groupListAdapter);




        Log.d("groups", "group list at update" + mGroups);
    }
}
