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

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLFormElement;
import org.w3c.dom.html2.HTMLInputElement;
import org.w3c.dom.html2.HTMLTextAreaElement;

/**
 * The interface used to manage a WebBrowser. This interface is intended to  be used by crawling system, because this,
 * the methods that it provides are oriented to crawling specific tasks. 
 * @author usuario 
 */
public interface LadyrBrowser {

	/**
	 * Returns a Configuration object to configure the browser.
	 * @return
	 */
	public Configuration getConfiguration();
	
	/**
	 * Submits the passed form and blocks until new page is loaded 
	 * @param form
	 */
	public void submitForm(HTMLFormElement form);

	/**
	 * Submits the passed form and blocks until new page is loaded. The method blocks a maximum of
	 * waitSeconds, if this time is reached and the page is not fully loaded, a TimeoutException
	 * will be thrown.
	 * @param form
	 * @param waitSeconds
	 * @throws TimeoutException
	 */
	public void submitForm(HTMLFormElement form, double waitSeconds) throws TimeoutException;

	/**
	 * Tell WebBrowser to go this url. Blocks until page has been fully loaded. 
	 * @param url
	 * @throws FilteredURLException
	 */
	public void go(String url) throws FilteredURLException;

	/**
	 * Tell WebBrowser to go this url. The method blocks a maximum of waitSeconds, if this time
	 * is reached and the page is not fully loaded, a TimeoutException will be thrown.  
	 * @param url
	 * @param secondsWait	if -1, then the method wait until page has been fully loaded
	 * 	(same behaviour that {@ling {@link #go(String)}})
	 * @throws TimeoutException
	 * @throws FilteredURLException
	 */
	public void go(String url, double secondsWait) throws TimeoutException, FilteredURLException;

	/**
	 * Tell WebBrowser to go this url. This method internally wait waitSeconds for page loading. If the 
	 * time is reached then, it retry numRetries times. If all retries throws TimeoutExceptiom, then 
	 * TimeoutException is thrown to caller.   
	 * @param url
	 * @param secondsWait
	 * @throws TimeoutException
	 * @throws FilteredURLException
	 */
	public void go(String url, double secondsWait, int numRetries) throws TimeoutException, FilteredURLException;

	/**
	 * Invoke click in passed node. The node must be a HTMLInputElement, HTMLAnchorElement,
	 * or HTMLElements with onclick attribute. This method blocks until web page is fully 
	 * loaded.
	 * @param node
	 */
	public void click(Node node);

	/**
	 * Invoke click in passed node. The node must be a HTMLInputElement, HTMLAnchorElement,
	 * or HTMLElements with onclick attribute. The method blocks a maximum of waitSeconds,
	 * if this time is reached and the page is not fully loaded, a TimeoutException will be
	 * thrown.
	 * @param node
	 * @param waitSeconds
	 * @throws TimeoutException
	 */
	public void click(Node node, double waitSeconds) throws TimeoutException;

	/**
	 * Invoke click in the node corresponding to the xpath expression. The node must be a
	 * HTMLInputElement, HTMLAnchorElement, or HTMLElements with onclick attribute. The 
	 * method blocks a maximum of waitSeconds, if this time is reached and the page is not
	 * fully loaded, a TimeoutException will be thrown.
	 * @param xpathNode
	 */
	public void click(String xpathNode);

	/**
	 * Enter the specified text in passed Node. The node must be a class of HTMLInputElement or 
	 * HTMLTextAreaElement. 
	 * @param node
	 * @param text
	 */
	public void enter(Node node, String text);

	/**
	 * Enter the specified text in passed HTMLInputElement
	 * @param component
	 * @param text
	 */
	public void enter(HTMLInputElement component, String text);

	/**
	 * Enter the specified text in passed HTMLTextAreaElement
	 * @param component
	 * @param text
	 */
	public void enter(HTMLTextAreaElement component, String text);

	/**
	 * Returns the org.w3c.dom.Document loaded in WebBrowser
	 * @return
	 */
	public Document getDocument();

