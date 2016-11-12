package com.example.bodang.co_life.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bodang.co_life.R;

public class UserDetailActivity extends AppCompatActivity {

    private final String PREFS_NAME = "preferences";
    private final String PREF_UNAME = "Username";
    private final String PREF_GROUP = "Groupname";
    private final String DefaultUnameValue = "Guest";
    private final String DefaultGroupValue = "You have not enrolled in any group";
    private String temp;

    private Button changeGroup;
    private Button createGroup;
    private Button logoff;
    private TextView userName;
    private TextView groupID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        groupID = (TextView) findViewById(R.id.detail_groupnumber);
        userName = (TextView) findViewById(R.id.detail_username);
        createGroup = (Button) findViewById(R.id.btn_newgroup);
        logoff = (Button) findViewById(R.id.btn_logout);
        changeGroup = (Button) findViewById(R.id.btn_changegroup);
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
                        String id = groupnumber.getText().toString().trim();
                        String pscd = password.getText().toString();
                        Toast.makeText(UserDetailActivity.this, "Groupnumber: " + id + ", password: " + pscd, Toast.LENGTH_SHORT).show();
                        saveGroupPreferences("You are enroled in Group " + temp);
                        groupID.setText(id);
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

        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Log off");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Log off", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        saveUserPreferences(DefaultUnameValue);
                        saveGroupPreferences(DefaultGroupValue);
                        Toast.makeText(UserDetailActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(UserDetailActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

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
                        String pswd = password.getText().toString();
                        String pswdcf = passwordConfirm.getText().toString();
                        if(pswdcf.equals(pswd)) {
                            Toast.makeText(UserDetailActivity.this, "password: " + pswd + ", passwordconfirm: " + pswdcf, Toast.LENGTH_SHORT).show();
//                          saveGroupPreferences("You are enroled in Group " + temp);
//                          groupID.setText(id);
                        } else {
                            passwordConfirm.setError(getString(R.string.error_incorrect_password));
                            passwordConfirm.requestFocus();
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


    private void saveGroupPreferences(String groupID) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_GROUP, groupID);
        editor.commit();
    }

    private void loadGroupPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        temp = settings.getString(PREF_GROUP, DefaultGroupValue);
        if (temp != DefaultGroupValue) {
            groupID.setText("You are enroled in Group " + temp);
        } else {
            groupID.setText(temp);
        }
    }

    private void saveUserPreferences(String userID) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_UNAME, userID);
        editor.commit();
    }

    private void loadUserPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        temp = settings.getString(PREF_UNAME, DefaultUnameValue);
        if (userName.getText() != DefaultUnameValue) {
            MainActivity.isLogedin = true;
            userName.setText("Welcome back! " + temp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroupPreferences();
        loadUserPreferences();
    }

    public class createGroupTask extends AsyncTask<Void, Void, Boolean> {
        public createGroupTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }
}
