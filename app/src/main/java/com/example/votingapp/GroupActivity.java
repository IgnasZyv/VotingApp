package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class GroupActivity extends AppCompatActivity {

    public static String groupId;
    private Group mGroup;
    private MenuProvider mMenuProvider;


    public GroupActivity() {
        super(R.layout.activity_fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }



        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        groupId = intent.getStringExtra("groupId");

        if (intent.getSerializableExtra("group") != null) {
            mGroup = (Group) intent.getSerializableExtra("group");
            Objects.requireNonNull(getSupportActionBar()).setTitle(mGroup.getName());
            bundle.putSerializable("group", mGroup);
        }

        if (bundle.getSerializable("group") == null) {

            addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menuInflater.inflate(R.menu.home_menu, menu);
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                    if (menuItem.getItemId() == R.id.log_out) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(GroupActivity.this, LogInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    } else if (menuItem.getItemId() == R.id.join_group) {
                        JoinGroupDialogFragment joinGroupDialogFragment = new JoinGroupDialogFragment();
                        joinGroupDialogFragment.show(getSupportFragmentManager(), "joinGroupDialogFragment");
                    }

                    return false;
                }
            });


            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, GroupFragment.class, bundle)

                    .commit();
        }

        if (Objects.equals(intent.getStringExtra("create_group"), "create_group")) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, CreateGroupFragment.class, null)
                    .commit();
        }

        if (Objects.equals(intent.getStringExtra("group_page"), "group_page")) {
            myToolbar.getMenu().clear();

            addMenuProvider(new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menuInflater.inflate(R.menu.group_page_menu, menu);
                }

                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                    if (menuItem.getItemId() == R.id.invite) {
                        GroupCodeDialogFragment groupCodeDialogFragment = new GroupCodeDialogFragment(bundle);
                        groupCodeDialogFragment.show(getSupportFragmentManager(), "groupCodeDialogFragment");
                        return true;
                    }

                    return false;
                }
            });

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, GroupPageFragment.class, bundle)
                    .commit();
        }
    }
}
