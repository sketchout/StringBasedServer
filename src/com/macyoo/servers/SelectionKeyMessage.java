package com.macyoo.servers;

import java.nio.channels.SelectionKey;

public class SelectionKeyMessage {

	SelectionKey key;
	String message;
	
	public SelectionKeyMessage(SelectionKey key, String message) { 
		
		this.key = key;
		this.message = message;
	}
	
	public SelectionKey getKey() {
		return key;
	}
	public String getMessage() {
		return message;
	}
}
