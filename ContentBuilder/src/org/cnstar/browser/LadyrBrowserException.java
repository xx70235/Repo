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

public class LadyrBrowserException extends RuntimeException {

	private static final long serialVersionUID = -2591910918273220741L;

	public LadyrBrowserException(Exception exception){
		super(exception);
	}

	public LadyrBrowserException(String msg) {
		super(msg);
	}
	
	public LadyrBrowserException(String msg, Throwable t) {
		super(msg,t);
	}
	
}
