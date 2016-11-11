package com.example.bodang.co_life.Management;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.bodang.co_life.Activities.MainActivity;
import com.example.bodang.co_life.R;

public class BackgroundService extends Service {
    private static final String TAG = "Test";
    private String Title = "All systems Green";
    private String Text = "Waiting fo instructions";

    // 2000ms
    private static final long minTime = 1200;
    // 最小变更距离 10m
    private static final float minDistance = 10;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public BackgroundService() {
    }

//    Handler handler = new Handler();
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            if (timerSwitch == true) {
//                timer++;
//                sendNoti(Title, Text);
//                handler.postDelayed(runnable, 4000);
//            }
//        }
//    };

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service onStart--->");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new GpsLocationListener();
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance,
                    locationListener);
            System.out.println("Start service");
        } catch (SecurityException e) {
            System.out.println("Can not get permission");
        }
//        return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
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
        mNotificationManager.notify(4, mBuilder.build());
    }
}
