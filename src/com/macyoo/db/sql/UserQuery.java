package com.macyoo.db.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserQuery {


	public Boolean SelectUser(Connection conn, 
			String userid, String passwd) 
			throws SQLException {
	
		String userpw = null;
		
		Statement stmt = conn.createStatement();
		
		String query = "select userpw from tuser where userid=\'"
				+userid+"\'" ;
		
		ResultSet rs = stmt.executeQuery(query);
		while( rs.next () ) {
			userpw = rs.getString("userpw");
		}
		rs.close();
		
		if( stmt != null ) stmt.close();

		if ( userpw == null) { 
			return false;
		}
		
		if ( userpw.equals(passwd)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean RegisterUser(Connection conn, String userid, String userpw) 
			throws SQLException {
	
		
		Statement stmt = conn.createStatement();
		
		String query = "insert into tuser (userid,userpw) "+
		" values (\'"+userid+"\',\'"+ userpw+"\' )" ;
		
		ResultSet rs = stmt.executeQuery(query);
		while( rs.next () ) {
			userpw = rs.getString("userpw");
		}
		
		rs.close();
		if( stmt != null ) stmt.close();
		
		return true;
	}
}
