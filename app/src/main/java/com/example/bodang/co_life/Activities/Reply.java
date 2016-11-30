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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.bodang.co_life.Database.Data;
import com.example.bodang.co_life.Database.LocalDatabaseHelper;
import com.example.bodang.co_life.Management.CustomListView;
import com.example.bodang.co_life.Objects.Message;
import com.example.bodang.co_life.R;

import static com.example.bodang.co_life.Database.Data.client;
import static com.example.bodang.co_life.Management.BackgroundService.clientBackground;
/**
 * A message list screen that shows messages which the user has not replied, and allow user to reply.
 */
public class Reply extends AppCompatActivity {
    String myusername;
    String requestername;
    String content;
    boolean replyOK;
    private sendReplyTask mreplyTask = null;
    private SwipeRefreshLayout swipeLayout;
    private LocalDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private CustomListView list;
    private SimpleCursorAdapter adapter;
    private String deleteContent;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Intent intent = getIntent();
        requestername = intent.getStringExtra("requestername");
        myusername = intent.getStringExtra("myname");
        replyOK = intent.getBooleanExtra("reply", false);
        dbHelper = new LocalDatabaseHelper(this, "localDatabase.db", null, 1);
        list = (CustomListView) findViewById(R.id.reply_list);
        db = dbHelper.getReadableDatabase();
        scrollView = (ScrollView) findViewById(R.id.reply_scrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.reply_refresh_layout);
        //when refresh, update the list( from local database)
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inflateRequestList();
            }
        });
        //set color of the refresh layout (circle)
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_dark);
        //when click one item(message/request), call a dialog
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
                //send button for sending the reply using this app
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String sendContent = content.getText().toString();
                        mreplyTask = new sendReplyTask(sendContent, item.getString(2), 1);
                        mreplyTask.execute((Void) null);
                    }
                });
                //send email
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
                //cancel
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        //if clicked OK on notification,directly send it.
        if (replyOK) {
            content = "OK";
            mreplyTask = new Reply.sendReplyTask(content, requestername, 1);
            mreplyTask.execute((Void) null);
        }
    }

    @Override
    //this method will call when activity resume
    protected void onResume() {
        inflateRequestList();
        super.onResume();
    }

    public boolean reply(String content, String receiver, int type) {
        boolean replysuccess = false;
        Message message = new Message(receiver, myusername, content, type, null);
        replysuccess = clientBackground.sendMessage(myusername, message);
        return replysuccess;
    }
    //fill the ListView with requests/messages which has not been replied.
    public void inflateRequestList() {
        cursor = db.rawQuery("select * from localDatabase_request where username = '" + myusername + "'", null);
        adapter = new SimpleCursorAdapter(this, R.layout.blackboard_item,
                cursor, new String[]{"requester", "content", "time"}, new int[]{R.id.blackboard_name, R.id.blackboard_content, R.id.blackboard_time},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list);
        list.setFocusable(false);
        swipeLayout.setRefreshing(false);
    }
    //the class for sending the reply
    public class sendReplyTask extends AsyncTask<Void, Void, Boolean> {
        private String content;
        private String requester;
        private int type;
    //constructor
        public sendReplyTask(String content, String requester, int type) {
            super();
            this.content = content;
            this.requester = requester;
            this.type = type;
        }
        //show the refresh circle
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }
        ////if connect success, send the notice.
        //if send success, delete that message from the local database.
        @Override
        protected Boolean doInBackground(Void... params) {
            boolean sendSuccess = false;
            int result = clientBackground.Init();
            if (result == 1) {
                sendSuccess = reply(content, requester, type);
            }
            if (sendSuccess) {
                System.out.println("delete the haha");
                System.out.println(requester);
                System.out.println(deleteContent);
                Data.deleteRequest(db, new String[]{requester, deleteContent});

            }
            return sendSuccess;
        }
        //show what happened.
        @Override
        protected void onPostExecute(final Boolean success) {
            swipeLayout.setRefreshing(false);
            if (!success) {
                Toast.makeText(Reply.this, "Something wrong, please check your internet connection", Toast.LENGTH_SHORT).show();
            } else {
                inflateRequestList();
                Toast.makeText(Reply.this, "Your reply has been sent", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mreplyTask = null;
        }
    }

    //this method return the static height to custom listview
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
}
