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

/**
 * This interface is used by WebBrowser to knows if a url is allowed to be crawled. It is basically
 * for implementing robots.txt guides. 
 * @author usuario 
 */
public interface URLFilter {

	/**
	 * Invoked by WebBrowser to know if this url is allowed or filtered. 
	 * @param url 
	 * @return true if this url is allowed, false otherwise
	 */
	boolean isAllowed(String url);
	
}
