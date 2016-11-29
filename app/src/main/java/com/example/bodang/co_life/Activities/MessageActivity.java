package com.example.bodang.co_life.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

    private String[] desc0 = {"Hello World"};

    private int[] imageids0 = {R.drawable.back1};
//Tesco
    private final String[] name1 = {"Beef","Sausage","Bread","Cereal","Fruit","Milk"};
    private final String[] desc1 = {"Beef muscle meat can be cut into roasts, short ribs or steak.", "Sausages may be preserved by curing,drying,smoking or freezing.", "Bread is served in various forms with any meal of the day.", "In their natural form, they are a rich source of vitamins, minerals." +
            "carbohydrates, fats, oils, and protein.", "Apples, bananas, grapes, lemons, oranges, and strawberries.", "Milk is a pale liquid produced by the mammary glands of mammals."};
    private final int[] imageids1 = {R.drawable.steak,R.drawable.sausage,R.drawable.bread,R.drawable.cereal,R.drawable.fruit,R.drawable.milk};

//library
    private final String[] name2 = {"Book","Print","Pen","Copy","Scan","Notebook"};
    private final String[] desc2 = {"Rent a specific book from the library.", "What files do you want to be printed?","Do you need some pens?",
            "What files do you want to be copied?","What files do you want to be scanned?","Do you want a new notebook?"};
    private final int[] imageids2 = {R.drawable.book,R.drawable.print,R.drawable.pen,R.drawable.copier,R.drawable.scanner,R.drawable.notebook};

    //restaurant
    private final String[] name3 = {"Breakfast","Pasta","Pizza","Rice","Salad","Cupcake"};
    private final String[] desc3 = {"A full breakfast is a breakfast meal, usually including bacon, sausages, eggs, and a variety of other cooked foods, with a beverage such as coffee or tea which originated in England, United Kingdom.","As a category in Italian cuisine, both fresh and dried pastas are classically used in one of three kinds of prepared dishes.","Pizza is a flatbread generally topped with tomato sauce and cheese and baked in an oven.",
            "As a cereal grain, it is the most widely consumed staple food for a large part of the world's human population, especially in Asia.","A salad is a dish consisting of a mixture of small pieces of food, which may be mixed with a sauce or salad dressing.","As with larger cakes, icing and other cake decorations, such as candy, may be applied."};
    private final int[] imageids3 = {R.drawable.breakfast,R.drawable.noodle,R.drawable.pizza,R.drawable.rice,R.drawable.salad,R.drawable.cupcake};

    //home
    private final String[] name4 = {"Laundry","Kettle","Mop","Rubbish","Vacuum","Heater"};
    private final String[] desc4 = {"Can you help me clean my clothing and linens?","Can you heat a kettle of water?","It is time to mop the ground","It is time to throw the rubbish!",
            "It is time to vacuum the dormitory!", "Can you open heater in room?"};
    private final int[] imageids4 = {R.drawable.laundry,R.drawable.kettle,R.drawable.mopper,R.drawable.rubbish,R.drawable.vacuum,R.drawable.heater};

//others
    private final String[] name5 = {"Beer","Movie","Lunch","Party","Date","Dinner"};
    private final String[] desc5 = {"Would you like to drink a beer with me?","Would you like to et lunch with me?","Would you like to see a moive with me tonight?","Let's join party tonight!",
    "Would you have a date with me today?","Would you like to eat dinner with me?"};
    private final int[] imageids5={R.drawable.beer,R.drawable.moive,R.drawable.lunch,R.drawable.party,R.drawable.dating,R.drawable.dinner};


    private sendMessageTask msendMessageTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        final String sender = intent.getStringExtra("username");
        final int locationType = intent.getIntExtra("locationType", 0);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(sender);
        setSupportActionBar(toolbar);
        list = (CustomListView) findViewById(R.id.list_message);
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        switch (locationType) {
            case R.drawable.nowhere:
                name = name5;
                desc = desc5;
                imageids = imageids5;
                break;
            case R.drawable.shopping:
                name = name2;
                desc = desc2;
                imageids = imageids2;
                break;
            case R.drawable.library:
                name = name2;
                desc = desc2;
                imageids = imageids2;
                break;
            case R.drawable.restaurant:
                name = name3;
                desc = desc3;
                imageids = imageids3;
                break;
            case R.drawable.home:
                name = name4;
                desc = desc4;
                imageids = imageids4;
                break;
            default:
                break;
        }
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
                final String sendContent = item.get("desc").toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
                builder.setIcon(R.drawable.notice);
                builder.setTitle("Send Express Message");
                View dailog = LayoutInflater.from(MessageActivity.this).inflate(R.layout.dialog_expressmessage, null);
                builder.setView(dailog);
                final TextView content = (TextView) dailog.findViewById(R.id.dialog_expressmessage_content);
                content.setText(sendContent);
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        msendMessageTask = new sendMessageTask(MainActivity.UnameValue, sender, sendContent, 0);
                        msendMessageTask.execute((Void) null);
                    }
                });
                builder.setNeutralButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent(Intent.ACTION_SENDTO);
                        data.setData(Uri.parse("mailto:" + sender));
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
                        msendMessageTask = new sendMessageTask(MainActivity.UnameValue, sender, sendContent, 0);
                        msendMessageTask.execute((Void) null);
                    }
                });
                builder.setNeutralButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent(Intent.ACTION_SENDTO);
                        data.setData(Uri.parse("mailto:" + sender));
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
