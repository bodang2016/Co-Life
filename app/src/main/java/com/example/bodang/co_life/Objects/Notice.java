package com.example.bodang.co_life.Objects;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * This is the Class for sent carried by Carrier, and store the information about a notice, which would be published on the blackboard.
 */
public class Notice implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private int groupId;
	private String content;
	private Timestamp time;
	//constructor
	public Notice(String username,String content, Timestamp time, int groupid){
		this.userName=username;
		this.content=content;
		this.time=time;
		this.groupId=groupid;
	}
	//get and set methods
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
}
