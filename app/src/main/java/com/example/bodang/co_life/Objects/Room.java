package com.example.bodang.co_life.Objects;

import java.io.Serializable;

public class Room implements Serializable{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int   roomId;
	String roomPassword;
    public Room(int roomId, String roomPassword) {
		//super();
		this.roomId = roomId;
		this.roomPassword = roomPassword;
	}

	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getRoomPassword() {
		return roomPassword;
	}
	public void setRoomPassword(String roomPassword) {
		this.roomPassword = roomPassword;
	}
     
}
