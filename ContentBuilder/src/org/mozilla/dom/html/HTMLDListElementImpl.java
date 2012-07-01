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


public class HTMLDListElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLDListElement
{
    protected HTMLDListElementImpl(nsIDOMHTMLDListElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLDListElementImpl getDOMInstance(nsIDOMHTMLDListElement mozInst)
    {
        HTMLDListElementImpl node = (HTMLDListElementImpl) instances.get(mozInst);
        return node == null ? new HTMLDListElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLDListElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLDListElement) moz;
    }
    
    public boolean getCompact()
    {
        boolean result = getInstance().getCompact();
        return result;
    }
    
    public void setCompact(boolean compact)
    {
        getInstance().setCompact(compact);
    }
    
}