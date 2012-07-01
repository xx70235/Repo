package org.cnstar.dbutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DBModify {

	DBQuery dbQuery;

	public DBQuery getDbQuery() {
		return dbQuery;
	}

	public void setDbQuery(DBQuery dbQuery) {
		this.dbQuery = dbQuery;
	}

	public DBModify() {
		dbQuery = new DBQuery();
	}

	public void modifyDBDatumType(String tableName) {
		ResultSet rs = dbQuery.getQueryResult("SELECT max(id) FROM "
				+ tableName);
		int maxId = 0;
		Random random;
		List<Integer> signList;
		try {
			while (rs.next()) {
				maxId = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (maxId != 0) {
			random = new Random(47);
			signList = new LinkedList<Integer>();
			for (int i = 0; i < maxId; i++) {
				signList.add(i);
			}
			int id = 0;
			int datum = 0;
			int count =0;
			String datumType = "";
			while (signList.size() != 0) {
				count++;
				if(count%500==0)
				{
					dbQuery.reConnect();
				}
				id = signList.remove(random.nextInt(signList.size()));
				datum = random.nextInt(7);
				switch (datum) {
				case 4:
				case 0:
					datumType = "“…À∆";
					break;
				case 1:
					datumType = "»∑’Ô";
					break;
				case 5:
				case 6:
				case 2:
					datumType = "÷Œ”˙";
					break;
				case 3:
					datumType = "À¿Õˆ";
					break;
				}
				dbQuery.ExcuteUpdate("update " + tableName
						+ " set datum_type = '" + datumType + "' where id ="
						+ id);
				System.out.println(signList.size());
			}
		}

	}
	
	
	public void createNewTable(String tableName){
		ArrayList<String> timeList = new ArrayList<String>();
		ArrayList<String> address_geocodeList = new ArrayList<String>();
		ResultSet rs = dbQuery.getQueryResult("SELECT distinct time FROM "+tableName+" order by time;" );
		try {
			while(rs.next())
			{
				timeList.add(rs.getDate(1).toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		rs = dbQuery.getQueryResult("SELECT distinct address_geocode FROM "+tableName);
		try {
			while(rs.next())
			{
				address_geocodeList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<String> geocodeList = new ArrayList<String>();
		for(String geocode: address_geocodeList)
		{
			if(geocode.contains("44030"))
				geocodeList.add(geocode);
		}
		
		long infected=0;
		long cured=0;
		long died=0;
		long suspected = 0;
		
		for(String time : timeList)
		{
			int count=0;
			for(String geocode: geocodeList)
			{
				count++;
				String sqlStr = "SELECT COUNT(datum_type) FROM " + tableName
				+ " where time =DateValue('" + time
				+ "') and address_geocode = '" + geocode + "' and datum_type = '“…À∆'";
				rs = dbQuery.getQueryResult(sqlStr);
				
				try {
					while(rs.next())
					{
						suspected = rs.getLong(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				count++;
				sqlStr = "SELECT COUNT(datum_type) FROM " + tableName
				+ " where time =DateValue('" + time
				+ "') and address_geocode = '" + geocode + "' and datum_type = 'À¿Õˆ'";
				rs = dbQuery.getQueryResult(sqlStr);
				
				try {
					while(rs.next())
					{
						died = rs.getLong(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				count++;
				sqlStr = "SELECT COUNT(datum_type) FROM " + tableName
				+ " where time =DateValue('" + time
				+ "') and address_geocode = '" + geocode + "' and datum_type = '»∑’Ô'";
				rs = dbQuery.getQueryResult(sqlStr);
				
				try {
					while(rs.next())
					{
						infected = rs.getLong(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				count++;
				sqlStr = "SELECT COUNT(datum_type) FROM " + tableName
				+ " where time =DateValue('" + time
				+ "') and address_geocode = '" + geocode + "' and datum_type = '÷Œ”˙'";
				rs = dbQuery.getQueryResult(sqlStr);
				
				try {
					while(rs.next())
					{
						cured = rs.getLong(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				count++;
				sqlStr ="INSERT INTO daily_status (timevalue,address_geocode,disease,infected, cured, died, suspected) VALUES ('"+time+"','"+geocode+"','"+tableName+"',"+infected+","+cured+","+died+","+died+")";
				dbQuery.ExcuteUpdate(sqlStr);
				System.out.println("time: "+time+"\tgeocode: "+geocode+"\tdisease: "+tableName+"\tinfected: "+infected+"\tcured: "+cured+"\tdied: "+ died+"\tsuspected: "+suspected);
				
				if(count%100==0)
				dbQuery.reConnect();
			}
			
			
		}
	}

	public static void main(String[] args) {
		DBModify db = new DBModify();
		DBQuery dbQuery = db.getDbQuery();
		db.createNewTable("nj");
		db.createNewTable("szk");
		db.createNewTable("lg");
//		db.modifyDBDatumType("nj");
//		db.modifyDBDatumType("szk");
		dbQuery.closeDB();
	}

}
