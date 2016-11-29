package com.example.bodang.co_life.Objects;

import java.io.Serializable;

/**
 * This is the Class for sent carried by Carrier, and store the information about a location defined by user.
 */
public class DefinedLocation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	int locationType;
	double longitude;
	double latitude;
	int roomId;
	//constructor
	public DefinedLocation(String name, int locationType, double longitude, double latitude, int roomId) {
		super();
		this.name = name;
		this.locationType = locationType;
		this.longitude = longitude;
		this.latitude = latitude;
		this.roomId = roomId;
	}
	//get and set methods
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLocationType() {
		return locationType;
	}
	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	

}
