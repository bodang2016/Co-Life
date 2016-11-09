package com.example.bodang.co_life.Objects;

import java.util.ArrayList;


public class User {
	private String userId;
	private String userName;
	private String userPassword;
	private int userRoomNo;
	
	public User(String userId, String userName, String userPassword, int userRoomNo) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userRoomNo = userRoomNo;
	}
	public User() {
		
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	
	//DAI 2016/10/30
	private ArrayList<User> friendList;
	
	public ArrayList<User> getFriendList() {
		return friendList;
	}
	
	public void setFriendList(ArrayList<User> friendlist){
		this.friendList=friendlist;
	}
	
	
	
	

}
