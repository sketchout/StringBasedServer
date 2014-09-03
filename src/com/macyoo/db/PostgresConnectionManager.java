package com.macyoo.db;

import com.macyoo.dbpool.DBConnectionPoolManager;

public class PostgresConnectionManager extends ConnectionManager {

	public PostgresConnectionManager() {
		
		super("postgresql");
		
		String JDBCDriver = "org.postgresql.Driver";
		
		String JDBCDriverType ="jdbc:postgresql";
		String url =JDBCDriverType+"://"+dbServer+":"+port+"/"+dbName;
		
		connMgr = DBConnectionPoolManager.getInstance();
		
		connMgr.init( poolName, JDBCDriver, url, userID, passwd, maxConn, initConn, maxWait);
	}

}
