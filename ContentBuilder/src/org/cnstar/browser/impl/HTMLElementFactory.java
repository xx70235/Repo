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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.html2.HTMLElement;

public class HTMLElementFactory {

	private static HTMLElementFactory instance;
	
	private Map<String, String> corresp;
		
	private HTMLElementFactory() {
		initCorrespondence();
	}
	
	public static HTMLElementFactory getInstance(){
		if(instance == null){
			instance = new HTMLElementFactory();
		}
		return instance;
	}
	
	public static HTMLElement getHTMLElement(nsIDOMNode nsNode) {
		return getInstance().getConcreteNode(nsNode);
	}
	
	private void initCorrespondence() {

		corresp = new HashMap<String, String>();
		corresp.put("a", "Anchor");
		corresp.put("applet", "Applet");
		corresp.put("area", "Area");
		corresp.put("base", "Base");
		corresp.put("basefont", "BaseFont");
		corresp.put("body", "Body");
		corresp.put("br", "BR");
		corresp.put("button", "Button");
		corresp.put("dir", "Directory");
		corresp.put("div", "Div");
		corresp.put("dl", "DList");
		corresp.put("fieldset", "FieldSet");
		corresp.put("font", "Font");
		corresp.put("form", "Form");
		corresp.put("frame", "Frame");
		corresp.put("frameset", "FrameSet");
		corresp.put("head", "Head");
		corresp.put("h1", "Heading");
		corresp.put("h2", "Heading");
		corresp.put("h3", "Heading");
		corresp.put("h4", "Heading");
		corresp.put("h5", "Heading");
		corresp.put("h6", "Heading");
		corresp.put("hr", "HR");
		corresp.put("html", "Html");
		corresp.put("iframe", "IFrame");
		corresp.put("img", "Image");
		corresp.put("input", "Input");
		corresp.put("isindex", "IsIndex");
		corresp.put("label", "Label");
		corresp.put("legend", "Legend");
		corresp.put("li", "LI");
		corresp.put("link", "Link");
		corresp.put("map", "Map");
		corresp.put("menu", "Menu");
		corresp.put("meta", "Meta");
		corresp.put("ins", "Mod");
		corresp.put("del", "Mod");
		corresp.put("object", "Object");
		corresp.put("ol", "OList");
		corresp.put("optgroup", "OptGroup");
		corresp.put("option", "Option");
		corresp.put("p", "Paragraph");
		corresp.put("param", "Param");
		corresp.put("pre", "Pre");
		corresp.put("q", "Quote");
		corresp.put("script", "Script");
		corresp.put("select", "Select");
		corresp.put("style", "Style");
		corresp.put("caption", "TableCaption");
		corresp.put("td", "TableCell");
		corresp.put("col", "TableCol");
		corresp.put("table", "Table");
		corresp.put("tr", "TableRow");
		corresp.put("thead", "TableSection");
		corresp.put("tfoot", "TableSection");
		corresp.put("tbody", "TableSection");
		corresp.put("textarea", "TextArea");
		corresp.put("title", "Title");
		corresp.put("ul", "UList");

	}

	public HTMLElement getConcreteNode(nsIDOMNode nsNode) {

		if (nsNode.getNodeType() == nsIDOMNode.ELEMENT_NODE) {

			String htmlElementType = corresp.get(nsNode.getNodeName()
					.toLowerCase());
			
			if(htmlElementType == null){
				return null;
			}
			
			String nsClassName = "org.mozilla.interfaces.nsIDOMHTML"
					+ htmlElementType + "Element";
			String nsFieldInterfaceName = "NS_IDOMHTML"
					+ htmlElementType.toUpperCase() + "ELEMENT_IID";

			try {
				Class nsClass = Class.forName(nsClassName);
				Field field = nsClass.getField(nsFieldInterfaceName);
				String iid = (String) field.get(null);
				Object nsElement = nsNode.queryInterface(iid);

				String w3cClassName = "org.mozilla.dom.html.HTML"
						+ htmlElementType + "ElementImpl";
				Class w3cClass = Class.forName(w3cClassName);
				Method creationMethod = w3cClass.getMethod("getDOMInstance",
						nsClass);
				HTMLElement node = (HTMLElement) creationMethod.invoke(null, nsElement);
				return node;

			} catch (Exception e) {
				throw new Error(e);
			}
		}

		return null;
	}

}
