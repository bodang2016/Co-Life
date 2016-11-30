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

//This class is a background service which handle location service of the application and pull
// notification from server if user is login
public class BackgroundService extends Service {
    private static final String TAG = "Test";
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

    //The minimum location update time
    private static final long minTime = 20000;
    //The minimum location update distance
    private static final float minDistance = 300;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocalDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int notifyId = 1;

    public BackgroundService() {
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    //This method is called when the service has been started.
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStart--->");
        loadUserPreferences();
        System.out.println("Is login: " + logIn);
        System.out.println(checkIdentifer);
        System.out.println(identiferGroup);
        this.messagePendingThread = new MessagePendingThread();
        this.messagePendingThread.start();
        //Check whether the user is login, and start location service
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
    //This method called when the service has been destory, it contains the statement
    // to shutdown background socket connection
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy--->");
        super.onDestroy();
        this.flag = false;
        exit = true;
        this.messagePendingThread.interrupt();
        this.messagePendingThread = null;
        clientBackground.close();
    }

    //This method called when Service has been created, and prepare for connection
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
         * @return
         */
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    //this is the method for "sending"(evoke) different notifications on the client side, according to the type, sender and content.
    public void sendNoti(String title, String text, int type) {
        String smalltext = "message coming!";
        if (type == 0) {
            smalltext = "You received a request.";
        } else {
            smalltext = text;
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setTicker("Hi,message comming.")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.receive)
                        .setContentTitle(title)
                        .setContentText(smalltext)
                        .setAutoCancel(true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notification);
        builder.setLargeIcon(bitmap);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        if (type == 0) {
            Intent okintent = new Intent(this, Reply.class);
            okintent.putExtra("requestername", title);
            okintent.putExtra("myname", checkIdentifer);
            okintent.putExtra("reply", true);
            Intent replyintent = new Intent(this, Reply.class);
            replyintent.putExtra("requestername", title);
            replyintent.putExtra("myname", checkIdentifer);
            replyintent.putExtra("reply", false);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(okintent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent resultPendingIntent2 = PendingIntent.getActivity(this, 1, replyintent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
            builder.addAction(R.drawable.receive, "OK", resultPendingIntent);
            builder.addAction(R.drawable.reply, "Reply", resultPendingIntent2);
        } else {
            Intent showMessagesIntent = new Intent(this, Reply.class);
            showMessagesIntent.putExtra("requestername", title);
            showMessagesIntent.putExtra("myname", checkIdentifer);
            showMessagesIntent.putExtra("reply", false);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
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

    //This method load username and group number from share preference
    private void loadUserPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        checkIdentifer = settings.getString(PREF_UNAME, DefaultUnameValue);
        identiferGroup = settings.getString(PREF_GROUP, DefaultGroupValue);
        if (!checkIdentifer.equals(DefaultUnameValue) && !identiferGroup.equals(DefaultGroupValue)) {
            logIn = true;
        }
    }

    //This method pull notification from the server and store it in local database
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

        //
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                for (int i = 0; i < messages.size(); i++) {
                    Message messagei = messages.get(i);
                    sendNoti(messagei.getSender(), messagei.getContent(), messagei.getType());
                    dbHelper = new LocalDatabaseHelper(BackgroundService.this, "localDatabase.db", null, 1);
                    db = dbHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("username", messagei.getReceiver());
                    values.put("requester", messagei.getSender());
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


    //This is a thread that allow the service call pullMessageTask 20seconds a time
    private class MessagePendingThread extends Thread {
        @Override
        public void run() {
            while (!exit) {
                try {
                    if (!gettingMessage) {
                        Thread.sleep(20000);
                        if (!checkIdentifer.equals(DefaultUnameValue) && !identiferGroup.equals(DefaultGroupValue)) {
                            mpullMessageTask = new PullMessageTask(checkIdentifer);
                            mpullMessageTask.execute((Void) null);
                        }
                    } else {
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
