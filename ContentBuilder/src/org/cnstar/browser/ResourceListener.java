/*********************************************************************************
 *
 * Copyright (C) 2007  Micael Gallego Carrillo,
 * 					   Alberto P�rez Garc�a-Plaza,
 * 					   LADyR Team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Micael Gallego Carrillo
 *     Alberto P�rez Garc�a-Plaza
 *     LADyR Team (http://www.ladyr.es/)
 * 
 * Contact information: info-ladyr at gsyc.es
 *
 *********************************************************************************/
package org.cnstar.browser;

import java.io.OutputStream;

public interface ResourceListener {
	
	public void setContentType(String contentType);
	public OutputStream getOutputStream();
	public void setURL(String url);

}