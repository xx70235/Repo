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

import java.util.ArrayList;
import java.util.List;

import org.cnstar.browser.LadyrBrowser;
import org.cnstar.browser.LadyrXPathEvaluator;
import org.mozilla.dom.DocumentImpl;
import org.mozilla.dom.NodeFactory;
import org.mozilla.dom.NodeImpl;
import org.mozilla.dom.html.HTMLDocumentImpl;
import org.mozilla.xpcom.Mozilla;
import org.mozilla.interfaces.nsIComponentManager;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMHTMLDocument;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMXPathEvaluator;
import org.mozilla.interfaces.nsIDOMXPathNSResolver;
import org.mozilla.interfaces.nsIDOMXPathResult;
import org.mozilla.interfaces.nsISupports;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class XPathEvaluatorImpl implements LadyrXPathEvaluator {

	private nsIDOMXPathEvaluator xpathEval;

	public XPathEvaluatorImpl() {
		Mozilla moz = Mozilla.getInstance();
		nsIComponentManager componentManager = moz.getComponentManager();
		String NS_IDOMXPATHEVALUATOR_CONTACTID = "@mozilla.org/dom/xpath-evaluator;1"; //$NON-NLS-1$
		xpathEval = (nsIDOMXPathEvaluator) componentManager
				.createInstanceByContractID(NS_IDOMXPATHEVALUATOR_CONTACTID,
						null, nsIDOMXPathEvaluator.NS_IDOMXPATHEVALUATOR_IID);
	}

	/**
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @return the node corresponding to the given xpath expression. If more
	 *         than one node is found for that xpath, then the first node found
	 *         is returned. The whole document is taken as context for the
	 *         evaluation.
	 */
	public Node xpathNode(LadyrBrowser browser, String xpath) {
		return xpathNode(browser, xpath, browser.getDocument());
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the node corresponding to the given xpath expression in the given
	 *         context. If more than one node is found for that xpath, then the
	 *         first node found is returned.
	 **/
	public Node xpathNode(LadyrBrowser browser, String xpath, Node context) {
		return xpathNode(browser, xpath, Node.class, context);
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted node
	 * @return the node corresponding to the given xpath expression. If more
	 *         than one node is found for that xpath, then the first node found
	 *         is returns. The whole document is taken as context for the
	 *         evaluation.
	 **/
	public <T extends Node> T xpathNode(LadyrBrowser browser, String xpath,
			Class<T> nodeClass) {
		return xpathNode(browser, xpath, nodeClass, browser.getDocument());
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted node
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the node corresponding to the given xpath expression. If more
	 *         than one node is found for that xpath, then the first node found
	 *         is returns.
	 **/
	public <T extends Node> T xpathNode(LadyrBrowser browser, String xpath,
			Class<T> nodeClass, Node context) {

		List<Node> nodes = xpathNodes(browser, xpath, context);

		if (nodes != null && nodes.size() > 0) {
			return (T) nodes.get(0);
		} else {
			return null;
		}

	}

	/**
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @return the nodes corresponding to the given xpath expression. The whole
	 *         document is taken as context for the evaluation.
	 */
	public List<Node> xpathNodes(LadyrBrowser browser, String xpath) {
		return xpathNodes(browser, xpath, browser.getDocument());
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted nodes
	 * @return the nodes corresponding to the given xpath expression. The whole
	 *         document is taken as context for the evaluation.
	 **/
	public <T extends Node> List<T> xpathNodes(LadyrBrowser browser,
			String xpath, Class<T> nodeClass) {
		return xpathNodes(browser, xpath, nodeClass, browser.getDocument());
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the nodes corresponding to the given xpath expression within the
	 *         given context.
	 **/
	public List<Node> xpathNodes(LadyrBrowser browser, String xpath,
			Node context) {
		return xpathNodes(browser, xpath, Node.class, context);
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param nodeClass
	 *            class to cast the extracted nodes
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the nodes corresponding to the given xpath expression within the
	 *         given context.
	 **/
	public <T extends Node> List<T> xpathNodes(LadyrBrowser browser,
			String xpath, Class<T> nodeClass, Node context) {

		if (nodeClass == null) {
			nodeClass = (Class<T>) Node.class;
		}

		return (List<T>) resolveXPathNodes(browser, xpath, context);
	}

	/**
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @return the String corresponding to the given xpath expression. The whole
	 *         document is taken as context for the evaluation.
	 */
	public String xpathString(LadyrBrowser browser, String xpath) {
		return xpathString(browser, xpath, browser.getDocument());
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the <code>String</code> corresponding to the given xpath
	 *         expression within the given context.
	 **/
	public String xpathString(LadyrBrowser browser, String xpath, Node context) {

		nsIDOMXPathResult result = resolveXPath(browser, xpath, context,
				nsIDOMXPathResult.STRING_TYPE);

		return result.getStringValue();
	}

	/**
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @return the <code>String</code> corresponding to the given xpath
	 *         expression. The whole document is taken as context for the
	 *         evaluation.
	 */
	public boolean xpathBoolean(LadyrBrowser browser, String xpath) {
		return xpathBoolean(browser, xpath, browser.getDocument());
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the <code>boolean</code> corresponding to the given xpath
	 *         expression within the given context.
	 **/
	public boolean xpathBoolean(LadyrBrowser browser, String xpath, Node context) {

		nsIDOMXPathResult result = resolveXPath(browser, xpath, context,
				nsIDOMXPathResult.BOOLEAN_TYPE);

		return result.getBooleanValue();
	}

	/**
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @return the <code>double</code> corresponding to the given xpath
	 *         expression. The whole document is taken as context for the
	 *         evaluation.
	 */
	public double xpathDouble(LadyrBrowser browser, String xpath) {
		return xpathDouble(browser, xpath, browser.getDocument());
	}

	/**
	 * 
	 * @param browser
	 * @param xpath
	 *            xpath expression
	 * @param context
	 *            <b>If context is null, then the whole document is taken as
	 *            context</b>. Represents the context from which the search is
	 *            performed.
	 * @return the <code>double</code> corresponding to the given xpath
	 *         expression within the given context.
	 **/
	public double xpathDouble(LadyrBrowser browser, String xpath, Node context) {

		nsIDOMXPathResult result = resolveXPath(browser, xpath, context,
				nsIDOMXPathResult.NUMBER_TYPE);

		return result.getNumberValue();
	}

	private nsIDOMHTMLDocument getNsDocument(LadyrBrowser browser) {
		HTMLDocumentImpl document = (HTMLDocumentImpl) browser.getDocument();
		return document.getInstance();
	}

	public nsIDOMNode getNsNode(Node item) {
		NodeImpl node = (NodeImpl) item;
		return node.getInstance();
	}

	private <T> List<T> getNodes(nsIDOMXPathResult result) {
		List<T> nodes = new ArrayList<T>();

		nsIDOMNode rNode;
		while ((rNode = result.iterateNext()) != null) {

			nodes.add((T) NodeFactory.getNodeInstance(rNode));
		}
		return nodes;
	}

	private List<Node> resolveXPathNodes(LadyrBrowser browser, String xpath,
			Node context) {

		if (context == null) {
			context = browser.getDocument();
		}

		// nsIDOMHTMLDocument document = getNsDocument(browser);
		// HTMLDocumentImpl document = (HTMLDocumentImpl) browser.getDocument();
		nsIDOMDocument document = null;
		if (context instanceof Document) {
			Document doc = (Document) context;
			if(doc instanceof DocumentImpl)
			{
				DocumentImpl documentImpl = (DocumentImpl) doc;
				document =  documentImpl.getInstance();
			}
//			if(doc instanceof HTMLDocumentImpl)
//			{
//			HTMLDocumentImpl documentImpl = (HTMLDocumentImpl) doc;
//			document = documentImpl.getInstance();
//			}
			
		}
		nsIDOMNode contextNode = getNsNode(context);

		nsIDOMXPathNSResolver res = xpathEval.createNSResolver(document);

		int retries = 2;

		List<Node> rNodes = null;

		do {

			nsISupports obj = xpathEval.evaluate(xpath, contextNode, res,
					nsIDOMXPathResult.ORDERED_NODE_ITERATOR_TYPE, null);

			nsIDOMXPathResult result = (nsIDOMXPathResult) obj
					.queryInterface(nsIDOMXPathResult.NS_IDOMXPATHRESULT_IID);

			try {
				rNodes = getNodes(result);
				break;
			} catch (org.mozilla.xpcom.XPCOMException e) {

				if (retries == 0) {
					throw e;
				} else {
					retries--;
				}
			}

		} while (true);

		return rNodes;
	}

	private nsIDOMXPathResult resolveXPath(LadyrBrowser browser, String xpath,
			Node context, int type) {
		if (context == null) {
			context = browser.getDocument();
		}

		nsIDOMHTMLDocument document = getNsDocument(browser);
		nsIDOMNode contextNode = getNsNode(context);

		nsIDOMXPathNSResolver res = xpathEval.createNSResolver(document);
		nsISupports obj = xpathEval.evaluate(xpath, contextNode, res, type,
				null);

		nsIDOMXPathResult result = (nsIDOMXPathResult) obj
				.queryInterface(nsIDOMXPathResult.NS_IDOMXPATHRESULT_IID);

		return result;
	}

}
