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
package org.cnstar.browser.impl;

public abstract class Filter<T> {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 *  This method must implements the filter.
	 *  
	 * @return false indicates that element must be filtered. 
	 */
	public abstract boolean passFilter(T element);

}