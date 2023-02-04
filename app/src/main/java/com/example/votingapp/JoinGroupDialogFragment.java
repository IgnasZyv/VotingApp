package com.example.votingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class JoinGroupDialogFragment extends DialogFragment {
    public JoinGroupDialogFragment() {
        super(R.layout.dialog_fragment_join);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_join, null);

        EditText mInvitationCode = view.findViewById(R.id.et_invitation_code);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        builder.setView(view)
                .setPositiveButton(R.string.join_group, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get the invitation code
                        int inviteCode = Integer.parseInt(mInvitationCode.getText().toString());

                        // get the reference to the root of the database
                        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/")
                                .getReference();
                        // get the reference to the group that has the invite code
                        Query groupQuery = rootRef.child("Group").orderByChild("inviteCode").equalTo(inviteCode);

                        // get the group
                        groupQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // if the group exists
                                if (dataSnapshot.exists()) {
                                    // get the group
                                    for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                                        Group group = groupSnapshot.getValue(Group.class); // create a group object from the data in the database
                                        HashMap<String, Boolean> groupMap = new HashMap<>(); // create a hashmap to add to the user's groups
                                        groupMap.put(group.getId(), true);
                                        // add the group to the user's groups
                                        rootRef.child("UserGroups").child(Objects.requireNonNull(auth.getUid())).child(group.getId()).setValue(groupMap);
                                        Toast.makeText(getContext(), "Joined group " + group.getName(), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Group not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("JoinGroupFragment", "onCancelled: " + databaseError.getMessage());
                            }
                        });

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Objects.requireNonNull(JoinGroupDialogFragment.this.getDialog()).cancel();
                    }
                });
        return builder.create();
    }
}