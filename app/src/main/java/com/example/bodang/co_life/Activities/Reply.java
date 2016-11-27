package com.example.bodang.co_life.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bodang.co_life.Database.LocalDatabaseHelper;
import com.example.bodang.co_life.Objects.Message;
import com.example.bodang.co_life.R;

import static com.example.bodang.co_life.Database.Data.client;
import static com.example.bodang.co_life.Management.BackgroundService.clientBackground;

public class Reply extends AppCompatActivity {
    String myusername;
    String requestername;
    String content;
    boolean replyOK;
    boolean sendSuccess=false;
    private sendReplyTask mreplyTask=null;
    EditText writeReply;
    Button sendReply;
    private SwipeRefreshLayout swipeLayout;
    private LocalDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView list;
    private SimpleCursorAdapter adapter;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Intent intent=getIntent();
        requestername=intent.getStringExtra("requestername");
        myusername=intent.getStringExtra("myname");
        replyOK=intent.getBooleanExtra("reply",false);
        writeReply=(EditText)findViewById(R.id.writeReply);
        sendReply=(Button)findViewById(R.id.sendReply);
        dbHelper = new LocalDatabaseHelper(this, "localDatabase.db", null, 1);
        list = (ListView) findViewById(R.id.reply_list);
        db = dbHelper.getReadableDatabase();
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.reply_refresh_layout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inflateRequestList();
            }
        });
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark);
        sendReply.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                content=writeReply.getText().toString();
                mreplyTask = new Reply.sendReplyTask(content);
                mreplyTask.execute((Void) null);
            }
        });
        if(replyOK){
            content="OK";
            mreplyTask = new Reply.sendReplyTask(content);
            mreplyTask.execute((Void) null);
        }
    }

    @Override
    protected void onResume() {
        inflateRequestList();
        super.onResume();
    }

    public boolean reply(String content){
        boolean replysuccess=false;
        Message message=new Message(requestername,myusername,content,1,null);
        replysuccess=clientBackground.sendMessage(myusername,message);
        return replysuccess;
    }
    public void inflateRequestList() {
        cursor = db.rawQuery("select * from localDatabase_request where username = '"+myusername+"'", null);
        adapter = new SimpleCursorAdapter(this, R.layout.blackboard_item,
                cursor, new String[]{"requester", "content", "time"}, new int[]{R.id.blackboard_name, R.id.blackboard_content, R.id.blackboard_time},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        list.setFocusable(false);
        swipeLayout.setRefreshing(false);
    }
    public class sendReplyTask extends AsyncTask<Void, Void, Boolean> {
        private String content;
        public sendReplyTask(String content) {
            super();
            this.content=content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean sendSuccess=false;
            int result = clientBackground.Init();
            if (result == 1) {
                sendSuccess= reply(content);
            }
            if(sendSuccess){
                db.rawQuery("delete from localDatabase_request where _id = '"+id+"'", null);
            }
            return sendSuccess;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            swipeLayout.setRefreshing(false);
            if(!success) {
                Toast.makeText(Reply.this, "replyFailed", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Reply.this,"replySuccessful",Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mreplyTask = null;
        }
    }
}
