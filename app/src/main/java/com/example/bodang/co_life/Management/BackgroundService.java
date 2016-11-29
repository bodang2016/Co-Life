package com.example.bodang.co_life.Management;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bodang.co_life.Activities.MainActivity;
import com.example.bodang.co_life.Activities.Reply;
import com.example.bodang.co_life.Database.Data;
import com.example.bodang.co_life.Database.LocalDatabaseHelper;
import com.example.bodang.co_life.Objects.Message;
import com.example.bodang.co_life.R;

import java.util.ArrayList;

import static com.example.bodang.co_life.Activities.MainActivity.client;

public class BackgroundService extends Service {
    private static final String TAG = "Test";
    private String Title = "All systems Green";
    private String Text = "Waiting fo instructions";

    private final String PREFS_NAME = "preferences";
    private final String PREF_UNAME = "Username";
    private final String PREF_GROUP = "Groupname";
    private final String DefaultUnameValue = "Guest";
    private final String DefaultGroupValue = "You have not enrolled in any group";
    private String identiferGroup = DefaultGroupValue;
    private String checkIdentifer = DefaultUnameValue;
    private Boolean logIn = false;
    private boolean flag;
    public static Client clientBackground;
    public Boolean gettingMessage = false;

    private PullMessageTask mpullMessageTask = null;
    private MessagePendingThread messagePendingThread;
    public volatile boolean exit = false;

    // 2000ms
    private static final long minTime = 20000;
    // 最小变更距离 10m
    private static final float minDistance = 300;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocalDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int notifyId=1;
//    public int requestCode=0;
    public BackgroundService() {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStart--->");
        loadUserPreferences();
        System.out.println("Is login: " + logIn);
        System.out.println(checkIdentifer);
        System.out.println(identiferGroup);
        this.messagePendingThread = new MessagePendingThread();
        this.messagePendingThread.start();
        if (logIn) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new GpsLocationListener(checkIdentifer, identiferGroup);
            try {
                this.flag = true;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance,
                        locationListener);
                System.out.println("Start Location service");
            } catch (SecurityException e) {
                System.out.println("Can not get permission");
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy--->");
        super.onDestroy();
        this.flag = false;
        exit = true;
        this.messagePendingThread.interrupt();
        this.messagePendingThread=null;
        clientBackground.close();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        clientBackground = new Client();
        Log.i(TAG, "Service onCreate--->");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }


    public void sendNoti(String title, String text, int type) {
        String smalltext="message coming!";
        if(type==0) {
            smalltext="You received a request.";
        }
        else {
            smalltext=text;
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setTicker("Hi,message comming.")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.notice)
                        .setContentTitle(title)
                        .setContentText(smalltext)
                        .setAutoCancel(true);
        if(type==0) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notice);
        builder.setLargeIcon(bitmap);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
            // Creates an explicit intent for an Activity in your app
            Intent okintent = new Intent(this, Reply.class);
            okintent.putExtra("requestername", title);
            okintent.putExtra("myname", checkIdentifer);
            okintent.putExtra("reply", true);
            Intent replyintent = new Intent(this, Reply.class);
            replyintent.putExtra("requestername", title);
            replyintent.putExtra("myname", checkIdentifer);
            replyintent.putExtra("reply", false);
            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(okintent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent resultPendingIntent2 = PendingIntent.getActivity(this, 1, replyintent, PendingIntent.FLAG_UPDATE_CURRENT);
            //builder.setContentIntent(resultPendingIntent);
            //
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
            builder.addAction(R.drawable.notice, "OK", resultPendingIntent);
            builder.addAction(R.drawable.notice, "Reply", resultPendingIntent2);
        }
        else{
            Intent showMessagesIntent = new Intent(this, Reply.class);
            showMessagesIntent.putExtra("requestername", title);
            showMessagesIntent.putExtra("myname", checkIdentifer);
            showMessagesIntent.putExtra("reply", false);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(showMessagesIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
        }
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        mNotificationManager.notify(notifyId, builder.build());
        notifyId++;
    }
    private void loadUserPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        checkIdentifer = settings.getString(PREF_UNAME, DefaultUnameValue);
        identiferGroup = settings.getString(PREF_GROUP, DefaultGroupValue);
        if (!checkIdentifer.equals(DefaultUnameValue) && !identiferGroup.equals(DefaultGroupValue)) {
            logIn = true;
        }
    }

    public class PullMessageTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private ArrayList<Message> messages;

        public PullMessageTask(String userName) {
            super();
            mUsername = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gettingMessage = true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = clientBackground.Init();
            if (result == 1) {
                messages = clientBackground.readMessage(checkIdentifer);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                for (int i = 0; i < messages.size(); i++) {
                    Message messagei=messages.get(i);
                    sendNoti(messagei.getSender(), messagei.getContent(),messagei.getType());
                    dbHelper = new LocalDatabaseHelper(BackgroundService.this, "localDatabase.db", null, 1);
                    db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("username",messagei.getReceiver());
                    values.put("requester",messagei.getSender());
                    values.put("content", messagei.getContent());
                    values.put("time", messagei.getTime().toString());
                    Data.insertRequest(db, values);
                    System.out.println("PullMessageTask return true");
                }
            } else {

            }
            gettingMessage = false;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mpullMessageTask = null;
        }
    }


    private class MessagePendingThread extends Thread {
        @Override
        public void run() {
            while (!exit) {
                try {
                    if(!gettingMessage) {
                        Thread.sleep(20000);
                        if (!checkIdentifer.equals(DefaultUnameValue) && !identiferGroup.equals(DefaultGroupValue)) {
                            mpullMessageTask = new PullMessageTask(checkIdentifer);
                            mpullMessageTask.execute((Void) null);
                        }
                    }
                    else{
                        Thread.sleep(20000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Service exit!");
        }
    }

}
