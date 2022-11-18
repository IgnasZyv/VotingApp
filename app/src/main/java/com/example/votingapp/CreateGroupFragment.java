package com.example.votingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        EditText groupName = view.findViewById(R.id.et_title);
        Button createGroupButton = view.findViewById(R.id.btn_submit);

        createGroupButton.setOnClickListener(view1 -> {
            Group group = new Group(groupName.getText().toString());
            GroupLab.get(requireActivity()).addGroup(group);
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
