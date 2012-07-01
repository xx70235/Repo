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

public class Statistics {
	
	private int numRequest;
	private int numResponse;
	
	
	public void reset() {
		numRequest = 0;
		numResponse = 0;
		
	}
	
	public int getNumRequest(){
		return numRequest;
	}
	
	public int getNumResponse(){
		return numResponse;
	}
	
	public void newRequest(){
		++numRequest;
	}
	
	public void newResponse(){
		++numResponse;
	}
	
	public void print(){
	//	System.out.println("***************************************");
	//	System.out.println("***********  STATISTICS  **************");
	//	System.out.println("***************************************");
		System.out.println("     Total requests: "+numRequest);
		System.out.println("     Total responses: "+numResponse);
		
	}
/*este objeto tendr?métodos para acceder al
número de peticiones, y respuestas, al tamaño de las peticiones, al tamaño de
las respuestas,*/
	
}
