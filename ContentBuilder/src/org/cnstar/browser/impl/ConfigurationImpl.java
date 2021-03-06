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
package org.cnstar.browser.impl;

//import org.eclipse.swt.internal.mozilla.nsIServiceManager;
//import org.eclipse.swt.internal.mozilla.nsIPrefBranch;
//import org.eclipse.swt.internal.mozilla.nsIPrefService;

import org.mozilla.xpcom.Mozilla;
import org.mozilla.interfaces.nsICache;
import org.mozilla.interfaces.nsICacheService;
import org.mozilla.interfaces.nsIPrefBranch;
import org.mozilla.interfaces.nsIPrefService;
import org.mozilla.interfaces.nsIServiceManager;

import org.cnstar.browser.Configuration;
import org.cnstar.browser.RequestListener;
import org.cnstar.browser.ResponseListener;
import org.cnstar.browser.Statistics;
import org.cnstar.utils.*;
public class ConfigurationImpl implements Configuration {

	private String userAgent = "";
	private long timeout = 100000; // Default timeout to 60 seconds

	private nsIPrefBranch preferences;
	private HTTPObserver httpObserver;

	public ConfigurationImpl() {
		nsIServiceManager serviceManager = Mozilla.getInstance()
				.getServiceManager();
		String contractID = "@mozilla.org/preferences-service;1";
		nsIPrefService prefService = (nsIPrefService) serviceManager
				.getServiceByContractID(contractID,
						nsIPrefService.NS_IPREFSERVICE_IID);
		preferences = prefService.getBranch("");
	}

	public void setSocksProxy(String host, int port) {
		preferences.setIntPref("network.proxy.type", 1);

		boolean validConfig = false;
		if (host != null && host.length() > 0 && port > 0) {
			preferences.setCharPref("network.proxy.socks", host); //$NON-NLS-1$
			preferences.setIntPref("network.proxy.socks_port", port); //$NON-NLS-1$
			validConfig = true;
		} else {
			preferences.setCharPref("network.proxy.socks", ""); //$NON-NLS-1$ //$NON-NLS-2$
			preferences.setIntPref("network.proxy.socks_port", 0); //$NON-NLS-1$
		}
		if (!validConfig) {
			preferences.setIntPref("network.proxy.type", 0);
		}

	}

	public void setHttpProxy(String host, int port) {
		preferences.setIntPref("network.proxy.type", 1); //$NON-NLS-1$

		boolean validConfig = false;
		if (host != null && host.length() > 0 && port > 0) {
			preferences.setCharPref("network.proxy.http", host); //$NON-NLS-1$
			preferences.setIntPref("network.proxy.http_port", port); //$NON-NLS-1$
			validConfig = true;
		} else {
			preferences.setCharPref("network.proxy.http", ""); //$NON-NLS-1$ //$NON-NLS-2$
			preferences.setIntPref("network.proxy.http_port", 0); //$NON-NLS-1$
		}
		if (!validConfig) {
			preferences.setIntPref("network.proxy.type", 0);
		}
	}

