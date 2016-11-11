package com.example.bodang.co_life.Management;

/**
 * Created by Bodang on 09/11/2016.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GpsLocationListener implements LocationListener {


    private final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
    @Override
    public void onLocationChanged(Location location) {
        // get data and post to the server
        if(null != location)
        {
            Date curDate = new Date(System.currentTimeMillis());

            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("Latitude:");
            strBuffer.append(location.getLatitude());
            strBuffer.append(", Longitude:");
            strBuffer.append(location.getLongitude());
            strBuffer.append(", time: ");
            strBuffer.append(timestampFormat.format(curDate));

//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("LocationData", strBuffer.toString()));
//            userServ.PostDataToServer(params);
            System.out.println("纬度："+location.getLatitude());
            System.out.println("经度："+location.getLongitude());
            System.out.println("精度："+location.getAccuracy());
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

}
