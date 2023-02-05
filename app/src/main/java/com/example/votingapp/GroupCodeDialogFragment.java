package com.example.votingapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;

public class GroupCodeDialogFragment extends androidx.fragment.app.DialogFragment {

    private Bundle mBundle;
    public GroupCodeDialogFragment(Bundle bundle) {
        super(R.layout.dialog_fragment_group_code);
        mBundle = bundle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_group_code, null);

        Group group = (Group) mBundle.getSerializable("group");
        TextView mInvitationCode = view.findViewById(R.id.tv_invitation_code);

        mInvitationCode.setText(String.valueOf(group.getInviteCode()));
        TextView mAdminCode = view.findViewById(R.id.tv_admin_invitation_code);

        ConstraintLayout cl = view.findViewById(R.id.cl_admin_code);
        Button btn = view.findViewById(R.id.btn_show_admin_code);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cl.getVisibility() == View.VISIBLE) {
                    cl.setVisibility(View.GONE);
                    mAdminCode.clearComposingText();
                } else {
                    mAdminCode.setText(String.valueOf(group.getAdminInviteCode()));
                    cl.setVisibility(View.VISIBLE);
                }
            }
        });
        builder.setView(view)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Objects.requireNonNull(GroupCodeDialogFragment.this.getDialog()).cancel();

                    }
                });
        return builder.create();
    }

}
