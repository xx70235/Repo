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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cnstar.browser.impl.HTTPObserver;
import org.mozilla.interfaces.nsIHttpChannel;
import org.mozilla.interfaces.nsIRequest;




public class Request {
	private String method;
	private String uri; 
	private String host;				
	private String userAgent;
	private String accept;
	private String acceptLanguage;
	private String acceptEncoding;
	private String acceptCharset;
	private String keepAlive;
	private String connection;
	private String cookie;
	//contains all headers <header,value>
	private Map<String, String> headers = new HashMap<String,String>();
	
	private nsIHttpChannel httpChannel;
	
	public Request(nsIHttpChannel httpChannel) {
		this.httpChannel = httpChannel;
	}
	
	public void cancel(){
		System.out.println("\n------CANCELED----");
//		httpChannel.cancel(org.eclipse.swt.internal.mozilla.XPCOM.NS_ERROR_ABORT);
//		httpChannel.cancel(org.eclipse.swt.internal.mozilla.XPCOM.NS_ERROR_NOT_AVAILABLE);
		httpChannel.cancel(HTTPObserver.NS_ERROR_NOT_AVAILABLE);
	}
	

	public void setMethod(String method){
		this.method = method;
	}
	
	public void setURI(String uri){
		this.uri = uri;
	}

	public void setHost(String host){
		this.host = host;
	}
	public void setUserAgent(String userAgent){
		this.userAgent = userAgent;
	}
	public void setAccept(String  accept){
		this.accept = accept;
	}
	public void setAcceptLanguage(String acceptLanguage){
		this.acceptLanguage = acceptLanguage;
	}
	public void setAcceptEncoding(String acceptEncoding){
		this.acceptEncoding = acceptEncoding;
	}
	public void setAcceptCharset(String acceptCharset ){
		this.acceptCharset = acceptCharset;
	}
	public void setKeepAlive(String keepAlive){
		this.keepAlive = keepAlive;
	}
	public void setConnection(String connection){
		this.connection = connection;
	}
	public void setCookie(String cookie){
		this.cookie = cookie;
	}
	public void setHeader(String header, String value){
		headers.put(header, value);
	}


	public void print() {
		// TODO Auto-generated method stub
		System.out.println(method + "  "+uri); 
		Iterator iter = headers.entrySet().iterator();
		for (int i=0; i< headers.size(); i++){
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			System.out.println(key+":"+value);
		}
		
	}


	public void write(BufferedWriter log) {

		try {
			log.write(method + "  "+uri);log.newLine();
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
