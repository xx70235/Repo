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

import java.net.URL;

public class URLLoadException extends LadyrBrowserException {

	private static final long serialVersionUID = 1521397116281795698L;
	
	private URL url;
	private int responseCode;
	
	public URLLoadException(URL url, int responseCode) {
		super("URL: "+url+" with problems (responseCode = "+responseCode+")");
		this.url = url;		
	}
	
	public URLLoadException(URL url, Throwable cause) {
		super("URL: "+url+" load with exception",cause);
		this.url = url;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public URL getUrl() {
		return url;
	}	
	
}
