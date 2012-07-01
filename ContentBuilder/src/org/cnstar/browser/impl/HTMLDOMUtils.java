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

import org.mozilla.dom.html.HTMLElementImpl;
import org.mozilla.interfaces.*;
import org.w3c.dom.Document;
import org.w3c.dom.html2.HTMLElement;

public class HTMLDOMUtils {

	public static String getInnerHTML(HTMLElement element) {
		HTMLElementImpl elementImpl = (HTMLElementImpl) element;

		nsIDOMHTMLElement mozillaDOMElement = elementImpl.getInstance();
		nsIDOMNSHTMLElement mozillaSpecElement = (nsIDOMNSHTMLElement) mozillaDOMElement
				.queryInterface(nsIDOMNSHTMLElement.NS_IDOMNSHTMLELEMENT_IID);
		return mozillaSpecElement.getInnerHTML();
	}
	
	public static String getInnerHTML(Document document){
		return getInnerHTML((HTMLElement) document.getDocumentElement());
	}

}
