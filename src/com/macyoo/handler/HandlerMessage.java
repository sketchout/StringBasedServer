package com.macyoo.handler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.javagamesfactory.nioservers.StringBasedServer;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.macyoo.main.DatabasePostgresql;
import com.macyoo.util.LogHandler;


public class HandlerMessage extends StringBasedServer {

	//private static HashMap<String, Room> roomHash;
	//private static HashMap<SelectionKey, User> userHash;
	
	MessageBuffer m;
	
	protected Logger logger ;
	protected LogHandler logHandler = null ;
	
	JSONTokener token = null; 
	JSONObject root = null;	

	int maxReadCount=0;
	int maxSendCount=0;
	int maxConnectCount=0;
	
	Iterator<Map.Entry<SelectionKey,String>> iter;
	Map.Entry<SelectionKey,String> entry;
	
	Iterator<KeyMessage> it;
	KeyMessage kM;
	
	public HandlerMessage(MessageBuffer m, int p) throws UnknownHostException {
		super(p);
		setLogger();
		this.m = m;
	}

	public HandlerMessage(MessageBuffer m,int p,int newBufferSize) throws UnknownHostException {
		super(p, newBufferSize);
		setLogger();
		this.m = m;
	}
	
	public void setLogger() {
		logger = Logger.getLogger( getClass() ); 
		if ( logHandler == null ) {
			logHandler = new LogHandler(logger);
		}
	}
	
	/** Subclasses SHOULD override this method to process all incoming messages */
	@Override
	protected void processStringMessage(String message, SelectionKey key)
			throws ClosedChannelException {
		
		//System.out.println("Key [ "+ key + "] Get : "+ message );
		//System.out.println("Key [ "+ key + "] Get : "+ message );
		//process(message, key);
		/*
		synchronized(m.messageHash) {
			m.readCount++;
			m.messageHash.put(key, message);	
		}
		*/
		synchronized(m.messageQueue) {
			// m.messageQueue.addLast ( new KeyMessage(key, message) );
			m.messageQueue.offer( new KeyMessage(key, message) );
			m.readCount++;
			
			// set MAX
			if( m.readCount > maxReadCount ) maxReadCount = m.readCount;
			
		}
	}
/*
	public void process(String message, SelectionKey key) {

		token = new JSONTokener(message);
		root = new JSONObject(token);
		
		this.testCount ++;
		// print out the get message
		//System.out.println("Key [ "+ key + "] Get : "+ message );
		//System.out.println("["+testCount+ "] cmd : "+ root.get("cmd") +", msg : " + root.get("msg") );
		
		logHandler.error( "["+testCount+ "] cmd : "+ root.get("cmd") +", msg : " + root.get("msg") );
		
		if( root.get("cmd").equals("login") ) {
			User u = new User();
			u.setSocketChannel( (SocketChannel)key.channel() );
			//addUserHash(key, u);
		} 
		else if ( root.get("cmd").equals("roomlist") ) {
			// room search - no, title, master,  
		}
		else if ( root.get("cmd").equals("roomjoin") ) {
			// room - no  , join user
		}
		else if ( root.get("cmd").equals("roomcreate") ) {
			// room - name , make room / join user
		}
		else if ( root.get("cmd").equals("roomuserlist") ) {
			// room - no, user list
		}
		else if ( root.get("cmd").equals("roomexit") ) {
			// normal case - cmd
			// abnormal case - disconnected...
		}
		else if ( root.get("cmd").equals("chat") ) {
			
			// reply the message for echo
			try {
				addMessageToKey(key, message);
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			}
		
		} else {
			logHandler.error( "not defined message !!!!" );
		}
		
	}
*/
	
