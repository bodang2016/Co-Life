package com.example.bodang.co_life.Activities;

import android.content.ContentValues;
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
                String content=writeOnBlackboard.getText().toString();
                boolean uploadsuccessful=client.uploadNotice(MainActivity.UnameValue,content);
                if(uploadsuccessful) {
                    writeOnBlackboard.setText("");
                    mupdateBlackboardTask = new updateBlackboardTask(MainActivity.UnameValue);
                    mupdateBlackboardTask.execute((Void) null);
                    Toast.makeText(Blackboard.this,"uploadSuccessful",Toast.LENGTH_LONG);
                }
                else{
                    Toast.makeText(Blackboard.this,"uploadFailed",Toast.LENGTH_LONG);
                }
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
        setListViewHeightBasedOnChildren(list);
        list.setFocusable(false);
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
        public boolean uploadNotice(){


            return true;
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
            swipeLayout.setRefreshing(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mupdateBlackboardTask = null;
        }
    }
}