	public void setManualProxy(final String httpHost, final int httpPort,
			final String sslHost, final int sslPort, final String ftpHost,
			final int ftpPort, final String socksHost, final int socksPort,
			final String noProxyFor) {

		// switch to manual configuration
		preferences.setIntPref("network.proxy.type", 1); //$NON-NLS-1$

		boolean validConfig = false;
		// http proxy
		if (httpHost != null && httpHost.length() > 0 && httpPort > 0) {
			preferences.setCharPref("network.proxy.http", httpHost); //$NON-NLS-1$
			preferences.setIntPref("network.proxy.http_port", httpPort); //$NON-NLS-1$
			validConfig = true;
		} else {
			preferences.setCharPref("network.proxy.http", ""); //$NON-NLS-1$ //$NON-NLS-2$
			preferences.setIntPref("network.proxy.http_port", 0); //$NON-NLS-1$
		}

		// ssl proxy
		if (sslHost != null && sslHost.length() > 0 && sslPort > 0) {
			preferences.setCharPref("network.proxy.ssl", sslHost); //$NON-NLS-1$
			preferences.setIntPref("network.proxy.ssl_port", sslPort); //$NON-NLS-1$
			validConfig = true;
		} else {
			preferences.setCharPref("network.proxy.ssl", ""); //$NON-NLS-1$ //$NON-NLS-2$
			preferences.setIntPref("network.proxy.ssl_port", 0); //$NON-NLS-1$
		}

		// ftp proxy
		if (ftpHost != null && ftpHost.length() > 0 && ftpPort > 0) {
			preferences.setCharPref("network.proxy.ftp", ftpHost); //$NON-NLS-1$
			preferences.setIntPref("network.proxy.ftp_port", ftpPort); //$NON-NLS-1$
			validConfig = true;
		} else {
			preferences.setCharPref("network.proxy.ftp", ""); //$NON-NLS-1$ //$NON-NLS-2$
			preferences.setIntPref("network.proxy.ftp_port", 0); //$NON-NLS-1$
		}

		// socks proxy
		if (socksHost != null && socksHost.length() > 0 && socksPort > 0) {
			preferences.setCharPref("network.proxy.socks", socksHost); //$NON-NLS-1$
			preferences.setIntPref("network.proxy.socks_port", socksPort); //$NON-NLS-1$
			validConfig = true;
		} else {
			preferences.setCharPref("network.proxy.socks", ""); //$NON-NLS-1$ //$NON-NLS-2$
			preferences.setIntPref("network.proxy.socks_port", 0); //$NON-NLS-1$
		}

		// no proxy for
		if (noProxyFor != null && noProxyFor.length() > 0) {
			preferences.setCharPref("network.proxy.no_proxies_on", noProxyFor); //$NON-NLS-1$
		} else {
			preferences.setCharPref(
					"network.proxy.no_proxies_on", "localhost, 127.0.0.1"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (!validConfig) {
			// reset proxy type
			preferences.setIntPref("network.proxy.type", 0); //$NON-NLS-1$
		}

	}

	public void disableProxy() {

		// switch to direct connection
		preferences.setIntPref("network.proxy.type", 0); //$NON-NLS-1$

	}
	
	  /**
     * Cleans browser cache.
     */
    public static void cleanCache() {
            nsICacheService cache =XPCOMUtils.getService("@mozilla.org/network/cache-service;1", nsICacheService.class); //$NON-NLS-1$
            cache.evictEntries(nsICache.STORE_ANYWHERE);
        
    }

    public void setIntPreference(String preference, int value) {
		preferences.setIntPref(preference, value);
	}

	public int getIntPreference(String preference) {
		return preferences.getIntPref(preference);
	}

	public void setBooleanPreference(String preference, boolean value) {
		preferences.setBoolPref(preference, value ? 1 : 0);
	}

	public boolean getBooleanPreference(String preference) {
		return preferences.getBoolPref(preference);
	}

	public void setStringPreference(String preference, String value) {
		preferences.setCharPref(preference, value);
	}

	public String getStringPreference(String preference) {
		return preferences.getCharPref(preference);
	}

	public void addFilter(Filter filter) {
		getHTTPObserver().addURLFilter(filter);
	}

	public void removeFilter(String name) {
		getHTTPObserver().removeURLFilter(name);
	}

	public void addMimeFilter(Filter filter) {
		getHTTPObserver().addMimeFilter(filter);
	}

	public void removeMimeFilter(String name) {
		getHTTPObserver().removeMimeFilter(name);
	}

	public void addRequestListener(RequestListener listener) {
		getHTTPObserver().addRequestListener(listener);

	}

	public void addResponseListener(ResponseListener listener) {
		getHTTPObserver().addResponseListener(listener);

	}

	public void removeRequestListener(RequestListener listener) {
		getHTTPObserver().removeRequestListener(listener);

	}

	public void removeResponseListener(ResponseListener listener) {
		getHTTPObserver().removeResponseListener(listener);

	}

	public Statistics getStatistics() {
		return getHTTPObserver().getStatistics();

	}

	public void resetStatistics() {
		getHTTPObserver().resetStatistics();
	}

	public void setMonitor(boolean activate) {
		getHTTPObserver().setMonitor(activate);

	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		setStringPreference("general.useragent.override", userAgent);
	}

	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Sets if the browser has to load images.
	 * 
	 * @param imageLoading
	 */
	public void setImageLoading(boolean imageLoading) {
		setIntPreference("network.image.imageBehavior", imageLoading ? 0 : 2);
	}

	/**
	 * Returns true is browser is able to load images. False otherwise
	 * 
	 * @return
	 */
	public boolean isImageLoading() {
		return getIntPreference("network.image.imageBehavior") == 0;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	// @Override
	// public void setImageLoading(boolean imageLoading) {
	// super.setImageLoading(imageLoading);
	// // Se filtran las imágenes porque en Windows no funciona la propiedad
	//
	// nsIObserver httpRequestObserver = new nsIObserver() {
	// public void observe(nsISupports subject, String topic, String data) {
	// if (topic.equals("http-on-modify-request")) {
	//
	// nsIHttpChannel httpChannel = (nsIHttpChannel) subject
	// .queryInterface(nsIHttpChannel.NS_IHTTPCHANNEL_IID);
	// System.out.println(httpChannel.getRequestMethod() + " "
	// + httpChannel.getName());// getOriginalURI().getPrePath());
	// // //tb
	// // valdria
	// // getName
	//
	// }
	// }
	//
	// public nsISupports queryInterface(String aIID) {
	// return Mozilla.queryInterface(this, aIID);
	// }
	// };
	//
	// Mozilla Moz = Mozilla.getInstance();
	// nsIServiceManager serviceManager = Moz.getServiceManager();
	// nsIObserverService observerService = (nsIObserverService) serviceManager
	// .getServiceByContractID("@mozilla.org/observer-service;1",
	// nsIObserverService.NS_IOBSERVERSERVICE_IID);
	// observerService.addObserver(httpRequestObserver,
	// "http-on-modify-request", false);
	//
	// }
	//
	// // public void removeHttpObserver(){
	// // observerService.removeObserver(httpRequestObserver,
	// // "http-on-modify-request");
	// // }

	// Create HTTPObserver with first request
	private HTTPObserver getHTTPObserver() {
		if (this.httpObserver == null) {
			// add an observer service to listen HTTP traffic
			httpObserver = new HTTPObserver();
			httpObserver.register();
		}
		return httpObserver;

	}

}
