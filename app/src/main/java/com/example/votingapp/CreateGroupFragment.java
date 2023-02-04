package com.example.votingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class CreateGroupFragment extends Fragment {

        public CreateGroupFragment() {
            super(R.layout.fragment_create_group);
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        assert view != null;
        EditText groupName = view.findViewById(R.id.et_title);
        Button createGroupButton = view.findViewById(R.id.btn_submit);



        DAOGroup daoGroup = new DAOGroup();
        createGroupButton.setOnClickListener(view1 -> {
            Group group = new Group(groupName.getText().toString());
            group.addAdministrator(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            daoGroup.add(group).addOnSuccessListener(success -> {
                Log.w("CreateGroupFragment", "Group created successfully");

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseDatabase db = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference userGroupsRef = db.getReference("UserGroups").child(Objects.requireNonNull(auth.getUid()));
                HashMap<String, Boolean> groupMap = new HashMap<>();
                groupMap.put(group.getId(), true);
                userGroupsRef.child(group.getId()).setValue(groupMap);

            }).addOnFailureListener(failure -> {
                Log.w("CreateGroupFragment", "Group creation failed");
            });


            requireActivity().finish();
        });


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putString("group_name", requireView().findViewById(R.id.et_title).toString());
    }
}
