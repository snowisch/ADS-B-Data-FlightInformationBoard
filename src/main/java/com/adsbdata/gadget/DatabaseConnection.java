package com.adsbdata.gadget;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.adsbdata.executer.AdsbMain;

public class DatabaseConnection {
	
	public static boolean databaseExist(String tablename) throws SQLException {
        ResultSet rs = AdsbMain.conn.getMetaData().getTables(null, null, tablename, null);
        if (rs.next()) {
        	return true;  
        } else {  
        	return false;
        }  
    }
	
	public static Connection connectDatabase(String username, String password, String databasename) throws SQLException {
		//String driver="com.mysql.jdbc.Driver"; 
		String url="jdbc:mysql://localhost:3306/" + databasename;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url
	                + "?useUnicode=true&characterEncoding=utf-8&useSSL=false", username , password);
			System.out.println("DataBase " + databasename + " Connected.");
		} catch(Exception e){
			e.printStackTrace();
		}
	return conn;
	}
	
	public static void disconnectDatabase(Connection conn, String databasename) throws SQLException {
		if (conn != null) {
			conn.close();
			System.out.println("DataBase " + databasename + " Disonnected.");
		}
	}
}