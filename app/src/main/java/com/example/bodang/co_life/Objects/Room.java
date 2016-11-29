package com.example.bodang.co_life.Objects;

import java.io.Serializable;

/**
 * This is the Class for sent carried by Carrier, and store the information about a room which could be enrolled by a group of users.
 */
public class Room implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int roomId;
    String roomPassword;

    //constructor
    public Room(int roomId, String roomPassword) {
        //super();
        this.roomId = roomId;
        this.roomPassword = roomPassword;
    }

    //get and set methods
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
