package com.macyoo.main;

import java.net.UnknownHostException;

import com.macyoo.db.PostgresConnectionManager;
import com.macyoo.handler.HandlerMessage;
import com.macyoo.handler.HandlerQueue;
import com.macyoo.handler.MessageBuffer;

public class Main {
	
	//static DatabasePostgresql con = null;
	
	static MessageBuffer storage;
	
	static PostgresConnectionManager pcm;
	
	// main ()
	public static void main(String[] args) throws UnknownHostException  {
		try {
			//con = new DatabasePostgresql();
			//con.connect("mydb");
			
			// // con.close(); //
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		pcm = new PostgresConnectionManager();
		
		storage = new MessageBuffer();
		
		HandlerMessage mH = new HandlerMessage(storage,9090);
		mH.start();
		
		// multi thread
		HandlerQueue t = new HandlerQueue(storage, pcm);
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
