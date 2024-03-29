package com.example.votingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class JoinGroupDialogFragment extends DialogFragment {
    private final Context mContext;
    public JoinGroupDialogFragment(Context context) {
        super(R.layout.dialog_fragment_join);
        this.mContext = context;
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
                        String invitationCode = mInvitationCode.getText().toString();
                        try {
                            // Check if the invitation code is valid
                            int inviteCode = Integer.parseInt(invitationCode);
                            CheckBox isAdmin = view.findViewById(R.id.cb_admin); // Check if the user is an admin
                            joinGroup(inviteCode, isAdmin.isChecked()); // Join the group

                        } catch (NumberFormatException e) {
                            Toast.makeText(mContext, "Invalid invitation code", Toast.LENGTH_SHORT).show();
                        }
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

    public void joinGroup(int inviteCode, boolean isAdmin) {
        // get the reference to the root of the database
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://votingapp-6e7b7-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        Query groupQuery;
        if (isAdmin) {
            groupQuery = rootRef.child("Group").orderByChild("adminInviteCode").equalTo(inviteCode);
        } else {
            groupQuery = rootRef.child("Group").orderByChild("inviteCode").equalTo(inviteCode);

        }
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
                        if (isAdmin) {
                            groupMap.put("administrator", true);
                            assert group != null;
                            ArrayList<String> admins;
                            if (group.getAdministrators() == null) {
                                admins = new ArrayList<>();
                            } else {
                                admins = group.getAdministrators();
                            }
                            admins.add(auth.getUid());
                            rootRef.child("Group").child(group.getId()).child("administrators").setValue(admins);
                        } else {
                            groupMap.put("administrator", false);
                            // add the group to the user's groups
                            assert group != null;
                            ArrayList<String> members;
                            if (group.getMembers() == null) {
                                members = new ArrayList<>();
                            } else {
                                members = group.getMembers();
                            }
                            members.add(auth.getUid());
                            rootRef.child("Group").child(group.getId()).child("members").setValue(members);
                        }
                    rootRef.child("UserGroups").child(Objects.requireNonNull(auth.getUid())).child(group.getId()).setValue(groupMap);
                    }

                } else {
                    Toast.makeText(mContext, "Group not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("JoinGroupFragment", "onCancelled: " + databaseError.getMessage());
            }
        });

    }
}