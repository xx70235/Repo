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


public class HTMLHtmlElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLHtmlElement
{
    protected HTMLHtmlElementImpl(nsIDOMHTMLHtmlElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLHtmlElementImpl getDOMInstance(nsIDOMHTMLHtmlElement mozInst)
    {
        HTMLHtmlElementImpl node = (HTMLHtmlElementImpl) instances.get(mozInst);
        return node == null ? new HTMLHtmlElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLHtmlElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLHtmlElement) moz;
    }
    
    public String getVersion()
    {
        String result = getInstance().getVersion();
        return result;
    }
    
    public void setVersion(String version)
    {
        getInstance().setVersion(version);
    }
    
}
