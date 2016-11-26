package com.example.bodang.co_life.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
    private String[] name;

    private String[] desc;

    private int[] imageids;
    private String[] name0 = {"Coming"};

    private String[] desc0= {"Hello World"};

    private int[] imageids0={R.drawable.back1};
    private final String[] name1 = {"Beef", "Lamb", "Pizza", "Chicken fillet roll"};

    private final String[] desc1= {"Hello World", "Hello World", "Hello World", "Hello World"};

    private final int[] imageids1 = {R.drawable.back1, R.drawable.back2,
            R.drawable.back3, R.drawable.tesco};
    private final String[] name2 = {"study", "print"};

    private final String[] desc2= {"Hello World", "Hello World"};

    private final int[] imageids2 = {R.drawable.study, R.drawable.study,};
    private final String[] name3 = {"salad"};

    private final String[] desc3= {"Hello World"};

    private final int[] imageids3 = {R.drawable.salad};
    private final String[] name4 = {"coffee"};

    private final String[] desc4= {"Hello World"};

    private final int[] imageids4 = {R.drawable.coffee};
    private sendMessageTask msendMessageTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent=getIntent();
        final String sender=intent.getStringExtra("username");
        final int locationType=intent.getIntExtra("locationType",0);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(sender);
        setSupportActionBar(toolbar);
        list = (CustomListView) findViewById(R.id.list_message);
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        switch (locationType) {
            case 0:
                name = name2;
                desc= desc2;
                imageids = imageids2;
                break;
            case R.drawable.shopping:
                name = name2;
                desc= desc2;
                imageids = imageids2;
                break;
            case R.drawable.library:
                name = name2;
                desc= desc2;
                imageids = imageids2;
                break;
            case R.drawable.restaurant:
                name = name3;
                desc= desc3;
                imageids = imageids3;
                break;
            case R.drawable.home:
                name = name4;
                desc= desc4;
                imageids = imageids4;
                break;
            default:
                break;
        }
        System.out.println(locationType+"hahahahhahaha");
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
//                String sendTo = sender;
                String sendContent = item.get("name").toString();
                msendMessageTask = new sendMessageTask(MainActivity.UnameValue, sender,sendContent,0);
                msendMessageTask.execute((Void) null);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMessage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Write a Message");
                View dailog = LayoutInflater.from(MessageActivity.this).inflate(R.layout.dialog_sendnoti, null);
                builder.setView(dailog);

                final EditText content = (EditText) dailog.findViewById(R.id.dialog_sendnoti_content);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String sendContent = content.getText().toString();
                        msendMessageTask = new sendMessageTask(MainActivity.UnameValue, sender,sendContent,0);
                        msendMessageTask.execute((Void) null);
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
        private Message message;

        public sendMessageTask(String sender, String receiver, String content, int type) {
            super();
            this.mUsername = sender;
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
