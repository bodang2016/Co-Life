package com.example.bodang.co_life.Management;

/**
 * Created by Bodang on 09/11/2016.
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;

import static com.example.bodang.co_life.Activities.MainActivity.client;
import static com.example.bodang.co_life.Management.BackgroundService.clientBackground;

public class GpsLocationListener implements LocationListener {


    private final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
    private final String DefaultUnameValue = "Guest";
    private final String DefaultGroupValue = "You have not enrolled in any group";
    private String userName = DefaultUnameValue;
    private String userGroup = DefaultGroupValue;
    private Double Longitude;
    private Double Latitude;
    private Boolean updateResult = false;
    private updateLocationTask mupdateLocationTask = null;

    public GpsLocationListener(String uName, String uGroup) {
        this.userName = uName;
        this.userGroup = uGroup;
    }

    @Override
    public void onLocationChanged(Location location) {
        // get data and post to the server
        System.out.println(!userName.equals(DefaultUnameValue) + " " + !userGroup.equals(DefaultGroupValue));
        if (null != location && !userName.equals(DefaultUnameValue) && !userGroup.equals(DefaultGroupValue)) {
            Date curDate = new Date(System.currentTimeMillis());

            System.out.println(userName + " with Group " + userGroup + " ready for update location");
            Longitude = location.getLongitude();
            Latitude = location.getLatitude();
            System.out.println("纬度：" + Longitude);
            System.out.println("经度：" + Latitude);
            System.out.println("精度：" + location.getAccuracy());
            mupdateLocationTask = new updateLocationTask(userName, Longitude, Latitude);
            mupdateLocationTask.execute((Void) null);
        }

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    public class updateLocationTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final Double mLongtitude;
        private final Double mLatitude;

        public updateLocationTask(String userName, Double Longtitude, Double Latitude) {
            super();
            mUsername = userName;
            mLongtitude = Longtitude;
            mLatitude = Latitude;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            updateResult = false;
            int result = clientBackground.Init();
            if (result == 1) {
                updateResult = clientBackground.UpdateLocation(mUsername, mLongtitude, mLatitude);
                return updateResult;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            System.out.println("Update location result is " + updateResult);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mupdateLocationTask = null;
        }
    }

}
