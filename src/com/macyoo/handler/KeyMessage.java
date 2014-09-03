package com.macyoo.handler;

import java.nio.channels.SelectionKey;

public class KeyMessage {

	SelectionKey key;
	String message;
	
	public KeyMessage(SelectionKey key, String message) { 
		
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
