package com.macyoo.chat;

import java.nio.channels.SocketChannel;

public class User {
	
	private SocketChannel sc;
	private String userID;
	
	int	type;
	
	public User() {
	}

	public void setSocketChannel(SocketChannel sc) {
		this.sc = sc;
	}

	public SocketChannel getSocketChannel() {
		return this.sc;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}


}
