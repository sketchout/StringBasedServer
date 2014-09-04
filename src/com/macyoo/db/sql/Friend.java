package com.macyoo.db.sql;

public class Friend {

	private String friendId;
	private String onLine;
	
	public Friend(String id, String on) {
		
		this.friendId = id;
		this.onLine = on;
	}
	
	public String getFriendId() {
		return friendId;
	}
	public String getOnLine() {
		return onLine;
	}
}
