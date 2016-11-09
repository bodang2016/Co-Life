package com.example.bodang.co_life;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class BackgroundService extends Service {
    private static final String TAG = "Test";
    private int timer = 0;
    private boolean timerSwitch = false;
    private String Title = "All systems Green";
    private String Text = "Waiting fo instructions";

    public BackgroundService() {
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setText(String text) {
        Text = text;
    }

    public int getTime() {
        return timer;
    }

    public void startTimer() {
        if (timerSwitch == false) {
            timerSwitch = true;
            handler.post(runnable);
        }
    }

    public void stopTimer() {
        timerSwitch = false;
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (timerSwitch == true) {
                timer++;
                System.out.println(timer);
                sendNoti(Title, Text);
                handler.postDelayed(runnable, 8000);
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStart--->");
        startTimer();
//        String data = intent.getStringExtra("input");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service onDestroy--->");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
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


    public void sendNoti(String title, String text) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setTicker("Hi,Notification is here")
                        .setSmallIcon(R.drawable.notice)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notice);
        mBuilder.setLargeIcon(bitmap);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
//
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        Intent resultIntent2 = new Intent(this, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent2 = PendingIntent.getActivity(this,1,resultIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
        //mBuilder.setContentIntent(resultPendingIntent);
        //
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("New power sterted"));
        mBuilder.addAction(R.drawable.notice,"buy",resultPendingIntent);
        mBuilder.addAction(R.drawable.notice,"nobuy",resultPendingIntent2);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
// mId allows you to update the notification later on.
        mNotificationManager.notify(4, mBuilder.build());
    }
}
