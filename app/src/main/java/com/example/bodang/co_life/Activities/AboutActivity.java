package com.example.bodang.co_life.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.bodang.co_life.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:bo.wang@ucdconnect.ie"));
                data.putExtra(Intent.EXTRA_SUBJECT, "Feedback from " + MainActivity.UnameValue + " --- co-life");
                data.putExtra(Intent.EXTRA_TEXT,"");
                startActivity(data);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
