package com.example.votingapp;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupFragment extends Fragment {
    public GroupListAdapter mAdapter;
    public RecyclerView mGroupRecyclerView;
//    private List<Group> mGroups;

    private DAOGroup mDAOGroup;
    private ArrayList<Group> mGroups;
    private GroupListAdapter mGroupListAdapter;

    public GroupFragment() {
        super(R.layout.fragment_group);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadData() {

//        mDAOGroup.get().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Group group = ds.getValue(Group.class);
//                    mGroups.add(group);
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("GroupFragment", "loadPost:onCancelled", error.toException());
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        mDAOGroup = new DAOGroup();

        mGroups = new ArrayList<>();

        mGroupRecyclerView = v.findViewById(R.id.rv_group);

        mGroupListAdapter = new GroupListAdapter(mGroups, getContext());
        mGroupRecyclerView.setAdapter(mGroupListAdapter);

        ImageButton createGroupButton = v.findViewById(R.id.btn_add_group);
        ImageButton signOutButton = v.findViewById(R.id.ib_sign_out);

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

        signOutButton.setOnClickListener(v1 -> {
            AuthUI.getInstance().signOut(requireContext())
                    .addOnCompleteListener(task -> {
                        Intent intentLogOut = new Intent(getActivity(), LogInActivity.class);
                        intentLogOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear the back stack
                        startActivity(intentLogOut);
                    });
        });


        if (savedInstanceState != null) {
            Log.d("GroupFragment", "onViewStateRestored: " + savedInstanceState.getString("group_name"));
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            updateUi();

        }
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("group_name", "group_name");
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        updateUi();
//    }


    private void updateUi() {

        if (mAdapter == null) {
            mAdapter = new GroupListAdapter(mGroups);
            mGroupRecyclerView.setAdapter(mAdapter);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mGroupRecyclerView.setLayoutManager(layoutManager);
        mGroupRecyclerView.setHasFixedSize(true);
        
        mDAOGroup.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroups.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    mGroups.add(group);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GroupFragment", "loadPost:onCancelled", error.toException());
            }
        });
    }
}
