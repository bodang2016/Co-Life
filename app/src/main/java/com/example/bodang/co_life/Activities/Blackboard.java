package com.example.bodang.co_life.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.bodang.co_life.Database.Data;
import com.example.bodang.co_life.Database.LocalDatabaseHelper;
import com.example.bodang.co_life.Objects.Notice;
import com.example.bodang.co_life.R;

import java.util.ArrayList;

import static com.example.bodang.co_life.Activities.MainActivity.client;

public class Blackboard extends AppCompatActivity {
    private LocalDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ListView list;
    private SwipeRefreshLayout swipeLayout;
    private updateBlackboardTask mupdateBlackboardTask = null;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private EditText writeOnBlackboard;
    private Button uploadNotice;
    private uploadNiticeOnBlackboardTask muploadNiticeOnBlackboardTask=null;
    private boolean uploadSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackboard);
        dbHelper = new LocalDatabaseHelper(this, "localDatabase.db", null, 1);
        list = (ListView) findViewById(R.id.blackboard_list);
        db = dbHelper.getReadableDatabase();
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.blackboard_refresh_layout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mupdateBlackboardTask = new updateBlackboardTask(MainActivity.UnameValue);
                mupdateBlackboardTask.execute((Void) null);
            }
        });
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark);
        writeOnBlackboard=(EditText)findViewById(R.id.writeNotice);
        uploadNotice=(Button)findViewById(R.id.uploadNotice);
        uploadNotice.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                InputMethodManager inputManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                String content=writeOnBlackboard.getText().toString();
                inputManager.hideSoftInputFromWindow(writeOnBlackboard.getWindowToken(), 0);
                muploadNiticeOnBlackboardTask = new uploadNiticeOnBlackboardTask(MainActivity.UnameValue,content);
                muploadNiticeOnBlackboardTask.execute((Void) null);

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        mupdateBlackboardTask = new updateBlackboardTask(MainActivity.UnameValue);
        mupdateBlackboardTask.execute((Void) null);
    }
    public void inflateBlackboardList(Cursor cursor) {
        adapter = new SimpleCursorAdapter(this, R.layout.blackboard_item,
                cursor, new String[]{"username", "content", "time"}, new int[]{R.id.blackboard_name, R.id.blackboard_content, R.id.blackboard_time},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        list.setFocusable(false);
    }
    public void uploadedSuccessful(){
        mupdateBlackboardTask = new updateBlackboardTask(MainActivity.UnameValue);
        mupdateBlackboardTask.execute((Void) null);
        writeOnBlackboard.setText("");
    }
    public class updateBlackboardTask extends AsyncTask<Void, Void, Boolean> {
            private final String mUsername;
            private ArrayList<Notice> noticeList = null;

            public updateBlackboardTask(String userName) {
                super();
                mUsername = userName;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swipeLayout.setRefreshing(true);
            }
            @Override
            protected Boolean doInBackground(Void... params) {
                int result = client.Init();
                if (result == 1) {
                    Data.deleteNotice(dbHelper.getReadableDatabase());
                    noticeList = client.getBlackboardNoticeList(mUsername);
                    for (int i = 0; i < noticeList.size(); i++) {
                        ContentValues values = new ContentValues();
                        values.put("username", noticeList.get(i).getUserName());
                        values.put("content", noticeList.get(i).getContent());
                        values.put("groupid",noticeList.get(i).getGroupId());
                        values.put("time", noticeList.get(i).getTime().toString());
                        Data.insertNotice(dbHelper.getReadableDatabase(), values);
                    }
                    cursor = db.rawQuery("select * from localDatabase_blackboard", null);
                    return true;
                }
                cursor = db.rawQuery("select * from localDatabase_blackboard", null);
                return false;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                inflateBlackboardList(cursor);
                adapter.notifyDataSetChanged();
                if(!success) {
                    Toast.makeText(Blackboard.this, "No internet connection, local cache is loaded", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(Blackboard.this, "Refreshed", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                mupdateBlackboardTask = null;
            }
    }
    public class uploadNiticeOnBlackboardTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private String content;
        public uploadNiticeOnBlackboardTask(String userName,String content) {
            super();
            this.content=content;
            mUsername = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            uploadSuccess=false;
            int result = client.Init();
            if (result == 1) {
                uploadSuccess=client.uploadNotice(MainActivity.UnameValue,content);
                if(uploadSuccess) {
                    return true;
                }
            }
//            Toast.makeText(Blackboard.this,"FailedToConnectToServer",Toast.LENGTH_LONG).show();
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            swipeLayout.setRefreshing(false);
            if(!success) {
                Toast.makeText(Blackboard.this, "uploadFailed", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Blackboard.this,"uploadSuccessful",Toast.LENGTH_LONG);
                uploadedSuccessful();
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            muploadNiticeOnBlackboardTask = null;
        }
    }
}
