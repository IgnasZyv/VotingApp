package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;
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


        TextView userEmail = v.findViewById(R.id.tv_user_email);
        userEmail.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());


        mDAOGroup = new DAOGroup();

        mGroups = new ArrayList<>();

        mGroupRecyclerView = v.findViewById(R.id.rv_group);

        mGroupListAdapter = new GroupListAdapter(mGroups, getContext());
        mGroupRecyclerView.setAdapter(mGroupListAdapter);

        ImageButton createGroupButton = v.findViewById(R.id.btn_add_group);
        ImageButton signOutButton = v.findViewById(R.id.ib_sign_out);

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

//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseDatabase db = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/");
//        DatabaseReference userGroupsRef = db.getReference("UserGroups").child(Objects.requireNonNull(auth.getUid()));


//        mDAOGroup.get().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mGroups.clear();
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String userId = currentUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference userGroupsRef = database.getReference("UserGroups").child(userId);

        DatabaseReference groupRef = database.getReference("Group");
        ValueEventListener userGroupsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGroups.clear();
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                final int[] groupCount = {0};
                for (DataSnapshot groupIdSnapshot : dataSnapshot.getChildren()) {
                    String groupId = groupIdSnapshot.getKey();
                    assert groupId != null;
                    DatabaseReference specificGroupRef = groupRef.child(groupId);
                    specificGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Group group = dataSnapshot.getValue(Group.class);
                                assert group != null;
                                try {
                                    if (group.getGroupEncryptionKey() == null) {
                                        group.setGroupEncryptionKey(EncryptionUtils.getOrCreateEncryptionKey(group.getId()));
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                mGroups.add(group);

                                groupCount[0]++;
                                if (groupCount[0] == childrenCount) {
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("GroupFragment", "loadPost:onCancelled", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("GroupFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };

        userGroupsRef.addValueEventListener(userGroupsListener);

    }

}
