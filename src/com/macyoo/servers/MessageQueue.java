package com.macyoo.servers;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.macyoo.chat.User;

public class MessageQueue {

	//public HashMap<SelectionKey, String> messageHash;
	public LinkedList<SelectionKeyMessage> messageQueue;
	
	//public HashMap<SelectionKey, String> replyHash;
	public LinkedList<SelectionKeyMessage> replyQueue;
	
	public HashMap<SelectionKey, User> userHash;
	
	int maxHandle = 1000;
	
	public int readCount;
	public int sendCount;
	
	public int connectCount;
	
	public MessageQueue() {

		//messageHash = new HashMap<SelectionKey,String>();
		messageQueue = new LinkedList<SelectionKeyMessage>();
		
		//replyHash = new HashMap<SelectionKey,String>();
		replyQueue = new LinkedList<SelectionKeyMessage>();
		
		userHash = new HashMap<SelectionKey, User>();
	}
	
	public int getMaxHandle() {
		return maxHandle;
	}
}
