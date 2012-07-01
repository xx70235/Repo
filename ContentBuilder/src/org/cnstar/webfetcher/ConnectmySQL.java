package org.cnstar.webfetcher;

import java.sql.*;
import java.util.*;

public class ConnectmySQL {

    /**
     * 获取对数据库的 连接
     * 
     * @return
     * @throws Exception
     */

    public Connection getConn() throws Exception {
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/fetchResults";
	String user = "root";
	String password = "123456";
	Class.forName(driver);
	Connection conn = DriverManager.getConnection(url, user, password);
	if (conn.isClosed())
	    System.out.println("False connecting to the Database!");

	return conn;
    }

    /**
     * 获取数据库中所有表的名称
     * 
     * @param conn
     * @return
     * @throws Exception
     */
    public List<String> gettableList(Connection conn) throws Exception {
	Statement statement = conn.createStatement();
	String sql = "select table_name  from information_schema.tables where table_schema=\"fetchResults\"";
	ResultSet rs = statement.executeQuery(sql);

	List<String> tableList = new ArrayList<String>();
	while (rs.next()) {
	    tableList.add(rs.getString("table_name"));
	}
	rs.close();
	return tableList;
    }

    /**
     * 返回该表下的所有字段名称
     * 
     * @param conn
     * @param tablename
     *            表名称
     * @return
     * @throws Exception
     */
    public List<String> gettablefiled(Connection conn, String tablename)
	    throws Exception {
	Statement statement = conn.createStatement();
	String sql = "select COLUMN_NAME from information_schema.columns where table_name=\""
		+ tablename + "\"";
	ResultSet rs = statement.executeQuery(sql);

	List<String> tableList = new ArrayList<String>();
	while (rs.next()) {
	    tableList.add(rs.getString("COLUMN_NAME"));
	}
	rs.close();
	return tableList;
    }

    public void excuteUpdate(Connection conn, String sqlStr)
	    throws Exception {
	Statement statement = conn.createStatement();
	statement.executeUpdate(sqlStr);

    }

    public static void main(String[] args) throws Exception {
	// 驱动程序名
	ConnectmySQL connectmySQL = new ConnectmySQL();
	Connection conn = connectmySQL.getConn();
	List<String> tableList = connectmySQL.gettableList(conn);
	for (String table : tableList) {
	    List<String> tablefield = connectmySQL.gettablefiled(conn, table);
	    for (String field : tablefield) {
		System.out.println(field + "   test");
	    }
	}
    }
}
