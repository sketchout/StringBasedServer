package com.macyoo.handler;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.macyoo.chat.User;

public class MessageBuffer {

	//public HashMap<SelectionKey, String> messageHash;
	public LinkedList<KeyMessage> messageQueue;
	
	//public HashMap<SelectionKey, String> replyHash;
	public LinkedList<KeyMessage> replyQueue;
	
	public HashMap<SelectionKey, User> userHash;
	
	int maxHandle = 1000;
	
	public int readCount;
	public int sendCount;
	
	public int connectCount;
	
	public MessageBuffer() {

		//messageHash = new HashMap<SelectionKey,String>();
		messageQueue = new LinkedList<KeyMessage>();
		
		//replyHash = new HashMap<SelectionKey,String>();
		replyQueue = new LinkedList<KeyMessage>();
		
		userHash = new HashMap<SelectionKey, User>();
	}
	
	public int getMaxHandle() {
		return maxHandle;
	}
}
