package org.cnstar.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbutilSQL {

	private static DbutilSQL dbutil = null;


	private Connection con = null;
	
	private String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String dbName = "DatabaseName=Analysis";
	private String urlRoot = "jdbc:sqlserver://localhost:1433;" + dbName;
	private String username = "sa";
	private String password = "880117";
//	String connectionUrl = urlRoot+"?user="+username+"&password="+password+"&useUnicode=true&characterEncoding=GB2312&autoReconnect=true";
	

	private DbutilSQL() throws Exception {
	    
	    
	    
//	    	String path = getAppPath(Dbutil.class).substring(1)+"/disease_data/diseaseInfo.mdb";
//		System.out.println(path);
//		System.out.println(this.getClass().getResource("")+"this.getClass().getResource(");
//		connectionUrl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="
//				+ path;
//		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	    
	    Class.forName(jdbcDriver).newInstance();
	}

	public static synchronized DbutilSQL getInsstance() {
		try {
			if (dbutil == null) {
				dbutil = new DbutilSQL();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbutil;
	}

	/**
	 * 获取db连接
	 * 
	 * @param insertList
	 * @return
	 */
	public Connection getConnection() throws Exception {
		Properties prop = new Properties();
		prop.put("charSet", "GBK"); 
		con =  DriverManager.getConnection(urlRoot, username, password);
//		con = DriverManager.getConnection(connectionUrl,prop);
		return con;
	}
	
	public static String getAppPath(Class cls){   
	    //����û�����Ĳ����Ƿ�Ϊ��   password
	    if(cls==null)   
	     throw new java.lang.IllegalArgumentException("��������Ϊ�գ�");   
	     ClassLoader loader=cls.getClassLoader();   
	    //������ȫ������������   
	     String clsName=cls.getName()+".class";   
	    //��ô���������ڵİ�   
	     Package pack=cls.getPackage();   
	     String path="";   
	    //���������������������ת��Ϊ·��   
	    if(pack!=null){   
	         String packName=pack.getName();   
	       //�˴����ж��Ƿ���Java������⣬��ֹ�û�����JDK���õ����   
	       if(packName.startsWith("java.")||packName.startsWith("javax."))   
	          throw new java.lang.IllegalArgumentException("��Ҫ����ϵͳ�࣡");   
	        //����������У�ȥ�������Ĳ��֣��������ļ���   
	         clsName=clsName.substring(packName.length()+1);   
	        //�ж������Ƿ��Ǽ򵥰���������ǣ���ֱ�ӽ�����ת��Ϊ·����   
	        if(packName.indexOf(".")<0) path=packName+"/";   
	        else{//�����հ�������ɲ��֣�������ת��Ϊ·��   
	            int start=0,end=0;   
	             end=packName.indexOf(".");   
	            while(end!=-1){   
	                 path=path+packName.substring(start,end)+"/";   
	                 start=end+1;   
	                 end=packName.indexOf(".",start);   
	             }   
	             path=path+packName.substring(start)+"/";   
	         }   
	     }   
	    //����ClassLoader��getResource�������������·����Ϣ�����ļ���   
	     java.net.URL url =loader.getResource(path+clsName);   
	    //��URL�����л�ȡ·����Ϣ   
	     String realPath=url.getPath();   
	    //ȥ��·����Ϣ�е�Э����"file:"   
	    int pos=realPath.indexOf("file:");   
	    if(pos>-1) realPath=realPath.substring(pos+5);   
	    //ȥ��·����Ϣ���������ļ���Ϣ�Ĳ��֣��õ������ڵ�·��   
	     pos=realPath.indexOf(path+clsName);   
	     realPath=realPath.substring(0,pos-1);   
	    //������ļ��������JAR���ļ���ʱ��ȥ����Ӧ��JAR�ȴ���ļ���   
	    if(realPath.endsWith("!"))   
	         realPath=realPath.substring(0,realPath.lastIndexOf("/"));   
	  /*------------------------------------------------------------
	    ClassLoader��getResource����ʹ����utf-8��·����Ϣ�����˱��룬��·��
	     �д������ĺͿո�ʱ���������Щ�ַ�����ת�����������õ�����������������Ҫ
	     ����ʵ·�����ڴˣ�������URLDecoder��decode�������н��룬�Ա�õ�ԭʼ��
	     ���ļ��ո�·��
	   -------------------------------------------------------------*/   
	  try{   
	     realPath=java.net.URLDecoder.decode(realPath,"utf-8");   
	    }catch(Exception e){throw new RuntimeException(e);}   
	   return realPath;   
	}//getAppPath�������  
}
