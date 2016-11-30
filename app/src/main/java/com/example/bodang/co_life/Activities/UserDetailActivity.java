package com.example.bodang.co_life.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bodang.co_life.Management.BackgroundService;
import com.example.bodang.co_life.R;

import static com.example.bodang.co_life.Activities.MainActivity.client;

//This class show list the operation of user to enroll in group or logoff
public class UserDetailActivity extends AppCompatActivity {

    private final String PREFS_NAME = "preferences";
    private final String PREF_UNAME = "Username";
    private final String PREF_GROUP = "Groupname";
    private final String DefaultUnameValue = "Guest";
    private final String DefaultGroupValue = "You have not enrolled in any group";
    private String temp;

    private createGroupTask mcreateGroupTask = null;
    private changeGroupTask mchangeGroupTask = null;
    private getGroupTask mgetGroupTask = null;

    private Button changeGroup;
    private Button createGroup;
    private Button logoff;
    private TextView userName;
    private TextView groupID;
    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        groupID = (TextView) findViewById(R.id.detail_groupnumber);
        userName = (TextView) findViewById(R.id.detail_username);
        createGroup = (Button) findViewById(R.id.btn_newgroup);
        logoff = (Button) findViewById(R.id.btn_logout);
        changeGroup = (Button) findViewById(R.id.btn_changegroup);
        serviceIntent = new Intent(UserDetailActivity.this, BackgroundService.class);
        //The dialog for change group
        changeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Which Group you want to Enrol?");
                View view = LayoutInflater.from(UserDetailActivity.this).inflate(R.layout.dialog_changegroup, null);
                builder.setView(view);

                final EditText groupnumber = (EditText) view.findViewById(R.id.dialog_changegroup_groupid);
                final EditText password = (EditText) view.findViewById(R.id.dialog_changegroup_password);

                builder.setPositiveButton("Enrol", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = MainActivity.UnameValue;
                        String id = groupnumber.getText().toString().trim();
                        String pswd = password.getText().toString();
                        mchangeGroupTask = new changeGroupTask(username, id, pswd);
                        mchangeGroupTask.execute((Void) null);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        //The dialog for logoff
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Log off");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Log off", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveUserPreferences(DefaultUnameValue);
                        saveGroupPreferences(DefaultGroupValue);
                        stopService(serviceIntent);
                        startService(serviceIntent);
                        MainActivity.userGroupID.setText(DefaultGroupValue);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        //The dialog for create group
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Give your group a password");
                View view = LayoutInflater.from(UserDetailActivity.this).inflate(R.layout.dialog_creategroup, null);
                builder.setView(view);

                final EditText password = (EditText) view.findViewById(R.id.dialog_creategroup_password);
                final EditText passwordConfirm = (EditText) view.findViewById(R.id.dialog_creategroup_confirmpassword);

                builder.setPositiveButton("Create and Enrol", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = MainActivity.UnameValue;
                        String pswd = password.getText().toString();
                        String pswdcf = passwordConfirm.getText().toString();
                        if (pswdcf.equals(pswd)) {
                            mcreateGroupTask = new createGroupTask(username, pswd);
                            mcreateGroupTask.execute((Void) null);
                        } else {
                            Toast.makeText(UserDetailActivity.this, "Two password not match, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

    //This method save group preference into share preference
    private void saveGroupPreferences(String groupID) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_GROUP, groupID);
        editor.commit();
    }

    //This method load group preference from share preference
    private void loadGroupPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        temp = settings.getString(PREF_GROUP, DefaultGroupValue);
        if (!temp.equals(DefaultGroupValue)) {
            groupID.setText("You are enroled in Group " + temp);
        } else {
            groupID.setText(temp);
        }
    }

    //This method save user preference into share preference
    private void saveUserPreferences(String userID) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_UNAME, userID);
        editor.commit();
    }

    //This method load user preference into share preference
    private void loadUserPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        temp = settings.getString(PREF_UNAME, DefaultUnameValue);
        if (!userName.getText().equals(DefaultUnameValue)) {
            MainActivity.isLogedin = true;
            userName.setText("Welcome back! " + temp);
        }
    }

    //This method called when the activity resume
    @Override
    protected void onResume() {
        super.onResume();
        loadUserPreferences();
        mgetGroupTask = new getGroupTask(MainActivity.UnameValue);
        mgetGroupTask.execute((Void) null);
    }

    //This inner class implemented a background task for create group
    public class createGroupTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final String mPassword;
        private String groupID;

        public createGroupTask(String username, String password) {
            super();
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = client.Init();
            if (result == 1) {
                groupID = String.valueOf(client.createroom(mUsername, mPassword));
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                saveGroupPreferences(groupID);
                Toast.makeText(UserDetailActivity.this, "Create success, your Group number is " + groupID, Toast.LENGTH_SHORT).show();
//                stopService(serviceIntent);
//                startService(serviceIntent);
                finish();
            } else {
                Toast.makeText(UserDetailActivity.this, "Some thing wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mcreateGroupTask = null;
        }
    }

    //This inner class implemented a background task for change group
    public class changeGroupTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final String mPassword;
        private final String mGroupID;

        public changeGroupTask(String userName, String groupID, String password) {
            super();
            mUsername = userName;
            mGroupID = groupID;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = client.Init();
            if (result == 1) {
                return client.JoinRoom(mUsername, mGroupID, mPassword);
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                saveGroupPreferences(mGroupID);
                Toast.makeText(UserDetailActivity.this, "You are now a member of group " + mGroupID, Toast.LENGTH_SHORT).show();
//                stopService(serviceIntent);
//                startService(serviceIntent);
                finish();
            } else {
                Toast.makeText(UserDetailActivity.this, "Some thing wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mchangeGroupTask = null;
        }
    }

    //This inner class implemented a background task for get group number according to username
    public class getGroupTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private String GroupID;

        public getGroupTask(String userName) {
            super();
            mUsername = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = client.Init();
            if (result == 1) {
                GroupID = String.valueOf(client.roomId(mUsername));
                if (!GroupID.equals(0)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                saveGroupPreferences(GroupID);
                loadGroupPreferences();

            } else {
                Toast.makeText(UserDetailActivity.this, "Some thing wrong with your internet connection", Toast.LENGTH_SHORT).show();
                loadGroupPreferences();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mgetGroupTask = null;
        }
    }

}
