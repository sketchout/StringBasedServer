package com.macyoo.db.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FriendQuery {
	
	public ArrayList<Friend>  SelectFriend(Connection conn, String userid) 
			throws SQLException {
		
		Statement stmt = conn.createStatement();
/*
	select a.friendid, nullif('off','on')  as online
    	from tfriend a 
    	left join tonline b 
    	on a.friendid = b.userid
    	where a.userid='guest'
*/
		String query = 
				"select a.friendid, nullif('off','on')  as online " +
				" from tfriend a " + 
				" left join tonline b " + 
				" on a.friendid = b.userid " +
				" where a.userid = \'"+ userid +"\'";
		
		ResultSet rs = stmt.executeQuery(query);

		ArrayList<Friend> list = new ArrayList<Friend>();
		
		while (rs.next() ) {
			list.add( new Friend(rs.getString("friendid"), 
						rs.getString("online") ) );
		}
		
		// close
		rs.close();
		if ( stmt != null ) stmt.close();
		
		return list;
	}

}
