package com.example.bodang.co_life.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bodang.co_life.Database.Data;
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
    private String deleteContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Intent intent=getIntent();
        requestername=intent.getStringExtra("requestername");
        myusername=intent.getStringExtra("myname");
        replyOK=intent.getBooleanExtra("reply",false);
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Cursor item = (Cursor) parent.getItemAtPosition(position);
                deleteContent = item.getString(3);
                AlertDialog.Builder builder = new AlertDialog.Builder(Reply.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Write a Message");
                View dailog = LayoutInflater.from(Reply.this).inflate(R.layout.dialog_sendnoti, null);
                builder.setView(dailog);

                final EditText content = (EditText) dailog.findViewById(R.id.dialog_sendnoti_content);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String sendContent = content.getText().toString();
                        mreplyTask = new sendReplyTask(sendContent, item.getString(2),1);
                        mreplyTask.execute((Void) null);
                    }
                });
                builder.setNeutralButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent(Intent.ACTION_SENDTO);
                        data.setData(Uri.parse("mailto:" + item.getString(2)));
                        String sendContent = content.getText().toString();
                        data.putExtra(Intent.EXTRA_SUBJECT, "Request form " + MainActivity.UnameValue + " --- co-life");
                        data.putExtra(Intent.EXTRA_TEXT, sendContent);
                        startActivity(data);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
//        sendReply.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                content=writeReply.getText().toString();
//                mreplyTask = new Reply.sendReplyTask(content);
//                mreplyTask.execute((Void) null);
//            }
//        });
        if(replyOK){
            content="OK";
            mreplyTask = new Reply.sendReplyTask(content,requestername,1);
            mreplyTask.execute((Void) null);
        }
    }

    @Override
    protected void onResume() {
        inflateRequestList();
        super.onResume();
    }

    public boolean reply(String content, String receiver,int type){
        boolean replysuccess=false;
        Message message=new Message(receiver,myusername,content,type,null);
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
        private String requester;
        private int type;
        public sendReplyTask(String content, String requester,int type) {
            super();
            this.content=content;
            this.requester=requester;
            this.type=type;
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
                sendSuccess= reply(content, requester,type);
            }
            if(sendSuccess){
                System.out.println("delete the haha");
                System.out.println(requester);
                System.out.println(deleteContent);
               // System.out.println("delete from localDatabase_request where requester = '"+requester+"' and content = '"+deleteContent+"'");
              // db.rawQuery("delete from localDatabase_request where requester = '"+requester+"' and content = '"+deleteContent+"'" , null);
               // String where=requester"+"= "+requester+"and content"+"= ";
                //String[] whereValue={requester,deleteContent};
                Data.deleteRequest(db, new String[]{requester, deleteContent});

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
