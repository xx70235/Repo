/*
+-----------------------------------------------+
| do NOT edit this file!                        |
+-----------------------------------------------+
| This file is an auto-generated implementation | 
| of the coresponding org.w3c.dom interface via |
| Java-XPCOM                                    |
| (c)2005, Peter Szinek [peter@rt.sk]           |
+-----------------------------------------------+
*/


package org.mozilla.dom.html;

//XPCOM import
import org.mozilla.interfaces.*;


public class HTMLQuoteElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLQuoteElement
{
    protected HTMLQuoteElementImpl(nsIDOMHTMLQuoteElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLQuoteElementImpl getDOMInstance(nsIDOMHTMLQuoteElement mozInst)
    {
        HTMLQuoteElementImpl node = (HTMLQuoteElementImpl) instances.get(mozInst);
        return node == null ? new HTMLQuoteElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLQuoteElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLQuoteElement) moz;
    }
    
    public String getCite()
    {
        String result = getInstance().getCite();
        return result;
    }
    
    public void setCite(String cite)
    {
        getInstance().setCite(cite);
    }
    
}
