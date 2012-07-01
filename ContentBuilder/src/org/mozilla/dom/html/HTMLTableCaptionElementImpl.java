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


public class HTMLTableCaptionElementImpl extends HTMLElementImpl implements org.w3c.dom.html2.HTMLTableCaptionElement
{
    protected HTMLTableCaptionElementImpl(nsIDOMHTMLTableCaptionElement mozInst)
    {
        super( mozInst );
    }

    public static HTMLTableCaptionElementImpl getDOMInstance(nsIDOMHTMLTableCaptionElement mozInst)
    {
        HTMLTableCaptionElementImpl node = (HTMLTableCaptionElementImpl) instances.get(mozInst);
        return node == null ? new HTMLTableCaptionElementImpl(mozInst) : node;
    }
    
    @Override
	public nsIDOMHTMLTableCaptionElement getInstance()
    {
        //This was already queryinterfaced, so safe to cast
        return (nsIDOMHTMLTableCaptionElement) moz;
    }
    
    public String getAlign()
    {
        String result = getInstance().getAlign();
        return result;
    }
    
    public void setAlign(String align)
    {
        getInstance().setAlign(align);
    }
    
}
