package com.example.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupHolder> {

    private final List<Group> mGroups;
    Context mContext;

    public GroupListAdapter(List<Group> groups) {
        mGroups = groups;
    }

    public static class GroupHolder extends RecyclerView.ViewHolder {
        private final TextView mGroupName;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            mGroupName = itemView.findViewById(R.id.tv_name);

        }

        public void bind(Group group) {
            mGroupName.setText(group.getName());
        }
    }

    public GroupListAdapter(List<Group> groups, Context context) {
        mContext = context;
        mGroups = groups;
    }


    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        return new GroupHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        Group group = mGroups.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }
}