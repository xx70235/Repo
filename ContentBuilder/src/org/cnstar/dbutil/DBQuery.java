package org.cnstar.dbutil;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBQuery {
	
	Dbutil dbUtil = null;
	Statement stmt = null;  
	Connection conn = null;
	ResultSet rs = null;

	public DBQuery()
	{
		dbUtil = Dbutil.getInsstance();
		try {
			conn = dbUtil.getConnection();
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public void reConnect()
	{
		try {
			conn.close();
			conn = dbUtil.getConnection();
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public ResultSet getQueryResult(String sqlStr)
	{
		try {   
//			
            stmt = conn.createStatement();  
            rs = stmt.executeQuery(sqlStr);         
        } catch (SQLException e) {  
            e.printStackTrace();        
        }  
        return rs;		
	}
	
	public void ExcuteUpdate(String sqlSt)
	{
		
		try {   
        stmt = conn.createStatement(); 
        stmt.execute(sqlSt);
		}catch (SQLException e) {  
            e.printStackTrace();        
        }  
		
	}
	
	public void closeDB()
	{
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e) {
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (Exception e) {
			}
		if (conn != null)
			try {
				conn.close();
			} catch (Exception e) {
			}
	}

}
