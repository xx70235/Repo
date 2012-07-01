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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Resource {
	
	private String contentType;
	private byte[] data;
	private String url;
	
	public Resource(){}
	
	public Resource(String contentType, byte[] data){
		this.contentType = contentType;
		this.data = data;
	}
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public void saveToFile(File file) throws IOException{
		FileOutputStream fos = new FileOutputStream(file); 
		fos.write(data);
		fos.close();
	}

	public void setURL(String url) {
		this.url = url;		
	}

	public String getUrl() {
		return url;
	}
	
	

}
