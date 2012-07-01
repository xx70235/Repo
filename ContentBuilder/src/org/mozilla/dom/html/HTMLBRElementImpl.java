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


public class HTMLBRElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLBRElement
{
    protected HTMLBRElementImpl(nsIDOMHTMLBRElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLBRElementImpl getDOMInstance(nsIDOMHTMLBRElement mozInst)
    {
        HTMLBRElementImpl node = (HTMLBRElementImpl) instances.get(mozInst);
        return node == null ? new HTMLBRElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLBRElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLBRElement) moz;
    }
    
    public String getClear()
    {
        String result = getInstance().getClear();
        return result;
    }
    
    public void setClear(String clear)
    {
        getInstance().setClear(clear);
    }
    
}
