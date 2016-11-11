package com.example.bodang.co_life.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bodang.co_life.R;

public class UserDetailActivity extends AppCompatActivity {

    private Button changeGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        changeGroup = (Button)findViewById(R.id.btn_changegroup);
        changeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailActivity.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Which Group you want to Enrol?");
                View view = LayoutInflater.from(UserDetailActivity.this).inflate(R.layout.dialog_changegroup, null);
                builder.setView(view);

                final EditText groupnumber = (EditText)view.findViewById(R.id.dialog_changegroup_groupid);
                final EditText password = (EditText)view.findViewById(R.id.dialog_changegroup_password);

                builder.setPositiveButton("Enrol", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String a = groupnumber.getText().toString().trim();
                        String b = password.getText().toString().trim();
                        Toast.makeText(UserDetailActivity.this, "Groupnumber: " + a + ", password: " + b, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });
    }
}
