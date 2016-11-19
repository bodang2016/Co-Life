package com.example.bodang.co_life.Objects;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String userPassword;
	private int userRoomNo;
	private String location;
	private double longtitude;
	private double latitude;
	private String time;
	public User(String userId, String userPassword, int userRoomNo, double longtitude,
			double latitude) {
		//super();
		this.time="No location data";
		this.userId = userId;
		this.userPassword = userPassword;
		this.userRoomNo = userRoomNo;
		this.longtitude = longtitude;
		this.latitude = latitude;
	}
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
	public User() {
		
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


}
