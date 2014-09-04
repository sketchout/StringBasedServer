package com.macyoo.db.connection;

import com.macyoo.db.pool.DBConnectionPoolManager;

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
