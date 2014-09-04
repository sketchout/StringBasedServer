package com.macyoo.db.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import com.macyoo.db.pool.DBConnectionPoolManager;

public abstract class ConnectionManager {

	
	protected DBConnectionPoolManager connMgr = null;
	protected String poolName;
	
	protected String dbServer,dbName, port;
	protected String userID,passwd;
	
	int maxConn, initConn, maxWait;
	
	private String configFile;
	private Properties dbProperties;
	
	public ConnectionManager(String pool) {
		
		this.poolName = pool;
		
		configFile = poolName+".properties";
	
		try {
			dbProperties = readProperties();
			
			dbServer = getProperty("dbServer");
			port = getProperty("port");
			
			dbName = getProperty("dbName");
						
			userID = getProperty("userID");
			passwd = getProperty("passwd");
			
			maxConn = Integer.parseInt(getProperty("maxConn") );
			initConn = Integer.parseInt(getProperty("initConn") );
			maxWait = Integer.parseInt(getProperty("maxWait") );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Connection getConnection() {
		return ( connMgr.getConnection(poolName) );
	}

	public void freeConnection(Connection conn) {
		connMgr.freeConnection(poolName, conn);
	}
	
	private String getProperty(String string) {
		return (dbProperties.getProperty(string));
	}

	private Properties readProperties() throws IOException {

		Properties tempProperties = new Properties();
		
		InputStream in =  this.getClass().getClassLoader().getResourceAsStream(configFile) ;
		tempProperties.load(in);
		
		return tempProperties;
	}
}
