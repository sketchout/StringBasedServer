package com.macyoo.main;

import java.sql.*;

public class DatabasePostgresql {

    public Connection conn;
    //private String db = null;

    public DatabasePostgresql() throws Exception {

        //Trying to get the driver
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (java.lang.ClassNotFoundException e) {
            java.lang.System.err.print("ClassNotFoundException: Postgres Server JDBC");
            java.lang.System.err.println(e.getMessage());
            throw new Exception("No JDBC Driver found in Server");
        }

    }
    
    public void connect(String db) throws Exception{

        //this.db=db;
        //jdbc:postgresql://127.0.0.1:5432/mydb
        //Trying to connectpostgresql:/
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+db,
            		"postgres","admin");
            //conn.setCatalog(db);
            System.out.println("Connection with: "+db+"!!");
        }
        catch (SQLException E) {

            java.lang.System.out.println("SQLException: " + E.getMessage());
            java.lang.System.out.println("SQLState: " + E.getSQLState());
            java.lang.System.out.println("VendorError: " + E.getErrorCode());

            //throw new Exception("Can't connect to SQL server");
            throw E;
        }
    }


    //Close Conn
    public void close() throws SQLException{

        try {
            conn.close();
            System.out.println("Connection close ");
        } catch (SQLException E) {


            java.lang.System.out.println("SQLException: " + E.getMessage());
            java.lang.System.out.println("SQLState: " + E.getSQLState());
            java.lang.System.out.println("VendorError: " + E.getErrorCode());
            throw E;
        }

    }



}