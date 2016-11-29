package com.example.bodang.co_life.Objects;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This is the Class for sent carried by Carrier, and store the information about a user.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userPassword;
    private int userRoomNo;
    private String locationName;
    private double longtitude;
    private double latitude;
    private Timestamp time;
    private int locationType;

    //constructor
    public User(String userId, String userPassword, int userRoomNo, double longtitude,
                double latitude) {
        java.util.Date date = new java.util.Date();
        Timestamp time = new Timestamp(date.getTime());
        this.time = time;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userRoomNo = userRoomNo;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.locationType = 0;
        this.locationName = " ";
    }

    //a different constructor with more parameters.
    public User(String userId, String userPassword, int userRoomNo, double longtitude,
                double latitude, Timestamp time, String locationName, int locationType) {
        this.time = time;
        this.userId = userId;
        this.userPassword = userPassword;
        this.userRoomNo = userRoomNo;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.locationName = locationName;
        this.locationType = locationType;
    }

    //constructor without any parameter
    public User() {

    }

    //get and set methods
    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserRoomNo() {
        return userRoomNo;
    }

    public void setUserRoomNo(int userRoomNo) {
        this.userRoomNo = userRoomNo;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }


}
