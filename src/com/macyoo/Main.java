package com.macyoo;

import java.net.UnknownHostException;

import com.macyoo.db.connection.PostgresConnectionManager;
import com.macyoo.servers.MessageHandleServer;
import com.macyoo.servers.MessageQueue;
import com.macyoo.servers.QueueHandleThread;

public class Main {
	
	//static DatabasePostgresql con = null;
	
	static MessageQueue storage;
	
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
		
		storage = new MessageQueue();
		
		MessageHandleServer mH = new MessageHandleServer(storage,9090);
		mH.start();
		
		// multi thread
		QueueHandleThread t = new QueueHandleThread(storage, pcm);
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
