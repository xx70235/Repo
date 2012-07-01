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

import java.util.List;

import org.w3c.dom.Node;

public interface LadyrXPathEvaluator {

	public Node xpathNode(LadyrBrowser browser, String xpath);
	public Node xpathNode(LadyrBrowser browser, String xpath, Node context);
	public <T extends Node> T xpathNode(LadyrBrowser browser, String xpath, Class<T> nodeClass);
	public <T extends Node> T xpathNode(LadyrBrowser browser, String xpath, Class<T> nodeClass, Node context);
	
	public List<Node> xpathNodes(LadyrBrowser browser, String xpath);
	public List<Node> xpathNodes(LadyrBrowser browser, String xpath, Node context);
	public <T extends Node> List<T> xpathNodes(LadyrBrowser browser, String xpath, Class<T> nodeClass);
	public <T extends Node> List<T> xpathNodes(LadyrBrowser browser, String xpath, Class<T> nodeClass, Node context);
	
	public String xpathString(LadyrBrowser browser,String xpath);
	public String xpathString(LadyrBrowser browser,String xpath, Node context);

	public boolean xpathBoolean(LadyrBrowser browser,String xpath);
	public boolean xpathBoolean(LadyrBrowser browser,String xpath, Node context);

	public double xpathDouble(LadyrBrowser browser,String xpath);
	public double xpathDouble(LadyrBrowser browser,String xpath, Node context);
	
}
