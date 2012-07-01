/*********************************************************************************
 *
 * Copyright (C) 2007  Micael Gallego Carrillo,
 * 					   Alberto Pérez García-Plaza,
 * 					   LADyR Team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Micael Gallego Carrillo
 *     Alberto Pérez García-Plaza
 *     LADyR Team (http://www.ladyr.es/)
 * 
 * Contact information: info-ladyr at gsyc.es
 *
 *********************************************************************************/
package org.cnstar.browser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Response {

	private long status;
	private String statusText;
	private String contentType;
	private int contentLength;
	private String contentEncoding;
	private String server;
	//	contains all headers <header,value>
	private Map<String, String> headers = new HashMap<String,String>();
	
	public Response(){}
	
	public void setStatus(long status){
		this.status = status;		
	}
	
	public void setStatusText(String statusText){
		this.statusText = statusText;
	}
	public void setContentType(String contentType){
		this.contentType = contentType;
	}
	public void setContentLength(int contentLength ){
		this.contentLength = contentLength;
	}
	public void setContentEncoding(String contentEncoding){
		this.contentEncoding = contentEncoding;
	}
	public void setServer(String server){
		this.server = server;
	}
	public void setHeader(String header, String value){
		headers.put(header, value);
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public long getStatus(){
		return status;	
	}
	
	public String getStatusText(){
		return statusText;
	}

	public void print() {
		// TODO Auto-generated method stub
		System.out.println(status + "  "+statusText);
		Iterator iter = headers.entrySet().iterator();
		for (int i=0; i< headers.size(); i++){
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			System.out.println(key+":           "+value);
		}
	}
	
	
	public void write(BufferedWriter log) {

		try {
			log.write(status + "  "+statusText);log.newLine();			
			Iterator iter = headers.entrySet().iterator();
			for (int i=0; i< headers.size(); i++){
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();
				log.write(key+":           "+value);
			}
			log.newLine();
			log.newLine();
			log.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
