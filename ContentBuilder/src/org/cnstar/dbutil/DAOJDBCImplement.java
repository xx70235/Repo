package org.cnstar.dbutil;

import java.sql.ResultSet;
import java.util.ArrayList;

import DBConnection.DB;

public class DAOJDBCImplement {
    private DB db = new DB();

    public int getCount(String sql) {
	db.CloseDB();
	ResultSet rs = null;
	int count = 0;
	try {
	    rs = db.sm.executeQuery(sql);
	    rs.last(); // ָ�����
	    count = rs.getRow();// ȡ���к�
	    rs.beforeFirst();
	} catch (Exception e) {
	    e.printStackTrace();

	} finally {
	    db.CloseDB();
	}
	return count;
    }

    public String getName(String code) {
	db.ConnectDB();
	ResultSet rs = null;
	String name = null;
	try {
	    String sql = "select name from geo where code =" + code;
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		name = rs.getString("name");
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return name;
    }

    public String getUpName(String code) {
	db.ConnectDB();
	ResultSet rs = null;
	String name = null;
	try {
	    String sql = "select ups from geo where code =" + code;
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		name = rs.getString("ups");
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return name;
    }

    // ����������֣����ص�����롣������������������벻һ����һ����
    public ArrayList<String> getCodeByLocName(String name) {
	db.ConnectDB();
	ResultSet rs = null;
	ArrayList<String> code = new ArrayList<String>();
	try {
	    String sql = "select code from geo where name like '%" + name
		    + "%'";
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		code.add(rs.getString("code"));
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return code;
    }

    // ����������ֺ�����һ������λ�õ����ƣ����ص�����롣
    public ArrayList<String> getCodeByLocName(String name, String upname) {
	db.ConnectDB();
	ResultSet rs = null;
	ArrayList<String> code = new ArrayList<String>();
	try {
	    String sql = "select code from geo where name like '%" + name
		    + "%'" + "and ups like '%" + upname + "%'";
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		code.add(rs.getString("code"));
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return code;
    }

    /**
     * 
     * @param loccode
     *            �������
     * @return ĳ������������Χ�ĵ�������
     */
    public ArrayList<String> getCodeAroundLoc(String loccode, double range) {
	db.ConnectDB();
	ResultSet rs = null;
	ArrayList<String> code = new ArrayList<String>();
	try {
	    String lon = null;
	    String lat = null;
	    String sql = "select lon,lat from geo where code =" + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		lon = rs.getString("lon");
		lat = rs.getString("lat");
	    }

	    String lonMax = Double.toString((Double.parseDouble(lon) + range));
	    String lonMin = Double.toString((Double.parseDouble(lon) - range));
	    String latMax = Double.toString((Double.parseDouble(lat) + range));
	    String latMin = Double.toString((Double.parseDouble(lat) - range));
	    sql = "select code from geo where lon between " + lonMin + " and "
		    + lonMax + " and lat between " + latMin + " and " + latMax
		    + " and code <> " + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		code.add(rs.getString("code"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return code;
    }

    /**
     * 
     * @param loccode
     *            �������
     * @return ĳ���������ӱ��ߵ�������
     */
    public ArrayList<String> getCodeNorthOfLoc(String loccode, double range) {
	db.ConnectDB();
	ResultSet rs = null;
	ArrayList<String> code = new ArrayList<String>();
	try {
	    String lon = null;
	    String lat = null;
	    String sql = "select lon,lat from geo where code =" + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		lon = rs.getString("lon");
		lat = rs.getString("lat");
	    }

	    String lonMax = Double
		    .toString((Double.parseDouble(lon) + range / 4));
	    String lonMin = Double
		    .toString((Double.parseDouble(lon) - range / 4));
	    String latMax = Double.toString((Double.parseDouble(lat) + range));
	    String latMin = lat;
	    sql = "select code from geo where lon between " + lonMin + " and "
		    + lonMax + " and lat between " + latMin + " and " + latMax
		    + " and code <> " + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		code.add(rs.getString("code"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return code;
    }

    /**
     * 
     * @param loccode
     *            �������
     * @return ĳ�����������ϱߵ�������
     */
    public ArrayList<String> getCodeSouthOfLoc(String loccode, double range) {
	db.ConnectDB();
	ResultSet rs = null;
	ArrayList<String> code = new ArrayList<String>();
	try {
	    String lon = null;
	    String lat = null;
	    String sql = "select lon,lat from geo where code =" + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		lon = rs.getString("lon");
		lat = rs.getString("lat");
	    }

	    String lonMax = Double
		    .toString((Double.parseDouble(lon) + range / 4));
	    String lonMin = Double
		    .toString((Double.parseDouble(lon) - range / 4));
	    String latMax = lat;
	    String latMin = Double.toString((Double.parseDouble(lat) - range));
	    sql = "select code from geo where lon between " + lonMin + " and "
		    + lonMax + " and lat between " + latMin + " and " + latMax
		    + " and code <> " + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		code.add(rs.getString("code"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return code;
    }

    /**
     * 
     * @param loccode
     *            �������
     * @return ĳ���������Ӷ��ߵ�������
     */
    public ArrayList<String> getCodeEastOfLoc(String loccode, double range) {
	db.ConnectDB();
	ResultSet rs = null;
	ArrayList<String> code = new ArrayList<String>();
	try {
	    String lon = null;
	    String lat = null;
	    String sql = "select lon,lat from geo where code =" + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		lon = rs.getString("lon");
		lat = rs.getString("lat");
	    }

	    String lonMax = Double.toString((Double.parseDouble(lon) + range));
	    String lonMin = lon;
	    String latMax = Double
		    .toString((Double.parseDouble(lat) + range / 4));
	    String latMin = Double
		    .toString((Double.parseDouble(lat) - range / 4));
	    sql = "select code from geo where lon between " + lonMin + " and "
		    + lonMax + " and lat between " + latMin + " and " + latMax
		    + " and code <> " + loccode;
	    System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		code.add(rs.getString("code"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return code;
    }

    /**
     * 
     * @param loccode
     *            �������
     * @return ĳ�������������ߵ�������
     */
    public ArrayList<String> getCodeWestOfLoc(String loccode, double range) {
	db.ConnectDB();
	ResultSet rs = null;
	ArrayList<String> code = new ArrayList<String>();
	try {
	    String lon = null;
	    String lat = null;
	    String sql = "select lon,lat from geo where code =" + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		lon = rs.getString("lon");
		lat = rs.getString("lat");
	    }

	    String lonMax = lon;
	    String lonMin = Double.toString((Double.parseDouble(lon) - range));
	    String latMax = Double
		    .toString((Double.parseDouble(lat) + range / 4));
	    String latMin = Double
		    .toString((Double.parseDouble(lat) - range / 4));
	    sql = "select code from geo where lon between " + lonMin + " and "
		    + lonMax + " and lat between " + latMin + " and " + latMax
		    + " and code <> " + loccode;
	    // System.out.println(sql);
	    rs = db.sm.executeQuery(sql);
	    while (rs.next()) {
		code.add(rs.getString("code"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.CloseDB();
	}
	return code;
    }
}
