package org.cnstar.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;



public class DB{
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	private String dbName = "geoinfo";
	private String urlRoot = "jdbc:mysql://localhost:3306/" + dbName;
	private String username = "root";
	private String password = "123";
	String url = urlRoot+"?user="+username+"&password="+password+"&useUnicode=true&characterEncoding=GB2312&autoReconnect=true";
	
	public Statement sm = null;
	private Connection conn = null;

	// private Database database1 = new Database();

	public DB() {
	}
	
	public void ConnectDB(){
		try {
			Class.forName(jdbcDriver).newInstance();
			conn = DriverManager.getConnection(url);
			sm = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void CloseDB(){
		try{
			if(sm != null){
				sm.close();
			}
			conn.close();
//			System.out.println("############");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