	/*
	public void addUserHash(SelectionKey sk, User user) {
		User u = userHash.get(sk);
		if ( u == null ) {
			userHash.put( sk,  user) ;
		}
	}
	public void removeUserHash(SelectionKey sk ) {
		User u = userHash.get(sk);
		if ( u != null ) {
			userHash.remove(sk) ;
		}
	}
	*/
	/**
	 * Subclasses MUST override this method to remove data from local data structures whenever
	 * connections are dropped / closed
	 */

	// when ever connections are dropped or closed
	@Override
	protected void keyCancelled(SelectionKey key) {
		// remove user
		//removeUserHash(key);
		m.connectCount--;
		
		if ( m.connectCount == 0 ) {
			m.readCount=0;
			m.sendCount=0;
			
			logHandler.error( "[All] Channel Closed - Reset read/send Count- max R["
					+ maxReadCount+"] S["+maxSendCount+"] Connect["+this.maxConnectCount+"]");
			maxReadCount=0;
			maxSendCount=0;
			maxConnectCount=0;
		}
	}

	/**
	 * Invoked every time a select completes, IRRESPECTIVE of whether there was any network data.
	 * 
	 * <P>
	 * This is primarily useful for servers that wish to be purely single-threaded and do all their
	 * local processing in the gap between performing successive selects (lets you avoid using
	 * synchronized blocks anywhere)
	 * 
	 * @param millisecondsSinceLastStarted the number of milliseconds since this method was last
	 *           called; i.e. the time at which it was INVOKED, not the time at which it RETURNED;
	 *           this is perfect for maintaining fixed-rate game loops
	 */
	
	
	// for fixed-rate game loops
	
	@Override
	protected void postSelect(long millisecondsSinceLastStarted) {
		
		replyMessage();
		
	}
	void replyMessage() {
		
		
		/*
		 processCount = 0;
		synchronized (m.replyHash) {
	        	
        	iter = m.replyHash.entrySet().iterator();
        	while ( iter.hasNext() ) {
        		entry = iter.next();
	    		//logHandler.error( "postSelect[ String("+ entry.getValue()+")"
		    				//+"~~~~("+ entry.getKey() +")]" );
        		try {
					addMessageToKey(entry.getKey(), entry.getValue() );
					m.sendCount++;
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}

        		logHandler.error( "postSelect(R/S:"+ m.readCount +"/"+ m.sendCount 
        				+"[String("+ entry.getValue()+")"
        				+"~~~~("+ entry.getKey() +")]" );

        		iter.remove();
	    		processCount++;
	    		if( processCount > m.getMaxHandle() ) break;
        	}
		}
		*/
		
		synchronized( m.replyQueue ) {
		
			if ( ! m.replyQueue.isEmpty() ) {
				//kM = m.replyQueue.getFirst();
				kM = m.replyQueue.poll();
				try {
					addMessageToKey(kM.getKey(), kM.getMessage() );
					m.sendCount++;
					
					// set MAX
					if( m.sendCount > maxSendCount ) maxSendCount = m.sendCount;
					
					JSONTokener token = new JSONTokener( kM.getMessage());
					JSONObject root = new JSONObject(token);
					if( root.get("cmd").equals("login") && root.get("result").equals("fail") ) 
					{
						//keyCancel( kM.getKey() );
					}
					
				} catch (ClosedChannelException e) {

					e.printStackTrace();
				}
				//m.replyQueue.removeFirst();
				if( m.connectCount == m.sendCount )
					logHandler.error( "postSelect(R/S:"+ m.readCount +"/"+ m.sendCount 
        				+"[String("+ kM.getMessage()+")"
        				+"~~~~("+ kM.getKey() +")]" );
			}
		}
	}

	@Override
	protected void getConnected(SocketChannel sc) {

		try {
			//System.out.println("getConnected : " + sc.getRemoteAddress().toString());
			logHandler.error( "getConnected : " + sc.getRemoteAddress().toString()  );
		} catch (IOException e) {
			e.printStackTrace(); 
		}

		m.connectCount++;
		if ( m.connectCount > maxConnectCount )
			maxConnectCount = m.connectCount;
	}

}