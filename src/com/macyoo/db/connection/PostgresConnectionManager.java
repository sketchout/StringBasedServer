package com.macyoo.db.connection;

import com.macyoo.db.pool.DBConnectionPoolManager;

public class PostgresConnectionManager extends ConnectionManager {

	public PostgresConnectionManager() throws ClassNotFoundException {
		
		super("postgresql");
		
		String JDBCDriver = "org.postgresql.Driver";
		
		String JDBCDriverType ="jdbc:postgresql";
		String url =JDBCDriverType+"://"+dbServer+":"+port+"/"+dbName;
		
		connMgr = DBConnectionPoolManager.getInstance();
		
		System.out.println("dbServer["+dbServer+"]");
		System.out.println("port["+port+"]");
		System.out.println("dbName["+dbName+"]");
		
		connMgr.init( poolName, JDBCDriver, url, 
				userID, passwd, maxConn, initConn, maxWait);
		
	}

}
