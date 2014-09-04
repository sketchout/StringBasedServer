package com.macyoo;

import java.net.UnknownHostException;

import com.macyoo.db.connection.PostgresConnectionManager;
import com.macyoo.servers.MessageHandleServer;
import com.macyoo.servers.MessageQueue;
import com.macyoo.servers.QueueHandleThread;

public class Main {
	
	
	static MessageQueue storage;
	static PostgresConnectionManager pcm;

	MessageHandleServer mainThread;
	QueueHandleThread qThread;
	
	
	public Main() throws UnknownHostException, ClassNotFoundException {
		
		pcm = new PostgresConnectionManager();
		storage = new MessageQueue();
	
		mainThread = new MessageHandleServer(storage,9090);
		qThread = new QueueHandleThread(storage, pcm);
	}

	public void beginStart() throws InterruptedException {
		
		mainThread.start();
		qThread.start();
		
		qThread.join();
		
	}
	
	public static void main(String[] args)  {
		
		Main m = null;
		try {
			m = new Main();
		} catch (UnknownHostException | ClassNotFoundException e) {

			e.printStackTrace();
			System.out.println("System exit");
			System.exit(1);
			
		}
		try {
			m.beginStart();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("System exit");
			System.exit(1);
		}
	}
}
