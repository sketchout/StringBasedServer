package com.macyoo.servers;


import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.macyoo.chat.User;
import com.macyoo.db.connection.PostgresConnectionManager;
import com.macyoo.db.sql.Friend;
import com.macyoo.db.sql.FriendQuery;
import com.macyoo.db.sql.UserQuery;
import com.macyoo.util.LoggerHandle;

public class QueueHandleThread extends Thread {

	MessageQueue m;
	PostgresConnectionManager pcm;
	
	Iterator<Map.Entry<SelectionKey,String>> iter;
	Map.Entry<SelectionKey,String> entry;
	
	protected Logger logger ;
	protected LoggerHandle logHandler = null ;

	JSONTokener token = null; 
	JSONObject root = null;	
	
	int testCount =0;
	int processCount=0;
	
	User u;
	
	Iterator<SelectionKeyMessage> it;
	SelectionKeyMessage kM;
	
	public QueueHandleThread(MessageQueue m, PostgresConnectionManager pcm) {
		this.m = m;
		this.pcm = pcm;
		setLogger();
	}
	
	public void run() {
		
		while(true) {
			process();
		}
	}
	
	void process() {
		processCount=0;
		/*
		synchronized (m.messageHash) {
        	iter = m.messageHash.entrySet().iterator();
        	
        	while ( iter.hasNext() ) 
        	{
        		entry = iter.next();
	    		//logHandler.error( "[ String("+ entry.getValue()+")"
	    				//+"~~~~("+ entry.getKey() +")]" );
	    		parser(  entry.getKey(), entry.getValue() );
	    		iter.remove();
	    		
	    		processCount++;
	    		if( processCount > m.getMaxHandle() ) break;

	        }
		}
		*/
		
		synchronized( m.messageQueue ) {
		
			if ( ! m.messageQueue.isEmpty() ) {
				
				//kM = m.messageQueue.getFirst();
				
				// poll
				kM = m.messageQueue.poll();
				
				// parser
				parser( kM.getKey(), kM.getMessage() );
				
				//m.messageQueue.removeFirst();
			}
		}
		
	}

	public void parser(SelectionKey key, String message ) {

		logHandler.error( "message ["+message+ "]" );

		token = new JSONTokener(message);
		root = new JSONObject(token);
		
		this.testCount ++;

		logHandler.error( "parser ["+testCount+ "] cmd : "+ root.get("cmd") ); 
						// +", msg : " + root.get("msg") );
		
		if( root.get("cmd").equals("login") ) {
			DoUserQuery(key, root );
		}
		else if ( root.get("cmd").equals("getfriend") ) {
			DoFriendQuery(key, root) ;
		}
		else if ( root.get("cmd").equals("chat") ) {
			//process(message, key);
			/*
			synchronized(m.replyHash) {
				m.replyHash.put(key, message);	
			}
			*/
			synchronized( m.replyQueue ) {
				// m.replyQueue.addLast( new KeyMessage(key, message) );
				m.replyQueue.offer( new SelectionKeyMessage(key, message) );
			}
		}
		else {
			logHandler.error( "not defined message !!!!" );
		}
	}
	
	// Friend Query
	private void DoFriendQuery(SelectionKey key, JSONObject objMessage) {

		FriendQuery fQuery = new FriendQuery();
		// get connection from pool
		Connection con = pcm.getConnection();
		String userid = (String) objMessage.get("user") ;
		
		try {
			
			// JSONObject
			JSONObject objReply = new JSONObject();

			objReply.put("cmd", "getfriend");
			objReply.put("result", "success");
			
			ArrayList<Friend> result = fQuery.SelectFriend(con, userid);
			
			//Friend f = new Friend() ;
			
			if ( result.isEmpty() ) {
				objReply.put("count", "0");
			} else {
				objReply.put("count", result.size() );
				
				JSONArray onArray = new JSONArray();
				JSONArray offArray = new JSONArray();
				
				for ( int i=0; i < result.size(); i++ )	{
					Friend f = (Friend)result.get(i);
				
					if ( f.getOnLine().equals("on") )
					{
						//objReply.put("on", f.getFriendId() );
						onArray.put ( f.getFriendId() );
					} else {
						//objReply.put("off", f.getFriendId() );
						offArray.put ( f.getFriendId() );
					}
				}
				
				objReply.put( "on", onArray );
				objReply.put( "off", offArray );
				
				synchronized( m.replyQueue ) {
					// m.replyQueue.addLast( new KeyMessage(key, message) );
					m.replyQueue.offer( new SelectionKeyMessage(key, objReply.toString()) );
				}
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	// User Query
	public void DoUserQuery(SelectionKey key, JSONObject objMessage) {

		UserQuery uQuery = new UserQuery();
		Connection con = pcm.getConnection();
		String userid = (String) objMessage.get("user");
		String passwd = (String) objMessage.get("passwd");
		try {
			
			JSONObject objReply = new JSONObject();
			objReply.put("cmd", "login");

			if ( uQuery.SelectUser(con, userid, passwd ) ) {
				
				User u = new User();
				u.setUserID(userid);
				u.setSocketChannel( (SocketChannel)key.channel() );
				
				addUserHash(key, u);

				objReply.put("result", "success");
			}
			else {
				objReply.put("result", "fail");
				objReply.put("reason", "Username or Password is not correct.");
			}
			synchronized( m.replyQueue ) {
				// m.replyQueue.addLast( new KeyMessage(key, message) );
				m.replyQueue.offer( new SelectionKeyMessage(key, objReply.toString()) );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pcm.freeConnection(con);
		}
	}
	
	public void addUserHash(SelectionKey sk, User user) {
		u = m.userHash.get(sk);
		if ( u == null ) {
			m.userHash.put( sk,  user) ;
		}
	}
	
	public void removeUserHash(SelectionKey sk ) {
		u = m.userHash.get(sk);
		if ( u != null ) {
			m.userHash.remove(sk) ;
		}
	}
	
	public void setLogger() {
		logger = Logger.getLogger( getClass() ); 
		if ( logHandler == null ) {
			logHandler = new LoggerHandle(logger);
		}
	}
	
}
