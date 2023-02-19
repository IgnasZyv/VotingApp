package com.example.votingapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Group implements Serializable {

    private String mName;
    private String mId;
    private ArrayList<String> mMembers;
    private ArrayList<String> mAdministrators;
    private HashMap<String, Question> mQuestion;
    private int mInviteCode = 0;
    private int mAdminInviteCode = 0;

    private String GROUP_ENCRYPTION_KEY;

    public Group(){}

    public Group(String name) {
        if (this.GROUP_ENCRYPTION_KEY == null) {
            this.GROUP_ENCRYPTION_KEY = EncryptionUtils.getOrCreateEncryptionKey(mId);
        }
        this.mName = name;
        if (this.mId == null) {
            this.mId = UUID.randomUUID().toString();
        }
        this.mMembers = new ArrayList<>();
        this.mAdministrators = new ArrayList<>();
        this.mQuestion = new HashMap<>();
        this.mInviteCode = getInviteCode();
        this.mAdminInviteCode = getAdminInviteCode();
    }

    @PropertyName("name")
    public String getName() {
        return mName;
    }

    @PropertyName("name")
    public void setName(String name) {
        mName = name;
    }

    @PropertyName("id")
    public String getId() {
        return mId;
    }

    @PropertyName("id")
    public void setId(String id) {
        mId = id;
    }

    @PropertyName("members")
    public ArrayList<String> getMembers() {
        return mMembers;
    }

    @PropertyName("members")
    public void setMembers(ArrayList<String> members) {
        mMembers = members;
    }

    @PropertyName("administrators")
    public ArrayList<String> getAdministrators() {
        return mAdministrators;
    }

    @PropertyName("administrators")
    public void setAdministrators(ArrayList<String> administrators) {
        mAdministrators = administrators;
    }

    public void addAdministrator(String administrator) {
        mAdministrators.add(administrator);
    }

    @PropertyName("Question")
    public HashMap<String, Question> getQuestion() {
        return mQuestion;
    }

    @PropertyName("Question")
    public void setQuestion(HashMap<String, Question> question) {
        mQuestion = question;
    }

    public int getInviteCode() {
        if (mInviteCode == 0) {
            Random random = new Random();
            mInviteCode = 1000 + random.nextInt(8999);
        }
        return mInviteCode;
    }

    public void setInviteCode(int inviteCode) {
        mInviteCode = inviteCode;
    }

    public int getAdminInviteCode() {
        if (mAdminInviteCode == 0) {
            Random random = new Random();
            mAdminInviteCode = 1000 + random.nextInt(8999);
        }

        return mAdminInviteCode;
    }

    public void setAdminInviteCode(int adminInviteCode) {
        mAdminInviteCode = adminInviteCode;
    }

    public void addQuestion(Question question) {
        if (mQuestion == null) {
            mQuestion = new HashMap<>();
        }
        mQuestion.put(question.getId(), question);
    }

    @Exclude
    public String getGroupEncryptionKey() {
        return GROUP_ENCRYPTION_KEY;
    }

    @Exclude
    public void setGroupEncryptionKey(String groupEncryptionKey) throws JSONException {
        // Parse the JSON string into a JSONObject
        JSONObject json = new JSONObject(groupEncryptionKey);
        // Extract the value of the "key" field
        GROUP_ENCRYPTION_KEY = json.getString("key");
    }

}
