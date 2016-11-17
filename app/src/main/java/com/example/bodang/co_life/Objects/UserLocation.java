package com.example.bodang.co_life.Objects;

import java.io.Serializable;

public class UserLocation  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double longitude;
	double latitude;
	public UserLocation(double longitude,double latitude){
		this.longitude=longitude;
		this.latitude=latitude;
	}
	public void setlongitude(double longitude){
		this.longitude=longitude;
	}
	public double getlongitude(){
		return this.longitude;
	}
	public void setlatitude(double latitude){
		this.latitude=latitude;
	}
	public double getlatitude(){
		return this.latitude;
	}
}
