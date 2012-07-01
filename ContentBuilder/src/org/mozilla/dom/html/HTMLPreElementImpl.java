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


public class HTMLPreElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLPreElement
{
    protected HTMLPreElementImpl(nsIDOMHTMLPreElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLPreElementImpl getDOMInstance(nsIDOMHTMLPreElement mozInst)
    {
        HTMLPreElementImpl node = (HTMLPreElementImpl) instances.get(mozInst);
        return node == null ? new HTMLPreElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLPreElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLPreElement) moz;
    }
    
    public int getWidth()
    {
        int result = getInstance().getWidth();
        return result;
    }
    
    public void setWidth(int width)
    {
        getInstance().setWidth(width);
    }
    
}