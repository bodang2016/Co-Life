package com.example.bodang.co_life.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bodang.co_life.Management.CustomListView;
import com.example.bodang.co_life.Objects.Message;
import com.example.bodang.co_life.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.bodang.co_life.Activities.MainActivity.client;

public class MessageActivity extends AppCompatActivity {
    private CustomListView list;

    private String[] name = {"Beef", "Lamb", "Pizza", "Chicken fillet roll"};

    private String[] desc = {"Hello World", "Hello World", "Hello World", "Hello World"};

    private int[] imageids = {R.drawable.back1, R.drawable.back2,
            R.drawable.back3, R.drawable.tesco};
    private sendMessageTask msendMessageTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent=getIntent();
        final String sender=intent.getStringExtra("username");
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(sender);
        setSupportActionBar(toolbar);
        list = (CustomListView) findViewById(R.id.list_message);
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            listem.put("type", imageids[i]);
            listem.put("name", name[i]);
            listem.put("desc", desc[i]);
            listems.add(listem);
        }
        inflateList(listems);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                String sendTo = sender;
                String sendContent = item.get("name").toString();
                msendMessageTask = new sendMessageTask(MainActivity.UnameValue, sendTo,sendContent,0);
                msendMessageTask.execute((Void) null);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMessage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void inflateList(List<Map<String, Object>> listems) {
        SimpleAdapter adapter = new SimpleAdapter(this, listems,
                R.layout.message_item, new String[]{"type", "name", "desc"},
                new int[]{R.id.message_image, R.id.message_title, R.id.message_desc});
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


    public class sendMessageTask extends AsyncTask<Void, Void, Boolean> {
        private String mUsername;
//        private String mcontent;
//        private int mtype;
//        private String mreceiver;
        private Message message;

        public sendMessageTask(String sender, String receiver, String content, int type) {
            super();
//            this.mcontent = content;
            this.mUsername = sender;
//            this.mtype = type;
//            this.mreceiver = receiver;
            message = new Message(receiver, sender, content, type, null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = client.Init();
            Boolean sent;
            if (result == 1) {
                sent = client.sendMessage(mUsername, message);
                System.out.println("Sending message");
                return sent;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(MessageActivity.this, "send successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MessageActivity.this, "send unsuccessful", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            msendMessageTask = null;
        }
    }
}
