package com.macyoo.dbpool;

import java.sql.*;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.macyoo.util.LogHandler;

public class DBConnectionPool {

	private Vector<Connection> poolConnections = new Vector<Connection>();
	
	private int checkedOut;
	
	private int maxConn;	// Connection max 
	private int initConn;	// Connection initial
	private int maxWait;
	
	private String name;	// Connection Pool Name
	private String user;
	private String password;
	private String URL;
	
	protected Logger logger ;
	protected LogHandler logHandler = null ;
	
	DBConnectionPool(String name, String URL, String user, String password, 
				int maxConn, int initConn,int waitTime) {
		
		this.setLogger();
		
		this.maxConn = maxConn;
		this.initConn = initConn;
		this.maxWait = waitTime;
		
		this.name = name;
		this.URL = URL;
		this.user = user;
		this.password = password;
		
		
		for( int i=0; i < initConn; i++ ) {
			
			Connection con = newConnection();
			if ( con != null ) {
				poolConnections.addElement(con);
			} else {
				logHandler.error( "Fail to make new Connection :"+ i );
			}
		}
	}

	// free
	public synchronized void freeConnection(Connection con) {
		poolConnections.addElement(con);
		checkedOut--;
		notifyAll();
	}
	
	// get
	public synchronized Connection getConnection() {
		Connection conn = null;
		
		if( poolConnections.size() > 0 ) {
			conn = (Connection)poolConnections.firstElement();
			poolConnections.removeElementAt(0);
		
			try {
				if( conn.isClosed() ) {
					conn = null;
				}
			} catch (SQLException e) {
				//e.printStackTrace();
				conn= null;
			}
		} else if ( maxConn == 0 || checkedOut < maxConn) {
			conn = newConnection();
		}
		if ( conn != null ) checkedOut++;
		return conn;
	}
	
	// new
	private Connection newConnection() {
		
		Connection conn = null;
		try {
			if( user == null ) {
				conn = DriverManager.getConnection(URL);
			} else {
				conn = DriverManager.getConnection(URL, user, password);
			}
			
		} catch (SQLException e) {
			//e.printStackTrace();
			logHandler.error("Can't create a new Connection in pool :" + name );
			return null;
		}
		logHandler.error("Created a new Connection in pool :" + name );
		
		return conn;
	}
	
	public void setLogger() {
		logger = Logger.getLogger( getClass() ); 
		if ( logHandler == null ) {
			logHandler = new LogHandler(logger);
		}
	}
}
