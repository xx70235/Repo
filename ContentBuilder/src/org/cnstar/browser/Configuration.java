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

import org.cnstar.browser.impl.Filter;



public interface Configuration {

	/**
	 * Sets the User Agent of the browser. User Agent is used when the browser makes a petition 
	 * @param userAgent
	 */
	public void setUserAgent(String userAgent);
	
	/**
	 * Returns the User Agent of the browser.
	 * @return
	 */
	public String getUserAgent();
	
	/**
	 * Sets if the browser has to load images. 
	 * @param imageLoading
	 */
	public void setImageLoading(boolean imageLoading);
	
	/**
	 * Returns true is browser is able to load images. False otherwise
	 * @return
	 */
	public boolean isImageLoading();
	
	public abstract void setIntPreference(String preference, int value);
	public abstract int getIntPreference(String preference);
	
	public abstract void setBooleanPreference(String preference, boolean value);
	public abstract boolean getBooleanPreference(String preference);
	
	public abstract void setStringPreference(String preference, String value);
	public abstract String getStringPreference(String preference);
	
	public abstract void setTimeout(long millis);
	public abstract long getTimeout();
	
	
	/**
	 * Adds a filter that blocks all requests whose URL
	 * doesn't pass its filtrate rules.
	 * @param filter
	 */
	public void addFilter(Filter filter);
	
	/**
	 * Removes filter with the specified name
	 * @param name
	 */
	public void removeFilter(String name);
	
	/**
	 * Adds a filter that blocks all requests whose content type
	 * doesn't pass its filtrate rules.
	 * @param filter
	 */
	public void addMimeFilter(Filter filter);
	
	/**
	 * Removes filter with the specified name
	 * @param name
	 * @param regExpFilter
	 */
	public void removeMimeFilter(String name);
	
	
	/**

	 */
	
	/**
	 * Elimina ese request listener.
	 * @param listener
	 */
	public void removeRequestListener(RequestListener listener);
	
	/**
	 *  Añade un response listener y actúa de forma similar al addRequestListener()
	 * @param listener
	 */
	public void addResponseListener(ResponseListener listener);
	
	/**
	 * Elimina ese response listener.
	 * @param listener
	 */
	public void removeResponseListener(ResponseListener listener);
	 
	
	/**
	 * Activates or deactivates the monitoring of the requests 
	 * @param activate
	 */
	public void setMonitor(boolean activate);
	
	
	/**
	 * Resets all statistics
	 */
	public void resetStatistics();
	
	/**
	 * Returns an object that allows to accede to statistics information  
	 * @return
	 */
	public Statistics getStatistics();
	
	void setSocksProxy(String host, int port);
	void setHttpProxy(String host, int port);
}
