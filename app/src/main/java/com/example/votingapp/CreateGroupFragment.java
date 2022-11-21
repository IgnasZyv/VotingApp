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

import java.util.HashMap;

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
            daoGroup.add(group).addOnSuccessListener(success -> {
                Log.w("CreateGroupFragment", "Group created successfully");
            }).addOnFailureListener(failure -> {
                Log.w("CreateGroupFragment", "Group creation failed");
            });

//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("name", groupName.getText().toString());
//            daoGroup.update("-NHLWdEozDWLWCaDKgnP", hashMap).addOnSuccessListener(success -> {
//                Toast.makeText(this.getContext(), "Record is Updated", Toast.LENGTH_SHORT).show();
//            });

//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("name", groupName.getText().toString());
//            daoGroup.remove("-NHLWdEozDWLWCaDKgnP").addOnSuccessListener(success -> {
//                Toast.makeText(this.getContext(), "Record is Updated", Toast.LENGTH_SHORT).show();
//            });

//            GroupLab.get(requireActivity()).addGroup(group);
//            requireActivity().finish();
//            Log.d("groups", "sent this group name at create" + group.getName());

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