	/**
	 * Executes the specified JavaScript
	 * @param javaScript
	 */
	public void executeJavascript(String javaScript);
	
	/**
	 * Invokes back.
	 * @return
	 */
	public boolean back();

	/**
	 * Invokes forward
	 * @return
	 */
	public boolean forward();

	/**
	 * Invokes reload(); 
	 */
	public void reload();

	/**
	 * Returns the URL of loaded page
	 * @return
	 */
	public String getCurrentURL();

	/**
	 * Returns the referring URL 
	 * @return
	 */
	public String getReferringURL();
	
	public Resource downloadResource(String url);

	public void downloadResource(String url, ResourceListener listener);
	
	public void loadHtml(String html);
	
	/**
	 * Saves this url in the specified file
	 * @param url
	 * @param file
	 */
	public void saveURL(String url,File file);
	
	
	/**
	 * Clears web browser cache
	 */
	public void clearCache();
	
	/**
	 * Set the shell title with the specified title 
	 * @param title
	 */
	public void setTitle(String title);
	
	

	/**
	 * Returns the node resulting from evaluating this xpath in the document
	 * @param xpath
	 * @return
	 */
	public Node xpathNode(String xpath);

	/**
	 * Returns the node resulting from evaluating this xpath in the specified context
	 * @param xpath
	 * @param context
	 * @return
	 */
	public Node xpathNode(String xpath, Node context);
	
	/**
	 * Returns the node, converted to class <code>nodeClass</code>, resulting from
	 * evaluating this xpath 
	 * @param <T>
	 * @param xpath
	 * @param nodeClass
	 * @return
	 */
	public <T extends Node> T xpathNode(String xpath, Class<T> nodeClass);
	
	/**
	 * Returns the node, converted to class <code>nodeClass</code>,  resulting from
	 * evaluating this xpath in the specified context
	 * @param <T>
	 * @param xpath
	 * @param nodeClass
	 * @param context
	 * @return
	 */
	public <T extends Node> T xpathNode(String xpath, Class<T> nodeClass, Node context);
	
	/**
	 * Returns the list of the nodes resulting from evaluating this xpath
	 * @param xpath
	 * @return
	 */
	public List<Node> xpathNodes(String xpath);

	/**
	 * Returns the list of the nodes resulting from evaluating this xpath. The returned 
	 * list is a generified list with specified type. This method allows a more compact
	 * iteration over the returned nodes 
	 * @param xpath
	 * @return
	 */
	public <T extends Node> List<T> xpathNodes(String xpath, Class<T> nodeClass);

	/**
	 * Returns the list of the nodes resulting from evaluating this xpath in the specified context 
	 * @param xpath
	 * @param context
	 * @return
	 */
	public List<Node> xpathNodes(String xpath, Node context);
	
	/**
	 * Returns the list of the nodes resulting from evaluating this xpath in the specified context.
	 * The returned list is a generified list with specified type. This method allows a more 
	 * compact iteration over the returned nodes  
	 * @param xpath
	 * @param context
	 * @return
	 */
	public <T extends Node> List<T> xpathNodes(String xpath, Node context, Class<T> nodeClass);

	/**
	 * Returns the String resulting from evaluating this xpath
	 * @param xpath
	 * @return
	 */
	public String xpathString(String xpath);

	/**
	 * Returns the String resulting from evaluating this xpath in the specified context
	 * @param xpath
	 * @return
	 */
	public String xpathString(String xpath, Node context);

	/**
	 * Returns the boolean resulting from evaluating this xpath in the document
	 * @param xpath
	 * @return
	 */
	public boolean xpathBoolean(String xpath);

	/**
	 * Returns the boolean resulting from evaluating this xpath in the specified context
	 * @param xpath
	 * @param context
	 * @return
	 */
	public boolean xpathBoolean(String xpath, Node context);

	/**
	 * Returns the double resulting from evaluating this xpath in the document
	 * @param xpath
	 * @return
	 */
	public double xpathDouble(String xpath);

