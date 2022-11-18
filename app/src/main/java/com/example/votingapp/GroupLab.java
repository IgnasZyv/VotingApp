package com.example.votingapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupLab {
    UUID mGroupId;
    private static GroupLab sGroupLab;
    private List<Group> mGroups;

    private GroupLab(Context context) {
        mGroupId = UUID.randomUUID();
        mGroups = new ArrayList<>();

    }

    public static GroupLab get(Context context) {
        if (sGroupLab == null) {
            sGroupLab = new GroupLab(context);
        }
        return sGroupLab;
    }

    public void addGroup(Group group) {
        mGroups.add(group);
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public Group getGroup(UUID id) {
        for (Group group : mGroups) {
            if (group.getGroupId().equals(id)) {
                return group;
            }
        }
        return null;
    }
}

