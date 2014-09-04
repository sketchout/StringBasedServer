package com.macyoo.db.pool;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.Vector;

// http://java-school.net/jdbc/12.php

public class DBConnectionPoolManager {

	// jdbc driver
	/**
	 * 
	 */
	private Vector<String> drivers = new Vector<String>();
	
	// db connection pool list
	/**
	 * 
	 */
	private Hashtable<String,DBConnectionPool> pools
							= new Hashtable<String,DBConnectionPool>();
	
	// singleton
	
	/**
	 * 
	 */
	static private DBConnectionPoolManager instance = null ;
	
	/**
	 * 
	 */
	private DBConnectionPoolManager() {
	}
	
	/**
	 * @Method Name : getInstance
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @return
	 */
	static synchronized public DBConnectionPoolManager getInstance() {
		if( instance == null ) {
			instance = new DBConnectionPoolManager();
		}
		return instance;
	}

	/**
	 * @Method Name : getConnection
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @param name
	 * @return
	 */
	public Connection getConnection(String name) {
		DBConnectionPool pool = (DBConnectionPool)pools.get(name);
		if( pool != null ) {
			// get
			return pool.getConnection();
		}
		return null;
	}

	/**
	 * @Method Name : freeConnection
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @param name
	 * @param con
	 */
	public void freeConnection(String name, Connection con) {
		DBConnectionPool pool = (DBConnectionPool)pools.get(name);
		if( pool != null ) {
			// free
			pool.freeConnection(con);
		} 
	}
	
	/**
	 * @Method Name : createPools
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @param poolName
	 * @param url
	 * @param user
	 * @param password
	 * @param maxConn
	 * @param initConn
	 * @param maxWait
	 */
	private void createPools(String poolName, String url, String user, String password, 
			int maxConn, int initConn, int maxWait ) {
		
		DBConnectionPool pool = new DBConnectionPool(poolName, url, user, password,
							maxConn, initConn, maxWait);
		pools.put( poolName, pool );
	}

	/**
	 * @Method Name : loadDrivers
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @param driverClassName
	 */
	private void loadDrivers(String driverClassName) {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			// Not Found
			e.printStackTrace();
		}
		drivers.addElement(driverClassName);
	}
	
	/**
	 * @Method Name : init
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @param poolName
	 * @param driver
	 * @param url
	 * @param user
	 * @param password
	 * @param maxConn
	 * @param initConn
	 * @param maxWait
	 */
	public void init(String poolName, String driver, String url, String user, String password, 
			int maxConn, int initConn, int maxWait) {
		loadDrivers(driver);
		createPools(poolName, url, user, password, maxConn, initConn, maxWait);
	}
	
	/**
	 * @Method Name : getPools
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @return
	 */
	public Hashtable<String, DBConnectionPool> getPools() {
		return pools;
	}
	
	/**
	 * @Method Name : getDriverNumber
	 * @date : Jul 10, 2014
	 * @author : 0813 1745 2041
	 * @revision : 
	 * @method description :
	 * @return
	 */
	public int getDriverNumber() {
		return drivers.size();
	}
}