	/**
	 * Returns the double resulting from evaluating this xpath in the specified context
	 * @param xpath
	 * @param context
	 * @return
	 */
	public double xpathDouble(String xpath, Node context);
		
	/**
	 * Returns the text resulting from evaluating the xpath expression and extracting its
	 * text content.
	 * @param xpath
	 * @return
	 */
	public List<String> extractText(String xpath);
	
	/**
	 * Returns the text resulting from evaluating the xpath expression, extracting its
	 * text content and applying a regular expression.
	 * @param xpath
	 * @param regExp
	 * @return
	 */
	public List<String> extractText(String xpath, String regExp);
	
	/**
	 * Returns the text resulting from evaluating the xpath expression in the given
	 * context and extracting its text content.
	 * @param xpath
	 * @param context
	 * @return
	 */
	public List<String> extractText(String xpath, Node context);
	
	/**
	 * Returns the text resulting from evaluating the xpath expression in the given
	 * context, extracting its text content and applying a regular expression.
	 * @param xpath
	 * @param regExp
	 * @param context
	 * @return
	 */
	public List<String> extractText(String xpath, String regExp, Node context);
	
	/**
	 * Returns the text resulting from evaluating the xpath expression in the given
	 * context, extracting its text content, separating text from different nodes
	 * with the given node separator and applying a regular expression.
	 * @param xpath
	 * @param regExp
	 * @param context
	 * @param nodeSeparator
	 * @return
	 */
	public List<String> extractText(String xpath, String regExp, Node context, String nodeSeparator);
	
	/**
	 * Returns the normalized text resulting from evaluating the xpath expression
	 * and extracting its text content.
	 * @param xpath
	 * @return
	 */
	public List<String> extractNormText(String xpath);
	
	/**
	 * Returns the normalized text resulting from evaluating the xpath expression,
	 * extracting its text content and applying a regular expression.
	 * @param xpath
	 * @param regExp
	 * @return
	 */
	public List<String> extractNormText(String xpath, String regExp);
	
	/**
	 * Returns the normalized text resulting from evaluating the xpath expression
	 * in the given context and extracting its text content.
	 * @param xpath
	 * @param context
	 * @return
	 */
	public List<String> extractNormText(String xpath, Node context);
	
	/**
	 * Returns the normalized text resulting from evaluating the xpath expression
	 * in the given context, extracting its text content and applying a regular
	 * expression.
	 * @param xpath
	 * @param regExp
	 * @param context
	 * @return
	 */
	public List<String> extractNormText(String xpath, String regExp, Node context);
	
	/**
	 * Returns the <code>Integer</code> resulting from evaluating the xpath expression
	 * and parsing its text content to <code>Integer</code>..
	 * @param xpath
	 * @return
	 */
	public List<Integer> extractInt(String xpath);
	
	/**
	 * Returns the <code>Integer</code> resulting from evaluating the xpath
	 * expression in the given context and parsing its text content to
	 *	<code>Integer</code>.
	 * @param xpath
	 * @param context
	 * @return
	 */
	public List<Integer> extractInt(String xpath, Node context);
	
	/**
	 * Returns the <code>Integer</code> resulting from evaluating the xpath
	 * expression, applying the regular expression and parsing its text content
	 * to <code>Integer</code>.
	 * @param xpath
	 * @param regExp
	 * @return
	 */
	public List<Integer> extractInt(String xpath, String regExp);
	
	/**
	 * Returns the <code>Integer</code> resulting from evaluating the xpath
	 * expression in the given context, applying the regular expression 
	 * and parsing its text content to <code>Integer</code>.
	 * @param xpath
	 * @param regExp
	 * @param context
	 * @return
	 */
	public List<Integer> extractInt(String xpath, String regExp, Node context);
	
	public void open();
	
	public void close();
	
	public void waitClick(String... xpaths);

	public String waitGetCurrentURL(String regex);

	public String waitExtractText(String xpath, String regex);

	public String waitExtractText(String xpath);
	
	public List<String> waitExtractTextList(String xpath);

	

}