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
package org.cnstar.webfetcher;

import java.util.List;

import org.cnstar.browser.Configuration;
import org.cnstar.browser.LadyrBrowserException;
import org.cnstar.browser.impl.BrowserImpl;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.html2.HTMLAnchorElement;
import org.w3c.dom.html2.HTMLInputElement;
import org.w3c.dom.Node;


public class SimpleBrowser {

    public SimpleBrowser() {
	BrowserImpl browser = new BrowserImpl();

	/*
	 * Uncomment the following lines to change some browser preferences set
	 * by default
	 */
//	Configuration config = browser.getConfiguration();
//	config.setIntPreference("network.image.imageBehavior ", 0);
//	config.setBooleanPreference("javascript.enabled", true);
//
	browser.go("http://www.renren.com/");
//
	
	sleep(3000);
Node node = browser.xpathNode("//INPUT[@type='password']");
browser.enter(node, "test");

	// Fill the google input text field to search some terms

	// browser.enter(browser.xpathNode("//input[2]"),
	// "LADyR: the lab for networks");
	// browser.enter(browser.xpathNode("//input[@id='regnow']"),"LADyR: the lab for networks");
	// browser.enter(browser.xpathNode("/html/body/div[@id='doc3']/div[@id='nhdrwrap']/div[@id='nhdrwrapinner']/div[@id='nhdrwrapsizer']/div[@id='gsea']/form[@id='sfrm']/table/tbody/tr[1]/td[2]/input[@id='q']"),
	// "LADyR: the lab for networks");

	// sleep(2000);

	// Push the button to search
	// nsIDOMNode node1 = browser.xpathnsINode("//input[@id='login']");
	//
	// browser.click((nsIDOMElement)node1.queryInterface(nsIDOMElement.NS_IDOMELEMENT_IID));

	// sleep(2000);

	// Click on the first search result
	// browser.click("//div[@class='g'][1]/h2[@class='r']/a[@class='l']");
	// browser.click("/html/body/div[@id='res']/div[1]/ol/li[1]/div/h2/a");

//	List<String> textos = browser
//		.extractNormText("/html/body[@id='gsr']/div[@id='header']/div[@id='gbar']/nobr/a[1]");
//	System.out.println("Comprobamos que imprime el texto");
//	System.out.println(textos.get(0));
//
//	browser.click("/html/body[@id='gsr']/div[@id='header']/div[@id='gbar']/nobr/a[1]");
//
//	sleep(10000);
//
//	System.out.println("...Finalizando este ejemplo");
//	System.out.println("...Empezando otro");
//
//	browser.go("http://www.google.es");
//
//	sleep(3000);

	// Get the W3C DOM anchor element containing the text 'Noticias'
//	HTMLAnchorElement a = browser.xpathNodes(
//		"//a[contains(text(),'Noticias')]", HTMLAnchorElement.class)
//		.get(0);
//	// Click on the anchor previously obtained
//	browser.click(a);
//
//	sleep(3000);
//
//	try {
//	    a = browser.xpathNodes("//a[contains(text(),'España')]",
//		    HTMLAnchorElement.class).get(0);
//	} catch (IndexOutOfBoundsException e) {
//	    e.printStackTrace();
//	}
//
//	browser.click(a);
//
//	sleep(5000);
//
//	browser.go("http://www.google.es");
//
//	sleep(3000);
//
//	// Get the W3C DOM anchor element containing the text 'Noticias'
//	a = browser.xpathNodes("//a[contains(text(),'Noticias')]",
//		HTMLAnchorElement.class).get(0);
//	// Click on the anchor previously obtained
//	browser.click(a);
//
//	sleep(3000);
//
//	try {
//	    Node node = browser.xpathNodes("//input[@name='q']").get(0);
//	    browser.enter(node, "nasdaq");
//	} catch (LadyrBrowserException sbe) {
//	    sbe.printStackTrace();
//	}
//
//	// Get the input button used to submit the form
//	HTMLInputElement e = browser.xpathNodes(
//		"//input[@value='Buscar en Noticias']", HTMLInputElement.class)
//		.get(0);
//
//	// Click the input button and start the search for the term 'nasdaq'
//	// in news section
//
//	browser.click(e);
//
//	sleep(3000);
//
//	// Load a different page with javascript examples
//	browser.go("http://www.codearchive.com/code/0300/0309-acces009.htm");
//
//	sleep(4000);
//	// Get a W3C anchor element containing an 'onlick' attribute
//
//	a = browser.xpathNodes("//a[contains(text(),'2')]",
//		HTMLAnchorElement.class).get(0);
//	// Click the anchor and then the javascript will be executed by
//	// our browser
//	browser.click(a);
//
//	sleep(5000);

	// Finish the application
	Runtime.getRuntime().halt(0);
    }

    /**
     * Wait some milliseconds
     * 
     * @param millis
     */
    private void sleep(int millis) {

	try {
	    Thread.sleep(millis);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

    }

    public static void main(String[] args) {
	new SimpleBrowser();
    }

}
