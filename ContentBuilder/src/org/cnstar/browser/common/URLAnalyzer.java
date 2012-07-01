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
package org.cnstar.browser.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class URLAnalyzer {

	private static final String varExtractorRegEx = "([^=&]+)=([^=&]+)";

	private static final Pattern varExtractorPattern = Pattern
			.compile(varExtractorRegEx);

	private static final String URL_ENCODING = "UTF-8";

	public static String getPath(String url) {
		try {
			URL rUrl = new URL(url);
			return rUrl.getPath();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static String getRef(String url) {
		try {
			URL rUrl = new URL(url);
			return rUrl.getRef();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static URL getURL(String url) {
		try {
			return new URL(new URL("http://ww.ww.ww"), url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static Map<String, String> getParams(String url) {

		URL rUrl = getURL(url);
		if (rUrl == null) {
			return Collections.emptyMap();
		} else {
			return getParams(url);
		}
	}

	public static Map<String, String> getParams(URL url) {

		Map<String, String> result = new HashMap<String, String>();

		if (url == null) {
			return result;
		}

		String query = url.getQuery();
		if (query == null) {
			return result;
		}

		Matcher m = varExtractorPattern.matcher(query);

		while (m.find()) {
			if (m.groupCount() != 2) {
				return result;
			}
			try {
				String varName = URLDecoder.decode(m.group(1), URL_ENCODING);
				String varValue = URLDecoder.decode(m.group(2), URL_ENCODING);
				result.put(varName, varValue);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				break;
			}
		}
		return result;
	}

	/**
	 * Case insesitive var matching.
	 * 
	 * @param paramName
	 *            case sensitive matching
	 * @return variable <code>varName</code> value
	 */
	public static String getParam(URL url, String paramName) {

		Map<String, String> vars = getParams(url);

		for (String varKey : vars.keySet()) {
			if (varKey.toLowerCase().equals(paramName.toLowerCase())) {
				return vars.get(varKey);
			}
		}

		return null;
	}

	public static String getParam(String url, String paramName) {
		if (url == null || url.equals("")) {
			return null;
		}

		try {
			URL rUrl = new URL(new URL("http://ww.ww.ww"), url);
			return getParam(rUrl, paramName);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public static String getParam(Node anchorNode, String paramName) {

		if (anchorNode == null) {
			return null;
		} else if (anchorNode instanceof Element) {
			Element element = (Element) anchorNode;

			String href = element.getAttribute("href");
			if (href == null) {
				return null;
			} else {
				return getParam(href, paramName);
			}
		} else {
			return null;
		}
	}

	public static BigDecimal getParamBigDecimal(String url, String paramName) {
		String paramValue = getParam(url, paramName);
		if (paramValue == null) {
			return null;
		} else {

			try {
				return new BigDecimal(paramValue);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	public static Integer getParamInt(String url, String paramName) {
		String paramValue = getParam(url, paramName);
		if (paramValue == null) {
			return null;
		} else {

			try {
				return new Integer(paramValue);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
}
